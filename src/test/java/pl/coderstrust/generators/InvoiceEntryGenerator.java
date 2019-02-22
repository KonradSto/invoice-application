package pl.coderstrust.generators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.Vat;

public class InvoiceEntryGenerator {

    private static Long id = 0L;

    private static InvoiceEntry getRandomEntry() {
        Random random = new Random();
        Long id = entryCounter();
        String productName = WordGenerator.getRandomWord();
        double quantity = random.nextInt(10);
        String unit = "szt.";
        BigDecimal price = BigDecimal.valueOf(random.nextInt(2000));
        Vat vatRate = Vat.VAT_23;
        BigDecimal nettValue = BigDecimal.valueOf(quantity).multiply(price);
        BigDecimal grossValue = nettValue.multiply(BigDecimal.valueOf(1 + vatRate.getValue())).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return new InvoiceEntry(id, productName, quantity, unit, price, nettValue, grossValue, vatRate);
    }

    private static Long entryCounter() {
        return ++id;
    }

    public static List<InvoiceEntry> getListOfRandomEntries() {
        List<InvoiceEntry> invoiceEntryList = new ArrayList<>();
        invoiceEntryList.add(getRandomEntry());
        invoiceEntryList.add(getRandomEntry());
        id = 0L;
        return invoiceEntryList;
    }
}
