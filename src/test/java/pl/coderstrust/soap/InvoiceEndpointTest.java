package pl.coderstrust.soap;


import static org.mockito.Mockito.when;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.payload;
import static org.springframework.ws.test.server.ResponseMatchers.validPayload;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.xml.transform.Source;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.Vat;
import pl.coderstrust.service.InvoiceService;
import pl.coderstrust.service.ServiceOperationException;

@SpringBootTest
class InvoiceEndpointTest {

    private Invoice invoice1;
    private String stringRequest;
    private String stringResponse;
    private File filePathRequest;
    private File filePathResponse;
    private Source requestPayload;
    private Source responsePayload;

    @Autowired
    private WebApplicationContext applicationContext;

    private MockWebServiceClient mockClient;
    private Resource xsdSchema = new ClassPathResource("invoices.xsd");

    @MockBean
    private InvoiceService invoiceService;

    @BeforeEach
    void init() {
        mockClient = MockWebServiceClient.createClient(applicationContext);
        Company company1 = new Company(3L, "ABC", "aaa", "1111", "123123", "00-00", "email@ABC.com");
        Company company2 = new Company(4L, "XYZ", "bbb", "2222", "321321", "11-10", "email@XYZ.com");
        InvoiceEntry invoiceEntry1 = new InvoiceEntry(1L, "product1", 5, "szt.", new BigDecimal(20), new BigDecimal(100), new BigDecimal(100), Vat.VAT_0);
        InvoiceEntry invoiceEntry2 = new InvoiceEntry(2L, "product2", 2, "szt.", new BigDecimal(10), new BigDecimal(20), new BigDecimal(20), Vat.VAT_0);
        List<InvoiceEntry> entryList = Arrays.asList(invoiceEntry1, invoiceEntry2);
        invoice1 = new Invoice(1L, "1/2019", LocalDate.of(2019, 3, 1), LocalDate.of(2019, 3, 20), company1, company2, entryList);
    }

    @Test
    void shouldValidateXsdGetInvoiceResponse() throws IOException, ServiceOperationException {
        //Given
        Optional<Invoice> invoiceOptional = Optional.of(invoice1);
        when(invoiceService.getInvoice(1L)).thenReturn(invoiceOptional);
        filePathRequest = new File("src/test/resources/getInvoiceRequest");
        filePathResponse = new File("src/test/resources/getInvoiceResponse");
        stringRequest = XmlFileReader.readFromFile(filePathRequest);
        stringResponse = XmlFileReader.readFromFile(filePathResponse);
        requestPayload = new StringSource(stringRequest);
        responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))

            //Then
            .andExpect(noFault())
            .andExpect(payload(responsePayload))
            .andExpect(validPayload(xsdSchema));
    }

    @Test
    void shouldValidateXsdGetInvoiceResponseWhenDataBaseExceptionOccurs() throws IOException, ServiceOperationException {
        //Given
        when(invoiceService.getInvoice(1L)).thenThrow(ServiceOperationException.class);
        filePathRequest = new File("src/test/resources/getInvoiceRequest");
        filePathResponse = new File("src/test/resources/getInvoiceWithExceptionResponse");
        stringRequest = XmlFileReader.readFromFile(filePathRequest);
        stringResponse = XmlFileReader.readFromFile(filePathResponse);
        requestPayload = new StringSource(stringRequest);
        responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))

            //Then
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldValidateXsdGetAllInvoicesResponse() throws IOException, ServiceOperationException {
        //Given
        Collection<Invoice> invoices = Arrays.asList(invoice1);
        when(invoiceService.getAllInvoices()).thenReturn(invoices);
        filePathRequest = new File("src/test/resources/getAllInvoicesRequest");
        filePathResponse = new File("src/test/resources/getAllInvoicesResponse");
        stringRequest = XmlFileReader.readFromFile(filePathRequest);
        stringResponse = XmlFileReader.readFromFile(filePathResponse);
        requestPayload = new StringSource(stringRequest);
        responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))

            //Then
            .andExpect(noFault())
            .andExpect(payload(responsePayload))
            .andExpect(validPayload(xsdSchema));
    }

    @Test
    void shouldValidateXsdGetAllInvoicesResponseWhenDataBaseExceptionOccurs() throws IOException, ServiceOperationException {
        //Given
        when(invoiceService.getAllInvoices()).thenThrow(ServiceOperationException.class);
        filePathRequest = new File("src/test/resources/getAllInvoicesRequest");
        filePathResponse = new File("src/test/resources/getAllInvoicesWithExceptionResponse");
        stringRequest = XmlFileReader.readFromFile(filePathRequest);
        stringResponse = XmlFileReader.readFromFile(filePathResponse);
        requestPayload = new StringSource(stringRequest);
        responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))

            //Then
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldValidateXsdGetAllInvoicesResponseWhenThereAreNoInvoices() throws IOException, ServiceOperationException {
        //Given
        Collection<Invoice> invoices = new ArrayList<>();
        when(invoiceService.getAllInvoices()).thenReturn(invoices);
        filePathRequest = new File("src/test/resources/getAllInvoicesRequest");
        filePathResponse = new File("src/test/resources/getAllInvoicesResponseWhenThereAreNoInvoicesResponse");
        stringRequest = XmlFileReader.readFromFile(filePathRequest);
        stringResponse = XmlFileReader.readFromFile(filePathResponse);
        requestPayload = new StringSource(stringRequest);
        responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))

            //Then
            .andExpect(noFault())
            .andExpect(payload(responsePayload))
            .andExpect(validPayload(xsdSchema));
    }

    @Test
    void shouldValidateXsdGetAllInvoicesByDateResponse() throws IOException, ServiceOperationException {
        //Given
        Collection<Invoice> invoices = Arrays.asList(invoice1);
        when(invoiceService.getAllInvoicesByDate(LocalDate.of(2019, 1, 1), LocalDate.of(2019, 12, 1))).thenReturn(invoices);
        filePathRequest = new File("src/test/resources/getAllInvoicesByDateRequest");
        filePathResponse = new File("src/test/resources/getInvoicesByDateResponse");
        stringRequest = XmlFileReader.readFromFile(filePathRequest);
        stringResponse = XmlFileReader.readFromFile(filePathResponse);
        requestPayload = new StringSource(stringRequest);
        responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))

            //Then
            .andExpect(noFault())
            .andExpect(payload(responsePayload))
            .andExpect(validPayload(xsdSchema));
    }

    @Test
    void shouldValidateXsdGetAllInvoicesByDateResponseWhenDataBaseExceptionOccurs() throws IOException, ServiceOperationException {
        //Given
        when(invoiceService.getAllInvoicesByDate(LocalDate.of(2019, 1, 1), LocalDate.of(2019, 12, 1))).thenThrow(ServiceOperationException.class);
        filePathRequest = new File("src/test/resources/getAllInvoicesByDateRequest");
        filePathResponse = new File("src/test/resources/getAllInvoicesByDateWithExceptionResponse");
        stringRequest = XmlFileReader.readFromFile(filePathRequest);
        stringResponse = XmlFileReader.readFromFile(filePathResponse);
        requestPayload = new StringSource(stringRequest);
        responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))

            //Then
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldValidateXsdGetAllInvoicesBySellerResponse() throws IOException, ServiceOperationException {
        //Given
        Collection<Invoice> invoices = Arrays.asList(invoice1);
        when(invoiceService.getAllInvoicesBySeller(3L)).thenReturn(invoices);
        filePathRequest = new File("src/test/resources/getInvoicesBySellerRequest");
        filePathResponse = new File("src/test/resources/getInvoicesBySellerResponse");
        stringRequest = XmlFileReader.readFromFile(filePathRequest);
        stringResponse = XmlFileReader.readFromFile(filePathResponse);
        requestPayload = new StringSource(stringRequest);
        responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))

            //Then
            .andExpect(noFault())
            .andExpect(payload(responsePayload))
            .andExpect(validPayload(xsdSchema));
    }

    @Test
    void shouldValidateXsdGetInvoicesByGivenSellerResponseWhenDataBaseExceptionOccurs() throws IOException, ServiceOperationException {
        //Given
        when(invoiceService.getAllInvoicesBySeller(3L)).thenThrow(ServiceOperationException.class);
        filePathRequest = new File("src/test/resources/getInvoicesBySellerRequest");
        filePathResponse = new File("src/test/resources/getAllInvoicesBySellerWithExceptionResponse");
        stringRequest = XmlFileReader.readFromFile(filePathRequest);
        stringResponse = XmlFileReader.readFromFile(filePathResponse);
        requestPayload = new StringSource(stringRequest);
        responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))

            //Then
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldValidateXsdGetAllInvoicesByBuyerResponse() throws IOException, ServiceOperationException {
        //Given
        Collection<Invoice> invoices = Arrays.asList(invoice1);
        when(invoiceService.getAllInvoicesByBuyer(4L)).thenReturn(invoices);
        filePathRequest = new File("src/test/resources/getInvoicesByBuyerRequest");
        filePathResponse = new File("src/test/resources/getInvoicesByBuyerResponse");
        stringRequest = XmlFileReader.readFromFile(filePathRequest);
        stringResponse = XmlFileReader.readFromFile(filePathResponse);
        requestPayload = new StringSource(stringRequest);
        responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))

            //Then
            .andExpect(noFault())
            .andExpect(payload(responsePayload))
            .andExpect(validPayload(xsdSchema));
    }

    @Test
    void shouldValidateXsdGetInvoicesByGivenBuyerResponseWhenDataBaseExceptionOccurs() throws IOException, ServiceOperationException {
        //Given
        when(invoiceService.getAllInvoicesByBuyer(4L)).thenThrow(ServiceOperationException.class);
        filePathRequest = new File("src/test/resources/getInvoicesByBuyerRequest");
        filePathResponse = new File("src/test/resources/getAllInvoicesByBuyerWithExceptionResponse");
        stringRequest = XmlFileReader.readFromFile(filePathRequest);
        stringResponse = XmlFileReader.readFromFile(filePathResponse);
        requestPayload = new StringSource(stringRequest);
        responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))

            //Then
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldValidateXsdDeleteInvoiceResponse() throws IOException {
        //Given
        filePathRequest = new File("src/test/resources/deleteInvoiceRequest");
        filePathResponse = new File("src/test/resources/deleteInvoiceResponse");
        stringRequest = XmlFileReader.readFromFile(filePathRequest);
        stringResponse = XmlFileReader.readFromFile(filePathResponse);
        requestPayload = new StringSource(stringRequest);
        responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))

            //Then
            .andExpect(noFault())
            .andExpect(payload(responsePayload))
            .andExpect(validPayload(xsdSchema));
    }

    @Test
    void shouldValidateXsdDeleteAllInvoicesResponse() throws IOException {
        //Given
        filePathRequest = new File("src/test/resources/deleteAllInvoicesRequest");
        filePathResponse = new File("src/test/resources/deleteAllInvoicesResponse");
        stringRequest = XmlFileReader.readFromFile(filePathRequest);
        stringResponse = XmlFileReader.readFromFile(filePathResponse);
        requestPayload = new StringSource(stringRequest);
        responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))

            //Then
            .andExpect(noFault())
            .andExpect(payload(responsePayload))
            .andExpect(validPayload(xsdSchema));
    }

    @Test
    void shouldValidateXsdSaveInvoiceResponse() throws ServiceOperationException, IOException {
        //Given
        when(invoiceService.saveInvoice(invoice1)).thenReturn(invoice1);
        filePathRequest = new File("src/test/resources/saveInvoiceRequest");
        filePathResponse = new File("src/test/resources/saveInvoiceResponse");
        stringRequest = XmlFileReader.readFromFile(filePathRequest);
        stringResponse = XmlFileReader.readFromFile(filePathResponse);
        requestPayload = new StringSource(stringRequest);
        responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))

            //Then
            .andExpect(noFault())
            .andExpect(payload(responsePayload))
            .andExpect(validPayload(xsdSchema));
    }

    @Test
    void shouldValidateXsdSaveInvoiceResponseWhenDataBaseExceptionOccurs() throws IOException, ServiceOperationException {
        //Given
        when(invoiceService.saveInvoice(invoice1)).thenThrow(ServiceOperationException.class);
        filePathRequest = new File("src/test/resources/saveInvoiceRequest");
        filePathResponse = new File("src/test/resources/saveInvoiceWithExceptionResponse");
        stringRequest = XmlFileReader.readFromFile(filePathRequest);
        stringResponse = XmlFileReader.readFromFile(filePathResponse);
        requestPayload = new StringSource(stringRequest);
        responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))

            //Then
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }
}
