package cn.ps.cherry.tool.log.aop;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.ps.cherry.tool.log.annotaion.LogProperties;
import cn.ps.cherry.tool.log.annotaion.RecordLog;
import cn.ps.cherry.tool.log.inf.DataSourceStorage;
import cn.ps.cherry.tool.log.inf.FileSourceStorage;
import cn.ps.cherry.tool.log.inf.RecordTableDataSourceStorage;
import cn.ps.cherry.tool.log.persist.GenerationType;
import cn.ps.cherry.tool.log.persist.annotion.RecordTable;
import cn.ps.cherry.tool.log.utils.Log;
import cn.ps.cherry.tool.log.utils.OgnlUtils;
import cn.ps.cherry.tool.log.utils.RecordTimeEnum;
import cn.ps.cherry.tool.log.utils.StorageEnum;
import ognl.OgnlException;

/**
 * 切面定义
 * 
 * @author 廖敏 973465719@qq.com 时间:2019年1月11日 下午3:42:41
 */
@Aspect 
public class RecordLogAspect implements ApplicationContextAware {	
	@Autowired(required=false)
	DataSourceStorage ds;
	@Autowired(required=false)
	FileSourceStorage fss;
	@Resource(name="consoleLog")
	Log consoleLog;
	@Resource(name="fileLog")
	Log fileLog;
	
	/**
	 * 拦截所有添加了RecordLog注解的类
	 */
	@Pointcut("@annotation(cn.ps.cherry.tool.log.annotaion.RecordLog)")
	public void pointcut() {
	}
	@Autowired
	private LogProperties logProperties;
	private DateTimeFormatter dtf=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	/**
	 * 前置通知中记录日志
	 * 
	 * @param joinPoint
	 */
	@Around(value = "pointcut()")
	public Object saveLog(ProceedingJoinPoint pjp) {
		try {
			Method method = resolveMethod(pjp);
			// 获取当前拦截方法上表示的RecordLog
			RecordLog recordLog = method.getAnnotation(RecordLog.class);
			// 获取main方法上EnableRecordLog注解
			//String[] beanNameArray = ac.getBeanNamesForAnnotation(EnableRecordLog.class);
			// 获取存储类型
			StorageEnum storageEnum = (StorageEnum)getRealValue(StorageEnum.defaultValue(),recordLog.type(),StorageEnum.value(logProperties.getStorageType()));
			// 获取触发记录的时机
			RecordTimeEnum recordTimeEnum =(RecordTimeEnum)getRealValue(StorageEnum.defaultValue(),recordLog.recordTime(),StorageEnum.value(logProperties.getRecordTime()));
			Map<String, Object> ognlParam = resolveMethodParam(pjp);
			if(recordTimeEnum==RecordTimeEnum.BEFORE) {
				logOut(storageEnum,recordLog.value(),ognlParam);
				if(storageEnum==StorageEnum.DATABASE) {
					RecordTable recordTable = method.getAnnotation(RecordTable.class);
					DataSourceStorage dataSourceStorage=null;
					if(recordTable==null) {
						dataSourceStorage = ac.getBean(DataSourceStorage.class);
					}else {
						dataSourceStorage=adapterDataSource(recordTable);
					}
					logOutToDb(recordLog.value(),ognlParam,dataSourceStorage);
				}
			}
			String visitTime=LocalDateTime.now().format(dtf);
			long startTime=System.nanoTime();
			String exception=null;
			Object result;
			try {
				//result = pjp.proceed(pjp.getArgs());
				Class<?> returnType = method.getReturnType();
				result=AopUtils.invokeJoinpointUsingReflection(pjp.getTarget(),method, pjp.getArgs());
			} catch (Exception e) {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				e.printStackTrace(new PrintWriter(byteArrayOutputStream));
				exception=new String(byteArrayOutputStream.toString());
				throw e;
			}
			long endTime=System.nanoTime();
			if(recordTimeEnum==RecordTimeEnum.AFTER) {
				ognlParam.put("visitTime", visitTime);
				ognlParam.put("excuteTime", endTime-startTime);
				ognlParam.put("returnValue", result);
				ognlParam.put("exception", exception);
				logOut(storageEnum,recordLog.value(),ognlParam);
				if(storageEnum==StorageEnum.DATABASE) {
					RecordTable recordTable = method.getAnnotation(RecordTable.class);
					DataSourceStorage dataSourceStorage=null;
					if(recordTable==null) {
						dataSourceStorage = ac.getBean(DataSourceStorage.class);
					}else {
						dataSourceStorage=adapterDataSource(recordTable);
					}
					logOutToDb(recordLog.value(),ognlParam,dataSourceStorage);
				}
			}
			return result;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将RecordTable注解转换成DataSourceStorage实现类
	 * @param recordTable
	 * @return
	 */
	public DataSourceStorage adapterDataSource(RecordTable recordTable) {
		return new RecordTableDataSourceStorage(recordTable,ac.getBean(DataSource.class));
	}
	private void logOutToDb(String[] values,Map<String,Object> paramInfo,DataSourceStorage dss) throws OgnlException, SQLException {
		values=getExressionText(values,paramInfo);
		String tableName = dss.tableName();
		GenerationType generationType = dss.generationType();
		String[] columns = dss.columns();
		DataSource dataSource = dss.dataSource();
		String keyName = dss.keyName();
		StringBuffer sb=new StringBuffer("insert into "+tableName+" (");
		List sqlParamList=new ArrayList();
		String keyValue=null;
		/**
		 *  identity表示columns所有列和value对应，uuid也不需要指定值
		 *  @RecordLog(value = {"'用户注册 用户名:'+#userName+',密码:'+#password","#datetime"})
			@RecordTable(table = @Table(name="my_log"), id = "id", columns = {
					@Column(name = "content"), 
					@Column(name = "createTime") 
					},generationType=GenerationType.IDENTITY)
			
			auto是自己指定比如第0列就是主键的参数
			@RecordLog(value = {"#id","'用户注册 用户名:'+#userName+',密码:'+#password","#datetime"})		
			@RecordTable(table = @Table(name="my_log"), id = "id", columns = {
					@Column(name = "content"), 
					@Column(name = "createTime") 
					},generationType=GenerationType.AUTO)		
					
		 */
		if(generationType==GenerationType.UUID) {
			keyValue=paramInfo.get("uuid").toString();
			sb.append(keyName+",");
			sqlParamList.add(keyValue);
		}else if(generationType==GenerationType.AUTO){
			sb.append(keyName+",");
			sqlParamList.add(values[0]);
			String[] valuesNew=new String[values.length-1];
			System.arraycopy(values, 1, valuesNew, 0, values.length-1);
			values=valuesNew;
		}
		for (int i = 0; i < columns.length; i++) {
			sb.append(columns[i]+",");
			sqlParamList.add(values[i]);
		}
		sb=new StringBuffer(sb.substring(0, sb.length()-1));
		sb.append(" ) values( ");
		for(int i=0;i<sqlParamList.size();i++) {
			sb.append("?,"); 
		}
		sb=new StringBuffer(sb.substring(0, sb.length()-1));
		sb.append(")");
		Connection connection = dataSource.getConnection();
		PreparedStatement prepareStatement = connection.prepareStatement(sb.toString());
		for(int i=0;i<sqlParamList.size();i++) {
			prepareStatement.setString(i+1, sqlParamList.get(i).toString());
		}
		prepareStatement.executeUpdate();
		prepareStatement.close();
	}
		
	/**
	 * 记录日志输出
	 * @param storageEnum 输出类型
	 * @param values 所有日志记录 可以是多条 
	 * @param paramInfo 所有日志记录中ognl表达式的参数
	 * @throws OgnlException
	 */
	private void logOut(StorageEnum storageEnum,String[] values,Map<String,Object> paramInfo) throws OgnlException {
		buildPubParam(paramInfo);
		if(storageEnum==StorageEnum.CONSOLE) {
			StringBuffer exressionText = getConcatExressionText(values, paramInfo);
			consoleLog.info(exressionText.toString());
		}else if(storageEnum==StorageEnum.FILE) {
			StringBuffer exressionText = getConcatExressionText(values, paramInfo);
			fileLog.info(exressionText.toString());
		}
	}
	/**
	 * 构建公共的参数
	 * @param paramInfo 参数map
	 */
	public void buildPubParam(Map<String,Object> paramInfo) {
		LocalDateTime ldt=LocalDateTime.now();
		paramInfo.put("date", ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		paramInfo.put("date", ldt.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
		paramInfo.put("datetime", ldt.format(dtf));
		paramInfo.put("uuid", UUID.randomUUID().toString());
	}
	/**
	 * 将文本中的表达式替换生成最终文本
	 * @param values 表达式文本数组
	 * @param paramInfo 所有参数
	 * @return
	 * @throws OgnlException
	 */
	private StringBuffer getConcatExressionText(String[] values,Map<String,Object> paramInfo) throws OgnlException {
		StringBuffer sb=new StringBuffer();
		String[] valueStrArray=getExressionText(values,paramInfo);
		for(String value:valueStrArray) {
			sb.append(value+fss.separtor());
		}
		return sb;
	}
	private String[] getExressionText(String[] values,Map<String,Object> paramInfo) throws OgnlException {
		String[] sb=new String[values.length];
		for (int i = 0; i < values.length; i++) {
			sb[i]=(OgnlUtils.paraseOgnl(values[i], paramInfo));
		}
		return sb;
	}
	/**
	 * 如果优先级高的值==默认值，说明优先级高的没有修改。
	 *   判断优先级低的==默认值 不等于返回优先级低的值。
	 * 如果优先级高的值 ！=默认值 说明优先级高的修改了  直接返回优先级高的值。
	 * @param defaultValue 默认值
	 * @param maxPriority 优先级高的值
	 * @param minPriority 优先级低的值
	 * @return
	 */
	private Object getRealValue(Object defaultValue,Object maxPriority,Object minPriority) {
		//RecordLog优先级更高 如果他是默认console，就可以判断EnableRecordLog是否有修改默认值
		if(maxPriority==defaultValue) {
			if(minPriority!=defaultValue) {
				return minPriority;
			}
		}else {
			//如果RecordLog设置了，不用去管EnableRecordLog了
			return maxPriority;
		}
		return defaultValue;
	}
	private Map<String,Object> resolveMethodParam(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String[] parameterNames = signature.getParameterNames();
		Object[] args = joinPoint.getArgs();
		Map<String,Object> paramMap=new HashMap();
		for(int i=0;i<parameterNames.length;i++) {
			paramMap.put(parameterNames[i], args[i]);
		}
		return paramMap;
	}
	
	private Method resolveMethod(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Class<?> targetClass = joinPoint.getTarget().getClass();

		Method method = getDeclaredMethodFor(targetClass, signature.getName(),
				signature.getMethod().getParameterTypes());
		if (method == null) {
			throw new IllegalStateException("Cannot resolve target method: " + signature.getMethod().getName());
		}
		return method;
	}

	private Method getDeclaredMethodFor(Class<?> clazz, String name, Class<?>... parameterTypes) {
		try {
			return clazz.getDeclaredMethod(name, parameterTypes);
		} catch (NoSuchMethodException e) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null) {
				return getDeclaredMethodFor(superClass, name, parameterTypes);
			}
		}
		return null;
	}

	ApplicationContext ac;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ac = applicationContext;
	}

}
