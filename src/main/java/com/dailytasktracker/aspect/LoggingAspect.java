package com.dailytasktracker.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.dailytasktracker..*(..))")
    public void applicationPackagePointcut() {}

    /**
     * Around advice to log method execution details.
     */
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        logger.info("Method Invoked: {} | Arguments: {}",
                joinPoint.getSignature(),
                Arrays.toString(joinPoint.getArgs()));

        Object result;
        try {
            // Proceed with the method execution
            result = joinPoint.proceed();
        } catch (Exception e) {
            logger.error("Exception in Method: {} | Message: {}", joinPoint.getSignature(), e.getMessage());
            throw e;
        }

        long duration = System.currentTimeMillis() - start;
        logger.info("Method Completed: {} | Result: {} | Duration: {} ms",
                joinPoint.getSignature(),
                result,
                duration);

        return result;
    }

}
