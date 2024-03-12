package com.mnus.gateway.constance;

/**
 * 过滤器的优先顺序
 * 必须维护：response < request < other
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/11 17:25:15
 */
public interface Order {

    /**
     * 请求日志过滤器 order
     */
    int REQUEST_LOG = 0x80000000 + 1;

    /**
     * 响应日志过滤器 order，
     */
    int RESPONSE_LOG = 0x80000000;
}
