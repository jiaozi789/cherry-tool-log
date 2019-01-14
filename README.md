# cherry-tool-log
通用的系统日志记录工具

##使用例子

. 添加maven依赖
. 有日志需求的service实现类方法上添加
 ```java
 @RecordLog(value = "'前置通知'+#h")
 ```
