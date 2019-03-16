package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    void shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new InMemoryDatabase(null));
    }

    @Test
    void shouldAddInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithoutId();

        //When
        Invoice addedInvoice = database.saveInvoice(invoice);

        //Then
        assertNotNull(addedInvoice.getId());
        assertEquals(1, (long) addedInvoice.getId());
        assertTrue(invoicesAreSame(invoice, addedInvoice));
    }

    private boolean invoicesAreSame(Invoice invoiceToAdd, Invoice addedInvoice) {
        return (invoiceToAdd.getBuyer() == addedInvoice.getBuyer()
            && invoiceToAdd.getSeller() == addedInvoice.getSeller()
            && invoiceToAdd.getDueDate() == addedInvoice.getDueDate()
            && invoiceToAdd.getIssuedDate() == addedInvoice.getIssuedDate()
            && invoiceToAdd.getNumber().equals(addedInvoice.getNumber())
            && invoiceToAdd.getEntries().equals(addedInvoice.getEntries()));
    }

    @Test
    void saveInvoiceMethodShouldThrowExceptionForNullInvoice() {
        assertThrows(IllegalArgumentException.class, () -> database.saveInvoice(null));
    }

    @Test
    void shouldUpdateInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoice();
        databaseStorage.put(invoice.getId(), invoice);
        Invoice invoiceToUpdate = new Invoice(invoice.getId(), "5/2019", LocalDate.now(), LocalDate.now(), invoice.getSeller(), invoice.getBuyer(), invoice.getEntries());

        //When
        Invoice updatedInvoice = database.saveInvoice(invoiceToUpdate);

        //Then
        assertEquals(updatedInvoice, databaseStorage.get(updatedInvoice.getId()));
        assertNotEquals(updatedInvoice, invoice);
    }

    @Test
    void saveInvoiceMethodShouldThrowExceptionDuringUpdatingNotExistingInvoice() {
        assertThrows(DatabaseOperationException.class, () -> {
            Invoice invoiceToUpdate = InvoiceGenerator.getRandomInvoice();
            database.saveInvoice(invoiceToUpdate);
        });
    }

    @Test
    void shouldDeleteInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoice();
        databaseStorage.put(invoice.getId(), invoice);

        //When
        database.deleteInvoice(invoice.getId());

        //Then
        assertFalse(databaseStorage.containsKey(invoice.getId()));
    }

    @Test
    void deleteInvoiceMethodShouldThrowExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> database.deleteInvoice(null));
    }

    @Test
    void deleteInvoiceMethodShouldThrowExceptionDuringDeletingNotExistingInvoice() {
        assertThrows(DatabaseOperationException.class, () -> database.deleteInvoice(1L));
    }

    @Test
    void shouldReturnInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoice();
        databaseStorage.put(invoice.getId(), invoice);

        //When
        Optional<Invoice> returnedInvoice = database.getInvoice(invoice.getId());

        //Then
        assertTrue(returnedInvoice.isPresent());
        assertEquals(invoice,returnedInvoice.get());
    }


    @Test
    void getInvoiceMethodShouldThrowExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> database.getInvoice(null));
    }

    @Test
    void shouldReturnEmptyOptionalWhenInvoiceDoesNotExist() throws DatabaseOperationException {
        //When
        Optional<Invoice> invoice = database.getInvoice(1L);

        //Then
        assertFalse(invoice.isPresent());
    }

    @Test
    void shouldReturnAllInvoices() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
        Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
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
        Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
        Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
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
        Invoice invoice = InvoiceGenerator.getRandomInvoice();
        databaseStorage.put(invoice.getId(), invoice);

        //Then
        assertTrue(database.invoiceExists(invoice.getId()));
    }

    @Test
    void shouldReturnFalseForNonExistingInvoice() throws DatabaseOperationException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoice();
        databaseStorage.put(invoice.getId(), invoice);

        //Then
        assertFalse(database.invoiceExists(invoice.getId() + 1L));
    }

    @Test
    void invoiceExistsMethodShouldThrowExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> database.invoiceExists(null));
    }

    @Test
    void shouldReturnNumberOfInvoices() throws DatabaseOperationException {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
        Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
        databaseStorage.put(invoice1.getId(), invoice1);
        databaseStorage.put(invoice2.getId(), invoice2);

        //When
        long actualNumberOfInvoices = database.countInvoices();

        //Then
        assertEquals(databaseStorage.size(), actualNumberOfInvoices);
    }
}
