package cn.ps.cherry.tool.log.inf;

import java.io.OutputStream;
import java.nio.charset.Charset;
/**
 * 使用文件存储的实现的接口
 * @author 廖敏 973465719@qq.com
 * 时间:2019年1月11日 下午4:31:46
 */
public abstract class FileSourceStorage extends AbstractSourceStorage {
	
	/**
	 * 输出日志的流
	 * @return
	 */
	public abstract OutputStream output();
	
}
