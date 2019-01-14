package cn.ps.cherry.tool.log.utils;
/**
 * 日志记录输出类型
 * @author 廖敏 973465719@qq.com
 * 时间:2019年1月11日 下午3:19:28
 */
public enum StorageEnum {
	/**
	 * 存储数据库
	 */
	DATABASE,
	/**
	 * 控制台
	 */
	CONSOLE,
	/**
	 * 文件
	 */
	FILE;
	public static StorageEnum defaultValue() {
		return CONSOLE;
	}
	public static StorageEnum value(String storageType) {
		if("console".equals(storageType))
			return CONSOLE;
		if("file".equals(storageType))
			return FILE;
		if("database".equals(storageType))
			return DATABASE;
		return CONSOLE;
	}
}
