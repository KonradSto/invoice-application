package pl.coderstrust.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.coderstrust.controller.InvoiceController;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.utils.ArgumentValidator;

@Service
public class InvoiceService {

    private static Logger log = LoggerFactory.getLogger(InvoiceController.class);
    private Database database;
    private String message;

    InvoiceService(Database database) {
        log.debug("Launching to InvoiceService with database: {}", database);
        ArgumentValidator.ensureNotNull(database, "database");
        this.database = database;
    }

    public Collection<Invoice> getAllInvoices() throws ServiceOperationException {
        try {
            log.debug("Getting all invoices");
            return database.getAllInvoices();
        } catch (DatabaseOperationException e) {
            message = "An error occurred during getting all invoices.";
            log.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<Invoice> getAllInvoicesByDate(LocalDate fromDate, LocalDate toDate) throws ServiceOperationException {
        ArgumentValidator.ensureNotNull(fromDate, "fromDate");
        ArgumentValidator.ensureNotNull(toDate, "toDate");
        if (fromDate.isAfter(toDate)) {
            message = "fromDate cannot be after toDate.";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        try {
            log.debug("Getting all invoices by dates: from {} to {}", fromDate, toDate);
            return database.getAllInvoices()
                .stream()
                .filter(invoice -> (invoice.getIssuedDate().compareTo(fromDate) >= 0 && invoice.getIssuedDate().compareTo(toDate) <= 0))
                .collect(Collectors.toList());
        } catch (DatabaseOperationException e) {
            message = "An error occurred during getting all invoices by dates.";
            log.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<Invoice> getAllInvoicesByBuyer(Long id) throws ServiceOperationException {
        ArgumentValidator.ensureNotNull(id, "id");
        try {
            log.debug("Getting all invoices by buyer: {}", id);
            return database.getAllInvoices()
                .stream()
                .filter(invoice -> (invoice.getBuyer().getId().equals(id)))
                .collect(Collectors.toList());
        } catch (DatabaseOperationException e) {
            message = "An error occurred during getting all invoices by buyer.";
            log.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Collection<Invoice> getAllInvoicesBySeller(Long id) throws ServiceOperationException {
        ArgumentValidator.ensureNotNull(id, "id");
        try {
            log.debug("Getting all invoices by seller: {}", id);
            return database.getAllInvoices()
                .stream()
                .filter(invoice -> (invoice.getSeller().getId().equals(id)))
                .collect(Collectors.toList());
        } catch (DatabaseOperationException e) {
            message = "An error occurred during getting all invoices by seller.";
            log.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Optional<Invoice> getInvoice(Long id) throws ServiceOperationException {
        ArgumentValidator.ensureNotNull(id, "id");
        try {
            log.debug("Getting an invoice by id: {}", id);
            return database.getInvoice(id);
        } catch (DatabaseOperationException e) {
            message = "An error occurred during getting invoice.";
            log.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public Invoice saveInvoice(Invoice invoice) throws ServiceOperationException {
        ArgumentValidator.ensureNotNull(invoice, "invoice");
        try {
            log.debug("Saving invoice: {}", invoice);
            return database.saveInvoice(invoice);
        } catch (DatabaseOperationException e) {
            message = "An error occurred during saving an invoice.";
            log.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public void deleteInvoice(Long id) throws ServiceOperationException {
        ArgumentValidator.ensureNotNull(id, "id");
        try {
            log.debug("Deleting invoice by id: {}", id);
            database.deleteInvoice(id);
        } catch (DatabaseOperationException e) {
            message = "An error occurred during deleting an invoice.";
            log.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }

    public void deleteAllInvoices() throws ServiceOperationException {
        try {
            log.debug("Deleting all invoices");
            database.deleteAllInvoices();
        } catch (DatabaseOperationException e) {
            message = "An error occurred during deleting all invoices.";
            log.error(message, e);
            throw new ServiceOperationException(message, e);
        }
    }
}
