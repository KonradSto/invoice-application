import java.util.List;

public interface Database {

    void saveInvoice(Invoice invoice);

    void deleteInvoiceById(Long id);

    void updateInvoice(Invoice invoice);

    Invoice getInvoiceById(Long id);

    List<Invoice> getInvoices();
}
