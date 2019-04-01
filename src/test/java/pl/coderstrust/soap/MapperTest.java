package pl.coderstrust.soap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.coderstrust.generators.CompanyGenerator.getRandomNumberAsString;
import static pl.coderstrust.soap.Mapper.convertLocalDateToXMLGregorianCalendar;
import static pl.coderstrust.soap.Mapper.convertXMLGregorianCalendarToLocalDate;
import static pl.coderstrust.soap.Mapper.mapOriginalInvoiceToSoapInvoice;
import static pl.coderstrust.soap.Mapper.mapOriginalInvoicesToSoapInvoices;
import static pl.coderstrust.soap.Mapper.mapSoapInvoiceToOriginalInvoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.coderstrust.generators.IdGenerator;
import pl.coderstrust.generators.InvoiceNumberGenerator;
import pl.coderstrust.generators.WordGenerator;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.Vat;
import pl.coderstrust.soap.bindingClasses.Entries;

class MapperTest {

    private Invoice modelInvoice;
    private pl.coderstrust.soap.bindingClasses.Invoice soapInvoice;
    private List<Invoice> modelInvoiceList;
    private List<pl.coderstrust.soap.bindingClasses.Invoice> soapInvoiceList;


    @BeforeEach
    void prepareRandomClasses() throws DatatypeConfigurationException {
        Random random = new Random();
        Long entryId = IdGenerator.getNextId();
        String entryProductName = WordGenerator.getRandomWord();
        double entryQuantity = random.nextInt(10);
        String entryUnit = "szt.";
        BigDecimal entryPrice = BigDecimal.valueOf(random.nextInt(2000));
        BigDecimal entryNettValue = BigDecimal.valueOf(entryQuantity).multiply(entryPrice);
        BigDecimal entryGrossValue = entryNettValue.multiply(BigDecimal.valueOf(1.23).setScale(2, BigDecimal.ROUND_HALF_EVEN));
        Long companyId = IdGenerator.getNextId();
        String companyName = WordGenerator.getRandomWord();
        String companyAddress = WordGenerator.getRandomWord();
        String companyTaxId = getRandomNumberAsString();
        String companyAccountNumber = getRandomNumberAsString();
        String companyPhoneNumber = getRandomNumberAsString();
        String companyEmail = WordGenerator.getRandomWord();
        Long invoiceId = IdGenerator.getNextId();
        String invoiceNumber = InvoiceNumberGenerator.getNextInvoiceNumber();
        LocalDate invoiceIssuedDate = LocalDate.of(2000, 1, 1);
        LocalDate invoiceDueDate = LocalDate.of(2000, 2, 1);
        Company modelCompany = new Company(companyId, companyName, companyAddress, companyTaxId, companyAccountNumber, companyPhoneNumber, companyEmail);
        pl.coderstrust.soap.bindingClasses.Company soapCompany = new pl.coderstrust.soap.bindingClasses.Company();
        soapCompany.setId(companyId);
        soapCompany.setName(companyName);
        soapCompany.setAddress(companyAddress);
        soapCompany.setTaxId(companyTaxId);
        soapCompany.setAccountNumber(companyAccountNumber);
        soapCompany.setPhoneNumber(companyPhoneNumber);
        soapCompany.setEmail(companyEmail);
        InvoiceEntry modelInvoiceEntry = new InvoiceEntry(entryId, entryProductName, entryQuantity, entryUnit, entryPrice, entryNettValue, entryGrossValue, Vat.VAT_23);
        List<InvoiceEntry> modelEntries = new ArrayList<>();
        modelEntries.add(modelInvoiceEntry);
        pl.coderstrust.soap.bindingClasses.InvoiceEntry soapInvoiceEntry = new pl.coderstrust.soap.bindingClasses.InvoiceEntry();
        soapInvoiceEntry.setId(entryId);
        soapInvoiceEntry.setProductName(entryProductName);
        soapInvoiceEntry.setQuantity(entryQuantity);
        soapInvoiceEntry.setUnit(entryUnit);
        soapInvoiceEntry.setPrice(entryPrice);
        soapInvoiceEntry.setNettValue(entryNettValue);
        soapInvoiceEntry.setGrossValue(entryGrossValue);
        soapInvoiceEntry.setVatRate(pl.coderstrust.soap.bindingClasses.Vat.VAT_23);
//        List<pl.coderstrust.soap.bindingClasses.InvoiceEntry> soapEntries = new ArrayList<>();
//        soapEntries.add(soapInvoiceEntry);
        pl.coderstrust.soap.bindingClasses.Entries soapInvoiceEntries = new Entries();
        soapInvoiceEntries.getInvoiceEntry().add(soapInvoiceEntry);

        modelInvoice = new Invoice(invoiceId, invoiceNumber, invoiceIssuedDate, invoiceDueDate, modelCompany, modelCompany, modelEntries);
        soapInvoice = new pl.coderstrust.soap.bindingClasses.Invoice();
        soapInvoice.setId(invoiceId);
        soapInvoice.setNumber(invoiceNumber);
        soapInvoice.setIssuedDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(2000, 1, 1, 0, 0, 0, 0, 0));
        soapInvoice.setLocalDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(2000, 2, 1, 0, 0, 0, 0, 0));
        soapInvoice.setSeller(soapCompany);
        soapInvoice.setBuyer(soapCompany);
        soapInvoice.setEntries(soapInvoiceEntries);

        modelInvoiceList = new ArrayList<>();
        modelInvoiceList.add(modelInvoice);
        soapInvoiceList = new ArrayList<>();
        soapInvoiceList.add(soapInvoice);
    }

    @Test
    void shouldMapSoapInvoiceToOriginalInvoice() throws DatatypeConfigurationException {
        //When
        Invoice resultInvoice = mapSoapInvoiceToOriginalInvoice(soapInvoice);

        //Then
        assertEquals(modelInvoice, resultInvoice);
    }

    @Test
    void shouldMapOriginalInvoiceToSoapInvoice() throws DatatypeConfigurationException {
        //When
        pl.coderstrust.soap.bindingClasses.Invoice resultInvoice = mapOriginalInvoiceToSoapInvoice(modelInvoice);

        //Then
        assertEquals(soapInvoice, resultInvoice);
    }

    @Test
    void shouldMapOriginalInvoicesToSoapInvoices() throws DatatypeConfigurationException {
        //When
        List<pl.coderstrust.soap.bindingClasses.Invoice> resultInvoices = mapOriginalInvoicesToSoapInvoices(modelInvoiceList);

        //Then
        assertEquals(soapInvoiceList, resultInvoices);
    }

    @Test
    void shouldConvertLocalDateToXmlGregorianCalendar() throws DatatypeConfigurationException {
        //When
        XMLGregorianCalendar resultDate = convertLocalDateToXMLGregorianCalendar(modelInvoice.getIssuedDate());

        //Then
        assertEquals(soapInvoice.getIssuedDate(), resultDate);
    }


    @Test
    void shouldConvertXmlGregorianCalendarToLocalDate() {
        //When
        LocalDate resultDate = convertXMLGregorianCalendarToLocalDate(soapInvoice.getIssuedDate());

        //Then
        assertEquals(modelInvoice.getIssuedDate(), resultDate);
    }
}
