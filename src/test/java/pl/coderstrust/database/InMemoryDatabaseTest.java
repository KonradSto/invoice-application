package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

class InMemoryDatabaseTest {

    private Database inMemoryDatabase;
    private Map<Long, Invoice> invoiceStorage;

    @BeforeEach
    void setup() {
        invoiceStorage = new HashMap<>();
        inMemoryDatabase = new InMemoryDatabase(invoiceStorage);
    }

    @Test
    void shouldAddInvoiceToInMemoryDatabase() throws DatabaseOperationException {
        //Given
        Invoice invoice = new InvoiceGenerator().getRandomInvoice();

        //When
        Invoice addedInvoice = inMemoryDatabase.saveInvoice(invoice);

        //Then
        assertEquals(addedInvoice, invoiceStorage.get(addedInvoice.getId()));
    }

    @Test
    void shouldThrowDatabaseOperationExceptionForNullInvoice() {
        Assertions.assertThrows(DatabaseOperationException.class, () -> {
            inMemoryDatabase.saveInvoice(null);
        });
    }

    @Test
    void shouldUpdateInvoiceWithinInMemoryDatabase() throws DatabaseOperationException {
        //Given
        Invoice invoice = new InvoiceGenerator().getRandomInvoice();
        Invoice invoiceToUpdate = new Invoice(1L, "5/2019", LocalDate.now(), LocalDate.now(), invoice.getSeller(), invoice.getBuyer(), null);

        //When
        Invoice addedInvoice = inMemoryDatabase.saveInvoice(invoice);
        Invoice updatedInvoice = inMemoryDatabase.saveInvoice(invoiceToUpdate);

        //Then
        assertEquals(updatedInvoice, invoiceStorage.get(updatedInvoice.getId()));
        assertNotEquals(addedInvoice, invoiceStorage.get(updatedInvoice.getId()));
    }

    @Test
    void shouldThrowDatabaseOperationExceptionForNonExistingInvoiceId() {
        Assertions.assertThrows(DatabaseOperationException.class, () -> {
            Invoice invoiceToUpdate = new Invoice(1L, "123", LocalDate.now(), LocalDate.now(), null, null, null);
            inMemoryDatabase.saveInvoice(invoiceToUpdate);
        });
    }

    @Test
    void shouldDeleteInvoiceByIdFromInMemoryDatabase() throws DatabaseOperationException {
        //Given
        Invoice invoice = new InvoiceGenerator().getRandomInvoice();
        invoiceStorage.put(1L, invoice);

        //When
        inMemoryDatabase.deleteInvoice(1L);

        //Then
        assertFalse(invoiceStorage.containsKey(1L));
    }

    @Test
    void shouldReturnInvoiceOfGivenId() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = new InvoiceGenerator().getRandomInvoice();
        invoiceStorage.put(1L, invoice1);

        //Then
        assertEquals(invoiceStorage.get(1L), inMemoryDatabase.getInvoice(1L));
    }

    @Test
    void shouldThrowDatabaseOperationExceptionWhenGettingNonExistingInvoice() {
        Assertions.assertThrows(DatabaseOperationException.class, () -> {
            inMemoryDatabase.getInvoice(1L);
        });
    }

    @Test
    void shouldReturnAllInvoicesStoredWithinInMemoryDatabase() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = new InvoiceGenerator().getRandomInvoice();
        Invoice invoice2 = new InvoiceGenerator().getRandomInvoice();
        invoiceStorage.put(1L, invoice1);
        invoiceStorage.put(2L, invoice2);

        //When
        Collection<Invoice> allInvoices = inMemoryDatabase.getAllInvoices();

        //Then
        assertEquals(allInvoices, invoiceStorage.values());
    }

    @Test
    void shouldDeleteAllInvoicesFromInMemoryDatabase() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = new InvoiceGenerator().getRandomInvoice();
        Invoice invoice2 = new InvoiceGenerator().getRandomInvoice();
        invoiceStorage.put(1L, invoice1);
        invoiceStorage.put(2L, invoice2);

        //When
        inMemoryDatabase.deleteAllInvoices();

        //Then
        assertTrue(invoiceStorage.isEmpty());
    }

    @Test
    void shouldReturnTrueForExistingInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice = new InvoiceGenerator().getRandomInvoice();
        invoiceStorage.put(1L, invoice);

        //Then
        assertTrue(inMemoryDatabase.invoiceExists(1L));
    }

    @Test
    void shouldReturnFalseForNonExistingInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice = new InvoiceGenerator().getRandomInvoice();
        invoiceStorage.put(1L, invoice);

        //Then
        assertFalse(inMemoryDatabase.invoiceExists(2L));
    }

    @Test
    void shouldReturnCorrectNumberOfInvoicesFromInMemoryDatabase() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = new InvoiceGenerator().getRandomInvoice();
        Invoice invoice2 = new InvoiceGenerator().getRandomInvoice();
        invoiceStorage.put(1L, invoice1);
        invoiceStorage.put(2L, invoice2);

        //When
        long actualNumberOfInvoices = inMemoryDatabase.countInvoices();

        //Then
        assertEquals(2L, actualNumberOfInvoices);
    }
}
