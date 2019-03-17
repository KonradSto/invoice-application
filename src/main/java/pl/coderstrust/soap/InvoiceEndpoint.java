package pl.coderstrust.soap;

import io.spring.guides.gs_producing_web_service.GetInvoicesRequest;
import io.spring.guides.gs_producing_web_service.GetInvoicesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class InvoiceEndpoint {

    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    private InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceEndpoint(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoicesRequest")
    @ResponsePayload
    public GetInvoicesResponse getInvoice(@RequestPayload GetInvoicesRequest request) {
        GetInvoicesResponse response = new GetInvoicesResponse();
        response.setInvoice(invoiceRepository.findInvoice(request.getName()));

        return response;
    }
}