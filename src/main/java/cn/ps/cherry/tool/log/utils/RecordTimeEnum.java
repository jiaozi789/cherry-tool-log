package cn.ps.cherry.tool.log.utils;
/**
 * 日志插入的时间
 * @author 廖敏 973465719@qq.com
 * 时间:2019年1月11日 下午3:45:53
 */
public enum RecordTimeEnum {
	/**
	 * 注解方法调用之前
	 */
	BEFORE,
	/**
	 * 注解方法调用之后
	 */
	AFTER;
	public static RecordTimeEnum defaultValue() {
		return BEFORE;
	}
	public static RecordTimeEnum value(String recordTime) {
		if("BEFORE".equals(recordTime))
			return BEFORE;
		if("AFTER".equals(recordTime))
			return AFTER;
		return BEFORE;
	}
}
