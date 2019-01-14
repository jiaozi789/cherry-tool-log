package cn.ps.cherry.tool.log.inf;

import javax.sql.DataSource;

import cn.ps.cherry.tool.log.persist.GenerationType;
/**
 * 如果是数据库的配置必须指定数据源
 * @author 廖敏 973465719@qq.com
 * 时间:2019年1月11日 下午4:27:45
 */
public abstract class DataSourceStorage extends AbstractSourceStorage {
	/**
	 * 定义数据库的数据源
	 * @return
	 */
	public abstract DataSource dataSource();
	/**
	 * 记录日志的表结构
	 * @return
	 */
	public abstract String tableName();
	/**
	 * 插入日志的column信息
	 * @return
	 */
	public abstract String[] columns();
	/**
	 * 主键名称
	 * @return
	 */
	public abstract String keyName();
	/**
	 * 主键生成策略
	 * @return
	 */
	public abstract GenerationType generationType();
}
