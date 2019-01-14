package cn.ps.cherry.tool.log.inf;

import java.nio.charset.Charset;

public abstract class AbstractSourceStorage {
	private static final String SEPARTOR_CHAR = "	";
	public static final String UTF_8 = "UTF-8";
	/**
	 * 写入日志到文件的字符集
	 * @return
	 */
	public Charset writeCharset() {
		return Charset.forName(UTF_8);
	}
	public String separtor() {
		return SEPARTOR_CHAR;
	};
}
