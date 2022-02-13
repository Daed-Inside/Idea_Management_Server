package web.application.IdeaManagement.config;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

import static web.application.IdeaManagement.constant.MailDomainConstant.*;


@Component
public class MailSenderFactory {
    public JavaMailSender getSender(String email, String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        String mailDomain = email.substring(email.indexOf("@"), email.length());
        if (mailDomain.equalsIgnoreCase(GmailDomain)) {
            mailSender.setHost(GmailHost);
            mailSender.setPort(GmailPort);
        }

        mailSender.setUsername(email);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", GmailPort);
        props.put("mail.smtp.ssl.enable", false);
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.timeout", 60000);
        props.put("mail.smtp.connectiontimeout", 60000);
        props.put("mail.smtp.port", GmailHost);

        return mailSender;
    }
}
