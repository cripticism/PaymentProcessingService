package org.example.payment.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Pointcut to match all methods in controller and service packages
    @Pointcut("within(org.example.payment.controller..*) || within(org.example.payment.service..*)")
    public void applicationPackagePointcut() {
    }

    // Log before each method execution
    @Before("applicationPackagePointcut()")
    public void logBeforeMethod(JoinPoint joinPoint) {
        logger.info("Entering method: {}.{} with arguments: {}", joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(), joinPoint.getArgs());
    }

    // Log after successful method execution
    @AfterReturning(pointcut = "applicationPackagePointcut()", returning = "result")
    public void logAfterMethod(JoinPoint joinPoint, Object result) {
        logger.info("Exiting method: {}.{} with result: {}", joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(), result.toString());
    }

    // Log if a method throws an exception
    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "exception")
    public void logAfterException(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in method: {}.{} with message: {}", joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(), exception.getMessage());
    }
}
