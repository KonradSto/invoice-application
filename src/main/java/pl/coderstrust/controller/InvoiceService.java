package pl.coderstrust.controller;

import java.time.LocalDate;
import java.util.List;

import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;

public interface InvoiceService {

    List<Invoice> getAllInvoices() throws DatabaseOperationException;

    List getAllInvoices(LocalDate fromDate, LocalDate toDate) throws DatabaseOperationException;

    List getAllInvoices(Company company) throws DatabaseOperationException;

    Invoice getInvoice(Long id) throws DatabaseOperationException;

    Invoice addInvoice(Invoice invoice) throws DatabaseOperationException;

    Invoice updateInvoice(Long id);

    Invoice deleteInvoice(Long id);

    Invoice deleteAllInvoices();
}
