package com.gemii.auth.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @ClassName SysAspect
 * @Description TODO
 * @Author wen.liu
 * @Date 2019-06-12 15:45
 **/
@Order(1)
@Aspect
@Component
@Slf4j
public class SysAspect {

    @Pointcut("execution(public * com.gemii.auth.controller.*.*(..))")
    public void log(){

    }

    ThreadLocal<Long> startTime = new ThreadLocal<Long>();

    @Around("log()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        startTime.set(System.currentTimeMillis());
        Object proceed = joinPoint.proceed();
        long costTime = System.currentTimeMillis() - startTime.get();
        if (costTime > 200) {
            Signature signature = joinPoint.getSignature();
            String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
            log.warn("this method cost time more than 200ms, methodName:{}, costTime:{}", methodName, costTime);
        }
        return proceed;
    }

}
