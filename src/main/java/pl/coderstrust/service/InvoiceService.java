package pl.coderstrust.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;

class InvoiceService {

    private Database database;

    InvoiceService(Database database) {
        if (database != null) {
            this.database = database;
        } else {
            throw new IllegalArgumentException("Invalid database argument");
        }
    }

    Collection<Invoice> getAllInvoices() throws ServiceOperationException {
        try {
            return database.getAllInvoices();
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException(e.getMessage());
        }
    }

    Collection<Invoice> getAllInvoicesByDate(LocalDate fromDate, LocalDate toDate) throws ServiceOperationException {
        if (fromDate == null || toDate == null || fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("Passed date arguments are invalid");
        }
        try {
            return database.getAllInvoices()
                .stream()
                .filter(invoice -> (invoice.getIssuedDate().compareTo(fromDate) >= 0 && invoice.getIssuedDate().compareTo(toDate) <= 0))
                .collect(Collectors.toList());
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException(e.getMessage());
        }
    }

    Collection<Invoice> getAllInvoicesByBuyer(Company company) throws ServiceOperationException {
        try {
            return database.getAllInvoices()
                .stream()
                .filter(invoice -> (invoice.getBuyer().equals(company)))
                .collect(Collectors.toList());
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException(e.getMessage());
        }
    }

    Collection<Invoice> getAllInvoicesBySeller(Company company) throws ServiceOperationException {
        try {
            return database.getAllInvoices()
                .stream()
                .filter(invoice -> (invoice.getSeller().equals(company)))
                .collect(Collectors.toList());
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException(e.getMessage());
        }
    }

    Invoice getInvoice(Long id) throws ServiceOperationException {
        try {
            return database.getInvoice(id);
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException(e.getMessage());
        }
    }

    Invoice saveInvoice(Invoice invoice) throws ServiceOperationException {
        try {
            return database.saveInvoice(invoice);
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException(e.getMessage());
        }
    }

    void deleteInvoice(Long id) throws ServiceOperationException {
        try {
            database.deleteInvoice(id);
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException(e.getMessage());
        }
    }

    void deleteAllInvoices() throws ServiceOperationException {
        try {
            database.deleteAllInvoices();
        } catch (DatabaseOperationException e) {
            throw new ServiceOperationException(e.getMessage());
        }
    }
}
