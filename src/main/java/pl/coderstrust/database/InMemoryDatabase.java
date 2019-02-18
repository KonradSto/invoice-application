package pl.coderstrust.database;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;

public class InMemoryDatabase implements Database {

    private Map<Long, Invoice> invoiceMap;
    private Long nextId = 1L;

    public InMemoryDatabase(Map<Long, Invoice> invoiceStorage) {
        this.invoiceMap = invoiceStorage;
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        if (invoice == null) {
            throw new DatabaseOperationException("Invoice cannot be null");
        }
        if (invoice.getId() == null) {
            return insertInvoice(invoice);
        }
        return updateInvoice(invoice);
    }

    private Invoice insertInvoice(Invoice invoice) {
        final Long id = nextId++;
        final String number = invoice.getNumber();
        final LocalDate issuedDate = invoice.getIssuedDate();
        final LocalDate dueDate = invoice.getDueDate();
        final Company seller = invoice.getSeller();
        final Company buyer = invoice.getBuyer();
        final List<InvoiceEntry> entries = invoice.getEntries();
        Invoice insertedInvoice = new Invoice(id, number, issuedDate, dueDate, seller, buyer, entries);
        invoiceMap.put(id, insertedInvoice);
        return insertedInvoice;
    }

    private Invoice updateInvoice(Invoice invoice) throws DatabaseOperationException {
        if (!invoiceMap.containsKey(invoice.getId())) {
            throw new DatabaseOperationException("Invoice does not exist");
        } else {
            final Long id = invoice.getId();
            final String number = invoice.getNumber();
            final LocalDate issuedDate = invoice.getIssuedDate();
            final LocalDate dueDate = invoice.getDueDate();
            final Company seller = invoice.getSeller();
            final Company buyer = invoice.getBuyer();
            final List<InvoiceEntry> entries = invoice.getEntries();
            Invoice updatedInvoice = new Invoice(id, number, issuedDate, dueDate, seller, buyer, entries);
            invoiceMap.put(id, updatedInvoice);
            return updatedInvoice;
        }
    }

    @Override
    public void deleteInvoice(Long id) {
        invoiceMap.remove(id);
    }

    @Override
    public Invoice getInvoice(Long id) throws DatabaseOperationException {
        if (!invoiceMap.containsKey(id)) {
            throw new DatabaseOperationException("Invoice of id: " + id + " does not exist");
        }
        return invoiceMap.get(id);
    }

    @Override
    public Collection<Invoice> getAllInvoices() {
        return invoiceMap.values();
    }

    @Override
    public void deleteAllInvoices() {
        invoiceMap.clear();
    }

    @Override
    public boolean invoiceExists(Long id) {
        return invoiceMap.containsKey(id);
    }

    @Override
    public long countInvoices() {
        return invoiceMap.size();
    }
}
