package com.lingxiao.blog.utils;

import lombok.Data;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Component
public class EmailUtil {
    @Data
    public static class EmailConfigure {
        private String sendAddress;
        private String authCode;
        private String receiveAddress;
        private String title;
        //private Object content;
        private String verifyCode;  //验证码
        private int minute; //分钟
    }

    @Async("taskExecutor")
    public void sendEmail(final EmailConfigure configure) throws MessagingException {
        Properties prop = new Properties();
        prop.setProperty("mail.host", "smtp.qq.com"); //// 设置QQ邮件服务器
        prop.setProperty("mail.transport.protocol", "smtp"); // 邮件发送协议
        prop.setProperty("mail.smtp.auth", "true"); // 需要验证用户名密码

        // 关于QQ邮箱，还要设置SSL加密，加上以下代码即可
       /* MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);*/

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
        ts.connect("smtp.qq.com", configure.getSendAddress(), configure.getAuthCode());
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
        MimeBodyPart text = new MimeBodyPart();
        text.setContent(htmlContent(configure.getVerifyCode(),configure.getMinute()), "text/html;charset=UTF-8");
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(text);
        mm.setSubType("related");

        //configure.getContent()
        message.setContent(mm);

        //5、发送邮件
        ts.sendMessage(message, message.getAllRecipients());
        ts.close();
    }


    private static String htmlContent(String verifyCode, int minute) {
        StringBuilder builder = new StringBuilder();
        builder.append("<html xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n" +
                "xmlns:w=\"urn:schemas-microsoft-com:office:word\"\n" +
                "xmlns:m=\"http://schemas.microsoft.com/office/2004/12/omml\"\n" +
                "xmlns=\"http://www.w3.org/TR/REC-html40\">\n" +
                "\n" +
                "<body lang=ZH-CN style='tab-interval:21.0pt'>\n" +
                "\n" +
                "<div class=WordSection1>\n" +
                "\n" +
                "<p class=MsoNormal><span lang=EN-US><o:p>&nbsp;</o:p></span></p>\n" +
                "\n" +
                "<table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0 width=\"100%\"\n" +
                " style='width:100.0%;border-collapse:collapse;mso-yfti-tbllook:1184;mso-padding-alt:\n" +
                " 0cm 0cm 0cm 0cm'>\n" +
                " <tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes;mso-yfti-lastrow:yes'>\n" +
                "  <td style='padding:0cm 0cm 0cm 0cm'>\n" +
                "  <div align=center>\n" +
                "  <table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0 width=600\n" +
                "   style='width:450.0pt;border-collapse:collapse;mso-yfti-tbllook:1184;\n" +
                "   mso-padding-alt:0cm 0cm 0cm 0cm'>\n" +
                "   <tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes;height:47.25pt'>\n" +
                "    <td style='background:#3B485B;padding:0cm 0cm 0cm 0cm;height:47.25pt'>\n" +
                "    <table class=MsoNormalTable border=0 cellpadding=0 style='mso-cellspacing:\n" +
                "     1.5pt;mso-yfti-tbllook:1184'>\n" +
                "     <tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes;mso-yfti-lastrow:yes'>\n" +
                "      <td style='padding:3.75pt .75pt .75pt 15.0pt'>\n" +
                "      <p class=MsoNormal><span style='font-family:\"微软雅黑\",sans-serif;color:white'>注册<span\n" +
                "      lang=EN-US>-</span>凌霄的博客<span lang=EN-US><o:p></o:p></span></span></p>\n" +
                "      </td>\n" +
                "     </tr>\n" +
                "    </table>\n" +
                "    </td>\n" +
                "   </tr>\n" +
                "   <tr style='mso-yfti-irow:1'>\n" +
                "    <td style='padding:0cm 0cm 0cm 0cm'>\n" +
                "    <table class=MsoNormalTable border=0 cellspacing=0 cellpadding=0\n" +
                "     width=\"100%\" style='width:100.0%;mso-cellspacing:0cm;mso-yfti-tbllook:\n" +
                "     1184;mso-padding-alt:0cm 0cm 0cm 0cm'>\n" +
                "     <tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes'>\n" +
                "      <td width=73 valign=top style='width:54.75pt;border-top:solid #D9D9D9 1.0pt;\n" +
                "      border-left:solid #D9D9D9 1.0pt;border-bottom:none;border-right:none;\n" +
                "      mso-border-top-alt:solid #D9D9D9 .75pt;mso-border-left-alt:solid #D9D9D9 .75pt;\n" +
                "      padding:0cm 0cm 0cm 0cm'></td>\n" +
                "      <td valign=top style='border:none;border-top:solid #D9D9D9 1.0pt;\n" +
                "      mso-border-top-alt:solid #D9D9D9 .75pt;padding:0cm 0cm 0cm 0cm'>\n" +
                "      <div>\n" +
                "      <p class=MsoNormal style='margin-bottom:12.0pt;mso-line-height-alt:7.5pt'><span\n" +
                "      lang=EN-US style='font-size:10.5pt'><br>\n" +
                "      <br style='mso-special-character:line-break'>\n" +
                "      <![if !supportLineBreakNewLine]><br style='mso-special-character:line-break'>\n" +
                "      <![endif]><o:p></o:p></span></p>\n" +
                "      </div>\n" +
                "      <div>\n" +
                "      <p class=MsoNormal style='margin-bottom:13.5pt;line-height:13.5pt'><span\n" +
                "      style='font-size:13.5pt;font-family:\"微软雅黑\",sans-serif;color:#444444'>尊敬的用户：<span\n" +
                "      lang=EN-US><br style='mso-special-character:line-break'>\n" +
                "      <![if !supportLineBreakNewLine]><br style='mso-special-character:line-break'>\n" +
                "      <![endif]><o:p></o:p></span></span></p>\n" +
                "      </div>\n" +
                "      <div>\n" +
                "      <p class=MsoNormal style='line-height:16.5pt'><b><span style='font-size:\n" +
                "      10.5pt;font-family:\"微软雅黑\",sans-serif;color:#444444'>您本次注册验证码为：</span></b><b><span\n" +
                "      lang=EN-US style='font-size:10.5pt;font-family:\"微软雅黑\",sans-serif;\n" +
                "      color:red'>")
                .append(verifyCode)
                .append("</span></b><b><span lang=EN-US style='font-size:10.5pt;\n" +
                        "      font-family:\"微软雅黑\",sans-serif;color:#444444'><o:p></o:p></span></b></p>\n" +
                        "      </div>\n" +
                        "      <div>\n" +
                        "      <p class=MsoNormal style='margin-bottom:12.0pt;mso-line-height-alt:7.5pt'><span\n" +
                        "      lang=EN-US style='font-size:10.5pt'><o:p>&nbsp;</o:p></span></p>\n" +
                        "      </div>\n" +
                        "      <div>\n" +
                        "      <p class=MsoNormal style='line-height:16.5pt'><span style='font-size:\n" +
                        "      10.5pt;font-family:\"微软雅黑\",sans-serif;color:#666666'>验证码<span lang=EN-US>")
                .append(minute)
                .append("</span>分钟内有效，请尽快完成注册！<span\n" +
                        "      lang=EN-US><o:p></o:p></span></span></p>\n" +
                        "      </div>\n" +
                        "      <div>\n" +
                        "      <p class=MsoNormal style='margin-bottom:12.0pt;mso-line-height-alt:7.5pt'><span\n" +
                        "      lang=EN-US style='font-size:10.5pt'><br>\n" +
                        "      <br style='mso-special-character:line-break'>\n" +
                        "      <![if !supportLineBreakNewLine]><br style='mso-special-character:line-break'>\n" +
                        "      <![endif]><o:p></o:p></span></p>\n" +
                        "      </div>\n" +
                        "      </td>\n" +
                        "      <td width=65 valign=top style='width:48.75pt;border-top:solid #D9D9D9 1.0pt;\n" +
                        "      border-left:none;border-bottom:none;border-right:solid #D9D9D9 1.0pt;\n" +
                        "      mso-border-top-alt:solid #D9D9D9 .75pt;mso-border-right-alt:solid #D9D9D9 .75pt;\n" +
                        "      padding:0cm 0cm 0cm 0cm'></td>\n" +
                        "     </tr>\n" +
                        "     <tr style='mso-yfti-irow:1'>\n" +
                        "      <td style='border:none;border-left:solid #D9D9D9 1.0pt;mso-border-left-alt:\n" +
                        "      solid #D9D9D9 .75pt;padding:0cm 0cm 0cm 0cm'>\n" +
                        "      <p class=MsoNormal><span lang=EN-US>&nbsp;</span></p>\n" +
                        "      </td>\n" +
                        "      <td valign=top style='padding:0cm 0cm 0cm 0cm'>\n" +
                        "      \n" +
                        "      <div style='min-height:1px'>\n" +
                        "      <p class=MsoNormal style='mso-line-height-alt:.75pt;background:#E0E0E0'><span\n" +
                        "      lang=EN-US style='font-size:1.0pt;color:#999999'>&nbsp;<o:p></o:p></span></p>\n" +
                        "      </div>\n" +
                        "      <div>\n" +
                        "      <p class=MsoNormal style='line-height:10.0pt'><span lang=EN-US\n" +
                        "      style='font-size:9.0pt;font-family:\"微软雅黑\",sans-serif;color:#999999'><br>\n" +
                        "      <br>\n" +
                        "      </span><span style='font-size:9.0pt;font-family:\"微软雅黑\",sans-serif;\n" +
                        "      color:#999999'>此邮件由凌霄博客系统自动发出，请勿回复！<span lang=EN-US><o:p></o:p></span></span></p>\n" +
                        "      </div>\n" +
                        "      </td>\n" +
                        "      <td style='border:none;border-right:solid #D9D9D9 1.0pt;mso-border-right-alt:\n" +
                        "      solid #D9D9D9 .75pt;padding:0cm 0cm 0cm 0cm'>\n" +
                        "      <p class=MsoNormal><span lang=EN-US>&nbsp;</span></p>\n" +
                        "      </td>\n" +
                        "     </tr>\n" +
                        "     <tr style='mso-yfti-irow:2;mso-yfti-lastrow:yes'>\n" +
                        "      <td colspan=3 style='border:solid #D9D9D9 1.0pt;border-top:none;\n" +
                        "      mso-border-left-alt:solid #D9D9D9 .75pt;mso-border-bottom-alt:solid #D9D9D9 .75pt;\n" +
                        "      mso-border-right-alt:solid #D9D9D9 .75pt;padding:0cm 0cm 0cm 0cm'>\n" +
                        "      <div style='min-height:42px'>\n" +
                        "      <p class=MsoNormal style='line-height:31.5pt'><span lang=EN-US\n" +
                        "      style='font-size:31.5pt'>&nbsp;<o:p></o:p></span></p>\n" +
                        "      </div>\n" +
                        "      </td>\n" +
                        "     </tr>\n" +
                        "    </table>\n" +
                        "    </td>\n" +
                        "   </tr>\n" +
                        "   \n" +
                        "  </table>\n" +
                        "  </div>\n" +
                        "  </td>\n" +
                        " </tr>\n" +
                        "</table>\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "</div>\n" +
                        "\n" +
                        "</body>\n" +
                        "\n" +
                        "</html>");
        return builder.toString();
    }
}
