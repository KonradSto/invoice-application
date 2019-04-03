package pl.coderstrust.soap;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.Vat;
import pl.coderstrust.soap.bindingclasses.Entries;

class Mapper {

    private Mapper() {
    }

    static Invoice mapSoapInvoiceToOriginalInvoice(pl.coderstrust.soap.bindingclasses.Invoice responseInvoice) {
        return new Invoice(
            responseInvoice.getId(),
            responseInvoice.getNumber(),
            convertXmlGregorianCalendarToLocalDate(responseInvoice.getIssuedDate()),
            convertXmlGregorianCalendarToLocalDate(responseInvoice.getLocalDate()),
            mapSoapSellerToOriginalSeller(responseInvoice.getSeller()),
            mapSoapBuyerToOriginalBuyer(responseInvoice.getBuyer()),
            mapSoapEntriesToOriginalEntries(responseInvoice.getEntries().getInvoiceEntry())
        );
    }

    private static List<InvoiceEntry> mapSoapEntriesToOriginalEntries(List<pl.coderstrust.soap.bindingclasses.InvoiceEntry> responseEntries) {
        List<InvoiceEntry> entries = new ArrayList<>();
        for (pl.coderstrust.soap.bindingclasses.InvoiceEntry entry : responseEntries) {
            entries.add(mapSoapEntryToOriginalEntry(entry));
        }
        return entries;
    }

    private static InvoiceEntry mapSoapEntryToOriginalEntry(pl.coderstrust.soap.bindingclasses.InvoiceEntry responseEntry) {
        pl.coderstrust.soap.bindingclasses.Vat responseVatRate = responseEntry.getVatRate();
        return new InvoiceEntry(
            responseEntry.getId(),
            responseEntry.getProductName(),
            responseEntry.getQuantity(),
            responseEntry.getUnit(),
            responseEntry.getPrice(),
            responseEntry.getNettValue(),
            responseEntry.getGrossValue(),
            Vat.valueOf(responseVatRate.value())
        );
    }

    private static Company mapSoapBuyerToOriginalBuyer(pl.coderstrust.soap.bindingclasses.Company responseBuyer) {
        return new Company(
            responseBuyer.getId(),
            responseBuyer.getName(),
            responseBuyer.getAddress(),
            responseBuyer.getTaxId(),
            responseBuyer.getAccountNumber(),
            responseBuyer.getPhoneNumber(),
            responseBuyer.getEmail()
        );
    }

    private static Company mapSoapSellerToOriginalSeller(pl.coderstrust.soap.bindingclasses.Company responseSeller) {
        return new Company(
            responseSeller.getId(),
            responseSeller.getName(),
            responseSeller.getAddress(),
            responseSeller.getTaxId(),
            responseSeller.getAccountNumber(),
            responseSeller.getPhoneNumber(),
            responseSeller.getEmail()
        );
    }

    static List<pl.coderstrust.soap.bindingclasses.Invoice> mapOriginalInvoicesToSoapInvoices(Collection<Invoice> invoices) throws DatatypeConfigurationException {
        List<pl.coderstrust.soap.bindingclasses.Invoice> responseInvoices = new ArrayList<>();
        for (Invoice invoice : invoices) {
            responseInvoices.add(mapOriginalInvoiceToSoapInvoice(invoice));
        }
        return responseInvoices;
    }

    static pl.coderstrust.soap.bindingclasses.Invoice mapOriginalInvoiceToSoapInvoice(Invoice invoice) throws DatatypeConfigurationException {
        pl.coderstrust.soap.bindingclasses.Invoice responseInvoice = new pl.coderstrust.soap.bindingclasses.Invoice();
        responseInvoice.setId(invoice.getId());
        responseInvoice.setNumber(invoice.getNumber());
        responseInvoice.setIssuedDate(convertLocalDateToXmlGregorianCalendar((invoice.getIssuedDate())));
        responseInvoice.setLocalDate(convertLocalDateToXmlGregorianCalendar(invoice.getDueDate()));
        responseInvoice.setSeller(mapOriginalSellerToSoapSeller(invoice.getSeller()));
        responseInvoice.setBuyer(mapOriginalBuyerToSoapBuyer(invoice.getBuyer()));
        List<InvoiceEntry> entries = invoice.getEntries();
        pl.coderstrust.soap.bindingclasses.Entries soapEntries = new Entries();
        for (InvoiceEntry entry : entries) {
            soapEntries.getInvoiceEntry().add(mapOriginalEntryToSoapEntry(entry));
            responseInvoice.setEntries(soapEntries);
        }
        return responseInvoice;
    }

    private static pl.coderstrust.soap.bindingclasses.InvoiceEntry mapOriginalEntryToSoapEntry(InvoiceEntry entry) {
        pl.coderstrust.soap.bindingclasses.InvoiceEntry responseEntry = new pl.coderstrust.soap.bindingclasses.InvoiceEntry();
        responseEntry.setId(entry.getId());
        responseEntry.setProductName(entry.getProductName());
        responseEntry.setQuantity(entry.getQuantity());
        responseEntry.setUnit(entry.getUnit());
        responseEntry.setPrice(entry.getPrice());
        responseEntry.setNettValue(entry.getNettValue());
        responseEntry.setGrossValue(entry.getGrossValue());
        responseEntry.setVatRate(pl.coderstrust.soap.bindingclasses.Vat.valueOf(entry.getVatRate().toString()));
        return responseEntry;
    }

    private static pl.coderstrust.soap.bindingclasses.Company mapOriginalSellerToSoapSeller(Company seller) {
        pl.coderstrust.soap.bindingclasses.Company responseSeller = new pl.coderstrust.soap.bindingclasses.Company();
        responseSeller.setId(seller.getId());
        responseSeller.setName(seller.getName());
        responseSeller.setAddress(seller.getAddress());
        responseSeller.setTaxId(seller.getTaxId());
        responseSeller.setAccountNumber(seller.getAccountNumber());
        responseSeller.setPhoneNumber(seller.getPhoneNumber());
        responseSeller.setEmail(seller.getEmail());
        return responseSeller;
    }

    private static pl.coderstrust.soap.bindingclasses.Company mapOriginalBuyerToSoapBuyer(Company buyer) {
        pl.coderstrust.soap.bindingclasses.Company responseBuyer = new pl.coderstrust.soap.bindingclasses.Company();
        responseBuyer.setId(buyer.getId());
        responseBuyer.setName(buyer.getName());
        responseBuyer.setAddress(buyer.getAddress());
        responseBuyer.setTaxId(buyer.getTaxId());
        responseBuyer.setAccountNumber(buyer.getAccountNumber());
        responseBuyer.setPhoneNumber(buyer.getPhoneNumber());
        responseBuyer.setEmail(buyer.getEmail());
        return responseBuyer;
    }

    static XMLGregorianCalendar convertLocalDateToXmlGregorianCalendar(LocalDate localDate) throws DatatypeConfigurationException {
        GregorianCalendar gregorianDate = new GregorianCalendar();
        gregorianDate.setTime(Date.valueOf(localDate));
        XMLGregorianCalendar xmlGregorianDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianDate);
        xmlGregorianDate.setTimezone(0);
        return xmlGregorianDate;
    }

    static LocalDate convertXmlGregorianCalendarToLocalDate(XMLGregorianCalendar gregorianDate) {
        return gregorianDate.toGregorianCalendar().toZonedDateTime().toLocalDate();
    }
}
