package com.mnus.common.aspect;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import com.mnus.common.constance.MDCKey;
import com.mnus.common.enums.BaseErrorCodeEnum;
import com.mnus.common.exception.BizException;
import com.mnus.common.utils.IpUtil;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

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
    private static final Logger LOG = LoggerFactory.getLogger(LogAspect.class);

    public LogAspect() {
        LOG.info("LogAspect start...");
    }

    public static final String[] EXCLUDE_WORDS = {"mobile", "phone", "email", "id_card", "idCard"};

    /**
     * 定义一个切点，com.mnus.. 表示所有项目共同，可以加上项目名
     */
    @Pointcut("execution(public * com.mnus..*Controller.*(..))")
    public void controllerPointcut() {
    }

    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        if (!StringUtils.hasText(MDC.get(MDCKey.TID))) {
            MDC.put(MDCKey.TID, UUID.randomUUID().toString());
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new BizException(BaseErrorCodeEnum.INTERNAL_SERVER_ERROR);
        }
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        String userIp = IpUtil.getClientIp(request);
        String serverIp = IpUtil.getServerIp();
        String method = request.getMethod();
        String declaredMethod = signature.getDeclaringTypeName() + "." + signature.getName();
        String url = request.getRequestURL().toString();
        // 排除特殊类型的参数，如文件类型、req、resp
        Object[] joinPointArgs = joinPoint.getArgs();
        Object[] args = new Object[joinPointArgs.length];
        for (int i = 0; i < joinPointArgs.length; i++) {
            if (joinPointArgs[i] instanceof ServletRequest
                    || joinPointArgs[i] instanceof ServletResponse
                    || joinPointArgs[i] instanceof MultipartFile) {
                continue;
            }
            args[i] = joinPointArgs[i];
        }
        // 排除字段，敏感字段或太长的字段不显示：身份证、手机号、邮箱、密码等
        String[] excludeProperties = {};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludeFilter = filters.addFilter(EXCLUDE_WORDS);
        excludeFilter.addExcludes(excludeProperties);
        String inputArgs = JSONObject.toJSONString(args, excludeFilter);
        // 打印请求信息
        LOG.info("[{}]→[{}]uri:{} {} [@{}],input:{} ==>begin",
                userIp,
                serverIp,
                method, url,
                declaredMethod,
                inputArgs);
    }

    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 排除字段，敏感字段或太长的字段不显示：身份证、手机号、邮箱、密码等
        String[] excludeProperties = {};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludeFilter = filters.addFilter(EXCLUDE_WORDS);
        excludeFilter.addExcludes(excludeProperties);
        long end = System.currentTimeMillis();
        LOG.info("result:{},process_time:{}ms <==end",
                JSONObject.toJSONString(result, excludeFilter),
                end - start);
        // 移除 tid
        MDC.remove(MDCKey.TID);
        return result;
    }
}
