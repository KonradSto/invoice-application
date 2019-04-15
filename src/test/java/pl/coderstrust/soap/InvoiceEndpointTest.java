package pl.coderstrust.soap;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.payload;

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

    private Invoice invoice;

    @Autowired
    private WebApplicationContext applicationContext;

    private MockWebServiceClient mockClient;
    private Resource xsdSchema = new ClassPathResource("invoices.xsd");

    @MockBean
    private InvoiceService invoiceService;

    @BeforeEach
    void init() {
        mockClient = MockWebServiceClient.createClient(applicationContext);
        createInvoice();
    }

    private void createInvoice() {
        Company company1 = new Company(3L, "ABC", "aaa", "1111", "123123", "00-00", "email@ABC.com");
        Company company2 = new Company(4L, "XYZ", "bbb", "2222", "321321", "11-10", "email@XYZ.com");
        InvoiceEntry invoiceEntry1 = new InvoiceEntry(1L, "product1", 5, "szt.", new BigDecimal(20), new BigDecimal(100), new BigDecimal(100), Vat.VAT_0);
        InvoiceEntry invoiceEntry2 = new InvoiceEntry(2L, "product2", 2, "szt.", new BigDecimal(10), new BigDecimal(20), new BigDecimal(20), Vat.VAT_0);
        List<InvoiceEntry> entryList = Arrays.asList(invoiceEntry1, invoiceEntry2);
        invoice = new Invoice(1L, "1/2019", LocalDate.of(2019, 3, 1), LocalDate.of(2019, 3, 20), company1, company2, entryList);
    }

    @Test
    void shouldReturnInvoice() throws IOException, ServiceOperationException {
        //Given
        Optional<Invoice> invoiceOptional = Optional.of(invoice);
        when(invoiceService.getInvoice(1L)).thenReturn(invoiceOptional);
        String filePathRequest = "src/test/resources/getInvoiceRequest";
        String filePathResponse = "src/test/resources/getInvoiceResponse";
        String stringRequest = XmlFileReader.readFromFile(filePathRequest);
        String stringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(stringRequest);
        Source responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldReturnErrorResponseWhenAnErrorOccurredDuringGettingInvoice() throws IOException, ServiceOperationException {
        //Given
        when(invoiceService.getInvoice(1L)).thenThrow(ServiceOperationException.class);
        String filePathRequest = "src/test/resources/getInvoiceRequest";
        String filePathResponse = "src/test/resources/getInvoiceWithExceptionResponse";
        String stringRequest = XmlFileReader.readFromFile(filePathRequest);
        String stringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(stringRequest);
        Source responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldReturnAllInvoices() throws IOException, ServiceOperationException {
        //Given
        Collection<Invoice> invoices = Arrays.asList(invoice);
        when(invoiceService.getAllInvoices()).thenReturn(invoices);
        String filePathRequest = "src/test/resources/getAllInvoicesRequest";
        String filePathResponse = "src/test/resources/getAllInvoicesResponse";
        String stringRequest = XmlFileReader.readFromFile(filePathRequest);
        String stringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(stringRequest);
        Source responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldReturnErrorResponseWhenAnErrorOccurredDuringGettingAllInvoices() throws IOException, ServiceOperationException {
        //Given
        when(invoiceService.getAllInvoices()).thenThrow(ServiceOperationException.class);
        String filePathRequest = "src/test/resources/getAllInvoicesRequest";
        String filePathResponse = "src/test/resources/getAllInvoicesWithExceptionResponse";
        String stringRequest = XmlFileReader.readFromFile(filePathRequest);
        String stringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(stringRequest);
        Source responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldReturnEmptyListWhenThereIsNoInvoices() throws IOException, ServiceOperationException {
        //Given
        Collection<Invoice> invoices = new ArrayList<>();
        when(invoiceService.getAllInvoices()).thenReturn(invoices);
        String filePathRequest = "src/test/resources/getAllInvoicesRequest";
        String filePathResponse = "src/test/resources/getAllInvoicesResponseWhenThereAreNoInvoicesResponse";
        String stringRequest = XmlFileReader.readFromFile(filePathRequest);
        String stringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(stringRequest);
        Source responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldReturnInvoicesByDate() throws IOException, ServiceOperationException {
        //Given
        Collection<Invoice> invoices = Arrays.asList(invoice);
        when(invoiceService.getAllInvoicesByDate(LocalDate.of(2019, 1, 1), LocalDate.of(2019, 12, 1))).thenReturn(invoices);
        String filePathRequest = "src/test/resources/getAllInvoicesByDateRequest";
        String filePathResponse = "src/test/resources/getInvoicesByDateResponse";
        String stringRequest = XmlFileReader.readFromFile(filePathRequest);
        String stringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(stringRequest);
        Source responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldReturnErrorResponseWhenAnErrorOccurredDuringGettingInvoicesByDate() throws IOException, ServiceOperationException {
        //Given
        when(invoiceService.getAllInvoicesByDate(LocalDate.of(2019, 1, 1), LocalDate.of(2019, 12, 1))).thenThrow(ServiceOperationException.class);
        String filePathRequest = "src/test/resources/getAllInvoicesByDateRequest";
        String filePathResponse = "src/test/resources/getAllInvoicesByDateWithExceptionResponse";
        String stringRequest = XmlFileReader.readFromFile(filePathRequest);
        String stringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(stringRequest);
        Source responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldReturnInvoicesBySeller() throws IOException, ServiceOperationException {
        //Given
        Collection<Invoice> invoices = Arrays.asList(invoice);
        when(invoiceService.getAllInvoicesBySeller(3L)).thenReturn(invoices);
        String filePathRequest = "src/test/resources/getInvoicesBySellerRequest";
        String filePathResponse = "src/test/resources/getInvoicesBySellerResponse";
        String stringRequest = XmlFileReader.readFromFile(filePathRequest);
        String stringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(stringRequest);
        Source responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldReturnErrorResponseWhenAnErrorOccurredDuringGettingInvoicesBySeller() throws IOException, ServiceOperationException {
        //Given
        when(invoiceService.getAllInvoicesBySeller(3L)).thenThrow(ServiceOperationException.class);
        String filePathRequest = "src/test/resources/getInvoicesBySellerRequest";
        String filePathResponse = "src/test/resources/getAllInvoicesBySellerWithExceptionResponse";
        String stringRequest = XmlFileReader.readFromFile(filePathRequest);
        String stringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(stringRequest);
        Source responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldReturnInvoicesByBuyer() throws IOException, ServiceOperationException {
        //Given
        Collection<Invoice> invoices = Arrays.asList(invoice);
        when(invoiceService.getAllInvoicesByBuyer(4L)).thenReturn(invoices);
        String filePathRequest = "src/test/resources/getInvoicesByBuyerRequest";
        String filePathResponse = "src/test/resources/getInvoicesByBuyerResponse";
        String stringRequest = XmlFileReader.readFromFile(filePathRequest);
        String stringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(stringRequest);
        Source responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldReturnErrorResponseWhenAnErrorOccurredDuringGettingInvoicesByBuyer() throws IOException, ServiceOperationException {
        //Given
        when(invoiceService.getAllInvoicesByBuyer(4L)).thenThrow(ServiceOperationException.class);
        String filePathRequest = "src/test/resources/getInvoicesByBuyerRequest";
        String filePathResponse = "src/test/resources/getAllInvoicesByBuyerWithExceptionResponse";
        String stringRequest = XmlFileReader.readFromFile(filePathRequest);
        String stringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(stringRequest);
        Source responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldDeleteInvoice() throws IOException, ServiceOperationException {
        //Given
        Optional<Invoice> invoiceOptional = Optional.of(invoice);
        when(invoiceService.getInvoice(1L)).thenReturn(invoiceOptional);
        String filePathRequest = "src/test/resources/deleteInvoiceRequest";
        String filePathResponse = "src/test/resources/deleteInvoiceResponse";
        String stringRequest = XmlFileReader.readFromFile(filePathRequest);
        String stringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(stringRequest);
        Source responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldDeleteAllInvoices() throws IOException {
        //Given
        String filePathRequest = "src/test/resources/deleteAllInvoicesRequest";
        String filePathResponse = "src/test/resources/deleteAllInvoicesResponse";
        String stringRequest = XmlFileReader.readFromFile(filePathRequest);
        String stringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(stringRequest);
        Source responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldUpdateInvoice() throws ServiceOperationException, IOException {
        //Given
        when(invoiceService.saveInvoice(invoice)).thenReturn(invoice);
        String filePathRequest = "src/test/resources/updateInvoiceRequest";
        String filePathResponse = "src/test/resources/updateInvoiceResponse";
        String stringRequest = XmlFileReader.readFromFile(filePathRequest);
        String stringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(stringRequest);
        Source responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldReturnErrorResponseWhenAnErrorOccurredDuringUpdatingInvoice() throws IOException, ServiceOperationException {
        //Given
        when(invoiceService.saveInvoice(invoice)).thenThrow(ServiceOperationException.class);
        String filePathRequest = "src/test/resources/updateInvoiceRequest";
        String filePathResponse = "src/test/resources/updateInvoiceWithExceptionResponse";
        String stringRequest = XmlFileReader.readFromFile(filePathRequest);
        String stringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(stringRequest);
        Source responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldSaveInvoice() throws ServiceOperationException, IOException {
        //Given
        when(invoiceService.saveInvoice(any(Invoice.class))).thenReturn(invoice);
        String filePathRequest = "src/test/resources/saveInvoiceRequest";
        String filePathResponse = "src/test/resources/saveInvoiceResponse";
        String stringRequest = XmlFileReader.readFromFile(filePathRequest);
        String stringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(stringRequest);
        Source responsePayload = new StringSource(stringResponse);

        //When
        mockClient.sendRequest(withPayload(requestPayload))
            .andExpect(noFault())
            .andExpect(payload(responsePayload));
    }

    @Test
    void shouldReturnErrorResponseWhenAnErrorOccurredDuringSavingInvoice() throws IOException, ServiceOperationException {
        //Given
        when(invoiceService.saveInvoice(any(Invoice.class))).thenThrow(ServiceOperationException.class);
        String filePathRequest = "src/test/resources/saveInvoiceRequest";
        String filePathResponse = "src/test/resources/saveInvoiceWithExceptionResponse";
        String stringRequest = XmlFileReader.readFromFile(filePathRequest);
        String stringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(stringRequest);
        Source responsePayload = new StringSource(stringResponse);
    }
}
