package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.coderstrust.configuration.InFileDatabaseProperties;
import pl.coderstrust.database.file.FileHelper;
import pl.coderstrust.database.file.InFileDatabase;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class InFileDatabaseTest {

    @MockBean
    private FileHelper fileHelper;

    @Autowired
    private InFileDatabase inFileDataBase;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private InFileDatabaseProperties inFileDatabaseProperties;

    @Test
    void shouldThrowIllegalArgumentExceptionForNullMapper() {
        assertThrows(IllegalArgumentException.class, () -> new InFileDatabase(null, fileHelper, inFileDatabaseProperties));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForNullFileHelper() {
        assertThrows(IllegalArgumentException.class, () -> new InFileDatabase(mapper, null, inFileDatabaseProperties));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForNullInFileDatabaseProperties() {
        assertThrows(IllegalArgumentException.class, () -> new InFileDatabase(mapper, fileHelper, null));
    }

    @Test
    void shouldReturnTrueForExistingInvoice() throws IOException, DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificId(1L);
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificId(2L);
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificId(3L);
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

        //Then
        assertTrue(exist1);
        assertTrue(exist2);
        assertTrue(exist3);
    }

    @Test
    void shouldThrowExceptionForNullInvoice() {
        assertThrows(org.springframework.dao.InvalidDataAccessApiUsageException.class, () -> inFileDataBase.saveInvoice(null));
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
    void shouldAddInvoiceToEmptyDatabase() throws IOException, DatabaseOperationException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithoutId();
        when(fileHelper.exists()).thenReturn(true);
        when(fileHelper.isEmpty()).thenReturn(true);

        //When
        Invoice returned = inFileDataBase.saveInvoice(invoice);
        Invoice inserted = new Invoice(1L, invoice.getNumber(), invoice.getIssuedDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());

        //Then
        verify(fileHelper).exists();
        verify(fileHelper).isEmpty();
        assertEquals(inserted, returned);
    }

    @Test
    void shouldThrowExceptionWhenCreatingDatabaseDuringAddingInvoice() throws IOException {
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithoutId();
        when(fileHelper.exists()).thenReturn(false);
        doThrow(new IOException()).when(fileHelper).create();
        assertThrows(DatabaseOperationException.class, () -> inFileDataBase.saveInvoice(invoice));
    }

    @Test
    void shouldThrowExceptionWhenCheckingIfDatabaseEmptyDuringAddingInvoice() throws IOException {
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithoutId();
        when(fileHelper.exists()).thenReturn(true);
        doThrow(new IOException()).when(fileHelper).isEmpty();
        assertThrows(DatabaseOperationException.class, () -> inFileDataBase.saveInvoice(invoice));
    }

    @Test
    void shouldThrowExceptionWhenDatabaseEmptyDuringAddingInvoice() throws IOException {
        //Given
        when(fileHelper.exists()).thenReturn(true);
        when(fileHelper.isEmpty()).thenReturn(false);
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithoutId();
        doThrow(new IOException()).when(fileHelper).writeLine(Mockito.anyString());
        assertThrows(DatabaseOperationException.class, () -> inFileDataBase.saveInvoice(invoice));
    }

    @Test
    void shouldThrowExceptionWhenInvoiceToBeUpdatedDoesNotExist() {
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithSpecificId(1L);
        assertThrows(DatabaseOperationException.class, () -> inFileDataBase.saveInvoice(invoice));
    }

    @Test
    void shouldUpdateInvoice() throws IOException, DatabaseOperationException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithSpecificId(1L);
        String invoiceAsJson = mapper.writeValueAsString(invoice);
        Invoice updated = new Invoice(invoice.getId(), invoice.getNumber(), invoice.getIssuedDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());
        when(fileHelper.readLinesFromFile()).thenReturn(Collections.singletonList(invoiceAsJson));

        //When
        Invoice actual = inFileDataBase.saveInvoice(invoice);

        //Then
        assertEquals(updated, actual);
        assertEquals(updated, actual);
    }


    @Test
    void shouldThrowExceptionForNotExistingDatabaseDuringUpdatingInvoice() throws IOException {
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithSpecificId(1L);
        String invoiceAsJson = mapper.writeValueAsString(invoice);
        when(fileHelper.isEmpty()).thenReturn(false);
        List<String> invoicesAsJson = Collections.singletonList(invoiceAsJson);
        when(fileHelper.readLinesFromFile()).thenReturn(invoicesAsJson);
        doThrow(new IOException()).when(fileHelper).writeLine(mapper.writeValueAsString(invoice));
        assertThrows(DatabaseOperationException.class, () -> inFileDataBase.saveInvoice(invoice));

    }

    @Test
    void shouldThrowExceptionForNullInvoiceIdDuringDeletingInvoice() {
        assertThrows(org.springframework.dao.InvalidDataAccessApiUsageException.class, () -> inFileDataBase.deleteInvoice(null));
    }

    @Test
    void shouldThrowExceptionForNotExistingDatabaseDuringDeletingInvoice() throws IOException {
        doThrow(new IOException()).when(fileHelper).readLinesFromFile();
        assertThrows(DatabaseOperationException.class, () -> inFileDataBase.deleteInvoice(1L));
    }

    @Test
    void shouldDeleteInvoice() throws IOException, DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificId(1L);
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificId(2L);
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificId(3L);
        String invoice1AsJson = mapper.writeValueAsString(invoice1);
        String invoice2AsJson = mapper.writeValueAsString(invoice2);
        String invoice3AsJson = mapper.writeValueAsString(invoice3);
        List<String> invoicesAsJson = Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson);
        when(fileHelper.readLinesFromFile()).thenReturn(invoicesAsJson);

        //When
        inFileDataBase.deleteInvoice(2L);

        //Then
        verify(fileHelper).removeLine(Mockito.anyInt());
    }

    @Test
    void shouldThrowExceptionForNullInvoiceIdDuringGettingInvoice() {
        assertThrows(org.springframework.dao.InvalidDataAccessApiUsageException.class, () -> inFileDataBase.getInvoice(null));
    }

    @Test
    void shouldThrowExceptionForNotExistingDatabaseDuringGettingInvoice() throws IOException {
        when(!fileHelper.exists()).thenReturn(false);
        doThrow(new IOException()).when(fileHelper).create();
        assertThrows(DatabaseOperationException.class, () -> inFileDataBase.getInvoice(1L));
    }

    @Test
    void shouldReturnEmptyOptionalDuringGettingNotExistingInvoice() throws IOException, DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificId(1L);
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificId(3L);
        Invoice invoice4 = InvoiceGenerator.getRandomInvoiceWithSpecificId(4L);
        String invoice1AsJson = mapper.writeValueAsString(invoice1);
        String invoice3AsJson = mapper.writeValueAsString(invoice3);
        String invoice4AsJson = mapper.writeValueAsString(invoice4);
        List<String> invoicesAsJson = Arrays.asList(invoice1AsJson, invoice3AsJson, invoice4AsJson);
        when(fileHelper.readLinesFromFile()).thenReturn(invoicesAsJson);

        //When
        Optional<Invoice> invoice = inFileDataBase.getInvoice(2L);

        //Then
        verify(fileHelper).readLinesFromFile();
        assertFalse(invoice.isPresent());
    }

    @Test
    void shouldReturnInvoice() throws IOException, DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificId(1L);
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificId(3L);
        Invoice invoice4 = InvoiceGenerator.getRandomInvoiceWithSpecificId(4L);
        String invoice1AsJson = mapper.writeValueAsString(invoice1);
        String invoice3AsJson = mapper.writeValueAsString(invoice3);
        String invoice4AsJson = mapper.writeValueAsString(invoice4);
        List<String> invoicesAsJson = Arrays.asList(invoice1AsJson, invoice3AsJson, invoice4AsJson);
        when(fileHelper.readLinesFromFile()).thenReturn(invoicesAsJson);

        //When
        Optional<Invoice> invoice = inFileDataBase.getInvoice(4L);

        //Then
        verify(fileHelper).readLinesFromFile();
        assertTrue(invoice.isPresent());
        assertEquals(invoice4, invoice.get());
    }

    @Test
    void shouldReturnEmptyCollectionForEmptyDatabaseDuringGettingAllInvoices() throws IOException, DatabaseOperationException {
        //Given
        when(fileHelper.isEmpty()).thenReturn(true);

        //When
        Collection<Invoice> invoices = inFileDataBase.getAllInvoices();

        //Then
        verify(fileHelper).isEmpty();
        assertTrue(invoices.isEmpty());
    }

    @Test
    void shouldReturnAllInvoices() throws IOException, DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificId(1L);
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificId(2L);
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificId(3L);
        String invoice1AsJson = mapper.writeValueAsString(invoice1);
        String invoice2AsJson = mapper.writeValueAsString(invoice2);
        String invoice3AsJson = mapper.writeValueAsString(invoice3);
        List<String> invoicesAsJson = Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson);
        when(fileHelper.isEmpty()).thenReturn(false);
        when(fileHelper.readLinesFromFile()).thenReturn(invoicesAsJson);
        Collection<Invoice> expected = Arrays.asList(invoice1, invoice2, invoice3);

        //When
        Collection<Invoice> invoices = inFileDataBase.getAllInvoices();

        //Then
        verify(fileHelper).readLinesFromFile();
        assertFalse(invoices.isEmpty());
        assertEquals(expected, invoices);
    }

    @Test
    void shouldThrowExceptionForNotExistingDatabaseWhenCheckingDatabaseIsEmptyDuringGettingAllInvoices() throws IOException {
        when(fileHelper.isEmpty()).thenThrow(IOException.class);
        assertThrows(DatabaseOperationException.class, () -> inFileDataBase.getAllInvoices());
    }

    @Test
    void shouldThrowExceptionForNotExistingDatabaseDuringGettingAllInvoices() throws IOException {
        when(fileHelper.isEmpty()).thenReturn(false);
        when(fileHelper.readLinesFromFile()).thenThrow(IOException.class);
        assertThrows(DatabaseOperationException.class, () -> inFileDataBase.getAllInvoices());
    }

    @Test
    void shouldThrowExceptionForNotExistingDatabaseDuringDeletingAllInvoices() {
        when(!fileHelper.exists()).thenReturn(false);
        assertThrows(DatabaseOperationException.class, () -> inFileDataBase.deleteAllInvoices());
    }

    @Test
    void shouldDeleteAllInvoices() throws IOException, DatabaseOperationException {
        //Given
        when(!fileHelper.exists()).thenReturn(true);

        //When
        inFileDataBase.deleteAllInvoices();

        //Then
        verify(fileHelper).clear();
    }


    @Test
    void shouldReturnCorrectNumberOfInvoices() throws IOException, DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificId(1L);
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificId(2L);
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificId(3L);
        String invoice1AsJson = mapper.writeValueAsString(invoice1);
        String invoice2AsJson = mapper.writeValueAsString(invoice2);
        String invoice3AsJson = mapper.writeValueAsString(invoice3);
        List<String> invoicesAsJson = Arrays.asList(invoice1AsJson, invoice2AsJson, invoice3AsJson);
        when(fileHelper.isEmpty()).thenReturn(false);
        when(fileHelper.readLinesFromFile()).thenReturn(invoicesAsJson);
        Collection<Invoice> expected = Arrays.asList(invoice1, invoice2, invoice3);

        //When
        long actual = inFileDataBase.countInvoices();

        //Then
        verify(fileHelper).isEmpty();
        verify(fileHelper).readLinesFromFile();
        assertEquals(expected.size(), actual);
    }
}
