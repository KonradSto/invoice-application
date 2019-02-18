package pl.coderstrust.generators;

import java.time.LocalDate;
import java.util.List;

import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

public class InvoiceGenerator {

    private Long id = null;
    private String number = InvoiceNumberGenerator.getNextInvoiceNumber();
    private LocalDate issueDate = LocalDate.now();
    private LocalDate dueDate = LocalDate.now();
    private Company seller = new CompanyGenerator().getRandomCompany();
    private Company buyer = new CompanyGenerator().getRandomCompany();
    private List<InvoiceEntry> entry = null;

    public Invoice getRandomInvoice() {
        return new Invoice(id, number, issueDate, dueDate, seller, buyer, entry);
    }
}
