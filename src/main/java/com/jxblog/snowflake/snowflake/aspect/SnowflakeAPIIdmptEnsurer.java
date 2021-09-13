package com.jxblog.snowflake.snowflake.aspect;

import com.jxblog.snowflake.snowflake.context.IdempotentChecker;
import com.jxblog.snowflake.snowflake.snowflake.SnowflakeRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SnowflakeAPIIdmptEnsurer {
    private static Logger s_logger = LogManager.getLogger(SnowflakeAPIIdmptEnsurer.class);
    @Autowired
    private IdempotentChecker<String, String> callContext;
    @Around("execution(* com.jxblog.snowflake.snowflake.controller.SnowflakeController.*(..))" +
            "&& args(reqBody)")
    public Object checkIfExecutedForSnowRequest(ProceedingJoinPoint jp, SnowflakeRequest reqBody) throws Throwable {
        s_logger.info("checking request with identifier: " + reqBody.getIdentifier());
        if (callContext.isRepeatedOperation(reqBody.getServiceName(), reqBody.getIdentifier())) {
            reqBody.setPassed(false);
        } else {
            s_logger.info("request id: " + reqBody.getIdentifier() + " passed successfully!");
            reqBody.setPassed(true);
            callContext.set(reqBody.getServiceName(), reqBody.getIdentifier());
        }
        return jp.proceed(new Object[]{reqBody});
    }

    @After("execution(* com.jxblog.snowflake.snowflake.controller.SnowflakeController.*(..))" +
            "&& args(reqBody)")
    public void releaseRequestLock(JoinPoint jp, SnowflakeRequest reqBody) {
        s_logger.info("releasing request num: " + reqBody.getIdentifier());
        callContext.release(reqBody.getServiceName(), reqBody.getIdentifier());
    }

}
