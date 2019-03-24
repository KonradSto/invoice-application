package pl.coderstrust.soap;

import java.util.List;

import io.spring.guides.gs_producing_web_service.Invoice;

public class InvoicesResponse extends ResponseBase {

    private List<Invoice> invoices;

    public InvoicesResponse(ResponseStatus status, List<Invoice> invoices) {
        super(status);
        this.invoices = invoices;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }
}
