package pl.coderstrust.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import pl.coderstrust.model.Invoice;

public interface InvoiceService {

    default Collection<Invoice> getAllInvoices() throws ServiceOperationException {
        return new ArrayList<>();
    }

    default Collection<Invoice> getAllInvoicesByDate(LocalDate fromDate, LocalDate toDate) throws ServiceOperationException {
        return new ArrayList<>();
    }

    default Collection<Invoice> getAllInvoicesByBuyer(Long id) throws ServiceOperationException {
        return new ArrayList<>();
    }

    default Collection<Invoice> getAllInvoicesBySeller(Long id) throws ServiceOperationException {
        return new ArrayList<>();
    }

    default Invoice getInvoice(Long id) throws ServiceOperationException {
        return null;
    }

    default Invoice saveInvoice(Invoice invoice) throws ServiceOperationException {
        return null;
    }

    default void deleteInvoice(Long id) throws ServiceOperationException {
    }

    void deleteAllInvoices() throws ServiceOperationException;
}
