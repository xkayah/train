package com.mnus.common.utils;

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

    /**
     * 获取客户端 IP
     *
     * @param request
     * @return
     */
    public static String getClientIp(Object request) {
        String ipAddr = "";
        try {
            if (request instanceof ServerHttpRequest) {
                ipAddr = getIpByServerHttp((ServerHttpRequest) request);
            } else if (request instanceof HttpServletRequest) {
                ipAddr = getIpByHttpServlet((HttpServletRequest) request);
            }
        } catch (Exception e) {
            LOG.error("Get user IP failed,{}", e.getMessage());
            return "";
        }
        return ipAddr;
    }

    private static String getIpByServerHttp(ServerHttpRequest request) throws UnknownHostException {
        String ipAddr;
        HttpHeaders headers = request.getHeaders();
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
                ipAddr = InetAddress.getLocalHost().getHostAddress();
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
    }

    private static String getIpByHttpServlet(HttpServletRequest request) throws UnknownHostException {
        String ipAddr;
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
                ipAddr = InetAddress.getLocalHost().getHostAddress();
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
    }

    public static String getServerIp() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
            return address.getHostAddress(); // 返回IP地址
        } catch (UnknownHostException e) {
            LOG.error("Get server IP failed,{}", e.getMessage());
        }
        return null;
    }

}
