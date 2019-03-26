package pl.coderstrust.database;

import java.util.Collection;
import java.util.Optional;

import pl.coderstrust.model.Invoice;

// TODO: 26/03/2019 synchronize
// TODO: 26/03/2019  atomic counter
public class InfileDataBase implements Database {
   private String inFileDatabasePath;

    public InfileDataBase(String inFileDatabasePath) {
        this.inFileDatabasePath = inFileDatabasePath;
    }

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
}
