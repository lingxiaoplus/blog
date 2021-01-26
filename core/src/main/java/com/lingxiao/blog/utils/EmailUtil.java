package com.lingxiao.blog.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.security.GeneralSecurityException;
import java.util.Properties;


/**
 * @author admin
 */
@Component
public class EmailUtil {
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Data
    public static class EmailConfigure {
        private String sendAddress;
        private String authCode;
        private String receiveAddress;
        private String title;
        //验证码
        private String verifyCode;
        //分钟
        private int minute;
        private String host;
        private String protocol;
    }

    @Async("taskExecutor")
    public void sendEmail(final EmailConfigure configure) throws MessagingException, GeneralSecurityException {
        Properties prop = new Properties();
        //设置QQ邮件服务器
        prop.setProperty("mail.host", configure.host);
        //邮件发送协议
        prop.setProperty("mail.transport.protocol", configure.protocol);
        //需要验证用户名密码
        prop.setProperty("mail.smtp.auth", "true");

        // 关于QQ邮箱，还要设置SSL加密，加上以下代码即可
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);

        //使用JavaMail发送邮件的5个步骤
        //创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                //发件人邮件用户名、授权码
                return new PasswordAuthentication(configure.getSendAddress(), configure.getAuthCode());
            }
        });
        //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(true);
        //2、通过session得到transport对象
        Transport ts = session.getTransport();
        //3、使用邮箱的用户名和授权码连上邮件服务器
        ts.connect(configure.host, configure.getSendAddress(), configure.getAuthCode());
        //4、创建邮件
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress(configure.getSendAddress()));
        //指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(configure.getReceiveAddress()));
        //邮件的标题
        message.setSubject(configure.getTitle());
        //邮件的文本内容

            /*// 准备图片数据
            MimeBodyPart image = new MimeBodyPart();
            DataHandler dh = new DataHandler(new FileDataSource("src/resources/bz.jpg"));
            image.setDataHandler(dh);
            image.setContentID("bz.jpg");

            // 准备正文数据
            MimeBodyPart text = new MimeBodyPart();
            text.setContent("这是一封邮件正文带图片<img src='cid:bz.jpg'>的邮件", "text/html;charset=UTF-8");

            // 描述数据关系
            MimeMultipart mm = new MimeMultipart();
            mm.addBodyPart(text);
            mm.addBodyPart(image);
            mm.setSubType("related");*/

        Context context = new Context();
        context.setVariable("expireTime", configure.getMinute());
        context.setVariable("verifyCode", configure.getVerifyCode());
        String emailText = templateEngine.process("email", context);

        MimeBodyPart text = new MimeBodyPart();
        text.setContent(emailText, "text/html;charset=UTF-8");
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(text);
        mm.setSubType("related");
        message.setContent(mm);
        //5、发送邮件
        ts.sendMessage(message, message.getAllRecipients());
        ts.close();
    }
}
