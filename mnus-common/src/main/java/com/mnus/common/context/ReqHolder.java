package com.mnus.common.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 请求上下文
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/11 19:59:56
 */
public class ReqHolder {
    private static final Logger LOG = LoggerFactory.getLogger(ReqHolder.class);
    private static final ThreadLocal<ReqInfo> THREAD_LOCAL = new ThreadLocal<>();

    public static Long getUid() {
        return get().getUid();
    }

    public static String getIp() {
        return get().getIp();
    }

    public static ReqInfo get() {
        return THREAD_LOCAL.get();
    }

    public static void set(ReqInfo reqInfo) {
        THREAD_LOCAL.set(reqInfo);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
