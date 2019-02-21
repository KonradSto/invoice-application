package pl.coderstrust.generators;

import java.time.LocalDate;
import java.util.List;

import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

public class InvoiceGenerator {

    private static Long id = IdGenerator.getNextId();
    private static String number = InvoiceNumberGenerator.getNextInvoiceNumber();
    private static LocalDate issueDate = LocalDate.now();
    private static LocalDate dueDate = issueDate.plusDays(2);
    private static Company seller = CompanyGenerator.getRandomCompany();
    private static Company buyer = CompanyGenerator.getRandomCompany();
    private static List<InvoiceEntry> entry = InvoiceEntryGenerator.getListOfRandomEntries();

    public static Invoice getRandomInvoice() {
        return new Invoice(id, number, issueDate, dueDate, seller, buyer, entry);
    }

    public static Invoice getRandomInvoiceWithoutId() {
        return new Invoice(null, number, issueDate, dueDate, seller, buyer, entry);
    }
}
