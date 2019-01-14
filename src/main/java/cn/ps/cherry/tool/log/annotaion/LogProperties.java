package cn.ps.cherry.tool.log.annotaion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
@Component
@ConfigurationProperties(prefix="record.log")
@Data
public class LogProperties {
	/**
	 * 如果配置了文件路径，表示文件存储的路径
	 */
	@Value("${record.log.filePath:record.log}")
	private String filePath;
	@Value("${record.log.storageType:console}")
	private String storageType;
	@Value("${record.log.recordTime:before}")
	private String recordTime;
	@Value("${record.log.logLevel:info}")
	private String logLevel;
}
