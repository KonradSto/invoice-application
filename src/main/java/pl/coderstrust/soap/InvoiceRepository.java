package pl.coderstrust.soap;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

import io.spring.guides.gs_producing_web_service.Invoice;
import io.spring.guides.gs_producing_web_service.Currency;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class InvoiceRepository {

    private static final Map<String, Invoice> invoices = new HashMap<>();

    @PostConstruct
    public void initData() {
        Invoice spain = new Invoice();
        spain.setName("Spain");
        spain.setCapital("Madrid");
        spain.setCurrency(Currency.EUR);
        spain.setPopulation(46704314);

        invoices.put(spain.getName(), spain);

        Invoice poland = new Invoice();
        poland.setName("Poland");
        poland.setCapital("Warsaw");
        poland.setCurrency(Currency.PLN);
        poland.setPopulation(38186860);

        invoices.put(poland.getName(), poland);

        Invoice uk = new Invoice();
        uk.setName("United Kingdom");
        uk.setCapital("London");
        uk.setCurrency(Currency.GBP);
        uk.setPopulation(63705000);

        invoices.put(uk.getName(), uk);
    }

    public Invoice findInvoice(String name) {
        Assert.notNull(name, "The invoice's name must not be null");
        return invoices.get(name);
    }
}