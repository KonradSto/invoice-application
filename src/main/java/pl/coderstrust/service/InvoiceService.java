package pl.coderstrust.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.utils.ArgumentValidator;

class InvoiceService {

    private Database database;

    InvoiceService(Database database) {
        ArgumentValidator.ensureNotNull(database, "database");
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
        ArgumentValidator.ensureNotNull(fromDate, "fromDate");
        ArgumentValidator.ensureNotNull(toDate, "toDate");
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
        ArgumentValidator.ensureNotNull(id, "id");
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
        ArgumentValidator.ensureNotNull(id, "id");
        try {
            return database.getAllInvoices()
                .stream()
                .filter(invoice -> (invoice.getSeller().getId().equals(id)))
                .collect(Collectors.toList());
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException("An error occurred during getting all invoices for chosen seller.", e);
        }
    }

    Optional<Invoice> getInvoice(Long id) throws ServiceOperationException {
        ArgumentValidator.ensureNotNull(id, "id");
        try {
            return database.getInvoice(id);
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException("An error occurred during getting invoice.", e);
        }
    }

    Invoice saveInvoice(Invoice invoice) throws ServiceOperationException {
        ArgumentValidator.ensureNotNull(invoice, "invoice");
        try {
            return database.saveInvoice(invoice);
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException("An error occurred during saving invoice.", e);
        }
    }

    void deleteInvoice(Long id) throws ServiceOperationException {
        ArgumentValidator.ensureNotNull(id, "id");
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
}
