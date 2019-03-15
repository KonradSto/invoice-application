package pl.coderstrust.database;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import pl.coderstrust.model.Invoice;

public class InMemoryDatabase implements Database {

    private Map<Long, Invoice> invoiceMap;
    private Long nextId = 1L;

    public InMemoryDatabase(Map<Long, Invoice> databaseStorage) {
        if (databaseStorage == null) {
            throw new IllegalArgumentException("Invoice storage cannot be null");
        }
        invoiceMap = databaseStorage;
    }

    @Override
    public synchronized Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        if (invoice.getId() == null) {
            return insertInvoice(invoice);
        }
        return updateInvoice(invoice);
    }

    private Invoice insertInvoice(Invoice invoice) {
        Long id = nextId++;
        Invoice insertedInvoice = new Invoice(id, invoice.getNumber(), invoice.getIssuedDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
        invoiceMap.put(id, insertedInvoice);
        return insertedInvoice;
    }

    private Invoice updateInvoice(Invoice invoice) throws DatabaseOperationException {
        if (!invoiceMap.containsKey(invoice.getId())) {
            throw new DatabaseOperationException(String.format("Update invoice failed. Invoice with following id does not exist: %d", invoice.getId()));
        }
        Invoice updatedInvoice = new Invoice(invoice.getId(), invoice.getNumber(), invoice.getIssuedDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
        invoiceMap.put(invoice.getId(), updatedInvoice);
        return updatedInvoice;
    }

    @Override
    public synchronized void deleteInvoice(Long id) throws DatabaseOperationException {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (!invoiceMap.containsKey(id)) {
            throw new DatabaseOperationException(String.format("Delete invoice failed. Invoice with following id does not exist: %d", id));
        }
        invoiceMap.remove(id);
    }

    @Override
    public synchronized Optional<Invoice> getInvoice(Long id) throws DatabaseOperationException {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (!invoiceMap.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(invoiceMap.get(id));
    }

    @Override
    public synchronized Collection<Invoice> getAllInvoices() {
        return invoiceMap.values();
    }

    @Override
    public synchronized void deleteAllInvoices() {
        invoiceMap.clear();
    }

    @Override
    public synchronized boolean invoiceExists(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return invoiceMap.containsKey(id);
    }

    @Override
    public synchronized long countInvoices() {
        return invoiceMap.size();
    }
}
