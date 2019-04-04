package pl.coderstrust.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.coderstrust.configuration.InFileDatabaseProperties;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.utils.ArgumentValidator;

// TODO: 26/03/2019 synchronize
// TODO: 26/03/2019  atomic counter
@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "in-file")
@Repository
public class InFileDatabase implements Database {
    private final InFileDatabaseProperties inFileDatabaseProperties;
    private ObjectMapper mapper;
    private FileHelper fileHelper;
    private Long nextId;

    @Autowired
    public InFileDatabase(ObjectMapper mapper, FileHelper fileHelper, InFileDatabaseProperties inFileDatabaseProperties) {
        this.mapper = mapper;
        this.fileHelper = fileHelper;
        this.inFileDatabaseProperties = inFileDatabaseProperties;
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        ArgumentValidator.ensureNotNull(invoice, "invoice");
        if (invoice.getId() == null) {
            return insertInvoice(invoice);
        }
        return update(invoice);
    }

    private Invoice update(Invoice invoice) throws DatabaseOperationException {
        /*   try {
         *//*if (!inFileDataBase.invoiceExists(invoice.getId())) {
                throw new DatabaseOperationException(String.format("Update invoice failed. Invoice with following id does not exist: %d", invoice.getId()));
            }*//*
            Invoice updatedInvoice = new Invoice(invoice.getId(), invoice.getNumber(), invoice.getIssuedDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
            fileHelper.writeLine(toJson(updatedInvoice));
            return updatedInvoice;
        } catch (DatabaseOperationException | IOException e) {
            throw new DatabaseOperationException("sfsd");
        }*/
        return null;
    }

    @Override
    public void deleteInvoice(Long id) throws DatabaseOperationException {

    }

    @Override
    public Optional<Invoice> getInvoice(Long id) throws DatabaseOperationException {
        return Optional.empty();
    }

    @Override
    public Collection<Invoice> getAllInvoices() throws DatabaseOperationException {
        return null;
    }

    @Override
    public void deleteAllInvoices() throws DatabaseOperationException {
        try {
            fileHelper.clear();
        } catch (IOException e) {
            throw new DatabaseOperationException("Cannot delete.......");
        }

    }

    @Override
    public boolean invoiceExists(Long id) throws DatabaseOperationException {
        ArgumentValidator.ensureNotNull(id, "id");
        // TODO: 27/03/2019 compare id with count invoices and then invoke reverse read - nie da sie teraz
        try {
            List<String> fileLines = fileHelper.readLinesFromFile();
            // TODO: 27/03/2019 parse just Id not whole object ?
            for (String fileLine : fileLines) {
                Invoice invoice = mapper.readValue(fileLine, Invoice.class);
                if (id.equals(invoice.getId())) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            throw new DatabaseOperationException("dfdf");
        } catch (IOException e) {
            // TODO: 27/03/2019 what exception to throw here
            throw new DatabaseOperationException("sfsds");
        }
        // TODO: 27/03/2019 what to return below  maybe exception

        return false;
    }

    @Override
    public long countInvoices() throws DatabaseOperationException {

        return 0;
    }

    Invoice insertInvoice(Invoice invoice) throws DatabaseOperationException {
        Long id = nextId++;
        Invoice insertedInvoice = new Invoice(id, invoice.getNumber(), invoice.getIssuedDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
        try {
            fileHelper.writeLine(toJson(insertedInvoice));
        } catch (IOException e) {
            // TODO: 27/03/2019 2 or 3 exceptions catched here
            throw new DatabaseOperationException("Save invoice failed");

        }
        return insertedInvoice;
    }

    String toJson(Invoice invoice) throws JsonProcessingException {
        return mapper.writeValueAsString(invoice);
    }

    Invoice getInvoiceFromJson(String invoiceAsJson) throws IOException {
        return mapper.readValue(invoiceAsJson, Invoice.class);
    }
}
