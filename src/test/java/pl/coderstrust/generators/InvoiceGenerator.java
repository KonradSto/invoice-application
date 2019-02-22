package pl.coderstrust.generators;

import java.time.LocalDate;
import java.util.List;

import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

public class InvoiceGenerator {

    public static Invoice getRandomInvoice() {
        Long id = IdGenerator.getNextId();
        String number = InvoiceNumberGenerator.getNextInvoiceNumber();
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(2);
        Company seller = CompanyGenerator.getRandomCompany();
        Company buyer = CompanyGenerator.getRandomCompany();
        List<InvoiceEntry> entry = InvoiceEntryGenerator.getListOfRandomEntries();
        return new Invoice(id, number, issueDate, dueDate, seller, buyer, entry);
    }

    public static Invoice getRandomInvoiceWithoutId() {
        String number = InvoiceNumberGenerator.getNextInvoiceNumber();
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(2);
        Company seller = CompanyGenerator.getRandomCompany();
        Company buyer = CompanyGenerator.getRandomCompany();
        List<InvoiceEntry> entry = InvoiceEntryGenerator.getListOfRandomEntries();
        return new Invoice(null, number, issueDate, dueDate, seller, buyer, entry);
    }
}
