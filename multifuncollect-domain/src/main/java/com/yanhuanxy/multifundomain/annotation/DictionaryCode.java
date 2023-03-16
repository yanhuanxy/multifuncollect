package com.yanhuanxy.multifundomain.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 字典编码 注解
 * @author yym
 * @date 2023/3/14
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface DictionaryCode {

    /**
     * 字典编码
     */
    String dictCode() default "";

    /**
     * 返回中文字段拓展名
     */
    String name() default "";
}
