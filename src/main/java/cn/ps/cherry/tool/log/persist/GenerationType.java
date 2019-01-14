package cn.ps.cherry.tool.log.persist;
/**
 * 主键生成策略
 * @author 廖敏 973465719@qq.com
 * 时间:2019年1月14日 下午12:56:02
 */
public enum GenerationType {
	/**
	 * 数据库自己控制
	 */
	IDENTITY,
	/**
	 * 生成32位uuid
	 */
	UUID,
	/**
	 * 程序自己指定
	 */
	AUTO
}
