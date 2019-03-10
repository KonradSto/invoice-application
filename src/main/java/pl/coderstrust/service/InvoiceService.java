package pl.coderstrust.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Invoice;

class InvoiceService {

    private Database database;

    InvoiceService(Database database) {
        if (database == null) {
            throw new IllegalArgumentException("Invalid database argument");
        }
        this.database = database;
    }

    Collection<Invoice> getAllInvoices() throws ServiceOperationException {
        try {
            return database.getAllInvoices();
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException("An error occurred during getting all invoices.", e);
        }
    }

    Collection<Invoice> getAllInvoicesByDate(LocalDate fromDate, LocalDate toDate) throws ServiceOperationException {
        if (fromDate == null) {
            throw new IllegalArgumentException("Initial date can not be null.");
        }
        if (toDate == null) {
            throw new IllegalArgumentException("Final date can not be null.");
        }
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("Initial date can not be after final date.");
        }
        try {
            return database.getAllInvoices()
                .stream()
                .filter(invoice -> (invoice.getIssuedDate().compareTo(fromDate) >= 0 && invoice.getIssuedDate().compareTo(toDate) <= 0))
                .collect(Collectors.toList());
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException("An error occurred during getting all invoices by given date.", e);
        }
    }

    Collection<Invoice> getAllInvoicesByBuyer(Long id) throws ServiceOperationException {
        try {
            return database.getAllInvoices()
                .stream()
                .filter(invoice -> (invoice.getBuyer().getId().equals(id)))
                .collect(Collectors.toList());
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException("An error occurred during getting all invoices for chosen buyer.", e);
        }
    }

    Collection<Invoice> getAllInvoicesBySeller(Long id) throws ServiceOperationException {
        try {
            return database.getAllInvoices()
                .stream()
                .filter(invoice -> (invoice.getSeller().getId().equals(id)))
                .collect(Collectors.toList());
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException("An error occurred during getting all invoices for chosen seller.", e);
        }
    }

    Invoice getInvoice(Long id) throws ServiceOperationException {
        if (id == null) {
            throw new IllegalArgumentException("Invoice id can not be null");
        }
        try {
            return database.getInvoice(id);
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException("An error occurred during getting invoice id: " + id + ".", e);
        }
    }

    Invoice saveInvoice(Invoice invoice) throws ServiceOperationException {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice can not be null");
        }
        try {
            return database.saveInvoice(invoice);
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException("An error occurred during saving invoice: " + invoice + ".", e);
        }
    }

    void deleteInvoice(Long id) throws ServiceOperationException {
        if (id == null) {
            throw new IllegalArgumentException("Invoice id can not be null");
        }
        try {
            database.deleteInvoice(id);
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException("An error occurred during deleting invoice id: " + id + ".", e);
        }
    }

    void deleteAllInvoices() throws ServiceOperationException {
        try {
            database.deleteAllInvoices();
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException("An error occurred during deleting all invoices", e);
        }
    }
}
