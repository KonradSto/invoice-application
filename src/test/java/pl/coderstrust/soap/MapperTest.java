package pl.coderstrust.soap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.coderstrust.generators.CompanyGenerator.getRandomNumberAsString;
import static pl.coderstrust.soap.Mapper.convertLocalDateToXMLGregorianCalendar;
import static pl.coderstrust.soap.Mapper.convertXMLGregorianCalendarToLocalDate;
import static pl.coderstrust.soap.Mapper.mapOriginalInvoiceToSoapInvoice;
import static pl.coderstrust.soap.Mapper.mapSoapInvoiceToOriginalInvoice;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.jupiter.api.Test;
import pl.coderstrust.generators.IdGenerator;
import pl.coderstrust.generators.InvoiceNumberGenerator;
import pl.coderstrust.generators.WordGenerator;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.Vat;

class MapperTest {

    private Invoice modelInvoice;
    private pl.coderstrust.soap.bindingClasses.Invoice soapInvoice;

    @Test
    void shouldMapSoapInvoiceToOriginalInvoice() throws DatatypeConfigurationException {
        //Given
        prepareRandomClasses();

        //When
        Invoice resultInvoice = mapSoapInvoiceToOriginalInvoice(soapInvoice);

        //Then
        assertEquals(modelInvoice, resultInvoice);
    }

    @Test
    void shouldMapSoapEntriesToOriginalEntries() throws DatatypeConfigurationException {
        //Given
        prepareRandomClasses();

        //When
        pl.coderstrust.soap.bindingClasses.Invoice resultInvoice = mapOriginalInvoiceToSoapInvoice(modelInvoice);

        //Then
        assertEquals(soapInvoice, resultInvoice);
    }

    @Test
    void shouldConvertLocalDateToXmlGregorianCalendar() throws DatatypeConfigurationException {
        //Given
        LocalDate localDate = LocalDate.of(2000, 1, 1);
        GregorianCalendar gregorianDate = new GregorianCalendar();
        gregorianDate.setTime(Date.valueOf(localDate));
        XMLGregorianCalendar gregorianCalendarDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianDate);

        //When
        XMLGregorianCalendar resultDate = convertLocalDateToXMLGregorianCalendar(localDate);

        //Then
        assertEquals(gregorianCalendarDate, resultDate);
    }

    @Test
    void shouldConvertXmlGregorianCalendarToLocalDate() throws DatatypeConfigurationException {
        //Given
        LocalDate localDate = LocalDate.of(2000, 1, 1);
        GregorianCalendar gregorianDate = new GregorianCalendar();
        gregorianDate.setTime(Date.valueOf(localDate));
        XMLGregorianCalendar gregorianCalendarDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianDate);

        //When
        LocalDate resultDate = convertXMLGregorianCalendarToLocalDate(gregorianCalendarDate);

        //Then
        assertEquals(localDate, resultDate);
    }
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
        LocalDate invoiceIssuedDate = LocalDate.now();
        LocalDate invoiceDueDate = invoiceIssuedDate.plusDays(2);
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
        List<pl.coderstrust.soap.bindingClasses.InvoiceEntry> soapEntries = new ArrayList<>();
        soapEntries.add(soapInvoiceEntry);
        modelInvoice = new Invoice(invoiceId, invoiceNumber, invoiceIssuedDate, invoiceDueDate, modelCompany, modelCompany, modelEntries);
        soapInvoice = new pl.coderstrust.soap.bindingClasses.Invoice();
        soapInvoice.setId(invoiceId);
        soapInvoice.setNumber(invoiceNumber);
        soapInvoice.setIssuedDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(invoiceIssuedDate.toString()));
        soapInvoice.setLocalDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(invoiceDueDate.toString()));
        soapInvoice.setSeller(soapCompany);
        soapInvoice.setBuyer(soapCompany);
        soapInvoice.getEntries().add(soapInvoiceEntry);
    }
}
