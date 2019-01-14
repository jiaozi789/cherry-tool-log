package cn.ps.cherry.tool.log.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 
 * 作者:LM
 * 创建日期:2015-1-5上午10:45:11
 * 项目名称:cherry
 * 包名称:com.lm.cherry.util
 * 文件名:Log.java
 */
public class Log {
	public static final int LOG_DEBUG=0;
	public static final int LOG_INFO=1;
	public static final int LOG_ERROR=2;
	private int logLevel=1;
	private boolean enableLog=true;
	private String logOut="syso";
	private String logPath="/log.log";
	private String realLogPath=logPath;
	private String workPath="";
	OutputStreamWriter output =null;
	private boolean ifSyso=false;
	private static final DateFormat format = new SimpleDateFormat(
	"yyyy-MM-dd HH:mm:ss");
	public Log(String logLevel,String enableLog,String logout,OutputStream os){
		try {
			init(logLevel, "true".equals(enableLog)?true:false,logout,os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Log(LogC log){
		try {
			if(log!=null){
				init(log.getLogLevel(), "true".equals(log.getEnableLog())?true:false, log.getLogOut(), 
						log.getLogPath()==null?null:new FileOutputStream(log.getLogPath()));
			}else{
				enableLog=false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 初始化日志的参数
	 * 作者:LM
	 * 创建日期:2015-1-5上午11:27:21
	 * @param logLevel 日志的级别
	 * @param enableLog 是否启用日志
	 * @param logOut 日志输出流
	 * @param logPath 日志输出文件
	 * @throws Exception 
	 */
	public void init(String logLevel,boolean enableLog,String logOut,OutputStream output) throws Exception{
		this.logLevel=logLevel.equals("debug")?LOG_DEBUG:(logLevel.equals("error")?LOG_ERROR:LOG_INFO);
		this.enableLog=enableLog;
		this.logOut=logOut;
		//如果启用日志 且输出中 存在file输出 可以创建文件
		if(this.enableLog && this.logOut.indexOf("file")>=0){
			parseLogPath();
			this.output=new OutputStreamWriter(output);
		}
		if(this.enableLog && this.logOut.indexOf("syso")>=0){
			this.ifSyso=true;
		}
		
	}
	/**
	 * 关闭日志输出
	 * 作者:LM
	 * 创建日期:2015-1-5上午11:48:00
	 * @throws IOException
	 */
	public void shutdown() throws IOException{
		if(this.output!=null){
			this.output.close();
		}
	}
	/**
	 * 替换日志文件的信息
	 * 作者:LM
	 * 创建日期:2015-1-5上午11:42:25
	 */
	public void parseLogPath(){
		String logFile=this.logPath;
		if(this.logPath.startsWith("/")){
			logFile=workPath+this.logPath;
		}
		Date date=new Date();
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);		
		logFile=logFile.replaceAll("!yyyy!",calendar.get(Calendar.YEAR)+"");
		logFile=logFile.replaceAll("!MM!",(calendar.get(Calendar.MONTH)+1)+"");
		logFile=logFile.replaceAll("!dd!",calendar.get(Calendar.DATE)+"");
		logFile=logFile.replaceAll("!hh!",calendar.get(Calendar.HOUR)+"");
		logFile=logFile.replaceAll("!mm!",calendar.get(Calendar.MINUTE)+"");
		logFile=logFile.replaceAll("!ss!",calendar.get(Calendar.SECOND)+"");
		this.realLogPath=logFile;
	}
	/**
	 * 获取日志的字母描述 
	 * 作者:LM
	 * 创建日期:2015-1-5上午11:55:35
	 * @param model
	 * @return
	 */
	public String getModel(int model){
		return (model==0?"DEBUG":(model==1?"INFO":"ERROR"));
	}
	/**
	 * 控制台输出
	 * 作者:LM
	 * 创建日期:2015-2-3上午09:24:54
	 * @param msg
	 */
	public void syso(String msg){
		System.out.println(msg);
	}
	/**
	 * 判断当前是否支持输入的 model的 比如定义的model是 info  如果传入的debug 则返回false
	 * 作者:LM
	 * 创建日期:2015-3-4下午01:24:27
	 * @param model
	 * @return
	 */
	public boolean ifSupportModel(int model){
		if(model>=logLevel){
			return true;
		}
		return false;
	}
	/**
	 * 输出日志
	 * 作者:LM
	 * 创建日期:2015-1-5上午11:55:46
	 * @param msg
	 * @param model
	 */
	private synchronized void log(String msg,int model){
		if(enableLog){
			try {
				msg=format.format(new Date())+" ["+getModel(model)+"] "+msg;
				if(ifSupportModel(model)){
					if(output!=null){
						output.write(msg+"\r\n");
						output.flush();
					}
					if(ifSyso){
						System.out.println(msg);
					}
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public  void debug(String msg){
		log(msg,Log.LOG_DEBUG);
	}
	public  void info(String msg){
		log(msg,Log.LOG_INFO);
	}
	public  void error(String msg){
		log(msg,Log.LOG_ERROR);
	}
}