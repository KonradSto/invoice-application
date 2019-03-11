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
            throw new IllegalArgumentException("Database cannot be null.");
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
            throw new IllegalArgumentException("From date cannot be null.");
        }
        if (toDate == null) {
            throw new IllegalArgumentException("To date cannot be null.");
        }
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("From date cannot be after to date.");
        }
        try {
            return database.getAllInvoices()
                .stream()
                .filter(invoice -> (invoice.getIssuedDate().compareTo(fromDate) >= 0 && invoice.getIssuedDate().compareTo(toDate) <= 0))
                .collect(Collectors.toList());
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException("An error occurred during getting all invoices in given date range.", e);
        }
    }

    Collection<Invoice> getAllInvoicesByBuyer(Long id) throws ServiceOperationException {
        validateIdArgumentForNull(id);
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
        validateIdArgumentForNull(id);
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
        validateIdArgumentForNull(id);
        try {
            return database.getInvoice(id);
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException("An error occurred during getting invoice.", e);
        }
    }

    Invoice saveInvoice(Invoice invoice) throws ServiceOperationException {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        try {
            return database.saveInvoice(invoice);
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException("An error occurred during saving invoice.", e);
        }
    }

    void deleteInvoice(Long id) throws ServiceOperationException {
        validateIdArgumentForNull(id);
        try {
            database.deleteInvoice(id);
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException("An error occurred during deleting invoice.", e);
        }
    }

    void deleteAllInvoices() throws ServiceOperationException {
        try {
            database.deleteAllInvoices();
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException("An error occurred during deleting all invoices", e);
        }
    }

    private void validateIdArgumentForNull(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
    }
}
