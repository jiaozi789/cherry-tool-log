package cn.ps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cn.ps.cherry.tool.log.annotaion.EnableRecordLog;
@SpringBootApplication
@EnableRecordLog
public class LogMain {

	
	public static void main(String[] args) {
		SpringApplication.run(LogMain.class, args);
	}
}
