package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

class InfileDataBaseTest {

    @BeforeEach
    void setup() {
     String   inFileDatabasePath = "src/test/resources/inFileDatabaseTest.txt";
        Database database = new FileHelper(inFileDatabasePath)
    }

    void shouldAddInvoice() {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithoutId();

        //When
        Invoice addedInvoice = database.saveInvoice(invoice);

        //Then
        assertNotNull(addedInvoice.getId());
        assertEquals(1, (long) addedInvoice.getId());
       // assertTrue(invoicesAreSame(invoice, addedInvoice));
    }

}
   /* FileHelper(String filePath) {
        this.file = new File(filePath);
    }*/