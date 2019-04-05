package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
//@RunWith(SpringRunner.class)
class InfileDataBaseIT {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private FileHelper fileHelper;

    @Autowired
    private InFileDatabase inFileDataBase;

    @BeforeEach
    void setup() throws IOException, DatabaseOperationException {
        if (!fileHelper.exists()) {
            fileHelper.create();
        }
        inFileDataBase.deleteAllInvoices();
    }

    @Test
    void shouldReturnTrueForExistingInvoice() throws IOException, DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice1);
        inFileDataBase.saveInvoice(invoice2);
        inFileDataBase.saveInvoice(invoice3);

        //When
        boolean exist0 = inFileDataBase.invoiceExists(0L);
        boolean exist1 = inFileDataBase.invoiceExists(1L);
        boolean exist2 = inFileDataBase.invoiceExists(2L);
        boolean exist3 = inFileDataBase.invoiceExists(3L);
        boolean exist4 = inFileDataBase.invoiceExists(4L);

        //Then
        assertFalse(exist0);
        assertTrue(exist1);
        assertTrue(exist2);
        assertTrue(exist3);
        assertFalse(exist4);
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

    @Test
    void shouldUpdateInvoice() throws DatabaseOperationException, IOException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice savedInvoice = inFileDataBase.saveInvoice(invoice);
        Invoice invoiceToUpdate = new Invoice(invoice.getId(), "5/2019", LocalDate.now(), LocalDate.now(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
        String invoiceToUpdateInJson = toJson(invoiceToUpdate);

//        fileHelper.writeLine(toJson(invoice));
        List<String> invoicesInJson;

        //When
        Invoice updatedInvoice = inFileDataBase.saveInvoice(invoiceToUpdate);

        //Then
        // assertEquals(updatedInvoice, getInvoiceFromJson(invoicesInJson.get(0)));
        //assertTrue(invoicesAreSame(invoice, getInvoiceFromJson(invoicesInJson.get(0))));

        //  assertEquals(updatedInvoice, databaseStorage.get(updatedInvoice.getId()));
        // assertNotEquals(updatedInvoice, invoice);
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
