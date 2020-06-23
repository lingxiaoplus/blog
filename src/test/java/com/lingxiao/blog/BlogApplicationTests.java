package com.lingxiao.blog;

import com.google.gson.Gson;
import com.lingxiao.blog.bean.Address;
import com.lingxiao.blog.bean.BingImageData;
import com.lingxiao.blog.service.file.impl.FileServiceImpl;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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


    @Test
    void testSignle() throws ParseException {
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.format(startDate);
        System.out.println("时间格式化"+startDate);*/
        boolean rqSjFormat = isRqFormat("2008-05-12");
        System.out.println("是否是日期格式"+rqSjFormat);
    }

    public int[] signleNumbers(int[] nums){
        int currentNum = nums[0];
        for (int i = 0; i < nums.length; i++) {
            currentNum = Math.max(nums[i], currentNum + nums[i]);

        }
        return null;
    }


    /***
     * 判断字符串是否是yyyyMMddHHmmss格式
     * @param mes 字符串
     * @return boolean 是否是日期格式
     */
    /***
     * 判断字符串是否是yyyyMMdd格式
     * @param mes 字符串
     * @return boolean 是否是日期格式
     */
    public static boolean isRqFormat(String mes){
        String format = "([0-9]{4})(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(mes);
        if (matcher.matches()) {
            pattern = Pattern.compile("(\\d{4})(\\d{2})(\\d{2})");
            matcher = pattern.matcher(mes);
            if (matcher.matches()) {
                int y = Integer.valueOf(matcher.group(1));
                int m = Integer.valueOf(matcher.group(2));
                int d = Integer.valueOf(matcher.group(3));
                if (d > 28) {
                    Calendar c = Calendar.getInstance();
                    c.set(y, m-1, 1);
                    //每个月的最大天数
                    int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                    return (lastDay >= d);
                }
            }
            return true;
        }
        return false;

    }

    @Test
    public void reverseString() {
        /*long beforDayStart = getBeforDayStart(1);
        Date date = new Date(beforDayStart);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);*/
        FileServiceImpl fileService = new FileServiceImpl();
    }

}
