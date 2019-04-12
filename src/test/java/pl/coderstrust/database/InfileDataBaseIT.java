package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import pl.coderstrust.configuration.InFileDatabaseProperties;
import pl.coderstrust.database.file.FileHelper;
import pl.coderstrust.database.file.InFileDatabase;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class InfileDataBaseIT {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private FileHelper fileHelper;

    @Autowired
    private InFileDatabaseProperties inFileDatabaseProperties;

    @Autowired
    private InFileDatabase inFileDataBase;

    @BeforeEach
    void setup() throws IOException {
        if (!fileHelper.isExist()) {
            fileHelper.create();
        }
    }

    @AfterEach
    void clear() throws IOException {
        if (fileHelper.isExist()) {
            fileHelper.clear();
        }
    }

    @Test
    void shouldReturnTrueForExistingInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice1);
        inFileDataBase.saveInvoice(invoice2);
        inFileDataBase.saveInvoice(invoice3);

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
    void shouldReturnFalseForNotExistingInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice1);
        inFileDataBase.saveInvoice(invoice2);
        inFileDataBase.saveInvoice(invoice3);

        //When
        boolean exist4 = inFileDataBase.invoiceExists(4L);
        boolean exist5 = inFileDataBase.invoiceExists(5L);
        boolean exist6 = inFileDataBase.invoiceExists(6L);

        //Then
        assertFalse(exist4);
        assertFalse(exist5);
        assertFalse(exist6);
    }

    @Test
    void shouldThrowExceptionForNotExistingInFileDatabaseDuringCheckingInvoiceExistence() throws IOException {
        fileHelper.delete();
        assertThrows(DatabaseOperationException.class, () -> inFileDataBase.invoiceExists(1L));
    }

    @Test
    void shouldReturnFalseForEmptyInFileDatabaseDuringCheckingForInvoiceExistence() throws IOException, DatabaseOperationException {
        //Given
        fileHelper.clear();

        //When
        boolean exist = inFileDataBase.invoiceExists(1L);

        //Then
        assertFalse(exist);
    }

    @Test
    void shouldCreateInFileDatabaseWhenAddingInvoiceToNotExistingDatabase() throws IOException, DatabaseOperationException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithoutId();
        fileHelper.delete();

        //When
        inFileDataBase.saveInvoice(invoice);

        //Then
        assertTrue(fileHelper.isExist());
    }

    @Test
    void shouldAddInvoiceToNotExistingDatabase() throws IOException, DatabaseOperationException {
        //Given
        fileHelper.delete();
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithoutId();
        final Invoice expected = new Invoice(1L, invoice.getNumber(), invoice.getIssuedDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());

        //When
        inFileDataBase.saveInvoice(invoice);

        //Then
        assertEquals(1, inFileDataBase.countInvoices());
        assertTrue(inFileDataBase.getInvoice(1L).isPresent());
        assertEquals(expected, inFileDataBase.getInvoice(1L).get());
    }

    @Test
    void shouldAddInvoiceToEmptyDatabase() throws IOException, DatabaseOperationException {
        //Given
        fileHelper.clear();
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithoutId();
        final Invoice expected = new Invoice(1L, invoice.getNumber(), invoice.getIssuedDate(), invoice.getDueDate(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());

        //When
        inFileDataBase.saveInvoice(invoice);

        //Then
        assertEquals(1, inFileDataBase.countInvoices());
        assertTrue(inFileDataBase.getInvoice(1L).isPresent());
        assertEquals(expected, inFileDataBase.getInvoice(1L).get());
    }

    @Test
    void shouldAddInvoiceToNonEmptyDatabase() throws IOException, DatabaseOperationException {
        //Given
        fileHelper.clear();
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice1);
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice2);
        assertEquals(2, inFileDataBase.countInvoices());
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithoutId();
        final Invoice expected = new Invoice(3L, invoice3.getNumber(), invoice3.getIssuedDate(), invoice3.getDueDate(), invoice3.getSeller(), invoice3.getBuyer(), invoice3.getEntries());

        //When
        inFileDataBase.saveInvoice(invoice3);

        //Then
        assertEquals(3, inFileDataBase.countInvoices());
        assertTrue(inFileDataBase.getInvoice(3L).isPresent());
        assertEquals(expected, inFileDataBase.getInvoice(3L).get());
    }

    @Test
    void shouldUpdateInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice1);
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice2);
        Invoice invoice1Update = InvoiceGenerator.getRandomInvoiceWithSpecificId(1L);

        //When
        inFileDataBase.saveInvoice(invoice1Update);

        //Then
        assertEquals(2, inFileDataBase.countInvoices());
        assertTrue(inFileDataBase.getInvoice(1L).isPresent());
        assertEquals(invoice1Update, inFileDataBase.getInvoice(1L).get());
    }

    @Test
    void shouldDeleteInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice1);
        inFileDataBase.saveInvoice(invoice2);
        inFileDataBase.saveInvoice(invoice3);
        assertEquals(3, inFileDataBase.countInvoices());

        //When
        inFileDataBase.deleteInvoice(2L);

        //Then
        assertEquals(2, inFileDataBase.countInvoices());
        assertFalse(inFileDataBase.getInvoice(2L).isPresent());
    }

    @Test
    void shouldDeleteCorrectInvoiceWhenOtherInvoicesWereUpdated() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice1);
        inFileDataBase.saveInvoice(invoice2);
        inFileDataBase.saveInvoice(invoice3);
        Invoice invoice1Update = InvoiceGenerator.getRandomInvoiceWithSpecificId(1L);
        inFileDataBase.saveInvoice(invoice1Update);
        Invoice invoice3Update = InvoiceGenerator.getRandomInvoiceWithSpecificId(3L);
        inFileDataBase.saveInvoice(invoice3Update);
        assertEquals(3, inFileDataBase.countInvoices());

        //When
        inFileDataBase.deleteInvoice(2L);

        //Then
        assertEquals(2, inFileDataBase.countInvoices());
        assertFalse(inFileDataBase.getInvoice(2L).isPresent());
    }

    @Test
    void shouldDeleteCorrectInvoiceWhenTheInvoiceWasUpdated() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice1);
        inFileDataBase.saveInvoice(invoice2);
        inFileDataBase.saveInvoice(invoice3);
        Invoice invoice2Update = InvoiceGenerator.getRandomInvoiceWithSpecificId(2L);
        inFileDataBase.saveInvoice(invoice2Update);
        assertEquals(3, inFileDataBase.countInvoices());

        //When
        inFileDataBase.deleteInvoice(2L);

        //Then
        assertEquals(2, inFileDataBase.countInvoices());
        assertFalse(inFileDataBase.getInvoice(2L).isPresent());
    }

    @Test
    void shouldThrowExceptionForNotExistingDatabaseDuringDeletingInvoice() throws IOException {
        fileHelper.delete();
        assertThrows(DatabaseOperationException.class, () -> inFileDataBase.deleteInvoice(1L));
    }

    @Test
    void shouldThrowExceptionDuringDeletingNonExistingInvoice() throws IOException, DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice1);
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice2);
        assertTrue(inFileDataBase.invoiceExists(1L));
        assertTrue(inFileDataBase.invoiceExists(2L));
        assertFalse(inFileDataBase.invoiceExists(3L));

        //Then
        assertThrows(DatabaseOperationException.class, () -> inFileDataBase.deleteInvoice(3L));
    }

    @Test
    void shouldReturnEmptyOptionalDuringGettingNotExistingInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice1);
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice2);

        //When
        Optional<Invoice> invoice = inFileDataBase.getInvoice(3L);

        //Then
        assertFalse(invoice.isPresent());
    }


    @Test
    void shouldThrowExceptionForNotExistingDatabaseDuringGettingInvoice() throws IOException {
        fileHelper.delete();
        assertThrows(DatabaseOperationException.class, () -> inFileDataBase.getInvoice(1L));
    }

    @Test
    void shouldReturnInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice1);
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice2);
        final Invoice expected = new Invoice(2L, invoice2.getNumber(), invoice2.getIssuedDate(), invoice2.getDueDate(), invoice2.getSeller(), invoice2.getBuyer(), invoice2.getEntries());

        //When
        Optional<Invoice> invoice = inFileDataBase.getInvoice(2L);

        //Then
        assertTrue(invoice.isPresent());
        assertEquals(expected, invoice.get());
    }

    @Test
    void shouldThrowExceptionForNotExistingDatabaseDuringGettingAllInvoices() throws IOException, DatabaseOperationException {
        fileHelper.delete();
        assertThrows(DatabaseOperationException.class, () -> inFileDataBase.getAllInvoices());
    }

    @Test
    void shouldReturnEmptyCollectionForEmptyDatabaseDuringGettingAllInvoices() throws DatabaseOperationException {
        //Given
        inFileDataBase.deleteAllInvoices();

        //When
        Collection<Invoice> invoices = inFileDataBase.getAllInvoices();

        //Then
        assertTrue(invoices.isEmpty());
    }

    @Test
    void shouldReturnAllInvoices() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice1);
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice2);
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice3);
        assertEquals(3, inFileDataBase.countInvoices());
        final Invoice expected1 = new Invoice(1L, invoice1.getNumber(), invoice1.getIssuedDate(), invoice1.getDueDate(), invoice1.getSeller(), invoice1.getBuyer(), invoice1.getEntries());
        final Invoice expected2 = new Invoice(2L, invoice2.getNumber(), invoice2.getIssuedDate(), invoice2.getDueDate(), invoice2.getSeller(), invoice2.getBuyer(), invoice2.getEntries());
        final Invoice expected3 = new Invoice(3L, invoice3.getNumber(), invoice3.getIssuedDate(), invoice3.getDueDate(), invoice3.getSeller(), invoice3.getBuyer(), invoice3.getEntries());
        final Collection<Invoice> expected = Arrays.asList(expected1, expected2, expected3);

        //When
        Collection<Invoice> invoices = inFileDataBase.getAllInvoices();

        //Then
        assertEquals(3, invoices.size());
        assertEquals(expected, invoices);
    }

    @Test
    void shouldThrowExceptionForNotExistingDatabaseDuringDeletingAllInvoices() throws IOException {
        fileHelper.delete();
        assertThrows(DatabaseOperationException.class, () -> inFileDataBase.deleteAllInvoices());
    }

    @Test
    void shouldDeleteAllInvoices() throws DatabaseOperationException, IOException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice1);
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice2);
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice3);
        assertEquals(3, inFileDataBase.countInvoices());

        //When
        inFileDataBase.deleteAllInvoices();

        //Then
        assertTrue(fileHelper.isEmpty());
        assertEquals(0, inFileDataBase.countInvoices());
    }

    @Test
    void shouldReturnCorrectInvoiceCount() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice1);
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice2);
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithoutId();
        inFileDataBase.saveInvoice(invoice3);

        //When
        long invoiceCount = inFileDataBase.countInvoices();

        //Then
        assertEquals(3, invoiceCount);
    }
}
