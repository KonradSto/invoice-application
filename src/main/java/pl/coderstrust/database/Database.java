package pl.coderstrust.database;

import java.util.Collection;

public interface Database {

    Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException;

    void deleteInvoice(Long id) throws DatabaseOperationException;

    Invoice getInvoice(Long id) throws DatabaseOperationException;

    Collection<Invoice> getAllInvoices() throws DatabaseOperationException;

    void deleteAll();

    boolean invoiceExists(Long id);

    long count();
}
