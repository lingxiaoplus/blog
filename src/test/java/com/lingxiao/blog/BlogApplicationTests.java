package com.lingxiao.blog;

import com.google.gson.Gson;
import com.lingxiao.blog.bean.Address;
import com.lingxiao.blog.bean.BingImageData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@SpringBootTest
class BlogApplicationTests {

    @Test
    void contextLoads() {
        String id = UUID.randomUUID().toString();
        System.out.println("生成的id"+id);
    }


    @Test
    void testHttpUrlConnection(){
        HttpURLConnection connection = null;
        try {
            String apiUrl = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=8&mkt=zh-CN";
            URL url = new URL(apiUrl);
            //得到connection对象。
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
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
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer sb = new StringBuffer();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String reponse = sb.toString();
                BingImageData bingImageData = new Gson().fromJson(reponse, BingImageData.class);
                System.out.println("结果" + bingImageData.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connection != null){
                connection.disconnect();
            }
        }
    }

    @Test
    void getRealAddrFromIp(){
        HttpURLConnection connection = null;
        try {
            String apiUrl = "http://whois.pconline.com.cn/ipJson.jsp?ip=119.4.144.148&json=true";
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
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer sb = new StringBuffer();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String reponse = sb.toString();
                Address address = new Gson().fromJson(reponse, Address.class);
                System.out.println("结果" + address.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connection != null){
                connection.disconnect();
            }
        }
    }

}
