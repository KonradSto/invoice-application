package pl.coderstrust.controller;

import java.time.LocalDate;
import java.util.Collection;

import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;

public interface InvoiceService {

    Collection<Invoice> getAllInvoices() throws DatabaseOperationException;

    Collection<Invoice> getAllInvoices(LocalDate fromDate, LocalDate toDate) throws DatabaseOperationException;

    Collection<Invoice> getAllInvoices(Company company) throws DatabaseOperationException;

    Invoice getInvoice(Long id) throws DatabaseOperationException;

    Invoice addInvoice(Invoice invoice) throws DatabaseOperationException;

    Invoice updateInvoice(Invoice invoice) throws DatabaseOperationException;

    void deleteInvoice(Long id) throws DatabaseOperationException;

    void deleteAllInvoices() throws DatabaseOperationException;
}