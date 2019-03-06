package pl.coderstrust.database;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;

@Component
public class InvoiceServiceImpl implements pl.coderstrust.controller.InvoiceService {

    Database database;

    public InvoiceServiceImpl(Database database) {
        Map<Long, Invoice> invoiceMap = new HashMap<>();
        database = new InMemoryDatabase(invoiceMap);
    }

    @Override
    public Collection<Invoice> getAllInvoices() throws DatabaseOperationException {
        return database.getAllInvoices();
    }

    @Override
    public Collection<Invoice> getAllInvoices(LocalDate fromDate, LocalDate toDate) throws DatabaseOperationException {
        if (isFromDateLaterThanToDate(fromDate, toDate)) {
            LocalDate temp = fromDate;
            fromDate = toDate;
            toDate = temp;
        }

        LocalDate finalFromDate = fromDate;
        LocalDate finalToDate = toDate;
        Predicate<Invoice> isInvoiceIssuedWithinGivenDateRange = invoice -> invoice.getIssuedDate().compareTo(finalFromDate) >= 0 && invoice.getIssuedDate().compareTo(finalToDate) <= 0;
        return database.getAllInvoices().stream()
            .filter(isInvoiceIssuedWithinGivenDateRange)
            .collect(Collectors.toList());
    }

    @Override
    public Collection<Invoice> getAllInvoices(Company company) throws DatabaseOperationException {
        return database.getAllInvoices().stream()
            .filter(invoice -> invoice.getBuyer().equals(company) || invoice.getSeller().equals(company))
            .collect(Collectors.toList());
    }

    @Override
    public Invoice getInvoice(Long id) throws DatabaseOperationException {
        return database.getInvoice(id);
    }

    @Override
    public Invoice addInvoice(Invoice invoice) throws DatabaseOperationException {
        return database.saveInvoice(invoice);
    }

    @Override
    public Invoice updateInvoice(Invoice invoice) throws DatabaseOperationException {
        return database.saveInvoice(invoice);
    }

    @Override
    public void deleteInvoice(Long id) throws DatabaseOperationException {
        database.deleteInvoice(id);
    }

    @Override
    public void deleteAllInvoices() throws DatabaseOperationException {
        database.deleteAllInvoices();
    }

    private boolean isFromDateLaterThanToDate(LocalDate fromDate, LocalDate toDate) {
        return fromDate.compareTo(toDate) > 0;
    }
}
