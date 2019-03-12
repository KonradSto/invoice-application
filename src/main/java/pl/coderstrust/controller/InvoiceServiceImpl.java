package pl.coderstrust.controller;

import org.springframework.stereotype.Service;
import pl.coderstrust.model.Invoice;

import java.time.LocalDate;
import java.util.Collection;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Override
    public Collection<Invoice> getAllInvoices() throws ServiceOperationException {
        return null;
    }

    @Override
    public Collection<Invoice> getAllInvoicesByDate(LocalDate fromDate, LocalDate toDate) throws ServiceOperationException {
        return null;
    }

    @Override
    public Collection<Invoice> getAllInvoicesByBuyer(Long id) throws ServiceOperationException {
        return null;
    }

    @Override
    public Collection<Invoice> getAllInvoicesBySeller(Long id) throws ServiceOperationException {
        return null;
    }

    @Override
    public Invoice getInvoice(Long id) throws ServiceOperationException {
        return null;
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) throws ServiceOperationException {
        return null;
    }

    @Override
    public void deleteInvoice(Long id) throws ServiceOperationException {

    }

    @Override
    public void deleteAllInvoices() throws ServiceOperationException {

    }
}
