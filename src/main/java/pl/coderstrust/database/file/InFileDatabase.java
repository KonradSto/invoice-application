package pl.coderstrust.database.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.coderstrust.configuration.InFileDatabaseProperties;
import pl.coderstrust.controller.InvoiceController;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.utils.ArgumentValidator;

@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "in-file")
@Repository
public class InFileDatabase implements Database {

    private static final String IO_EXCEPTION_MESSAGE = "An error occurred during accessing inFile database";
    private static final String DATABASE_NOT_EXIST = "InFileDatabase is not exist";
    private static Logger log = LoggerFactory.getLogger(InvoiceController.class);
    private ObjectMapper mapper;
    private FileHelper fileHelper;

    @Autowired
    public InFileDatabase(ObjectMapper mapper, FileHelper fileHelper, InFileDatabaseProperties inFileDatabaseProperties) {
        log.debug("Load InFileDatabase");
        ArgumentValidator.ensureNotNull(mapper, "mapper");
        this.mapper = mapper;
        ArgumentValidator.ensureNotNull(fileHelper, "fileHelper");
        this.fileHelper = fileHelper;
        ArgumentValidator.ensureNotNull(inFileDatabaseProperties, "inFileDatabaseProperties");
    }

    @Override
    public synchronized Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        ArgumentValidator.ensureNotNull(invoice, "invoice");
        if (invoice.getId() == null) {
            log.debug("Inserting invoice");
            return insertInvoice(invoice);
        }
        log.debug("Updating invoice");
        return update(invoice);
    }

    @Override
    public synchronized void deleteInvoice(Long id) throws DatabaseOperationException {
        log.debug("Deleting invoice by id: {}", id);
        ArgumentValidator.ensureNotNull(id, "id");
        try {
            List<String> invoicesAsJson = fileHelper.readLinesFromFile();
            for (int line = 0; line < invoicesAsJson.size(); line++) {
                Invoice invoice = mapper.readValue(invoicesAsJson.get(line), Invoice.class);
                if (id.equals(invoice.getId())) {
                    fileHelper.removeLine(++line);
                    log.debug("Invoice with id: {} successfully deleted", id);
                    return;
                }
            }
        } catch (IOException e) {
            log.error(IO_EXCEPTION_MESSAGE);
            throw new DatabaseOperationException(IO_EXCEPTION_MESSAGE, e);
        }
        String message = String.format("Failed to delete invoice. Invoice with following id is not exist: %d", id);
        log.error(message);
        throw new DatabaseOperationException(message);
    }

    @Override
    public synchronized Optional<Invoice> getInvoice(Long id) throws DatabaseOperationException {
        log.debug("Getting invoice by id: {}", id);
        ArgumentValidator.ensureNotNull(id, "id");
        try {
            List<String> invoicesAsJson = fileHelper.readLinesFromFile();
            for (String invoiceAsJson : invoicesAsJson) {
                Invoice invoice = mapper.readValue(invoiceAsJson, Invoice.class);
                if (id.equals(invoice.getId())) {
                    return Optional.of(invoice);
                }
            }
            log.debug("Failed to get invoice. Invoice with following id: {} is not exist", id);
            return Optional.empty();
        } catch (IOException e) {
            log.error(IO_EXCEPTION_MESSAGE);
            throw new DatabaseOperationException(IO_EXCEPTION_MESSAGE, e);
        }
    }

    @Override
    public synchronized Collection<Invoice> getAllInvoices() throws DatabaseOperationException {
        log.debug("Getting all invoices");
        List<Invoice> invoices = new ArrayList<>();
        try {
            List<String> invoicesAsJson = fileHelper.readLinesFromFile();
            for (String invoiceAsJson : invoicesAsJson) {
                invoices.add(mapper.readValue(invoiceAsJson, Invoice.class));
            }
        } catch (IOException e) {
            log.error(IO_EXCEPTION_MESSAGE);
            throw new DatabaseOperationException(IO_EXCEPTION_MESSAGE, e);
        }
        log.debug("Getting {} invoices.", invoices.size());
        return invoices;
    }

    @Override
    public synchronized void deleteAllInvoices() throws DatabaseOperationException {
        log.debug("Deleting all invoices");
        try {
            fileHelper.clear();
        } catch (IOException e) {
            log.error(DATABASE_NOT_EXIST);
            throw new DatabaseOperationException(DATABASE_NOT_EXIST, e);
        }
    }

    @Override
    public synchronized boolean invoiceExists(Long id) throws DatabaseOperationException {
        log.debug("Checking invoice existence of id {}: ", id);
        ArgumentValidator.ensureNotNull(id, "id");
        try {
            if (fileHelper.isEmpty()) {
                return false;
            }
            List<String> fileLines = fileHelper.readLinesFromFile();
            for (String fileLine : fileLines) {
                Invoice invoice = mapper.readValue(fileLine, Invoice.class);
                if (id.equals(invoice.getId())) {
                    log.debug("Invoice with id {} exist.", id);
                    return true;
                }
            }
        } catch (IOException e) {
            log.error(IO_EXCEPTION_MESSAGE);
            throw new DatabaseOperationException(IO_EXCEPTION_MESSAGE, e);
        }
        log.debug("Invoice with id {} is not exist.", id);
        return false;
    }

    @Override
    public synchronized long countInvoices() throws DatabaseOperationException {
        long number = getAllInvoices().size();
        log.debug("{} invoices counted", number);
        return number;
    }

    private Invoice insertInvoice(Invoice invoice) throws DatabaseOperationException {
        log.debug("Adding invoice: {}", invoice);
        if (!fileHelper.isExist()) {
            try {
                fileHelper.create();
            } catch (IOException e) {
                log.error(DATABASE_NOT_EXIST);
                throw new DatabaseOperationException(DATABASE_NOT_EXIST, e);
            }
        }
        Invoice insertedInvoice;
        Long id = getNextId();
        insertedInvoice = new Invoice(id, invoice.getNumber(), invoice.getIssuedDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
        try {
            fileHelper.writeLine(mapper.writeValueAsString(insertedInvoice));
            log.debug("Invoice with assigned id : {} added successfully. {}", id, invoice);
        } catch (IOException e) {
            log.error(DATABASE_NOT_EXIST);
            throw new DatabaseOperationException(DATABASE_NOT_EXIST, e);
        }
        return insertedInvoice;
    }

    private Invoice update(Invoice invoice) throws DatabaseOperationException {
        log.debug("Updating invoice: {}", invoice);
        try {
            Invoice updatedInvoice = new Invoice(invoice.getId(), invoice.getNumber(), invoice.getIssuedDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
            fileHelper.writeLine(mapper.writeValueAsString(updatedInvoice));
            deleteInvoice(invoice.getId());
            log.debug("Invoice: {} updated successfully", invoice);
            return updatedInvoice;
        } catch (IOException e) {
            log.error(DATABASE_NOT_EXIST);
            throw new DatabaseOperationException(DATABASE_NOT_EXIST, e);
        }
    }

    private Long getNextId() throws DatabaseOperationException {
        return countInvoices() + 1;
    }
}
