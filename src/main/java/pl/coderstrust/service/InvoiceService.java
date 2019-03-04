package pl.coderstrust.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;

public class InvoiceService {

    private Database database;

    public InvoiceService(Database database) {
        this.database = database;
    }

    Collection<Invoice> getAllInvoices() throws DatabaseOperationException {
        return new ArrayList<>(database.getAllInvoices());
    }

    Collection<Invoice> getAllInvoices(LocalDate fromDate, LocalDate toDate) throws DatabaseOperationException {
        return this.getAllInvoices()
            .stream()
            .filter(invoice -> (invoice.getIssuedDate().compareTo(fromDate) >= 0 && invoice.getIssuedDate().compareTo(toDate) <= 0))
            .collect(Collectors.toList());
    }

    Collection<Invoice> getAllInvoices(Company company) throws DatabaseOperationException {
        return this.getAllInvoices()
            .stream()
            .filter(invoice -> (invoice.getSeller().equals(company) || invoice.getBuyer().equals(company)))
            .collect(Collectors.toList());
    }

    Invoice getInvoice(Long id) throws DatabaseOperationException {
        return this.database.getInvoice(id);
    }

    Invoice addInvoice(Invoice invoice) throws DatabaseOperationException {
        return this.database.saveInvoice(invoice);
    }

    void deleteInvoice(Long id) throws DatabaseOperationException {
        this.database.deleteInvoice(id);
    }

    void deleteAllInvoices() throws DatabaseOperationException {
        this.database.deleteAllInvoices();
    }
}
