package pl.coderstrust.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import pl.coderstrust.model.Invoice;

// TODO: 26/03/2019 synchronize
// TODO: 26/03/2019  atomic counter
public class InfileDataBase implements Database {
    private String inFileDatabasePath;
    @Autowired
    private ObjectMapper mapper;
    private FileHelper fileHelper;
    private Long nextId = 1L;


    @Override
    public Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException {
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
        return false;
    }

    @Override
    public long countInvoices() throws DatabaseOperationException {
        return 0;
    }

    private String toJson(Invoice invoice) throws JsonProcessingException {
        return mapper.writeValueAsString(invoice);
    }

    private Invoice getInvoice(String invoiceAsJson) throws IOException {
        return mapper.readValue(invoiceAsJson, Invoice.class);
    }
}
