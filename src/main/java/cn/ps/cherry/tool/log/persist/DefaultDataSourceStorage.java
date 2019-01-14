package cn.ps.cherry.tool.log.persist;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import cn.ps.cherry.tool.log.inf.DataSourceStorage;
/**
 * 如果是数据库的配置必须指定数据源
 * 数据源必须在spring的bean容器中必须存在，否则出现错误
 * @author 廖敏 973465719@qq.com
 * 时间:2019年1月11日 下午4:27:45
 */
public class DefaultDataSourceStorage extends DataSourceStorage {
	/**
	 * 默认的日志表名
	 */
	private static final String DETAULT_LOG_TNAME = "recordLog";
	@Autowired
	private DataSource dataSource;
	/**
	 * 定义数据库的数据源
	 * @return
	 */
	public DataSource dataSource() {
		return dataSource;
	}
	/**
	 * 记录日志的表结构
	 * 默认的表结构：
	 *  create table recordLog(
	 *  	id int primary key auto_increment,
	 *  	content char(100) not null,
	 *      recordtime datetime not null,
	 *  )
	 * 
	 * 
	 * @return
	 */
	public String tableName() {
		return DETAULT_LOG_TNAME;
	}
	/**
	 * 插入日志的column信息
	 * 主键一般设置为自动增长，自己不添加
	 * @return
	 */
	public String[] columns() {
		return new String[] {"content","recordtime"};
	}
	@Override
	public String keyName() {
		return "id";
	}
	@Override
	public GenerationType generationType() {
		return GenerationType.AUTO;
	}
}
