package pl.coderstrust.soap;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import io.spring.guides.gs_producing_web_service.Company;
import io.spring.guides.gs_producing_web_service.Invoice;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


@Component
public class InvoiceRepository {

    private static final Map<Long, Invoice> invoices = new HashMap<>();

    public InvoiceRepository() throws DatatypeConfigurationException {
    }

    @PostConstruct
    public void initData() throws DatatypeConfigurationException {
        Invoice invoice1 = new Invoice();
        XMLGregorianCalendar xgc = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        GregorianCalendar now = new GregorianCalendar();
        xgc.setYear(now.get(Calendar.YEAR));
        xgc.setMonth(now.get(Calendar.MONTH) + 1);
        xgc.setDay(now.get(Calendar.DAY_OF_MONTH));
        invoice1.setId(1);
        invoice1.setNumber("111");
        invoice1.setIssuedDate(xgc);
        invoice1.setLocalDate(xgc);
        Company company1 = new Company();
        invoice1.setSeller(company1);
        invoice1.setBuyer(company1);
//        List<InvoiceEntry> invoices1
//        InvoiceEntry invoiceEntry1 = new InvoiceEntry();
//        invoiceEntry1.setProductName("sth");
//
        invoices.put(invoice1.getId(), invoice1);

        Invoice poland = new Invoice();
        poland.setId(2);
        poland.setNumber("222");
        poland.setIssuedDate(xgc);
        poland.setLocalDate(xgc);
        poland.setSeller(company1);
        poland.setBuyer(company1);

        invoices.put(poland.getId(), poland);

        Invoice uk = new Invoice();
        uk.setId(3);
        uk.setNumber("333");
        uk.setIssuedDate(xgc);
        uk.setLocalDate(xgc);
        uk.setSeller(company1);
        uk.setBuyer(company1);

        invoices.put(uk.getId(), uk);
    }

    public Invoice findInvoice(Long id) {
        Assert.notNull(id, "The invoice's id must not be null");
        return invoices.get(id);
    }
}