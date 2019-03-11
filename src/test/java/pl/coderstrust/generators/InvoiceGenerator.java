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
        List<InvoiceEntry> entry = InvoiceEntryGenerator.getRandomEntries(2);
        return new Invoice(id, number, issueDate, dueDate, seller, buyer, entry);
    }

    public static Invoice getRandomInvoiceWithSpecificBuyerId(Long id) {
        Long invoiceId = IdGenerator.getNextId();
        String number = InvoiceNumberGenerator.getNextInvoiceNumber();
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(2);
        Company seller = CompanyGenerator.getRandomCompany();
        Company buyer = CompanyGenerator.getRandomCompanyWithSpecificId(id);
        List<InvoiceEntry> entry = InvoiceEntryGenerator.getRandomEntries(2);
        return new Invoice(invoiceId, number, issueDate, dueDate, seller, buyer, entry);
    }

    public static Invoice getRandomInvoiceWithSpecificSellerId(Long id) {
        Long invoiceId = IdGenerator.getNextId();
        String number = InvoiceNumberGenerator.getNextInvoiceNumber();
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(2);
        Company seller = CompanyGenerator.getRandomCompanyWithSpecificId(id);
        Company buyer = CompanyGenerator.getRandomCompany();
        List<InvoiceEntry> entry = InvoiceEntryGenerator.getRandomEntries(2);
        return new Invoice(invoiceId, number, issueDate, dueDate, seller, buyer, entry);
    }

    public static Invoice getRandomInvoiceWithoutId() {
        String number = InvoiceNumberGenerator.getNextInvoiceNumber();
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(2);
        Company seller = CompanyGenerator.getRandomCompany();
        Company buyer = CompanyGenerator.getRandomCompany();
        List<InvoiceEntry> entry = InvoiceEntryGenerator.getRandomEntries(2);
        return new Invoice(null, number, issueDate, dueDate, seller, buyer, entry);
    }

    public static Invoice getRandomInvoiceWithSpecificIssueDate(LocalDate date) {
        Long id = IdGenerator.getNextId();
        String number = InvoiceNumberGenerator.getNextInvoiceNumber();
        LocalDate issueDate = date;
        LocalDate dueDate = issueDate.plusDays(2);
        Company seller = CompanyGenerator.getRandomCompany();
        Company buyer = CompanyGenerator.getRandomCompany();
        List<InvoiceEntry> entry = InvoiceEntryGenerator.getRandomEntries(2);
        return new Invoice(id, number, issueDate, dueDate, seller, buyer, entry);
    }
}
