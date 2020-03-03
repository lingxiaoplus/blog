package com.lingxiao.blog.utils;

import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.OperatingSystem;
import nl.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPUtils {
    public static long ipToNum(String ip) {
        String[] parts = ip.split("\\.");
        assert parts.length == 4;
        Long num = 0L;
        for (int i = 0; i < parts.length; i++) {
            num += (Long.parseLong(parts[i]) << (24 - i * 8));
        }
        return num;
    }


    public static String numToIP(long ipNum) {
        String result = String.format("%d.%d.%d.%d", ipNum >>> 24, (ipNum & 0x00FFFFFF) >>> 16, (ipNum & 0x0000FFFF) >>> 8, ipNum & 0x000000FF);
        return result;
    }


    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址。
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        return ip;
    }


    public static String getBrowserName(HttpServletRequest request) {
        //获取浏览器信息
        String ua = request.getHeader("User-Agent");
        //转成UserAgent对象
        UserAgent userAgent = UserAgent.parseUserAgentString(ua);
        //获取浏览器信息
        Browser browser = userAgent.getBrowser();
        //获取系统信息
        //OperatingSystem os = userAgent.getOperatingSystem();
        //系统名称
        //String system = os.getName();
        //浏览器名称
        String browserName = browser.getName();
        return browserName;
    }
}
