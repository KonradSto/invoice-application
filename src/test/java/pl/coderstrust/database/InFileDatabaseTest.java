package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

//@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class InFileDatabaseTest {
    //@Autowired
    @MockBean
    private FileHelper fileHelper;
    @Autowired
    private InFileDatabase inFileDataBase;
    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void init() {

    }

    @Test
    void shouldReturnTrueForExistingInvoice() throws IOException, DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificId(1L);
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificId(2L);
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificId(3L);
        Invoice invoice4 = InvoiceGenerator.getRandomInvoiceWithSpecificId(4L);
        String invoice1AsJson = mapper.writeValueAsString(invoice1);
        String invoice2AsJson = mapper.writeValueAsString(invoice2);
        String invoice3AsJson = mapper.writeValueAsString(invoice3);
        List<String> invoicesAsJson = Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson);
        when(fileHelper.readLinesFromFile()).thenReturn(invoicesAsJson);
        when(fileHelper.exists()).thenReturn(true);

        //When
        boolean exist1 = inFileDataBase.invoiceExists(1L);
        boolean exist2 = inFileDataBase.invoiceExists(2L);
        boolean exist3 = inFileDataBase.invoiceExists(3L);
        boolean exist4 = inFileDataBase.invoiceExists(4L);

        //Then
        assertTrue(exist1);
        assertTrue(exist2);
        assertTrue(exist3);
        assertFalse(exist4);
    }

    @Test
    void shouldThrowExceptionForNotExistingInFileDatabaseDuringCheckingInvoiceExistence() throws IOException {
        when(fileHelper.isEmpty()).thenThrow(IOException.class);
        assertThrows(DatabaseOperationException.class, () -> inFileDataBase.invoiceExists(1L));
    }

    @Test
    void shouldReturnFalseForEmptyInFileDatabaseDuringCheckingInvoiceExistence() throws IOException, DatabaseOperationException {
        //Given
        when(fileHelper.isEmpty()).thenReturn(true);

        //When
        boolean exist = inFileDataBase.invoiceExists(1L);

        //Then
        verify(fileHelper).isEmpty();
        assertFalse(exist);
    }

    @Test
    void shouldCreateInFileDatabaseWhenAddingInvoiceToNotExistentDatabase() throws IOException, DatabaseOperationException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithoutId();
        when(fileHelper.exists()).thenReturn(false);

        //When
        inFileDataBase.saveInvoice(invoice);

        //Then
        verify(fileHelper).exists();
        verify(fileHelper).create();
    }

    @Test
    void shouldThrowExceptionFor() {

    }

    @Test
    void shouldAddInvoiceToEmptyInFileDatabase() throws IOException, DatabaseOperationException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithoutId();
        when(fileHelper.exists()).thenReturn(true);
        when(fileHelper.isEmpty()).thenReturn(true);
        //  (fileHelper.writeLine(mapper.writeValueAsString(invoice))).thenAnswer()
        // when(fileHelper.writeLine(toJson(invoice))).thenReturn(invoice);
        //when(fileHelper.saveInvoice(invoice)).thenReturn(invoice);

        //When
        Invoice returned = inFileDataBase.saveInvoice(invoice);
        Invoice inserted = new Invoice(1L, invoice.getNumber(), invoice.getIssuedDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());

        //Then
        verify(fileHelper).exists();
        verify(fileHelper).isEmpty();
        assertEquals(inserted, returned);
    }


    @Test
    void shouldThrowExceptionForNullInvoice() throws IOException, DatabaseOperationException {
        assertThrows(org.springframework.dao.InvalidDataAccessApiUsageException.class, () -> inFileDataBase.saveInvoice(null));
    }

    @Test
    void shouldInsertInvoiceForNullId() throws IOException, DatabaseOperationException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithoutId();
        //When

        //Then

    }


    String toJson(Invoice invoice) throws JsonProcessingException {
        return mapper.writeValueAsString(invoice);
    }

    Invoice getInvoiceFromJson(String invoiceAsJson) throws IOException {
        return mapper.readValue(invoiceAsJson, Invoice.class);
    }

}
