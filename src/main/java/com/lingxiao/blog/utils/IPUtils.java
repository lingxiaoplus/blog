package com.lingxiao.blog.utils;

import com.google.gson.Gson;
import com.lingxiao.blog.bean.Address;
import lombok.extern.slf4j.Slf4j;
import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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


    public static String numToIP(Long ipNum) {
        if (ipNum == null) {
            return "";
        }
        String result = String.format("%d.%d.%d.%d", ipNum >>> 24, (ipNum & 0x00FFFFFF) >>> 16, (ipNum & 0x0000FFFF) >>> 8, ipNum & 0x000000FF);
        return result;
    }


    private static boolean checkIpAddressLocal(String ip){
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) || "127.0.0.1".equals(ip)) {
            return true;
        }
        return false;
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址。
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        Map<String, String> headersInfo = getHeadersInfo(request);
        String ip = request.getHeader("x-forwarded-for");
        if (checkIpAddressLocal(ip)) {
            ip = request.getHeader("X-Real-IP");
            log.debug("获取用户真实ip - X-Real-IP - String ip=" + ip);
        }
        if (checkIpAddressLocal(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            log.debug("获取用户真实ip - Proxy-Client-IP - String ip=" + ip);
        }
        if (checkIpAddressLocal(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            log.debug("获取用户真实ip - WL-Proxy-Client-IP - String ip=" + ip);
        }
        if (checkIpAddressLocal(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            log.debug("获取用户真实ip - HTTP_CLIENT_IP - String ip=" + ip);
        }
        if (checkIpAddressLocal(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            log.debug("获取用户真实ip - HTTP_X_FORWARDED_FOR - String ip=" + ip);
        }
        if (checkIpAddressLocal(ip)) {
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
                log.debug("获取用户真实ip - 根据网卡取本机配置的IP - String ip=" + ip);
            }
        }
        //nginx前后端分离，会获取到多个ip，只取第一个
        String[] split = StringUtils.split(ip, ",");
        if (split.length > 0){
            return split[0];
        }
        return ip;
    }

    private static Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }


    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request
     * @return
     */
    public final static String getIpAddress2(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");
        log.debug("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
                log.debug("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                log.debug("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
                log.debug("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                log.debug("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                log.debug("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }


    public static Address getRealAddrFromIp(String ip){
        if(StringUtils.isBlank(ip)){
            return null;
        }
        HttpURLConnection connection = null;
        try {
            String apiUrl = "http://whois.pconline.com.cn/ipJson.jsp?ip="+ ip +"&json=true";
            URL url = new URL(apiUrl);
            //得到connection对象。
            connection = (HttpURLConnection) url.openConnection();
            //设置请求方式
            connection.setRequestMethod("GET");
            //连接
            connection.connect();
            //得到响应码
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                //得到响应流
                InputStream inputStream = connection.getInputStream();
                //将响应流转换成字符串
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String reponse = sb.toString();
                Address address = new Gson().fromJson(reponse, Address.class);
                System.out.println("结果" + address.toString());
                return address;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return null;
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
