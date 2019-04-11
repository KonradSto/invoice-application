package pl.coderstrust.database.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.coderstrust.configuration.InFileDatabaseProperties;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.utils.ArgumentValidator;

@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "in-file")
@Repository
public class InFileDatabase implements Database {
    private static final String EXCEPTION_MESSAGE = "An error occurred during reading invoices from inFile database";
    private static final String DATABASE_NOT_EXIST = "InFileDatabase does not exist";
    private final InFileDatabaseProperties inFileDatabaseProperties;
    private ObjectMapper mapper;
    private FileHelper fileHelper;

    @Autowired
    public InFileDatabase(ObjectMapper mapper, FileHelper fileHelper, InFileDatabaseProperties inFileDatabaseProperties) {
        ArgumentValidator.ensureNotNull(mapper, "mapper");
        this.mapper = mapper;
        ArgumentValidator.ensureNotNull(fileHelper, "fileHelper");
        this.fileHelper = fileHelper;
        ArgumentValidator.ensureNotNull(inFileDatabaseProperties, "inFileDatabaseProperties");
        this.inFileDatabaseProperties = inFileDatabaseProperties;
    }

    @Override
    public synchronized Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        ArgumentValidator.ensureNotNull(invoice, "invoice");
        if (invoice.getId() == null) {
            return insertInvoice(invoice);
        }
        return update(invoice);
    }

    @Override
    public synchronized void deleteInvoice(Long id) throws DatabaseOperationException {
        ArgumentValidator.ensureNotNull(id, "id");
        // TODO: 10/04/2019 what if invoice does not exist - Map<Long,Boolean> (Big Omega=1) as a cache, exist method (Big Omega = n),
        try {
            // TODO: 11/04/2019 fileHelper(Long id) - deserialize line by line?
            List<String> invoicesAsJson = fileHelper.readLinesFromFile();
            for (int line = 0; line < invoicesAsJson.size(); line++) {
                Invoice invoice = mapper.readValue(invoicesAsJson.get(line), Invoice.class);
                if (id.equals(invoice.getId())) {
                    fileHelper.removeLine(++line);
                }
            }
        } catch (IOException e) {
            throw new DatabaseOperationException(EXCEPTION_MESSAGE, e);
        }
    }

    @Override
    public synchronized Optional<Invoice> getInvoice(Long id) throws DatabaseOperationException {
        ArgumentValidator.ensureNotNull(id, "id");
        try {
            if (!fileHelper.isExist()) {
                return Optional.empty();
            }
            List<String> invoicesAsJson = fileHelper.readLinesFromFile();
            for (String invoiceAsJson : invoicesAsJson) {
                Invoice invoice = mapper.readValue(invoiceAsJson, Invoice.class);
                if (id.equals(invoice.getId())) {
                    return Optional.of(invoice);
                }
            }
            return Optional.empty();
        } catch (IOException e) {
            throw new DatabaseOperationException(EXCEPTION_MESSAGE, e);
        }
    }

    @Override
    public synchronized Collection<Invoice> getAllInvoices() throws DatabaseOperationException {
        List<Invoice> invoices = new ArrayList<>();
        try {
            if (fileHelper.isEmpty()) {
                return invoices;
            }
            List<String> invoicesAsJson = fileHelper.readLinesFromFile();
            for (String invoiceAsJson : invoicesAsJson) {
                invoices.add(mapper.readValue(invoiceAsJson, Invoice.class));
            }
        } catch (IOException e) {
            throw new DatabaseOperationException(EXCEPTION_MESSAGE, e);
        }
        return invoices;
    }

    @Override
    public synchronized void deleteAllInvoices() throws DatabaseOperationException {
        if (!fileHelper.isExist()) {
            throw new DatabaseOperationException(DATABASE_NOT_EXIST);
        }
        try {
            fileHelper.clear();
        } catch (IOException e) {
            throw new DatabaseOperationException(DATABASE_NOT_EXIST, e);
        }
    }

    @Override
    public synchronized boolean invoiceExists(Long id) throws DatabaseOperationException {
        ArgumentValidator.ensureNotNull(id, "id");
        try {
            if (fileHelper.isEmpty()) {
                return false;
            }
            List<String> fileLines = fileHelper.readLinesFromFile();
            for (String fileLine : fileLines) {
                Invoice invoice = mapper.readValue(fileLine, Invoice.class);
                if (id.equals(invoice.getId())) {
                    return true;
                }
            }
        } catch (IOException e) {
            // TODO: 11/04/2019  root exception -
            throw new DatabaseOperationException(EXCEPTION_MESSAGE, e);
        }
        return false;
    }

    @Override
    public synchronized long countInvoices() throws DatabaseOperationException {
        return getAllInvoices().size();
    }

    private Invoice insertInvoice(Invoice invoice) throws DatabaseOperationException {
        if (!fileHelper.isExist()) {
            try {
                fileHelper.create();
            } catch (IOException e) {
                throw new DatabaseOperationException(DATABASE_NOT_EXIST, e);
            }
        }
        Invoice insertedInvoice;
        try {
            insertedInvoice = new Invoice(getNextId(), invoice.getNumber(), invoice.getIssuedDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
        } catch (IOException e) {
            throw new DatabaseOperationException(DATABASE_NOT_EXIST, e);
        }
        try {
            fileHelper.writeLine(mapper.writeValueAsString(insertedInvoice));
        } catch (IOException e) {
            throw new DatabaseOperationException(DATABASE_NOT_EXIST, e);
        }
        return insertedInvoice;
    }

    private Invoice update(Invoice invoice) throws DatabaseOperationException {
        if (!this.invoiceExists(invoice.getId())) {
            throw new DatabaseOperationException(String.format("Update invoice failed. Invoice with following id does not exist: %d", invoice.getId()));
        }
        try {
            Invoice updatedInvoice = new Invoice(invoice.getId(), invoice.getNumber(), invoice.getIssuedDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
            fileHelper.writeLine(mapper.writeValueAsString(updatedInvoice));
            this.deleteInvoice(invoice.getId());
            return updatedInvoice;
        } catch (IOException e) {
            throw new DatabaseOperationException(DATABASE_NOT_EXIST, e);
        }
    }

    private Long getNextId() throws IOException {
        final String errorMessage = "An error occurred during getting id for nextId";
        if (!fileHelper.isExist()) {
            try {
                fileHelper.create();
                return 1L;
            } catch (IOException e) {
                throw new IOException(errorMessage);
            }
        }
        try {
            // TODO: 10/04/2019  define in properties empty string - maybe put it to application properties and inject here and in other places instead declaring final

            if (fileHelper.isEmpty() || fileHelper.readLastLine().equals("")) {
                return 1L;
            }
        } catch (IOException e) {
            throw new IOException(errorMessage);
        }
        try {
            return this.countInvoices() + 1;
        } catch (DatabaseOperationException e) {
            throw new IOException(errorMessage);
        }
    }
}
