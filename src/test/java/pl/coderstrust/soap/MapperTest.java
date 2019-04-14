package pl.coderstrust.soap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.coderstrust.soap.Mapper.mapInvoice;
import static pl.coderstrust.soap.Mapper.mapInvoices;
import static pl.coderstrust.soap.Mapper.mapLocalDateToXmlGregorianCalendar;
import static pl.coderstrust.soap.Mapper.mapXmlGregorianCalendarToLocalDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.soap.bindingclasses.Entries;

class MapperTest {

    private Invoice modelInvoice;
    private pl.coderstrust.soap.bindingclasses.Invoice soapInvoice;
    private List<Invoice> modelInvoices;
    private List<pl.coderstrust.soap.bindingclasses.Invoice> soapInvoices;

    @BeforeEach
    void setup() throws DatatypeConfigurationException {
        modelInvoice = InvoiceGenerator.getRandomInvoice();
        soapInvoice = convertModelInvoiceToSoapInvoice(modelInvoice);
        modelInvoices = Arrays.asList(InvoiceGenerator.getRandomInvoice(), InvoiceGenerator.getRandomInvoice(), InvoiceGenerator.getRandomInvoice());
        soapInvoices = new ArrayList<>();
        for (Invoice modelInvoice : modelInvoices){
            soapInvoices.add(convertModelInvoiceToSoapInvoice(modelInvoice));
        }
    }

    @Test
    void shouldMapSoapInvoiceToOriginalInvoice() {
        //When
        Invoice resultInvoice = mapInvoice(soapInvoice);

        //Then
        assertEquals(modelInvoice, resultInvoice);
    }

    @Test
    void shouldMapOriginalInvoiceToSoapInvoice() throws DatatypeConfigurationException {
        //When
        pl.coderstrust.soap.bindingclasses.Invoice resultInvoice = Mapper.mapInvoice(modelInvoice);

        //Then
        assertEquals(soapInvoice, resultInvoice);
    }

    @Test
    void shouldMapOriginalInvoicesToSoapInvoices() throws DatatypeConfigurationException {
        //When
        List<pl.coderstrust.soap.bindingclasses.Invoice> resultInvoices = mapInvoices(modelInvoices);

        //Then
        assertEquals(soapInvoices, resultInvoices);
    }

    @Test
    void shouldConvertLocalDateToXmlGregorianCalendar() throws DatatypeConfigurationException {
        //When
        XMLGregorianCalendar resultDate = mapLocalDateToXmlGregorianCalendar(modelInvoice.getIssuedDate());

        //Then
        assertEquals(soapInvoice.getIssuedDate(), resultDate);
    }


    @Test
    void shouldConvertXmlGregorianCalendarToLocalDate() {
        //When
        LocalDate resultDate = mapXmlGregorianCalendarToLocalDate(soapInvoice.getIssuedDate());

        //Then
        assertEquals(modelInvoice.getIssuedDate(), resultDate);
    }

    private pl.coderstrust.soap.bindingclasses.Invoice convertModelInvoiceToSoapInvoice(Invoice invoice) throws DatatypeConfigurationException {
        pl.coderstrust.soap.bindingclasses.Invoice soapInvoice = new pl.coderstrust.soap.bindingclasses.Invoice();
        soapInvoice.setId(invoice.getId());
        soapInvoice.setNumber(invoice.getNumber());
        soapInvoice.setIssuedDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(
                invoice.getIssuedDate().getYear(),
                invoice.getIssuedDate().getMonth().getValue(),
                invoice.getIssuedDate().getDayOfMonth(),
                0,
                0,
                0,
                0,
                0));
        soapInvoice.setDueDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(
                invoice.getDueDate().getYear(),
                invoice.getDueDate().getMonth().getValue(),
                invoice.getDueDate().getDayOfMonth(),
                0,
                0,
                0,
                0,
                0));
        soapInvoice.setSeller(convertModelCompanyToSoapCompany(invoice.getSeller()));
        soapInvoice.setBuyer(convertModelCompanyToSoapCompany(invoice.getBuyer()));
        pl.coderstrust.soap.bindingclasses.Entries mappedEntries = new Entries();
        for (InvoiceEntry invoiceEntry : invoice.getEntries()) {
            mappedEntries.getInvoiceEntry().add(convertModelInvoiceEntryToSoapInvoiceEntry(invoiceEntry));
        }
        soapInvoice.setEntries(mappedEntries);
        return soapInvoice;
    }

    private pl.coderstrust.soap.bindingclasses.Company convertModelCompanyToSoapCompany(Company company) {
        pl.coderstrust.soap.bindingclasses.Company soapCompany = new pl.coderstrust.soap.bindingclasses.Company();
        soapCompany.setId(company.getId());
        soapCompany.setName(company.getName());
        soapCompany.setAddress(company.getAddress());
        soapCompany.setTaxId(company.getTaxId());
        soapCompany.setAccountNumber(company.getAccountNumber());
        soapCompany.setPhoneNumber(company.getPhoneNumber());
        soapCompany.setEmail(company.getEmail());
        return soapCompany;
    }

    private pl.coderstrust.soap.bindingclasses.InvoiceEntry convertModelInvoiceEntryToSoapInvoiceEntry(InvoiceEntry invoiceEntry) {
        pl.coderstrust.soap.bindingclasses.InvoiceEntry soapInvoiceEntry = new pl.coderstrust.soap.bindingclasses.InvoiceEntry();
        soapInvoiceEntry.setId(invoiceEntry.getId());
        soapInvoiceEntry.setProductName(invoiceEntry.getProductName());
        soapInvoiceEntry.setQuantity(invoiceEntry.getQuantity());
        soapInvoiceEntry.setUnit(invoiceEntry.getUnit());
        soapInvoiceEntry.setPrice(invoiceEntry.getPrice());
        soapInvoiceEntry.setNettValue(invoiceEntry.getNetValue());
        soapInvoiceEntry.setGrossValue(invoiceEntry.getGrossValue());
        soapInvoiceEntry.setVatRate(pl.coderstrust.soap.bindingclasses.Vat.valueOf(soapInvoiceEntry.getVatRate().toString()));
        return soapInvoiceEntry;
    }


}
