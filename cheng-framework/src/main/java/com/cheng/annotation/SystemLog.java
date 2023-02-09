package com.cheng.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)  //注解的生命周期到  runtime
@Target({ElementType.METHOD})  //注解加在方法上面
public @interface SystemLog {

    String bussinessName();  //自定义注解属性



}
