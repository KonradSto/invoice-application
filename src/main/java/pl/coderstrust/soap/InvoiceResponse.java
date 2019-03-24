package pl.coderstrust.soap;

import io.spring.guides.gs_producing_web_service.Invoice;

public class InvoiceResponse extends ResponseBase {

    private Invoice invoice;

    public InvoiceResponse(ResponseStatus status, Invoice invoice) {
        super(status);
        this.invoice = invoice;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
