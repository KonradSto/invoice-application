package pl.coderstrust.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.coderstrust.model.Invoice;

@Service
@ConfigurationProperties(prefix = "spring.mail")
public class InvoiceEmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private MailProperties mailProperties;

    public void sendEmail(Invoice invoice) throws MessagingException {
        MimeMessage email = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(email, true);
        //helper.setSubject(title);
        //helper.setText(content);
        helper.setTo(mailProperties.getProperties().get("receiver"));
        helper.setFrom("@coderstrust.pl");
        helper.addAttachment(String.format("%s.pdf", invoice.getNumber()), new ByteArrayResource(//invoiceService));
        emailSender.send(email);
    }
}
