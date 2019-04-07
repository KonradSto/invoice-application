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

    private InvoicePdfService invoicePdfService;

    @Autowired
    public InvoiceEmailService(InvoicePdfService invoicePdfService) {
        this.invoicePdfService = invoicePdfService;
    }

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private MailProperties mailProperties;

    public void sendEmailWithInvoice(Invoice invoice) throws MessagingException, ServiceOperationException {
        MimeMessage email = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(email, true);
        helper.setTo("ctrust.group9@gmail.com");
        helper.setFrom("ctrust.group9@gmail.com");
        helper.setSubject(String.format("Invoice nr: %s", invoice.getNumber()));
        helper.setText("Please see attachments for your saved invoice");
        helper.addAttachment(String.format("%s.pdf", invoice.getNumber()), new ByteArrayResource(invoicePdfService.getInvoiceAsPdf(invoice)));
        emailSender.send(email);
    }
}
