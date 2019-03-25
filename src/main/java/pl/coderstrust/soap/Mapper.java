package pl.coderstrust.soap;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.Vat;

public class Mapper {

    static Collection<pl.coderstrust.soap.bindingClasses.Invoice> mapOriginalInvoicesToSOAPInvoices(List<Invoice> invoices) throws DatatypeConfigurationException {
        Collection<pl.coderstrust.soap.bindingClasses.Invoice> responseInvoices = new ArrayList<>();
        for (Invoice invoice : invoices) {
            responseInvoices.add(mapOriginalInvoiceToSOAPInvoice(invoice));
        }
        return responseInvoices;
    }

    static Invoice mapSOAPInvoiceToOriginalInvoice(pl.coderstrust.soap.bindingClasses.Invoice responseInvoice) throws DatatypeConfigurationException {
        return new Invoice(
            responseInvoice.getId(),
            responseInvoice.getNumber(),
            convertXMLGregorianCalendarToLocalDate(responseInvoice.getIssuedDate()),
            convertXMLGregorianCalendarToLocalDate(responseInvoice.getLocalDate()),
            mapSOAPSellerToOriginalSeller(responseInvoice.getSeller()),
            mapSOAPBuyerToOriginalBuyer(responseInvoice.getBuyer()),
            mapSOAPEntriesToOriginalEntries(responseInvoice.getEntries())
        );
    }

    static List<InvoiceEntry> mapSOAPEntriesToOriginalEntries(List<pl.coderstrust.soap.bindingClasses.InvoiceEntry> responseEntries) {
        List<InvoiceEntry> entries = new ArrayList<>();
        for (pl.coderstrust.soap.bindingClasses.InvoiceEntry entry : responseEntries) {
            entries.add(mapSOAPEntryToOriginalEntry(entry));
        }
        return entries;
    }

    static InvoiceEntry mapSOAPEntryToOriginalEntry(pl.coderstrust.soap.bindingClasses.InvoiceEntry responseEntry) {
        pl.coderstrust.soap.bindingClasses.Vat responseVatRate = responseEntry.getVatRate();
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

    static Company mapSOAPBuyerToOriginalBuyer(pl.coderstrust.soap.bindingClasses.Company responseBuyer) {
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

    static Company mapSOAPSellerToOriginalSeller(pl.coderstrust.soap.bindingClasses.Company responseSeller) {
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

    static pl.coderstrust.soap.bindingClasses.Invoice mapOriginalInvoiceToSOAPInvoice(Invoice invoice) throws DatatypeConfigurationException {
        pl.coderstrust.soap.bindingClasses.Invoice responseInvoice = new pl.coderstrust.soap.bindingClasses.Invoice();
        responseInvoice.setId(invoice.getId());
        responseInvoice.setNumber(invoice.getNumber());
        responseInvoice.setIssuedDate(convertLocalDateToXMLGregorianCalendar((invoice.getIssuedDate())));
        responseInvoice.setLocalDate(convertLocalDateToXMLGregorianCalendar(invoice.getIssuedDate()));
        responseInvoice.setSeller(mapOriginalSellerToSOAPSeller(invoice.getSeller()));
        responseInvoice.setBuyer(mapOriginalBuyerToSOAPBuyer(invoice.getBuyer()));
        List<InvoiceEntry> entries = invoice.getEntries();
        for (InvoiceEntry entry : entries) {
            responseInvoice.getEntries().add(mapOriginalEntryToSOAPEntry(entry));
        }
        return responseInvoice;
    }

    static pl.coderstrust.soap.bindingClasses.InvoiceEntry mapOriginalEntryToSOAPEntry(InvoiceEntry entry) {
        pl.coderstrust.soap.bindingClasses.InvoiceEntry responseEntry = new pl.coderstrust.soap.bindingClasses.InvoiceEntry();
        responseEntry.setId(entry.getId());
        responseEntry.setProductName(entry.getProductName());
        responseEntry.setQuantity(entry.getQuantity());
        responseEntry.setUnit(entry.getUnit());
        responseEntry.setPrice(entry.getPrice());
        responseEntry.setNettValue(entry.getNettValue());
        responseEntry.setGrossValue(entry.getGrossValue());
        responseEntry.setVatRate(pl.coderstrust.soap.bindingClasses.Vat.valueOf(entry.getVatRate().toString()));
        return responseEntry;
    }

    static pl.coderstrust.soap.bindingClasses.Company mapOriginalSellerToSOAPSeller(Company seller) {
        pl.coderstrust.soap.bindingClasses.Company responseSeller = new pl.coderstrust.soap.bindingClasses.Company();
        responseSeller.setId(seller.getId());
        responseSeller.setName(seller.getName());
        responseSeller.setAddress(seller.getAddress());
        responseSeller.setTaxId(seller.getTaxId());
        responseSeller.setAccountNumber(seller.getAccountNumber());
        responseSeller.setPhoneNumber(seller.getPhoneNumber());
        responseSeller.setEmail(seller.getEmail());
        return responseSeller;
    }

    static pl.coderstrust.soap.bindingClasses.Company mapOriginalBuyerToSOAPBuyer(Company buyer) {
        pl.coderstrust.soap.bindingClasses.Company responseBuyer = new pl.coderstrust.soap.bindingClasses.Company();
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
