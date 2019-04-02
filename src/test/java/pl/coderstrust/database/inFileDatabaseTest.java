package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.coderstrust.database.memory.InMemoryDatabase;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

@ExtendWith(SpringExtension.class)
//@RunWith(PowerMockRunner.class)
@SpringBootTest
public class inFileDatabaseTest {

    Database database;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private FileHelper fileHelper;

    @BeforeEach
    void setup() {
        database = new inFileDatabase
        database = new InMemoryDatabase(databaseStorage);
    }

    @Test
    void shouldAddInvoiceForNullInvoiceId() throws IOException, DatabaseOperationException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice returned;
        when(database.saveInvoice(invoice)).thenReturn(invoice);

        //When
        returned = database.saveInvoice(invoice);

        //Then
        verify(database).saveInvoice(invoice);
        assertEquals(invoice, returned);
    }

    String toJson(Invoice invoice) throws JsonProcessingException {
        return mapper.writeValueAsString(invoice);
    }

    Invoice getInvoiceFromJson(String invoiceAsJson) throws IOException {
        return mapper.readValue(invoiceAsJson, Invoice.class);
    }

}
