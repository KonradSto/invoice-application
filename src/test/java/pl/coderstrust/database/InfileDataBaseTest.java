package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

@SpringBootTest
class InfileDataBaseTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private FileHelper fileHelper;

    @BeforeEach
    private void setup() throws IOException {
        String inFileDatabasePath = "src/test/resources/inFileDatabaseTest.txt";
        if (fileHelper.exists()) {
            fileHelper.delete();
        }
        fileHelper.create();
    }

    // TODO: 27/03/2019 check if fiele (DB) exists, of not then create,
    // TODO: 27/03/2019 what if  getting invoice, but file doesnt exists, or file opened
    @Test
    void shouldReturnTrueForExistingInvoice() throws DatabaseOperationException, IOException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoice();
        fileHelper.writeLine(toJson(invoice));
        List<String> invoicesInJson;

        //When
        invoicesInJson = fileHelper.readLinesFromFile();

        //Then
        assertEquals(invoice, getInvoiceFromJson(invoicesInJson.get(0)));
    }

    private String toJson(Invoice invoice) throws JsonProcessingException {
        return mapper.writeValueAsString(invoice);
    }

    private Invoice getInvoiceFromJson(String invoiceAsJson) throws IOException {
        return mapper.readValue(invoiceAsJson, Invoice.class);
    }
}
