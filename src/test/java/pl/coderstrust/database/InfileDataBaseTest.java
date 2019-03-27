package pl.coderstrust.database;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;

class InfileDataBaseTest {

    @MockBean
    FileHelper fileHelper;

    @BeforeEach
    void setup() {
        String inFileDatabasePath = "src/test/resources/inFileDatabaseTest.txt";
    }

    void shouldReturnTrueForExistingInvoice() throws DatabaseOperationException {
        //When
        when(fileHelper.exists()).thenReturn(true);

        //Then
        assertTrue(database.invoiceExists(invoice.getId()));
    }

}
   /* FileHelper(String filePath) {
        this.file = new File(filePath);
    }*/