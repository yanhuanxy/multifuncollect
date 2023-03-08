package com.yanhuanxy.multifunexport.fileservice.annotation;

import com.yanhuanxy.multifunexport.fileservice.autoconfig.OssAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 主动模式 需要手动关闭被动模式 spring.factories 去掉
 */
//@Target({ElementType.FIELD,ElementType.METHOD})//必须是方法配合字段同时注解
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(OssAutoConfiguration.class)
public @interface EnableOss {

}
