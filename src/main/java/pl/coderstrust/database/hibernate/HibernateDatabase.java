package pl.coderstrust.database.hibernate;

import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.coderstrust.controller.InvoiceController;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.utils.ArgumentValidator;

@ConditionalOnProperty(name = "database", havingValue = "hibernate")
@Repository
public class HibernateDatabase implements Database {

    private static Logger log = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    private InvoiceRepository invoiceRepository;

    public HibernateDatabase(InvoiceRepository invoiceRepository) {
        log.debug("Launching to HibernateDatabase");
        ArgumentValidator.ensureNotNull(invoiceRepository, "invoiceRepository");
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) {
        log.debug("Saving invoice: {}", invoice);
        ArgumentValidator.ensureNotNull(invoice, "invoice");
        return invoiceRepository.save(invoice);
    }

    @Override
    public void deleteInvoice(Long id) throws DatabaseOperationException {
        log.debug("Deleting invoice by id: {}", id);
        ArgumentValidator.ensureNotNull(id, "id");
        if (!invoiceRepository.existsById(id)) {
            String message = String.format("An error occurred during deleting an invoice, invoice with following id does not exist: %d", id);
            log.error(message);
            throw new DatabaseOperationException(message);
        }
        invoiceRepository.deleteById(id);
    }

    @Override
    public Optional<Invoice> getInvoice(Long id) {
        log.debug("Getting invoice by id: {}", id);
        ArgumentValidator.ensureNotNull(id, "id");
        return invoiceRepository.findById(id);
    }

    @Override
    public Collection<Invoice> getAllInvoices() {
        log.debug("Getting all invoices");
        return invoiceRepository.findAll();
    }

    @Override
    public void deleteAllInvoices() {
        log.debug("Deleting all invoices");
        invoiceRepository.deleteAll();
    }

    @Override
    public boolean invoiceExists(Long id) {
        log.debug("Verifying if passed invoice exists");
        ArgumentValidator.ensureNotNull(id, "id");
        return invoiceRepository.existsById(id);
    }

    @Override
    public long countInvoices() {
        log.debug("Counting number of invoices");
        return invoiceRepository.count();
    }
}
