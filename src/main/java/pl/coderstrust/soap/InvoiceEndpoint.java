package pl.coderstrust.soap;

import static pl.coderstrust.soap.Mapper.mapInvoice;
import static pl.coderstrust.soap.Mapper.mapInvoices;
import static pl.coderstrust.soap.Mapper.mapXmlGregorianCalendarToLocalDate;
import static pl.coderstrust.soap.bindingclasses.ResponseStatus.FAILURE;
import static pl.coderstrust.soap.bindingclasses.ResponseStatus.SUCCESS;

import java.util.Collection;
import java.util.Optional;
import javax.xml.datatype.DatatypeConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.coderstrust.controller.InvoiceController;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.InvoiceService;
import pl.coderstrust.service.ServiceOperationException;
import pl.coderstrust.soap.bindingclasses.DeleteInvoiceRequest;
import pl.coderstrust.soap.bindingclasses.GetInvoiceRequest;
import pl.coderstrust.soap.bindingclasses.GetInvoicesByBuyerRequest;
import pl.coderstrust.soap.bindingclasses.GetInvoicesByDateRequest;
import pl.coderstrust.soap.bindingclasses.GetInvoicesBySellerRequest;
import pl.coderstrust.soap.bindingclasses.InvoiceResponse;
import pl.coderstrust.soap.bindingclasses.InvoicesResponse;
import pl.coderstrust.soap.bindingclasses.ResponseBase;
import pl.coderstrust.soap.bindingclasses.SaveInvoiceRequest;
import pl.coderstrust.utils.ArgumentValidator;

@Endpoint
public class InvoiceEndpoint {

    private static Logger log = LoggerFactory.getLogger(InvoiceController.class);
    private static final String NAMESPACE_URI = "http://project-9-karolina-konrad-lukasz-piotr";
    private InvoiceService invoiceService;
    private String message;


    @Autowired
    public InvoiceEndpoint(InvoiceService invoiceService) {
        log.debug("Launching to SOAP service");
        ArgumentValidator.ensureNotNull(invoiceService, "invoiceService");
        this.invoiceService = invoiceService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoiceRequest")
    @ResponsePayload
    public InvoiceResponse getInvoice(@RequestPayload GetInvoiceRequest request) {
        try {
            log.debug("Getting an invoice by id: {}", request.getId());
            Optional<Invoice> optionalInvoice = invoiceService.getInvoice(request.getId());
            if (optionalInvoice.isPresent()) {
                Invoice invoice = optionalInvoice.get();
                pl.coderstrust.soap.bindingclasses.Invoice resultInvoice = Mapper.mapInvoice(invoice);
                return createSuccessInvoiceResponse(resultInvoice);
            }
            message = "Invoice with given id does not exist";
            log.error(message);
            return createErrorInvoiceResponse(message);
        } catch (ServiceOperationException | DatatypeConfigurationException e) {
            message = "An error occurred during getting invoice";
            log.error(message);
            return createErrorInvoiceResponse(message);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllInvoicesRequest")
    @ResponsePayload
    public InvoicesResponse getAllInvoices() {
        try {
            log.debug("Getting all invoices");
            Collection<Invoice> invoices = invoiceService.getAllInvoices();
            Collection<pl.coderstrust.soap.bindingclasses.Invoice> responseInvoices = mapInvoices(invoices);
            return createSuccessInvoicesResponse(responseInvoices);
        } catch (ServiceOperationException | DatatypeConfigurationException e) {
            message = "An error occurred during getting all invoices";
            log.error(message);
            return createErrorInvoicesResponse(message);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoicesByDateRequest")
    @ResponsePayload
    public InvoicesResponse getInvoicesByDate(@RequestPayload GetInvoicesByDateRequest request) {
        try {
            log.debug("Getting all invoices by dates: from {} to {}", request.getFromDate(), request.getToDate());
            Collection<Invoice> invoices = invoiceService.getAllInvoicesByDate(mapXmlGregorianCalendarToLocalDate(request.getFromDate()), mapXmlGregorianCalendarToLocalDate(request.getToDate()));
            Collection<pl.coderstrust.soap.bindingclasses.Invoice> responseInvoices = mapInvoices(invoices);
            return createSuccessInvoicesResponse(responseInvoices);
        } catch (ServiceOperationException | DatatypeConfigurationException e) {
            message = "An error occurred during getting invoices in given date range";
            log.error(message);
            return createErrorInvoicesResponse(message);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoicesByBuyerRequest")
    @ResponsePayload
    public InvoicesResponse getInvoicesByBuyer(@RequestPayload GetInvoicesByBuyerRequest request) {
        try {
            log.debug("Getting all invoices by buyer: {}", request.getBuyerId());
            Collection<Invoice> invoices = invoiceService.getAllInvoicesByBuyer(request.getBuyerId());
            Collection<pl.coderstrust.soap.bindingclasses.Invoice> responseInvoices = mapInvoices(invoices);
            return createSuccessInvoicesResponse(responseInvoices);
        } catch (ServiceOperationException | DatatypeConfigurationException e) {
            message = "An error occurred during getting invoices for chosen buyer";
            log.error(message);
            return createErrorInvoicesResponse(message);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoicesBySellerRequest")
    @ResponsePayload
    public InvoicesResponse getInvoicesBySeller(@RequestPayload GetInvoicesBySellerRequest request) {
        try {
            log.debug("Getting all invoices by seller: {}", request.getSellerId());
            Collection<Invoice> invoices = invoiceService.getAllInvoicesBySeller(request.getSellerId());
            Collection<pl.coderstrust.soap.bindingclasses.Invoice> responseInvoices = mapInvoices(invoices);
            return createSuccessInvoicesResponse(responseInvoices);
        } catch (ServiceOperationException | DatatypeConfigurationException e) {
            message = "An error occurred during getting invoices for chosen seller";
            log.error(message);
            return createErrorInvoicesResponse(message);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteInvoiceRequest")
    @ResponsePayload
    public ResponseBase deleteInvoice(@RequestPayload DeleteInvoiceRequest request) {
        try {
            log.debug("Deleting invoice by id: {}", request.getId());
            Optional<Invoice> invoice = invoiceService.getInvoice(request.getId());
            if (!invoice.isPresent()) {
                message = "Invoice with given id does not exist";
                log.error(message);
                return createErrorResponseBase(message);
            }
            invoiceService.deleteInvoice(request.getId());
            return createSuccessResponseBase();
        } catch (ServiceOperationException e) {
            message = "An error occurred during deleting invoice";
            log.error(message);
            return createErrorResponseBase(message);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteAllInvoicesRequest")
    @ResponsePayload
    public ResponseBase deleteAllInvoices() {
        try {
            log.debug("Deleting all invoices");
            invoiceService.deleteAllInvoices();
            return createSuccessResponseBase();
        } catch (ServiceOperationException e) {
            message = "An error occurred during deleting all invoices";
            log.error(message);
            return createErrorResponseBase(message);
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "saveInvoiceRequest")
    @ResponsePayload

    public InvoiceResponse saveInvoice(@RequestPayload SaveInvoiceRequest request) {
        try {
            log.debug("Saving invoice: {}", request.getInvoice());
            Invoice invoice = invoiceService.saveInvoice(mapInvoice(request.getInvoice()));
            pl.coderstrust.soap.bindingclasses.Invoice responseInvoice = Mapper.mapInvoice(invoice);
            return createSuccessInvoiceResponse(responseInvoice);
        } catch (ServiceOperationException | DatatypeConfigurationException e) {
            message = "An error occurred during saving invoice";
            log.error(message);
            return createErrorInvoiceResponse(message);
        }
    }

    private InvoiceResponse createSuccessInvoiceResponse(pl.coderstrust.soap.bindingclasses.Invoice invoice) {
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setStatus(SUCCESS);
        invoiceResponse.setInvoice(invoice);
        invoiceResponse.setMessage("");
        return invoiceResponse;
    }

    private InvoiceResponse createErrorInvoiceResponse(String errorMessage) {
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        invoiceResponse.setStatus(FAILURE);
        invoiceResponse.setMessage(errorMessage);
        return invoiceResponse;
    }

    private InvoicesResponse createSuccessInvoicesResponse(Collection<pl.coderstrust.soap.bindingclasses.Invoice> invoices) {
        InvoicesResponse invoicesResponse = new InvoicesResponse();
        invoicesResponse.setStatus(SUCCESS);
        invoicesResponse.getInvoices();
        for (pl.coderstrust.soap.bindingclasses.Invoice invoice : invoices) {
            invoicesResponse.getInvoices().add(invoice);
        }
        invoicesResponse.setMessage("");
        return invoicesResponse;
    }

    private InvoicesResponse createErrorInvoicesResponse(String errorMessage) {
        InvoicesResponse invoicesResponse = new InvoicesResponse();
        invoicesResponse.setStatus(FAILURE);
        invoicesResponse.setMessage(errorMessage);
        return invoicesResponse;
    }

    private ResponseBase createSuccessResponseBase() {
        ResponseBase responseBase = new ResponseBase();
        responseBase.setStatus(SUCCESS);
        responseBase.setMessage("");
        return responseBase;
    }

    private ResponseBase createErrorResponseBase(String errorMessage) {
        ResponseBase responseBase = new ResponseBase();
        responseBase.setStatus(FAILURE);
        responseBase.setMessage(errorMessage);
        return responseBase;
    }
}
