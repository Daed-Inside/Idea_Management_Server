package web.application.IdeaManagement.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import web.application.IdeaManagement.config.MailSenderFactory;
import web.application.IdeaManagement.model.request.MailRequest;

@Service
public class MailManager {
    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    public MailSenderFactory mailSenderFactory;

    //    @Override
    public Boolean sendMail(MailRequest request) {
        // Create a Simple MailMessage.
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(request.getReceiver());
        message.setSubject(request.getSubject());
        message.setText(request.getContent());

        // Send Message!
        this.emailSender.send(message);
        return true;
    }

    public Boolean sendMailWithCustomSender(String mail, String password, MailRequest request) {
        JavaMailSender customSender = mailSenderFactory.getSender(mail, password);
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(request.getReceiver());
        message.setSubject(request.getSubject());
        message.setText(request.getContent());
        customSender.send(message);
        return true;
    }
}
