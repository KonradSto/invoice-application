package pl.coderstrust.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.utils.ArgumentValidator;

// TODO: 26/03/2019 synchronize
// TODO: 26/03/2019  atomic counter
public class InFileDataBase implements Database {

    private String inFileDatabasePath;
    @Autowired
    private ObjectMapper mapper;
    //  @Autowired
    private FileHelper fileHelper;
    private Long nextId = 1L;

    //  @Autowired
    public InFileDataBase(String inFileDatabasePath) {
        this.inFileDatabasePath = inFileDatabasePath;
        FileHelper fileHelper = new FileHelper(inFileDatabasePath);
        this.fileHelper = fileHelper;
        this.inFileDatabasePath = inFileDatabasePath;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.mapper = mapper;
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
        ArgumentValidator.ensureNotNull(invoice, "invoice");
        if (invoice.getId() == null) {
            return insertInvoice(invoice);
        }
        return update(invoice);
    }

    private Invoice update(Invoice invoice) {
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
                return false;
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

    private Invoice insertInvoice(Invoice invoice) throws DatabaseOperationException {
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

    private String toJson(Invoice invoice) throws JsonProcessingException {
        return mapper.writeValueAsString(invoice);
    }

    private Invoice getInvoiceFromJson(String invoiceAsJson) throws IOException {
        return mapper.readValue(invoiceAsJson, Invoice.class);
    }
}
