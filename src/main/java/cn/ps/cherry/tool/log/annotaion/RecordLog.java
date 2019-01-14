package cn.ps.cherry.tool.log.annotaion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.ps.cherry.tool.log.utils.RecordTimeEnum;
import cn.ps.cherry.tool.log.utils.StorageEnum;

/**
 * 用戶使用該注解決定是否需要記錄日誌
 * @author 廖敏 973465719@qq.com
 * 时间:2019年1月11日 下午3:27:36
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RecordLog {
	/**
	 * 比如拦截某个方法 字符串使用ognl表達式  具體ognl表达式用法参考
	 *  http://commons.apache.org/proper/commons-ognl/language-guide.html
	 * @RecordLog("'调用名字'+#name+'查询列表'")
	 * public List<Map> queryByName(String name)
	 * 記錄日誌的的內容 可以使用 #aaa获取参数
	 * 默認可以使用的常量是
	 *   #date 表示格式化的yyyy-MM-dd日期，表示当前插入日志时间点
	 *   #time 表示格式化的 HH24:mi:ss時間
	 *   #datetime表示格式化的 yyyy-MM-dd HH24:mi:ss日期
	 *   #curClass 當前的類名
	 *   #curMethodName 當前的方法名稱
	 *   #visitTime表示调用该业务逻辑的时间，格式化的 yyyy-MM-dd HH24:mi:ss 只有AFTER触发才能使用该变量
	 *   #excuteTime 執行時間，只有AFTER触发才能使用该变量，否则为0 单位是ns
	 *   #returnValue表示方法的返回值 ，只有AFTER触发才能使用该变量 否则为空
	 *   #exception表示方法的异常信息 ，只有AFTER触发才能使用该变量 否则为空
	 *   #uuid 产生一个随机的uuid字符串
	 * @return
	 */
	String[] value() default "";
	/**
	 * 指定消息输出的类型 默认控制台输出
	 * @return
	 */
	StorageEnum type() default StorageEnum.CONSOLE;
	/**
	 * 在插入之前还是之后记录日志，默认是之前
	 * @return
	 */
	RecordTimeEnum recordTime() default RecordTimeEnum.BEFORE;

}
