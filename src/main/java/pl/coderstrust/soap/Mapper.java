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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.coderstrust.controller.InvoiceController;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.Vat;
import pl.coderstrust.soap.bindingclasses.Entries;

class Mapper {

    private static Logger log = LoggerFactory.getLogger(InvoiceController.class);

    static Invoice mapInvoice(pl.coderstrust.soap.bindingclasses.Invoice invoiceToMap) {
        log.debug("Mapping generated SOAP Invoice to model Invoice");
        return new Invoice(
            invoiceToMap.getId(),
            invoiceToMap.getNumber(),
            mapXmlGregorianCalendarToLocalDate(invoiceToMap.getIssuedDate()),
            mapXmlGregorianCalendarToLocalDate(invoiceToMap.getDueDate()),
            mapSeller(invoiceToMap.getSeller()),
            mapBuyer(invoiceToMap.getBuyer()),
            mapInvoiceEntries(invoiceToMap.getEntries().getInvoiceEntry())
        );
    }

    static pl.coderstrust.soap.bindingclasses.Invoice mapInvoice(Invoice invoiceToMap) throws DatatypeConfigurationException {
        log.debug("Mapping model Invoice to generated SOAP Invoice");
        pl.coderstrust.soap.bindingclasses.Invoice mappedInvoice = new pl.coderstrust.soap.bindingclasses.Invoice();
        mappedInvoice.setId(invoiceToMap.getId());
        mappedInvoice.setNumber(invoiceToMap.getNumber());
        mappedInvoice.setIssuedDate(mapLocalDateToXmlGregorianCalendar((invoiceToMap.getIssuedDate())));
        mappedInvoice.setDueDate(mapLocalDateToXmlGregorianCalendar(invoiceToMap.getDueDate()));
        mappedInvoice.setSeller(mapSeller(invoiceToMap.getSeller()));
        mappedInvoice.setBuyer(mapBuyer(invoiceToMap.getBuyer()));
        pl.coderstrust.soap.bindingclasses.Entries mappedEntries = new Entries();
        for (InvoiceEntry entryToMap : invoiceToMap.getEntries()) {
            mappedEntries.getInvoiceEntry().add(mapInvoiceEntry(entryToMap));
        }
        mappedInvoice.setEntries(mappedEntries);
        return mappedInvoice;
    }

    private static List<InvoiceEntry> mapInvoiceEntries(List<pl.coderstrust.soap.bindingclasses.InvoiceEntry> invoiceEntriesToMap) {
        log.debug("Mapping generated SOAP List of InvoiceEntries to model List of InvoiceEntries");
        List<InvoiceEntry> mappedInvoiceEntries = new ArrayList<>();
        for (pl.coderstrust.soap.bindingclasses.InvoiceEntry entry : invoiceEntriesToMap) {
            mappedInvoiceEntries.add(mapInvoiceEntry(entry));
        }
        return mappedInvoiceEntries;
    }

    private static InvoiceEntry mapInvoiceEntry(pl.coderstrust.soap.bindingclasses.InvoiceEntry invoiceEntryToMap) {
        log.debug("Mapping generated SOAP InvoiceEntry to model InvoiceEntry");
        return new InvoiceEntry(
            invoiceEntryToMap.getId(),
            invoiceEntryToMap.getProductName(),
            invoiceEntryToMap.getQuantity(),
            invoiceEntryToMap.getUnit(),
            invoiceEntryToMap.getPrice(),
            invoiceEntryToMap.getNettValue(),
            invoiceEntryToMap.getGrossValue(),
            Vat.valueOf(invoiceEntryToMap.getVatRate().value())
        );
    }

    private static pl.coderstrust.soap.bindingclasses.InvoiceEntry mapInvoiceEntry(InvoiceEntry invoiceEntryToMap) {
        log.debug("Mapping model InvoiceEntry to generated SOAP InvoiceEntry");
        pl.coderstrust.soap.bindingclasses.InvoiceEntry mappedInvoiceEntry = new pl.coderstrust.soap.bindingclasses.InvoiceEntry();
        mappedInvoiceEntry.setId(invoiceEntryToMap.getId());
        mappedInvoiceEntry.setProductName(invoiceEntryToMap.getProductName());
        mappedInvoiceEntry.setQuantity(invoiceEntryToMap.getQuantity());
        mappedInvoiceEntry.setUnit(invoiceEntryToMap.getUnit());
        mappedInvoiceEntry.setPrice(invoiceEntryToMap.getPrice());
        mappedInvoiceEntry.setNettValue(invoiceEntryToMap.getNetValue());
        mappedInvoiceEntry.setGrossValue(invoiceEntryToMap.getGrossValue());
        mappedInvoiceEntry.setVatRate(pl.coderstrust.soap.bindingclasses.Vat.valueOf(invoiceEntryToMap.getVatRate().toString()));
        return mappedInvoiceEntry;
    }

    private static Company mapBuyer(pl.coderstrust.soap.bindingclasses.Company buyerToMap) {
        log.debug("Mapping generated SOAP Buyer to model Buyer");
        return new Company(
            buyerToMap.getId(),
            buyerToMap.getName(),
            buyerToMap.getAddress(),
            buyerToMap.getTaxId(),
            buyerToMap.getAccountNumber(),
            buyerToMap.getPhoneNumber(),
            buyerToMap.getEmail()
        );
    }

    private static pl.coderstrust.soap.bindingclasses.Company mapBuyer(Company buyerToMap) {
        log.debug("Mapping model Buyer to generated SOAP Buyer");
        pl.coderstrust.soap.bindingclasses.Company mappedBuyer = new pl.coderstrust.soap.bindingclasses.Company();
        mappedBuyer.setId(buyerToMap.getId());
        mappedBuyer.setName(buyerToMap.getName());
        mappedBuyer.setAddress(buyerToMap.getAddress());
        mappedBuyer.setTaxId(buyerToMap.getTaxId());
        mappedBuyer.setAccountNumber(buyerToMap.getAccountNumber());
        mappedBuyer.setPhoneNumber(buyerToMap.getPhoneNumber());
        mappedBuyer.setEmail(buyerToMap.getEmail());
        return mappedBuyer;
    }

    private static Company mapSeller(pl.coderstrust.soap.bindingclasses.Company sellerToMap) {
        log.debug("Mapping generated SOAP Seller to model Seller");
        return new Company(
            sellerToMap.getId(),
            sellerToMap.getName(),
            sellerToMap.getAddress(),
            sellerToMap.getTaxId(),
            sellerToMap.getAccountNumber(),
            sellerToMap.getPhoneNumber(),
            sellerToMap.getEmail()
        );
    }

    private static pl.coderstrust.soap.bindingclasses.Company mapSeller(Company sellerToMap) {
        log.debug("Mapping model Seller to generated SOAP Seller");
        pl.coderstrust.soap.bindingclasses.Company responseSeller = new pl.coderstrust.soap.bindingclasses.Company();
        responseSeller.setId(sellerToMap.getId());
        responseSeller.setName(sellerToMap.getName());
        responseSeller.setAddress(sellerToMap.getAddress());
        responseSeller.setTaxId(sellerToMap.getTaxId());
        responseSeller.setAccountNumber(sellerToMap.getAccountNumber());
        responseSeller.setPhoneNumber(sellerToMap.getPhoneNumber());
        responseSeller.setEmail(sellerToMap.getEmail());
        return responseSeller;
    }

    static List<pl.coderstrust.soap.bindingclasses.Invoice> mapInvoices(Collection<Invoice> invoicesToMap) throws DatatypeConfigurationException {
        log.debug("Mapping model List of Invoices to generated SOAP List of Invoices");
        List<pl.coderstrust.soap.bindingclasses.Invoice> mappedInvoices = new ArrayList<>();
        for (Invoice invoiceToMap : invoicesToMap) {
            mappedInvoices.add(mapInvoice(invoiceToMap));
        }
        return mappedInvoices;
    }

    static XMLGregorianCalendar mapLocalDateToXmlGregorianCalendar(LocalDate localDateToMap) throws DatatypeConfigurationException {
        log.debug("Converting LocalDate date to XMLGregorianCalendar date");
        GregorianCalendar gregorianDate = new GregorianCalendar();
        gregorianDate.setTime(Date.valueOf(localDateToMap));
        XMLGregorianCalendar xmlGregorianDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianDate);
        xmlGregorianDate.setTimezone(0);
        return xmlGregorianDate;
    }

    static LocalDate mapXmlGregorianCalendarToLocalDate(XMLGregorianCalendar gregorianDateToMap) {
        log.debug("Converting XMLGregorianCalendar date to LocalDate date");
        return gregorianDateToMap.toGregorianCalendar().toZonedDateTime().toLocalDate();
    }
}
