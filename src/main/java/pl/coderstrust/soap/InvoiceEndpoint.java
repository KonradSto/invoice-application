package pl.coderstrust.soap;

import static pl.coderstrust.soap.Mapper.mapInvoice;
import static pl.coderstrust.soap.Mapper.mapInvoices;
import static pl.coderstrust.soap.Mapper.mapXmlGregorianCalendarToLocalDate;
import static pl.coderstrust.soap.bindingclasses.ResponseStatus.FAILURE;
import static pl.coderstrust.soap.bindingclasses.ResponseStatus.SUCCESS;

import java.util.Collection;
import java.util.Optional;
import javax.xml.datatype.DatatypeConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.InvoiceService;
import pl.coderstrust.service.ServiceOperationException;
import pl.coderstrust.soap.bindingclasses.GetInvoiceRequest;
import pl.coderstrust.soap.bindingclasses.GetInvoicesByBuyerRequest;
import pl.coderstrust.soap.bindingclasses.GetInvoicesByDateRequest;
import pl.coderstrust.soap.bindingclasses.GetInvoicesBySellerRequest;
import pl.coderstrust.soap.bindingclasses.InvoiceResponse;
import pl.coderstrust.soap.bindingclasses.InvoicesListResponse;
import pl.coderstrust.soap.bindingclasses.InvoicesResponse;
import pl.coderstrust.soap.bindingclasses.ResponseBase;
import pl.coderstrust.soap.bindingclasses.SingleInvoiceResponse;
import pl.coderstrust.soap.bindingclasses.StatusResponse;

@Endpoint
public class InvoiceEndpoint {

    private static final String NAMESPACE_URI = "http://project-9-karolina-konrad-lukasz-piotr";
    private InvoiceService invoiceService;

    @Autowired
    public InvoiceEndpoint(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoiceRequest")
    @ResponsePayload
    public SingleInvoiceResponse getInvoice(@RequestPayload GetInvoiceRequest request) {
        SingleInvoiceResponse response = new SingleInvoiceResponse();
        InvoiceResponse responseBase = new InvoiceResponse();
        responseBase.setStatus(FAILURE);
        try {
            Optional<Invoice> optionalInvoice = invoiceService.getInvoice(request.getId());
            if (optionalInvoice.isPresent()) {
                Invoice invoice = optionalInvoice.get();
                pl.coderstrust.soap.bindingclasses.Invoice resultInvoice = Mapper.mapInvoice(invoice);
                responseBase.setStatus(SUCCESS);
                responseBase.setMessage("");
                responseBase.setInvoice(resultInvoice);
            } else {
                responseBase.setMessage("No existing invoice with given Id");
            }
        } catch (ServiceOperationException | DatatypeConfigurationException e) {
            responseBase.setMessage("An error occurred during getting invoice");
        }
        response.setResponse(responseBase);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllInvoicesRequest")
    @ResponsePayload
    public pl.coderstrust.soap.bindingclasses.InvoicesListResponse getAllInvoices() {
        InvoicesListResponse response = new InvoicesListResponse();
        InvoicesResponse responseBase = new InvoicesResponse();
        try {
            Collection<Invoice> invoices = invoiceService.getAllInvoices();
            Collection<pl.coderstrust.soap.bindingclasses.Invoice> responseInvoices = mapInvoices(invoices);
            responseBase.getInvoices();
            for (pl.coderstrust.soap.bindingclasses.Invoice invoice : responseInvoices) {
                responseBase.getInvoices().add(invoice);
            }
            responseBase.setMessage("");
            responseBase.setStatus(SUCCESS);
            responseBase.setMessage("");
        } catch (ServiceOperationException | DatatypeConfigurationException e) {
            responseBase.setMessage("An error occurred during getting all invoices");
            responseBase.setStatus(FAILURE);
        }
        response.setResponse(responseBase);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoicesByDateRequest")
    @ResponsePayload
    public pl.coderstrust.soap.bindingclasses.InvoicesListResponse getInvoicesByDate(@RequestPayload GetInvoicesByDateRequest request) {
        InvoicesListResponse response = new InvoicesListResponse();
        InvoicesResponse responseBase = new InvoicesResponse();
        try {
            Collection<Invoice> invoices = invoiceService.getAllInvoicesByDate(mapXmlGregorianCalendarToLocalDate(request.getFromDate()), mapXmlGregorianCalendarToLocalDate(request.getToDate()));
            Collection<pl.coderstrust.soap.bindingclasses.Invoice> responseInvoices = mapInvoices(invoices);
            responseBase.getInvoices();
            for (pl.coderstrust.soap.bindingclasses.Invoice invoice : responseInvoices) {
                (responseBase).getInvoices().add(invoice);
            }
            responseBase.setStatus(SUCCESS);
            responseBase.setMessage("");

        } catch (ServiceOperationException | DatatypeConfigurationException e) {
            responseBase = new InvoicesResponse();
            responseBase.setStatus(FAILURE);
            responseBase.setMessage("An error occurred during getting invoices in given date range");
        }
        response.setResponse(responseBase);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoicesByBuyerRequest")
    @ResponsePayload
    public pl.coderstrust.soap.bindingclasses.InvoicesListResponse getInvoicesByBuyer(@RequestPayload GetInvoicesByBuyerRequest request) {
        InvoicesListResponse response = new InvoicesListResponse();
        InvoicesResponse responseBase = new InvoicesResponse();
        try {
            Collection<Invoice> invoices = invoiceService.getAllInvoicesByBuyer(request.getBuyerId());
            Collection<pl.coderstrust.soap.bindingclasses.Invoice> responseInvoices = mapInvoices(invoices);
            responseBase.getInvoices();
            for (pl.coderstrust.soap.bindingclasses.Invoice invoice : responseInvoices) {
                responseBase.getInvoices().add(invoice);
            }
            responseBase.setStatus(SUCCESS);
            responseBase.setMessage("");
        } catch (ServiceOperationException | DatatypeConfigurationException e) {
            responseBase.setStatus(FAILURE);
            responseBase.setMessage("An error occurred during getting invoices for chosen buyer");
        }
        response.setResponse(responseBase);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoicesBySellerRequest")
    @ResponsePayload
    public pl.coderstrust.soap.bindingclasses.InvoicesListResponse getInvoicesBySeller(@RequestPayload GetInvoicesBySellerRequest request) {
        pl.coderstrust.soap.bindingclasses.InvoicesListResponse response = new pl.coderstrust.soap.bindingclasses.InvoicesListResponse();
        InvoicesResponse responseBase = new InvoicesResponse();
        try {
            Collection<Invoice> invoices = invoiceService.getAllInvoicesBySeller(request.getSellerId());
            Collection<pl.coderstrust.soap.bindingclasses.Invoice> responseInvoices = mapInvoices(invoices);
            responseBase.getInvoices();
            for (pl.coderstrust.soap.bindingclasses.Invoice invoice : responseInvoices) {
                responseBase.getInvoices().add(invoice);
            }
            responseBase.setStatus(SUCCESS);
            responseBase.setMessage("");
        } catch (ServiceOperationException | DatatypeConfigurationException e) {
            responseBase.setStatus(FAILURE);
            responseBase.setMessage("An error occurred during getting invoices for chosen seller");
        }
        response.setResponse(responseBase);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteInvoiceRequest")
    @ResponsePayload
    public pl.coderstrust.soap.bindingclasses.StatusResponse deleteInvoice(@RequestPayload pl.coderstrust.soap.bindingclasses.DeleteInvoiceRequest request) {
        pl.coderstrust.soap.bindingclasses.StatusResponse response = new pl.coderstrust.soap.bindingclasses.StatusResponse();
        ResponseBase responseBase = new ResponseBase();
        try {
            invoiceService.deleteInvoice(request.getId());
            responseBase.setStatus(SUCCESS);
            responseBase.setMessage("");
        } catch (ServiceOperationException e) {
            responseBase.setStatus(FAILURE);
            responseBase.setMessage("An error occurred during deleting invoice");
        }
        response.setResponse(responseBase);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteAllInvoicesRequest")
    @ResponsePayload
    public pl.coderstrust.soap.bindingclasses.StatusResponse deleteAllInvoices() {
        StatusResponse response = new StatusResponse();
        ResponseBase responseBase = new ResponseBase();
        try {
            invoiceService.deleteAllInvoices();
            responseBase.setStatus(SUCCESS);
            responseBase.setMessage("");
        } catch (ServiceOperationException e) {
            responseBase.setStatus(FAILURE);
            responseBase.setMessage("An error occurred during deleting all invoices");
        }
        response.setResponse(responseBase);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "saveInvoiceRequest")
    @ResponsePayload
    public pl.coderstrust.soap.bindingclasses.SingleInvoiceResponse saveInvoice(@RequestPayload pl.coderstrust.soap.bindingclasses.SaveInvoiceRequest request) {
        SingleInvoiceResponse response = new SingleInvoiceResponse();
        InvoiceResponse responseBase = new InvoiceResponse();
        try {
            Invoice invoice = invoiceService.saveInvoice(mapInvoice(request.getInvoice()));
            pl.coderstrust.soap.bindingclasses.Invoice responseInvoice = Mapper.mapInvoice(invoice);
            responseBase.setStatus(SUCCESS);
            responseBase.setMessage("");
            responseBase.setInvoice(responseInvoice);
        } catch (ServiceOperationException | DatatypeConfigurationException e) {
            responseBase.setStatus(FAILURE);
            responseBase.setMessage("An error occurred during saving invoice");
        }
        response.setResponse(responseBase);
        return response;
    }
}
