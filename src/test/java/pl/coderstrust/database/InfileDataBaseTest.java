package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@RunWith(SpringRunner.class)
class InfileDataBaseTest {
    private ObjectMapper mapper;
    private FileHelper fileHelper;
    private InFileDataBase inFileDataBase;

    @BeforeEach
    void setup() throws IOException {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        String inFileDatabasePath = "src/test/resources/inFileDatabaseTest.txt";
        fileHelper = new FileHelper(inFileDatabasePath);
        inFileDataBase = new InFileDataBase(inFileDatabasePath);
        if (fileHelper.exists()) {
            fileHelper.delete();
        }
        fileHelper.create();
    }

    @Test
    void shouldReturnTrueForExistingInvoice() throws IOException, DatabaseOperationException {
        //Given
        String inFileDatabasePath = "src/test/resources/inFileDatabaseTest.txt";

        // InFileDataBase inFileDataBase = new InFileDataBase(inFileDatabasePath);

        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice1);
        inFileDataBase.saveInvoice(invoice2);
        inFileDataBase.saveInvoice(invoice3);
        List<String> allInvoicesInJson;

        //When
        boolean exist = inFileDataBase.invoiceExists(1L);

        //Then
        assertTrue(exist);
    }


    // TODO: 27/03/2019 check if fiele (DB) exists, of not then create,
    // TODO: 27/03/2019 what if  getting invoice, but file doesnt exists, or file opened
    @Test
    void shouldSaveInvoice() throws DatabaseOperationException, IOException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice savedInvoice = inFileDataBase.saveInvoice(invoice);

//        fileHelper.writeLine(toJson(invoice));
        List<String> invoicesInJson;

        //When
        invoicesInJson = fileHelper.readLinesFromFile();
        //Then
        assertEquals(savedInvoice, getInvoiceFromJson(invoicesInJson.get(0)));
        //assertTrue(invoicesAreSame(invoice, getInvoiceFromJson(invoicesInJson.get(0))));
    }

    private String toJson(Invoice invoice) throws JsonProcessingException {
        return mapper.writeValueAsString(invoice);
    }

    private Invoice getInvoiceFromJson(String invoiceAsJson) throws IOException {
        return mapper.readValue(invoiceAsJson, Invoice.class);
    }

    private boolean invoicesAreSame(Invoice invoiceToAdd, Invoice addedInvoice) {
        return (invoiceToAdd.getBuyer() == addedInvoice.getBuyer()
            && invoiceToAdd.getSeller() == addedInvoice.getSeller()
            && invoiceToAdd.getDueDate() == addedInvoice.getDueDate()
            && invoiceToAdd.getIssuedDate() == addedInvoice.getIssuedDate()
            && invoiceToAdd.getNumber().equals(addedInvoice.getNumber())
            && invoiceToAdd.getEntries().equals(addedInvoice.getEntries()));
    }
}
