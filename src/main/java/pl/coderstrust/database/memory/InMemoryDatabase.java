package pl.coderstrust.database.memory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.coderstrust.controller.InvoiceController;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Invoice;

@ConditionalOnProperty(name = "database", havingValue = "memory")
@Repository
public class InMemoryDatabase implements Database {

    private static Logger log = LoggerFactory.getLogger(InvoiceController.class);

    private Map<Long, Invoice> invoiceMap;
    private Long nextId = 1L;
    private String message;

    public InMemoryDatabase(Map<Long, Invoice> databaseStorage) {
        log.debug("Launching to InMemoryDatabase");
        if (databaseStorage == null) {
            message = "An error occurred during launching to InMemoryDatabase, invoice storage cannot be null";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        invoiceMap = databaseStorage;
    }

    @Override
    public synchronized Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        if (invoice == null) {
            message = "An error occurred during saving invoice, invoice cannot be null";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        if (invoice.getId() == null) {
            return insertInvoice(invoice);
        }
        return updateInvoice(invoice);
    }

    private Invoice insertInvoice(Invoice invoice) {
        log.debug("Saving invoice: {}", invoice);
        Long id = nextId++;
        Invoice insertedInvoice = new Invoice(id, invoice.getNumber(), invoice.getIssuedDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
        invoiceMap.put(id, insertedInvoice);
        return insertedInvoice;
    }

    private Invoice updateInvoice(Invoice invoice) throws DatabaseOperationException {
        if (!invoiceMap.containsKey(invoice.getId())) {
            message = String.format("An error occurred during updating invoice, invoice with following id does not exist: %d", invoice.getId());
            log.error(message);
            throw new DatabaseOperationException(message);
        }
        log.debug("Updating invoice: {}", invoice);
        Invoice updatedInvoice = new Invoice(invoice.getId(), invoice.getNumber(), invoice.getIssuedDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
        invoiceMap.put(invoice.getId(), updatedInvoice);
        return updatedInvoice;
    }

    @Override
    public synchronized void deleteInvoice(Long id) throws DatabaseOperationException {
        if (id == null) {
            message = "An error occurred during deleting invoice, invoice id cannot be null";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        if (!invoiceMap.containsKey(id)) {
            message = String.format("An error occurred during deleting an invoice, invoice with following id does not exist: %d", id);
            log.error(message);
            throw new DatabaseOperationException(message);
        }
        log.debug("Deleting invoice by id: {}", id);
        invoiceMap.remove(id);
    }

    @Override
    public synchronized Optional<Invoice> getInvoice(Long id) {
        if (id == null) {
            message = "An error occurred during getting an invoice, invoice id cannot be null";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        log.debug("Getting invoice by id: {}", id);
        return Optional.ofNullable(invoiceMap.get(id));
    }

    @Override
    public synchronized Collection<Invoice> getAllInvoices() {
        log.debug("Getting all invoices");
        return invoiceMap.values();
    }

    @Override
    public synchronized void deleteAllInvoices() {
        log.debug("Deleting all invoices");
        invoiceMap.clear();
    }

    @Override
    public synchronized boolean invoiceExists(Long id) {
        log.debug("Checking invoice existence of id {}: ", id);
        if (id == null) {
            message = "An error occurred during checking invoice existence, id cannot be null";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return invoiceMap.containsKey(id);
    }

    @Override
    public synchronized long countInvoices() {
        log.debug("Counting number of invoices");
        return invoiceMap.size();
    }
}
