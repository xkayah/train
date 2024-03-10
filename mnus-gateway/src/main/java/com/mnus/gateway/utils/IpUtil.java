package com.mnus.gateway.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

/**
 * IP 工具类
 *
 * @author: <a href="https://github.com/xkayah">xkayah</a>
 * @date: 2024/3/10 18:47:06
 */
public class IpUtil {
    private static final Logger LOG = LoggerFactory.getLogger(IpUtil.class);

    public static String getUserIpAddr(ServerHttpRequest request) {
        String ipAddr = null;
        HttpHeaders headers = request.getHeaders();
        try {
            ipAddr = headers.getFirst("x-forwarded-for");
            if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
                ipAddr = headers.getFirst("Proxy-Client-IP");
            }
            if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
                ipAddr = headers.getFirst("X-Forwarded-For");
            }
            if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
                ipAddr = headers.getFirst("WL-Proxy-Client-IP");
            }
            if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
                ipAddr = headers.getFirst("X-Real-IP");
            }

            if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
                ipAddr = Optional.ofNullable(request.getRemoteAddress())
                        .map(address -> address.getAddress().getHostAddress())
                        .orElse("");
                if (ipAddr.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    try {
                        ipAddr = InetAddress.getLocalHost().getHostAddress();
                    } catch (UnknownHostException e) {
                        LOG.error("用户IP获取异常，{}", e.getMessage());
                        return "";
                    }
                }
            }
            // 通过多个代理的情况，第一个IP为客户端真实IP，多个IP按照','分割
            if (ipAddr != null) {
                if (ipAddr.contains(",")) {
                    return ipAddr.split(",")[0];
                } else {
                    return ipAddr;
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            LOG.error("用户IP获取异常，{}", e.getMessage());
            return "";
        }
    }

    public static String getUserIpAddr(HttpServletRequest request) {
        String ipAddr = null;
        try {
            if (request == null) {
                return "unknown";
            }
            ipAddr = request.getHeader("x-forwarded-for");
            if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
                ipAddr = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
                ipAddr = request.getHeader("X-Forwarded-For");
            }
            if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
                ipAddr = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
                ipAddr = request.getHeader("X-Real-IP");
            }

            if (ipAddr == null || ipAddr.length() == 0 || "unknown".equalsIgnoreCase(ipAddr)) {
                ipAddr = request.getRemoteAddr();
                if (ipAddr.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    try {
                        ipAddr = InetAddress.getLocalHost().getHostAddress();
                    } catch (UnknownHostException e) {
                        LOG.error("用户IP获取异常，{}", e.getMessage());
                        return "";
                    }
                }
            }
            // 通过多个代理的情况，第一个IP为客户端真实IP，多个IP按照','分割
            if (ipAddr != null) {
                if (ipAddr.contains(",")) {
                    return ipAddr.split(",")[0];
                } else {
                    return ipAddr;
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            LOG.error("用户IP获取异常，{}", e.getMessage());
            return "";
        }
    }

    public static String getServiceIp() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
            return address.getHostAddress(); // 返回IP地址
        } catch (UnknownHostException e) {
            LOG.error("本地IP获取异常，{}", e.getMessage());
        }
        return null;
    }

}
