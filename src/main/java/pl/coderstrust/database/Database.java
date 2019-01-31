package pl.coderstrust.database;

import java.util.Collection;

public interface Database {

    Invoice saveInvoice(Invoice invoice) throws DatabaseOperationException;

    void deleteInvoice(Long id) throws DatabaseOperationException;

    Invoice getInvoice(Long id) throws DatabaseOperationException;

    Collection<Invoice> getAllInvoices() throws DatabaseOperationException;

    void deleteAllInvoices() throws DatabaseOperationException;

    boolean invoiceExists(Long id) throws DatabaseOperationException;

    long countInvoices() throws DatabaseOperationException;
}
