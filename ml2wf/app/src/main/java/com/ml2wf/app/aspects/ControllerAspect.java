package com.ml2wf.app.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ControllerAspect {

    @Around("execution(* com.ml2wf.app.controllers..*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Controller method :: {} called", joinPoint.getSignature().getName());
        return joinPoint.proceed();
    }
}
