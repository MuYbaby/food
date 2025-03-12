package com.muybaby.food.utils;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.Random;

public class EmailUtil {

    // 配置邮件服务器信息 - 针对163邮箱
    private static final String HOST = "smtp.163.com";
    private static final String USERNAME = "yuki_a@163.com"; // 替换为您的163邮箱
    private static final String PASSWORD = "LTZ8iKUk4CqBqP3p"; // 使用163邮箱的授权码，不是登录密码
    private static final int PORT = 465; // 163邮箱的SSL端口

    // 生成验证码
    public static String generateVerificationCode() {
        Random random = new Random();
        // 生成4位数验证码
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }

    // 发送验证码邮件
    public static void sendVerificationEmail(String toEmail, String code) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true"); // 使用SSL连接
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.socketFactory.port", PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // 创建会话
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        // 开启调试模式可以查看更多连接信息
//        session.setDebug(true);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(USERNAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("账户注册验证码");
//        message.setText("您的验证码是: " + code + "，有效期为5分钟，请勿泄露给他人。");
        String htmlContent =
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e1e1e1; border-radius: 5px;'>" +
                        "<div style='text-align: center; padding: 10px; background-color: #f8f8f8; border-bottom: 1px solid #e1e1e1;'>" +
                        "<h2 style='color: #333; margin: 0;'>账户注册验证</h2>" +
                        "</div>" +
                        "<div style='padding: 20px; text-align: center;'>" +
                        "<p style='font-size: 16px; color: #555;'>您好，</p>" +
                        "<p style='font-size: 16px; color: #555;'>感谢您注册我们的服务。请使用以下验证码完成注册流程：</p>" +
                        "<div style='margin: 30px auto; padding: 15px; background-color: #f5f5f5; border-radius: 4px; width: 200px; text-align: center;'>" +
                        "<span style='font-size: 24px; font-weight: bold; letter-spacing: 5px; color: #333;'>" + code + "</span>" +
                        "</div>" +
                        "<p style='font-size: 14px; color: #777;'>此验证码有效期为<strong>5分钟</strong>，请勿泄露给他人。</p>" +
                        "</div>" +
                        "<div style='padding: 15px; background-color: #f8f8f8; border-top: 1px solid #e1e1e1; text-align: center; font-size: 12px; color: #999;'>" +
                        "<p>此邮件由系统自动发送，请勿回复。</p>" +
                        "<p>© " + java.time.Year.now().getValue() + " MuYbaby。</p>" +
                        "</div>" +
                        "</div>";

// Set HTML content
        message.setContent(htmlContent, "text/html; charset=UTF-8");

// Send message
        Transport.send(message);

    }
}