package cn.ps.cherry.tool.log.annotaion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import cn.ps.cherry.tool.log.utils.RecordTimeEnum;
import cn.ps.cherry.tool.log.utils.StorageEnum;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RecordLogAutoConfiguration.class)
public @interface EnableRecordLog {
	StorageEnum type() default StorageEnum.CONSOLE;
	RecordTimeEnum recordTime() default RecordTimeEnum.BEFORE;
}
