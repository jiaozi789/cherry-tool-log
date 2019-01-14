package cn.ps.cherry.tool.log.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogC {
	private String enableLog;
	private String logLevel;
	private String logOut;
	private String logPath;
}
