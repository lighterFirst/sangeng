package com.cheng.Aspect;

import com.alibaba.fastjson.JSON;
import com.cheng.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

@Component
@Aspect  //切面类
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(com.cheng.annotation.SystemLog)")  //通用切点表达式
    public void first(){

    }

    @Around("first()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {

        Object proceed;
        try {
            handleBefore(joinPoint);
            //该值是目标方法的返回值
            proceed= joinPoint.proceed();
            handleAfter(proceed);
        } finally {
            //结束后换行  (System.lineSeparator()  系统换行符，windows系统或者linux系统的换行符是不同的，不要写死)
            log.info("=================="+System.lineSeparator());
        }
        return proceed;

    }

    private void handleAfter(Object proceed) {

        // 打印出参
        log.info("Response       : {}", JSON.toJSONString(proceed));

    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {
        log.info("=======Start=======");
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //获取被增强方法上的注解对象
        SystemLog systemLog = getSystemLog(joinPoint);

        // 打印请求 URL
        log.info("URL            : {}",request.getRequestURI());
        // 打印描述信息 (接口名字)
        log.info("BusinessName   : {}", systemLog.bussinessName());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}",joinPoint.getSignature().getDeclaringTypeName(),joinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));
        // 结束后换行
        log.info("=======End=======" + System.lineSeparator());
    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        SystemLog annotation = methodSignature.getMethod().getAnnotation(SystemLog.class);
        return annotation;

    }

}
