package pl.coderstrust.soap;

import static pl.coderstrust.soap.Mapper.convertXMLGregorianCalendarToLocalDate;
import static pl.coderstrust.soap.Mapper.mapOriginalInvoiceToSOAPInvoice;
import static pl.coderstrust.soap.Mapper.mapOriginalInvoicesToSOAPInvoices;
import static pl.coderstrust.soap.Mapper.mapSOAPInvoiceToOriginalInvoice;
import static pl.coderstrust.soap.bindingClasses.ResponseStatus.FAILURE;
import static pl.coderstrust.soap.bindingClasses.ResponseStatus.SUCCESS;

import java.util.Collection;
import java.util.List;
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
import pl.coderstrust.soap.bindingClasses.GetAllInvoicesRequest;
import pl.coderstrust.soap.bindingClasses.GetInvoiceRequest;
import pl.coderstrust.soap.bindingClasses.GetInvoiceResponse;
import pl.coderstrust.soap.bindingClasses.GetInvoicesByBuyerRequest;
import pl.coderstrust.soap.bindingClasses.GetInvoicesByDateRequest;
import pl.coderstrust.soap.bindingClasses.GetInvoicesBySellerRequest;
import pl.coderstrust.soap.bindingClasses.InvoiceResponse;
import pl.coderstrust.soap.bindingClasses.ResponseBase;

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
    public GetInvoiceResponse getInvoice(@RequestPayload GetInvoiceRequest request) {
        GetInvoiceResponse response = new GetInvoiceResponse();
        ResponseBase responseBase;
        try {
            Optional<Invoice> optionalInvoice = invoiceService.getInvoice(request.getId());
            if(optionalInvoice.isPresent()){

            }
            Invoice invoice = invoiceService.getInvoice(request.getId()).get();
            pl.coderstrust.soap.bindingClasses.Invoice responseInvoice = mapOriginalInvoiceToSOAPInvoice(invoice);
            responseBase = new pl.coderstrust.soap.bindingClasses.InvoiceResponse();
            responseBase.setStatus(SUCCESS);
            ((pl.coderstrust.soap.bindingClasses.InvoiceResponse) responseBase).setInvoice(responseInvoice);
            response.setResponse(responseBase);
            return response;
        } catch (ServiceOperationException | DatatypeConfigurationException e) {
            responseBase = new pl.coderstrust.soap.bindingClasses.ResponseBase();
            responseBase.setStatus(FAILURE);
            responseBase.setMessage("An error occurred during getting invoice");
            response.setResponse(responseBase);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllInvoicesRequest")
    @ResponsePayload
    public pl.coderstrust.soap.bindingClasses.GetAllInvoicesResponse getAllInvoices(@RequestPayload GetAllInvoicesRequest request) {
        pl.coderstrust.soap.bindingClasses.GetAllInvoicesResponse response = new pl.coderstrust.soap.bindingClasses.GetAllInvoicesResponse();

        try {
            Collection<Invoice> invoices = invoiceService.getAllInvoices();
            Collection<pl.coderstrust.soap.bindingClasses.Invoice> responseInvoices = mapOriginalInvoicesToSOAPInvoices(invoices);
            pl.coderstrust.soap.bindingClasses.InvoicesResponse responseBase = new pl.coderstrust.soap.bindingClasses.InvoicesResponse();
            responseBase.setStatus(SUCCESS);
            responseBase.getInvoices();
            for (pl.coderstrust.soap.bindingClasses.Invoice invoice : responseInvoices) {
                responseBase.getInvoices().add(invoice);
            }
            response.setResponse(responseBase);
            return response;
        } catch (ServiceOperationException | DatatypeConfigurationException e) {
            ResponseBase responseBase = new ResponseBase();
            responseBase.setStatus(FAILURE);
            responseBase.setMessage("An error occurred during getting all invoices");
            response.setResponse(responseBase);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoicesByDateRequest")
    @ResponsePayload
    public pl.coderstrust.soap.bindingClasses.GetInvoicesByDateResponse getInvoicesByDate(@RequestPayload GetInvoicesByDateRequest request) {
        pl.coderstrust.soap.bindingClasses.GetInvoicesByDateResponse response = new pl.coderstrust.soap.bindingClasses.GetInvoicesByDateResponse();
        ResponseBase responseBase;
        try {
            Collection<Invoice> invoices = invoiceService.getAllInvoicesByDate(convertXMLGregorianCalendarToLocalDate(request.getFromDate()), convertXMLGregorianCalendarToLocalDate(request.getToDate()));
            Collection<pl.coderstrust.soap.bindingClasses.Invoice> responseInvoices = mapOriginalInvoicesToSOAPInvoices((List<Invoice>) invoices);
            responseBase = new pl.coderstrust.soap.bindingClasses.InvoicesResponse();
            responseBase.setStatus(SUCCESS);
            for (pl.coderstrust.soap.bindingClasses.Invoice invoice : responseInvoices) {
                ((pl.coderstrust.soap.bindingClasses.InvoicesResponse) responseBase).getInvoices().add(invoice);
            }
            response.setResponse(responseBase);
            return response;
        } catch (ServiceOperationException | DatatypeConfigurationException e) {
            responseBase = new ResponseBase();
            responseBase.setStatus(FAILURE);
            responseBase.setMessage("An error occurred during getting invoices in given date range");
            response.setResponse(responseBase);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoicesByBuyerRequest")
    @ResponsePayload
    public pl.coderstrust.soap.bindingClasses.GetInvoicesByBuyerResponse getInvoicesByBuyer(@RequestPayload GetInvoicesByBuyerRequest request) {
        pl.coderstrust.soap.bindingClasses.GetInvoicesByBuyerResponse response = new pl.coderstrust.soap.bindingClasses.GetInvoicesByBuyerResponse();
        ResponseBase responseBase;
        try {
            Collection<Invoice> invoices = invoiceService.getAllInvoicesByBuyer(request.getBuyerId());
            Collection<pl.coderstrust.soap.bindingClasses.Invoice> responseInvoices = mapOriginalInvoicesToSOAPInvoices((List<Invoice>) invoices);
            responseBase = new pl.coderstrust.soap.bindingClasses.InvoicesResponse();
            responseBase.setStatus(SUCCESS);
            for (pl.coderstrust.soap.bindingClasses.Invoice invoice : responseInvoices) {
                ((pl.coderstrust.soap.bindingClasses.InvoicesResponse) responseBase).getInvoices().add(invoice);
            }
            response.setResponse(responseBase);
            return response;
        } catch (ServiceOperationException | DatatypeConfigurationException e) {
            responseBase = new ResponseBase();
            responseBase.setStatus(FAILURE);
            responseBase.setMessage("An error occurred during getting invoices for chosen buyer");
            response.setResponse(responseBase);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoicesBySellerRequest")
    @ResponsePayload
    public pl.coderstrust.soap.bindingClasses.GetInvoicesBySellerResponse getInvoicesBySeller(@RequestPayload GetInvoicesBySellerRequest request) {
        pl.coderstrust.soap.bindingClasses.GetInvoicesBySellerResponse response = new pl.coderstrust.soap.bindingClasses.GetInvoicesBySellerResponse();
        ResponseBase responseBase;
        try {
            Collection<Invoice> invoices = invoiceService.getAllInvoicesBySeller(request.getSellerId());
            Collection<pl.coderstrust.soap.bindingClasses.Invoice> responseInvoices = mapOriginalInvoicesToSOAPInvoices((List<Invoice>) invoices);
            responseBase = new pl.coderstrust.soap.bindingClasses.InvoicesResponse();
            responseBase.setStatus(SUCCESS);
            for (pl.coderstrust.soap.bindingClasses.Invoice invoice : responseInvoices) {
                ((pl.coderstrust.soap.bindingClasses.InvoicesResponse) responseBase).getInvoices().add(invoice);
            }
            response.setResponse(responseBase);
            return response;
        } catch (ServiceOperationException | DatatypeConfigurationException e) {
            responseBase = new ResponseBase();
            responseBase.setStatus(FAILURE);
            responseBase.setMessage("An error occurred during getting invoices for chosen seller");
            response.setResponse(responseBase);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteInvoiceRequest")
    @ResponsePayload
    public pl.coderstrust.soap.bindingClasses.DeleteInvoiceResponse deleteInvoice(@RequestPayload pl.coderstrust.soap.bindingClasses.DeleteInvoiceRequest request) {
        pl.coderstrust.soap.bindingClasses.DeleteInvoiceResponse response = new pl.coderstrust.soap.bindingClasses.DeleteInvoiceResponse();
        ResponseBase responseBase;
        try {
            invoiceService.deleteInvoice(request.getId());
            responseBase = new InvoiceResponse();
            responseBase.setStatus(SUCCESS);
            response.setResponse(responseBase);
            return response;
        } catch (ServiceOperationException e) {
            responseBase = new ResponseBase();
            responseBase.setStatus(FAILURE);
            responseBase.setMessage("An error occurred during deleting invoice");
            response.setResponse(responseBase);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteAllInvoicesRequest")
    @ResponsePayload
    public pl.coderstrust.soap.bindingClasses.DeleteAllInvoicesResponse deleteAllInvoices(@RequestPayload pl.coderstrust.soap.bindingClasses.DeleteAllInvoicesRequest request) {
        pl.coderstrust.soap.bindingClasses.DeleteAllInvoicesResponse response = new pl.coderstrust.soap.bindingClasses.DeleteAllInvoicesResponse();
        ResponseBase responseBase;
        try {
            invoiceService.deleteAllInvoices();
            responseBase = new InvoiceResponse();
            responseBase.setStatus(SUCCESS);
            response.setResponse(responseBase);
            return response;
        } catch (ServiceOperationException e) {
            responseBase = new ResponseBase();
            responseBase.setStatus(FAILURE);
            responseBase.setMessage("An error occurred during deleting all invoices");
            response.setResponse(responseBase);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "saveInvoiceRequest")
    @ResponsePayload
    public pl.coderstrust.soap.bindingClasses.SaveInvoiceResponse saveInvoice(@RequestPayload pl.coderstrust.soap.bindingClasses.SaveInvoiceRequest request) {
        pl.coderstrust.soap.bindingClasses.SaveInvoiceResponse response = new pl.coderstrust.soap.bindingClasses.SaveInvoiceResponse();
        ResponseBase responseBase;
        try {
            Invoice invoice = invoiceService.saveInvoice(mapSOAPInvoiceToOriginalInvoice(request.getInvoice()));
            pl.coderstrust.soap.bindingClasses.Invoice responseInvoice = mapOriginalInvoiceToSOAPInvoice(invoice);
            responseBase = new InvoiceResponse();
            responseBase.setStatus(SUCCESS);
            ((InvoiceResponse) responseBase).setInvoice(responseInvoice);
            response.setResponse(responseBase);
            return response;
        } catch (ServiceOperationException | DatatypeConfigurationException e) {
            responseBase = new ResponseBase();
            responseBase.setStatus(FAILURE);
            responseBase.setMessage("An error occurred during saving invoice");
            response.setResponse(responseBase);
        }
        return response;
    }
}