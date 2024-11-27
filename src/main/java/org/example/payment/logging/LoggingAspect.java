package org.example.payment.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(org.example.payment.controller..*) || within(org.example.payment.service..*)")
    public void applicationPackagePointcut() {
    }

    @Before("applicationPackagePointcut()")
    public void logBeforeMethod(JoinPoint joinPoint) {
        logger.info("Entering method: {}.{} with arguments: {}", joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),  joinPoint.getArgs()); //Arrays.stream(joinPoint.getArgs()).map(this::formatArgument).collect(Collectors.joining(", ")));
    }

    @AfterReturning(pointcut = "applicationPackagePointcut()", returning = "result")
    public void logAfterMethod(JoinPoint joinPoint, Object result) {
        if (result instanceof ResponseEntity<?> response) {
            logger.info("Exiting method: {}.{} with status: {}, body: {} and headers: {}",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName(),
                    response.getStatusCode(),
                    response.getBody(),
                    response.getHeaders());
        } else {
            logger.info("Exiting method: {}.{} with result: {}", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName(), result);
        }
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "exception")
    public void logAfterException(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in method: {}.{} with message: {}", joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(), exception.getMessage());
    }

    private String formatArgument(Object arg) {
        if (arg instanceof Throwable throwable) {
            return "[" + throwable.getClass().getSimpleName() + ": " + throwable.getMessage() + "]";
        }
        return arg != null ? arg.getClass().getSimpleName() + ": " + arg : "null";
    }
}
