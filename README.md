# cherry-tool-log
通用的系统日志记录工具
日志框架基于用java8+springboot2.0.6编写<br/>
如需使用，必须在springboot环境下

## 使用例子

- 添加maven依赖 
- main方法类上添加启用日志注解
  ```java
    @EnableRecordLog
  ```

- 有日志需求的service实现类方法上添加
```java
@RecordLog(value = "'前置通知'+#h")
public String hello(String h) {
    	return ("hello" + h);
}
```
默认日志使用控制台输出，目前支持三种方式 
 ### 1. 控制台输出（默认）
    @RecordLog的value值是个数组，可以多条日志，value使用ognl表达式 #参数名，也可以使用#参数对象.属性
   支持默认的一些参数:
   
    默認可以使用的常量是
     - \#date 表示格式化的yyyy-MM-dd日期，表示当前插入日志时间点
	 - \#time 表示格式化的 HH24:mi:ss時間。
	 - \#datetime表示格式化的 yyyy-MM-dd HH24:mi:ss日期。
	 - \#curClass 當前的類名。
	 - \#curMethodName 當前的方法名稱。
	 - \#visitTime表示调用该业务逻辑的时间，格式化的 yyyy-MM-dd HH24:mi:ss 只有AFTER触发才能使用该变量。
	 - \#excuteTime 執行時間，只有AFTER触发才能使用该变量，否则为0 单位是ns。
	 - \#returnValue表示方法的返回值 ，只有AFTER触发才能使用该变量 否则为空。
	 - \#exception表示方法的异常信息 ，只有AFTER触发才能使用该变量 否则为空。
	 - \#uuid 产生一个随机的uuid字符串。
 
 ### 2. 文件方式输出
   默认使用控制台输出，该项是可配置的，修改application.yml：
   
 ```yml
    record: 
      log: 
        storageType: file
        filePath: rr.log
```

 ### 3. 数据库方式输出
   修改application.yml
   
    ```yml
    record: 
      log: 
        storageType: database
    spring: 
      datasource: 
        url: jdbc:mysql://localhost:3306/test1?useUnicode=true&characterEncoding=utf8
        username: root
        password: 123456
        driverClassName: com.mysql.jdbc.Driver
    ```
    
    在service添加日志表信息:
    
    ```java
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
    ```
    
    RecordTable注解定义的列必须和RecordLog的value数组顺序一致
    主键类型（AUTO,IDENTIFY,UUID）:
      - identity表示columns所有列和value对应，uuid也不需要指定值：

      ```java
        	   @RecordLog(value = {"'用户注册 用户名:'+#userName+',密码:'+#password","#datetime"})
    			@RecordTable(table = @Table(name="my_log"), id = "id", columns = {
    					@Column(name = "content"), 
    					@Column(name = "createTime") 
    					},generationType=GenerationType.IDENTITY)
                   public boolean login(String userName,String password) {
      ```
      - auto是程序指定主键  第0列就是主键的参数
        ```java
    		@RecordLog(value = {"#id","'用户注册 用户名:'+#userName+',密码:'+#password","#datetime"})		
			@RecordTable(table = @Table(name="my_log"), id = "id", columns = {
					@Column(name = "content"), 
					@Column(name = "createTime") 
					},generationType=GenerationType.AUTO)
		public boolean login(String userName,String password) 			
					
        ```
      如果觉得使用@RecordTable每个日志类型都需要定义可以自己实现DataSourceStorage接口，注册到springbean的容器。
     此时就不需要使用@RecordTable注解。
   
     ```java
	     @Bean
	     public DataSourceStorage dss(){
		 return XXXDataSourceStorage();
	     }
     ```
  
