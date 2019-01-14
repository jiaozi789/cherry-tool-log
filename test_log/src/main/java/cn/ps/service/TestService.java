package cn.ps.service;

import javax.persistence.Column;
import javax.persistence.Table;

import org.springframework.stereotype.Service;

import cn.ps.cherry.tool.log.annotaion.RecordLog;
import cn.ps.cherry.tool.log.persist.GenerationType;
import cn.ps.cherry.tool.log.persist.annotion.RecordTable;

@Service
public class TestService {
	@RecordLog(value = "'前置通知'+#h")
	public String hello(String h) {
		return ("hello" + h);
	}
	
	@RecordLog(value = {"'user registriong userName:'+#userName+',password:'+#password","#datetime"})
	@RecordTable(table = @Table(name="my_log"), id = "id", columns = {
			@Column(name = "content"), 
			@Column(name = "createTime") 
			},generationType=GenerationType.IDENTITY)
	public boolean login(String userName,String password) {
		if("admin".equals(userName) && "password".equals(password)) {
			return true;
		}
		return false;
	}
	
}
