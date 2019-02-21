package pl.coderstrust.generators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.Vat;

public class InvoiceEntryGenerator {

    private static Random random = new Random();
    private static Long id = IdGenerator.getNextId();
    private static String productName = WordGenerator.getRandomWord();
    private static double quantity = random.nextInt(10);
    private static String unit = "szt.";
    private static BigDecimal price = BigDecimal.valueOf(random.nextInt(2000));
    private static Vat vatRate = Vat.VAT_23;
    private static BigDecimal nettValue = BigDecimal.valueOf(quantity).multiply(price);
    private static BigDecimal grossValue = nettValue.multiply(BigDecimal.valueOf(1 + vatRate.getValue())).setScale(2, BigDecimal.ROUND_HALF_EVEN);

    private static InvoiceEntry getRandomEntry() {
        return new InvoiceEntry(id, productName, quantity, unit, price, nettValue, grossValue, vatRate);
    }

    public static List<InvoiceEntry> getListOfRandomEntries() {
        List<InvoiceEntry> invoiceEntryList = new ArrayList<>();
        invoiceEntryList.add(getRandomEntry());
        invoiceEntryList.add(getRandomEntry());
        return invoiceEntryList;
    }
}
