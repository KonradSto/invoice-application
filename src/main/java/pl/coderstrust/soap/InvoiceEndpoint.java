package pl.coderstrust.soap;

import static pl.coderstrust.soap.Mapper.convertXmlGregorianCalendarToLocalDate;
import static pl.coderstrust.soap.Mapper.mapOriginalInvoiceToSoapInvoice;
import static pl.coderstrust.soap.Mapper.mapOriginalInvoicesToSoapInvoices;
import static pl.coderstrust.soap.Mapper.mapSoapInvoiceToOriginalInvoice;
import static pl.coderstrust.soap.bindingClasses.ResponseStatus.FAILURE;
import static pl.coderstrust.soap.bindingClasses.ResponseStatus.SUCCESS;

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
import pl.coderstrust.soap.bindingClasses.GetInvoiceRequest;
import pl.coderstrust.soap.bindingClasses.GetInvoicesByBuyerRequest;
import pl.coderstrust.soap.bindingClasses.GetInvoicesByDateRequest;
import pl.coderstrust.soap.bindingClasses.GetInvoicesBySellerRequest;
import pl.coderstrust.soap.bindingClasses.InvoiceResponse;
import pl.coderstrust.soap.bindingClasses.InvoicesListResponse;
import pl.coderstrust.soap.bindingClasses.InvoicesResponse;
import pl.coderstrust.soap.bindingClasses.ResponseBase;
import pl.coderstrust.soap.bindingClasses.SingleInvoiceResponse;
import pl.coderstrust.soap.bindingClasses.StatusResponse;

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
                pl.coderstrust.soap.bindingClasses.Invoice resultInvoice = mapOriginalInvoiceToSoapInvoice(invoice);
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
    public pl.coderstrust.soap.bindingClasses.InvoicesListResponse getAllInvoices() {
        InvoicesListResponse response = new InvoicesListResponse();
        InvoicesResponse responseBase = new InvoicesResponse();
        try {
            Collection<Invoice> invoices = invoiceService.getAllInvoices();
            Collection<pl.coderstrust.soap.bindingClasses.Invoice> responseInvoices = mapOriginalInvoicesToSoapInvoices(invoices);
            responseBase.getInvoices();
            for (pl.coderstrust.soap.bindingClasses.Invoice invoice : responseInvoices) {
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
    public pl.coderstrust.soap.bindingClasses.InvoicesListResponse getInvoicesByDate(@RequestPayload GetInvoicesByDateRequest request) {
        InvoicesListResponse response = new InvoicesListResponse();
        InvoicesResponse responseBase = new InvoicesResponse();
        try {
            Collection<Invoice> invoices = invoiceService.getAllInvoicesByDate(convertXmlGregorianCalendarToLocalDate(request.getFromDate()), convertXmlGregorianCalendarToLocalDate(request.getToDate()));
            Collection<pl.coderstrust.soap.bindingClasses.Invoice> responseInvoices = mapOriginalInvoicesToSoapInvoices(invoices);
            responseBase.getInvoices();
            for (pl.coderstrust.soap.bindingClasses.Invoice invoice : responseInvoices) {
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
    public pl.coderstrust.soap.bindingClasses.InvoicesListResponse getInvoicesByBuyer(@RequestPayload GetInvoicesByBuyerRequest request) {
        InvoicesListResponse response = new InvoicesListResponse();
        InvoicesResponse responseBase = new InvoicesResponse();
        try {
            Collection<Invoice> invoices = invoiceService.getAllInvoicesByBuyer(request.getBuyerId());
            Collection<pl.coderstrust.soap.bindingClasses.Invoice> responseInvoices = mapOriginalInvoicesToSoapInvoices(invoices);
            responseBase.getInvoices();
            for (pl.coderstrust.soap.bindingClasses.Invoice invoice : responseInvoices) {
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
    public pl.coderstrust.soap.bindingClasses.InvoicesListResponse getInvoicesBySeller(@RequestPayload GetInvoicesBySellerRequest request) {
        pl.coderstrust.soap.bindingClasses.InvoicesListResponse response = new pl.coderstrust.soap.bindingClasses.InvoicesListResponse();
        InvoicesResponse responseBase = new InvoicesResponse();
        try {
            Collection<Invoice> invoices = invoiceService.getAllInvoicesBySeller(request.getSellerId());
            Collection<pl.coderstrust.soap.bindingClasses.Invoice> responseInvoices = mapOriginalInvoicesToSoapInvoices(invoices);
            responseBase.getInvoices();
            for (pl.coderstrust.soap.bindingClasses.Invoice invoice : responseInvoices) {
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
    public pl.coderstrust.soap.bindingClasses.StatusResponse deleteInvoice(@RequestPayload pl.coderstrust.soap.bindingClasses.DeleteInvoiceRequest request) {
        pl.coderstrust.soap.bindingClasses.StatusResponse response = new pl.coderstrust.soap.bindingClasses.StatusResponse();
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
    public pl.coderstrust.soap.bindingClasses.StatusResponse deleteAllInvoices() {
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
    public pl.coderstrust.soap.bindingClasses.SingleInvoiceResponse saveInvoice(@RequestPayload pl.coderstrust.soap.bindingClasses.SaveInvoiceRequest request) {
        SingleInvoiceResponse response = new SingleInvoiceResponse();
        InvoiceResponse responseBase = new InvoiceResponse();
        try {
            Invoice invoice = invoiceService.saveInvoice(mapSoapInvoiceToOriginalInvoice(request.getInvoice()));
            pl.coderstrust.soap.bindingClasses.Invoice responseInvoice = mapOriginalInvoiceToSoapInvoice(invoice);
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
