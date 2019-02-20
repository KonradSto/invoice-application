package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

class InMemoryDatabaseTest {

    private Database database;
    private Map<Long, Invoice> databaseStorage;

    @BeforeEach
    void setup() {
        databaseStorage = new HashMap<>();
        database = new InMemoryDatabase(databaseStorage);
    }

    @Test
    void shouldAddInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice = new InvoiceGenerator().getRandomInvoiceWithoutId();

        //When
        Invoice addedInvoice = database.saveInvoice(invoice);

        //Then
        assertNotNull(addedInvoice.getId());
        assertEquals(1, (long) addedInvoice.getId());
        assertEquals(addedInvoice, databaseStorage.get(addedInvoice.getId())); //w środku asercji prywatna metoda do porównania invoiców (z wykluczeniem id)
    }

    @Test
    void shouldThrowDatabaseOperationExceptionForNullInvoice() {
        assertThrows(IllegalArgumentException.class, () -> database.saveInvoice(null));
    }

    @Test
    void shouldUpdateInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice = new InvoiceGenerator().getRandomInvoiceWithoutId();
        databaseStorage.put(invoice.getId(), invoice);
        Invoice invoiceToUpdate = new Invoice(invoice.getId(), "5/2019", LocalDate.now(), LocalDate.now(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());

        //When
        Invoice updatedInvoice = database.saveInvoice(invoiceToUpdate);

        //Then
        assertEquals(updatedInvoice, databaseStorage.get(updatedInvoice.getId()));
        assertNotEquals(updatedInvoice, invoice);
    }

    @Test
    void saveMethodShouldThrowExceptionDuringUpdatingNotExistingInvoice() {
        assertThrows(DatabaseOperationException.class, () -> {
            Invoice invoiceToUpdate = new InvoiceGenerator().getRandomInvoice();
            database.saveInvoice(invoiceToUpdate);
        });
    }

    @Test
    void shouldDeleteInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice = new InvoiceGenerator().getRandomInvoice();
        databaseStorage.put(invoice.getId(), invoice);

        //When
        database.deleteInvoice(invoice.getId());

        //Then
        assertFalse(databaseStorage.containsKey(invoice.getId()));
    }

    @Test
    void shouldReturnInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice = new InvoiceGenerator().getRandomInvoice();
        databaseStorage.put(invoice.getId(), invoice);

        //Then
        assertEquals(invoice, database.getInvoice(invoice.getId()));
    }

    @Test
    void shouldThrowDatabaseOperationExceptionWhenGettingNonExistingInvoice() {
        assertThrows(DatabaseOperationException.class, () -> database.getInvoice(1L));
    }

    @Test
    void shouldReturnAllInvoices() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = new InvoiceGenerator().getRandomInvoice();
        Invoice invoice2 = new InvoiceGenerator().getRandomInvoice();
        databaseStorage.put(invoice1.getId(), invoice1);
        databaseStorage.put(invoice2.getId(), invoice2);

        //When
        Collection<Invoice> allInvoices = database.getAllInvoices();

        //Then
        assertEquals(databaseStorage.values(), allInvoices);
    }

    @Test
    void shouldDeleteAllInvoices() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = new InvoiceGenerator().getRandomInvoice();
        Invoice invoice2 = new InvoiceGenerator().getRandomInvoice();
        databaseStorage.put(invoice1.getId(), invoice1);
        databaseStorage.put(invoice2.getId(), invoice2);

        //When
        database.deleteAllInvoices();

        //Then
        assertTrue(databaseStorage.isEmpty());
    }

    @Test
    void shouldReturnTrueForExistingInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice = new InvoiceGenerator().getRandomInvoice();
        databaseStorage.put(invoice.getId(), invoice);

        //Then
        assertTrue(database.invoiceExists(invoice.getId()));
    }

    @Test
    void shouldReturnFalseForNonExistingInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice = new InvoiceGenerator().getRandomInvoice();
        databaseStorage.put(invoice.getId(), invoice);

        //Then
        assertFalse(database.invoiceExists(invoice.getId() + 1L));
    }

    @Test
    void shouldReturnCorrectNumberOfInvoicesFromInMemoryDatabase() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = new InvoiceGenerator().getRandomInvoice();
        Invoice invoice2 = new InvoiceGenerator().getRandomInvoice();
        databaseStorage.put(invoice1.getId(), invoice1);
        databaseStorage.put(invoice2.getId(), invoice2);

        //When
        long actualNumberOfInvoices = database.countInvoices();

        //Then
        assertEquals(databaseStorage.size(), actualNumberOfInvoices);
    }
}
