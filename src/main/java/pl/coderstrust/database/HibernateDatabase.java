package pl.coderstrust.database;

import java.util.Collection;
import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.coderstrust.model.Invoice;

@ConditionalOnProperty(name = "database", havingValue = "hibernate")
@Repository
public class HibernateDatabase implements Database {

    private InvoiceRepository invoiceRepository;

    public HibernateDatabase(InvoiceRepository invoiceRepository) {
        if (invoiceRepository == null) {
            throw new IllegalArgumentException("Invoice repository cannot be null");
        }
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        return invoiceRepository.save(invoice);
    }

    @Override
    public void deleteInvoice(Long id) throws DatabaseOperationException {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (!invoiceRepository.existsById(id)) {
            throw new DatabaseOperationException(String.format("Delete invoice failed. Invoice with following id does not exist: %d", id));
        }
        invoiceRepository.deleteById(id);
    }

    @Override
    public Optional<Invoice> getInvoice(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return invoiceRepository.findById(id);
    }

    @Override
    public Collection<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @Override
    public void deleteAllInvoices() {
        invoiceRepository.deleteAll();
    }

    @Override
    public boolean invoiceExists(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return invoiceRepository.existsById(id);
    }

    @Override
    public long countInvoices() {
        return invoiceRepository.count();
    }
}
