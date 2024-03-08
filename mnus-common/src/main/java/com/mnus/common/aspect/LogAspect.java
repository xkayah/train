package com.mnus.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 系统日志，切面处理类
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/8 14:00:45
 */
@Aspect
@Component
public class LogAspect {

    public static final String TRACE_ID = "traceId";
    private static final Logger LOG = LoggerFactory.getLogger(LogAspect.class);

    public LogAspect() {
        LOG.info("logAspect start...");
    }

    /**
     * 定义一个切点，com.mnus.. 表示所有项目共同，可以加上项目名
     */
    @Pointcut("execution(public * com.mnus..*Controller.*(..))")
    public void logPointCut() {
    }

    @Around(value = "logPointCut()")
    public Object doAround(JoinPoint joinpoint) throws Throwable {
        if (!StringUtils.hasText(MDC.get(TRACE_ID))) {
            MDC.put(TRACE_ID, UUID.randomUUID().toString());
        }
        return combineLogInfo(joinpoint);
    }

    private Object combineLogInfo(JoinPoint joinPoint) throws Throwable {
        Object[] param = joinPoint.getArgs();
        LOG.info("uri:{},input:{},==>begin", joinPoint.getSignature(), param);
        long start = System.currentTimeMillis();
        // todo 数据脱敏。注意，敏感字段或字段太长需排除，特别是金融业类的业务需要数据脱敏
        Object result = ((ProceedingJoinPoint) joinPoint).proceed();
        long end = System.currentTimeMillis();
        LOG.info("uri:{},output:{},proc_time:{}ms,<==end", joinPoint.getSignature().toString(),
                result, end - start);
        return result;
    }
}
