package pl.coderstrust.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String emailAddress, String title, String content) throws MessagingException {
        MimeMessage email = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(email, true);
        helper.setSubject(title);
        helper.setText(content);
        helper.setTo(emailAddress);
        helper.setFrom("@coderstrust.pl");
        helper.addAttachment("attachement", new ClassPathResource("classPath"));
        emailSender.send(email);
    }
}
