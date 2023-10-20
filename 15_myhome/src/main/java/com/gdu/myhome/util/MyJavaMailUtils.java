package com.gdu.myhome.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@PropertySource(value="classpath:email.properties")
@Component
public class MyJavaMailUtils {

  @Autowired
  private Environment env;
  
  public void sendJavaMail(String to, String title, String contents) {
    
    try {
      
      // Properties 객체 생성 (이메일 보내는 호스트 정보)
      Properties properties = new Properties();
      properties.put("mail.smtp.host", env.getProperty("spring.mail.host"));
      properties.put("mail.smtp.port", env.getProperty("spring.mail.host"));
      properties.put("mail.smtp.auth", env.getProperty("spring.mail.properties.mail.smtp.auth"));
      properties.put("mail.smtp.starttls.enable", env.getProperty("spring.mail.properties.mail.smtp.starttls.enable"));
      
      // javax.mail.Session 객체 생성 (이메일 보내는 사용자 정보)
      Session session = Session.getInstance(properties, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(env.getProperty("spring.mail.username"), env.getProperty("spring.mail.password"));
        }
      });
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
}
