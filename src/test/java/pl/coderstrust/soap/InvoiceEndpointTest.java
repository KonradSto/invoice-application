package pl.coderstrust.soap;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.context.WebApplicationContext;
import pl.coderstrust.service.InvoiceService;

class InvoiceEndpointTest {

    @Autowired
    private WebApplicationContext applicationContext;

    private MockWebServiceClient mockClient;
    private Resource xsdSchema = new ClassPathResource(invoices.xsd);

    @Autowired
    private InvoiceService invoiceService;

    @Before
    public void init(){
        mockClient = MockWebServiceClient(applicationContext);
    }
    @Test
    void shouldValidateXsdGetInvoiceResponse() {
    String filePathRequuest;
    String filePathResponse;
    XmlFileReader fileReader = new XmlFileReader();
    String getInvoicesRequwes = fileReader.readFromFile(filePathRequuest);


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
    void saveInvoice() {
    }
}