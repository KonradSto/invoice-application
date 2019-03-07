package pl.coderstrust.controller;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;

public interface InvoiceService {

    Collection<Invoice> getAllInvoices() throws DatabaseOperationException;

    default Collection<Invoice> getAllInvoices(LocalDate fromDate, LocalDate toDate) throws DatabaseOperationException {
        return Collections.emptyList();
    }

    default Collection<Invoice> getAllInvoices(Company company) throws DatabaseOperationException {
        return Collections.emptyList();
    }

    default Invoice getInvoice(Long id) throws DatabaseOperationException {
        return new Invoice(null, null, null, null, null, null, null);
    }

    default Invoice addInvoice(Invoice invoice) throws DatabaseOperationException {
        return new Invoice(null, null, null, null, null, null, null);
    }

    default Invoice updateInvoice(Invoice invoice) throws DatabaseOperationException {
        return new Invoice(null, null, null, null, null, null, null);
    }

    default void deleteInvoice(Long id) throws DatabaseOperationException {
    }

    default void deleteAllInvoices() throws DatabaseOperationException {
    }
}
