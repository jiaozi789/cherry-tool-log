package cn.ps.cherry.tool.log.annotaion;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import cn.ps.cherry.tool.log.aop.RecordLogAspect;
import cn.ps.cherry.tool.log.inf.FileSourceStorage;
import cn.ps.cherry.tool.log.utils.Log;
import cn.ps.cherry.tool.log.utils.LogC;

@Configuration
public class RecordLogAutoConfiguration {
	public RecordLogAutoConfiguration() {
		System.out.println("heelo");
	}

	//@ConditionalOnMissingBean(RecordLogAspect.class)
	@Bean
	public RecordLogAspect recordLogAspect() {
		return new RecordLogAspect();
	}
	/**
	 * 创建一个默认的文件输出
	 * @return
	 */
	//@ConditionalOnProperty(name="record.log.")
	@ConditionalOnMissingBean(FileSourceStorage.class)
	@Bean
	public FileSourceStorage fileSourceStorage(@Autowired LogProperties lp) {
		return new FileSourceStorage() {
			
			@Override
			public OutputStream output() {
				try {
					if(StringUtils.isEmpty(lp.getFilePath())) {
						lp.setFilePath(lp.getFilePath());
					}
					return new FileOutputStream(lp.getFilePath(),true);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
	}
	
	@ConditionalOnMissingBean(name="consoleLog")
	@Bean("consoleLog")
	public Log log() {
		Log log=new Log(new LogC("true","info","syso",null));
		return log;
	}
	
	@Bean("fileLog")
	public Log fileLog(@Autowired FileSourceStorage fss) {
		Log log=new Log("info","true","file",fss.output());
		return log;
	}
}
