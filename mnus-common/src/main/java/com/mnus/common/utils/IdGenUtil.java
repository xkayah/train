package com.mnus.common.utils;

import cn.hutool.core.util.IdUtil;

/**
 * id生成工具类，封装雪花算法
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/9 13:26:08
 */
public class IdGenUtil {
    // 数据中心
    private static long dataCenterId = 1;
    // 机器标识
    private static long workerId = 1;

    public static long nextId() {
        return IdUtil.getSnowflake(workerId, dataCenterId).nextId();
    }

    public static String nextIdStr() {
        return IdUtil.getSnowflake(workerId, dataCenterId).nextIdStr();
    }
}
