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

//@ExtendWith(SpringExtension.class)

@SpringBootTest

//@AutoConfigureMockWebServiceClient
class InvoiceEndpointTest {

    private Invoice invoice1;
    private Invoice invoice2;

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
        invoice1 = new Invoice(null, "1/2019", LocalDate.of(2019, 3, 1), LocalDate.of(2019, 3, 20), company1, company2, entryList);
        invoice2 = new Invoice(null, "2/2019", LocalDate.of(2018, 1, 1), LocalDate.of(2018, 6, 20), company2, company1, entryList);
    }

    @Test
    void shouldValidateXsdGetAllInvoicesResponse() throws IOException, ServiceOperationException {
        //Given

        Collection<Invoice> invoices = Arrays.asList(invoice1, invoice2);
        when(invoiceService.getAllInvoices()).thenReturn(invoices);
        File filePathRequest = new File("src/test/resources/getAllInvoicesRequest");
        File filePathResponse = new File("src/test/resources/getAllInvoicesResponse");
        String getInvoiceStringRequest = XmlFileReader.readFromFile(filePathRequest);
        String getInvoiceStringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(getInvoiceStringRequest);
        Source responsePayload = new StringSource(getInvoiceStringResponse);
        System.out.println(withPayload(requestPayload));
        //When
        mockClient.sendRequest(withPayload(requestPayload))

            //Then
            .andExpect(noFault())
            .andExpect(payload(responsePayload))
            .andExpect(validPayload(xsdSchema));
    }

    @Test
    void shouldValidateXsdGetAllInvoicesResponseWhenThereAreNoInvoices() throws IOException, ServiceOperationException {
        //Given
        Collection<Invoice> invoices = new ArrayList<>();
        when(invoiceService.getAllInvoices()).thenReturn(invoices);
        File filePathRequest = new File("src/test/resources/getAllInvoicesRequest");
        File filePathResponse = new File("src/test/resources/getAllInvoicesResponseWhenThereAreNoInvoices");
        String getInvoiceStringRequest = XmlFileReader.readFromFile(filePathRequest);
        String getInvoiceStringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(getInvoiceStringRequest);
        Source responsePayload = new StringSource(getInvoiceStringResponse);
        System.out.println(withPayload(requestPayload));
        //When
        mockClient.sendRequest(withPayload(requestPayload))

            //Then
            .andExpect(noFault())
            .andExpect(payload(responsePayload))
            .andExpect(validPayload(xsdSchema));
    }

    @Test
    void getAllInvoices() {
    }

    @Test
    void getInvoicesByDate() {
    }

    @Test
    void getInvoicesByBuyer() {
    }

    @Test
    void getInvoicesBySeller() {
    }

    @Test
    void deleteInvoice() {
    }

    @Test
    void deleteAllInvoices() {
    }

    @Test
    void shouldValidateXsdSaveInvoiceResponse() throws ServiceOperationException, IOException {
        when(invoiceService.saveInvoice(invoice1)).thenReturn(invoice1);
        File filePathRequest = new File("src/test/resources/saveInvoiceRequest");
        File filePathResponse = new File("src/test/resources/saveInvoiceResponse");
        String getInvoiceStringRequest = XmlFileReader.readFromFile(filePathRequest);
        String getInvoiceStringResponse = XmlFileReader.readFromFile(filePathResponse);
        Source requestPayload = new StringSource(getInvoiceStringRequest);
        Source responsePayload = new StringSource(getInvoiceStringResponse);
        //When
        mockClient.sendRequest(withPayload(requestPayload))

            //Then
            .andExpect(noFault())
            .andExpect(payload(responsePayload))
            .andExpect(validPayload(xsdSchema));
    }
}