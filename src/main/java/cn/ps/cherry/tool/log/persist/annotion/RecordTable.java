package cn.ps.cherry.tool.log.persist.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.persistence.Column;
import javax.persistence.Table;

import cn.ps.cherry.tool.log.persist.GenerationType;
/**
 * 如果是数据库类型存储可以使用这个注解
 * @author 廖敏 973465719@qq.com
 * 时间:2019年1月14日 上午11:20:11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RecordTable {
	/**
	 * 表名
	 * @return
	 */
	public Table table();
	/**
	 * 所有需要插入的列名
	 * 注意列名的顺序必须和RecordLog注解values数组的顺序对应，如果
	 *   主键策略是auto（自己指定主键值)
	 *     values第0列表示是id列的值
	 *     values第1列才是columns第0列的值
	 *   主键策略是identifify（数据库生成） uuid（使用#uuid生成）
	 *     values第0列对应columns第0列的值
	 * @return
	 */
	public Column[] columns();
	/**
	 * 指定主键
	 * @return
	 */
	public String id();
	/**
	 * 主键生成策略
	 * @return
	 */
	public GenerationType generationType() default GenerationType.AUTO;
	
	
	
	
	
}
