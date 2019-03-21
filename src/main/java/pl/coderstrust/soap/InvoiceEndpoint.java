package pl.coderstrust.soap;

import java.time.LocalDate;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import io.spring.guides.gs_producing_web_service.GetInvoiceRequest;
import io.spring.guides.gs_producing_web_service.GetInvoiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.service.InvoiceService;
import pl.coderstrust.service.ServiceOperationException;

@Endpoint
public class InvoiceEndpoint {

    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    private InvoiceService invoiceService;

    @Autowired
    public InvoiceEndpoint(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getInvoicesRequest")
    @ResponsePayload
    public GetInvoiceResponse getInvoice(@RequestPayload GetInvoiceRequest request) {
        GetInvoiceResponse response = new GetInvoiceResponse();
        try {
            Invoice invoice = invoiceService.getInvoice(request.getId()).get();
            io.spring.guides.gs_producing_web_service.Invoice responseInvoice = mapOriginalInvoiceToResponseInvoice(invoice);
            response.setInvoice(responseInvoice);
            return response;
        } catch (ServiceOperationException | DatatypeConfigurationException e) {
//            e.printStackTrace();
            //ustawiamy status na fail + message
        }

        return response;
    }

    static io.spring.guides.gs_producing_web_service.Invoice mapOriginalInvoiceToResponseInvoice(Invoice invoice) throws DatatypeConfigurationException {
        io.spring.guides.gs_producing_web_service.Invoice responseInvoice = new io.spring.guides.gs_producing_web_service.Invoice();
        responseInvoice.setId(invoice.getId());
        responseInvoice.setNumber(invoice.getNumber());
        responseInvoice.setIssuedDate(convertLocalDateToXMLGregorianCalendar((invoice.getIssuedDate())));
        responseInvoice.setLocalDate(convertLocalDateToXMLGregorianCalendar(invoice.getIssuedDate()));
        responseInvoice.setSeller(mapOriginalSellerToSOAPSeller(invoice.getSeller()));
        responseInvoice.setBuyer(mapOriginalBuyerToSOAPBuyer(invoice.getBuyer()));
        List<InvoiceEntry> entries = invoice.getEntries();
        for (InvoiceEntry entry : entries) {
            responseInvoice.getEntries().add(mapOriginalEntryToResponseEntry(entry));
        }
        return responseInvoice;
    }

    static io.spring.guides.gs_producing_web_service.InvoiceEntry mapOriginalEntryToResponseEntry(InvoiceEntry entry) {
        io.spring.guides.gs_producing_web_service.InvoiceEntry responseEntry = new io.spring.guides.gs_producing_web_service.InvoiceEntry();
        responseEntry.setId(entry.getId());
        responseEntry.setProductName(entry.getProductName());
        responseEntry.setQuantity(entry.getQuantity());
        responseEntry.setUnit(entry.getUnit());
        responseEntry.setPrice(entry.getPrice());
        responseEntry.setNettValue(entry.getNettValue());
        responseEntry.setGrossValue(entry.getGrossValue());
        responseEntry.setVatRate(io.spring.guides.gs_producing_web_service.Vat.valueOf(entry.getVatRate().toString()));
        return responseEntry;
    }

    static io.spring.guides.gs_producing_web_service.Company mapOriginalSellerToSOAPSeller(Company seller) {
        io.spring.guides.gs_producing_web_service.Company responseSeller = new io.spring.guides.gs_producing_web_service.Company();
        responseSeller.setId(seller.getId());
        responseSeller.setName(seller.getName());
        responseSeller.setAddress(seller.getAddress());
        responseSeller.setTaxId(seller.getTaxId());
        responseSeller.setAccountNumber(seller.getAccountNumber());
        responseSeller.setPhoneNumber(seller.getPhoneNumber());
        responseSeller.setEmail(seller.getEmail());
        return responseSeller;
    }

    static io.spring.guides.gs_producing_web_service.Company mapOriginalBuyerToSOAPBuyer(Company buyer) {
        io.spring.guides.gs_producing_web_service.Company responseBuyer = new io.spring.guides.gs_producing_web_service.Company();
        responseBuyer.setId(buyer.getId());
        responseBuyer.setName(buyer.getName());
        responseBuyer.setAddress(buyer.getAddress());
        responseBuyer.setTaxId(buyer.getTaxId());
        responseBuyer.setAccountNumber(buyer.getAccountNumber());
        responseBuyer.setPhoneNumber(buyer.getPhoneNumber());
        responseBuyer.setEmail(buyer.getEmail());
        return responseBuyer;
    }

    static XMLGregorianCalendar convertLocalDateToXMLGregorianCalendar(LocalDate localdate) throws DatatypeConfigurationException {
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(localdate.toString());
    }

    static LocalDate convertXMLGregorianCalendarToLocalDate(XMLGregorianCalendar gregorianDate) throws DatatypeConfigurationException {
        return LocalDate.parse(gregorianDate.toXMLFormat());
    }
}