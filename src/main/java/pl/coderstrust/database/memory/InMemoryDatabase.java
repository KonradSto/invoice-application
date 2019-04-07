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

    public InMemoryDatabase(Map<Long, Invoice> databaseStorage) {
        log.debug("Launching to InMemoryDatabase");
        if (databaseStorage == null) {
            log.error("An error occurred during launching to InMemoryDatabase, database cannot be null");
            throw new IllegalArgumentException("Invoice storage cannot be null");
        }
        invoiceMap = databaseStorage;
    }

    @Override
    public synchronized Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        if (invoice == null) {
            log.error("Passed invoice cannot be null");
            throw new IllegalArgumentException("Invoice cannot be null");
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
            log.error("Update invoice failed. Invoice with following id does not exist: {} ", invoice.getId());
            throw new DatabaseOperationException(String.format("Update invoice failed. Invoice with following id does not exist: %d", invoice.getId()));
        }
        log.debug("Updating invoice: {}", invoice);
        Invoice updatedInvoice = new Invoice(invoice.getId(), invoice.getNumber(), invoice.getIssuedDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
        invoiceMap.put(invoice.getId(), updatedInvoice);
        return updatedInvoice;
    }

    @Override
    public synchronized void deleteInvoice(Long id) throws DatabaseOperationException {
        if (id == null) {
            log.error("An error occurred during deleting an invoice, invoice id cannot be null");
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (!invoiceMap.containsKey(id)) {
            log.error("An error occurred during deleting an invoice, invoice not found for passed id: {}", id);
            throw new DatabaseOperationException(String.format("Delete invoice failed. Invoice with following id does not exist: %d", id));
        }
        log.debug("Deleting invoice by id: {}", id);
        invoiceMap.remove(id);
    }

    @Override
    public synchronized Optional<Invoice> getInvoice(Long id) {
        if (id == null) {
            log.error("An error occurred during getting an invoice, invoice id cannot be null");
            throw new IllegalArgumentException("Id cannot be null");
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
            log.error("An error occurred during checking invoice existence, id cannot be null");
            throw new IllegalArgumentException("Id cannot be null");
        }
        return invoiceMap.containsKey(id);
    }

    @Override
    public synchronized long countInvoices() {
        log.debug("Counting number of invoices");
        return invoiceMap.size();
    }
}
