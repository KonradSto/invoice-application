package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.*;

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
  void setup(){
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
  void shouldThrowDatabaseOperationExceptionForNullInvoice(){
    Assertions.assertThrows(DatabaseOperationException.class, () -> {
      Invoice invoice = null;
      inMemoryDatabase.saveInvoice(invoice);
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

    //When
    inMemoryDatabase.saveInvoice(invoice);
    inMemoryDatabase.deleteInvoice(1L);

    //Then
    assertFalse(invoiceStorage.containsKey(1L));
  }

  @Test
  void shouldReturnInvoiceOfGivenId() throws DatabaseOperationException {
    //Given
    Invoice invoice1 = new InvoiceGenerator().getRandomInvoice();

    //When
    Invoice addedInvoice = inMemoryDatabase.saveInvoice(invoice1);

    //Then
    assertEquals(addedInvoice, inMemoryDatabase.getInvoice(1L));
  }

  @Test
  void shouldThrowDatabaseOperationExceptionWhenGettingNonExistingInvoice(){
    Assertions.assertThrows(DatabaseOperationException.class, () -> {
      inMemoryDatabase.getInvoice(1L);
    });
  }

  @Test
  void shouldReturnAllInvoicesStoredWithinInMemoryDatabase() throws DatabaseOperationException {
    //Given
    Invoice invoice1 = new InvoiceGenerator().getRandomInvoice();
    Invoice invoice2 = new InvoiceGenerator().getRandomInvoice();

    //When
    inMemoryDatabase.saveInvoice(invoice1);
    inMemoryDatabase.saveInvoice(invoice2);
    Collection<Invoice> allInvoices = inMemoryDatabase.getAllInvoices();

    //Then
    assertEquals(allInvoices, invoiceStorage.values());
  }

  @Test
  void shouldDeleteAllInvoicesFromInMemoryDatabase() throws DatabaseOperationException {
    //Given
    Invoice invoice1 = new InvoiceGenerator().getRandomInvoice();
    Invoice invoice2 = new InvoiceGenerator().getRandomInvoice();

    //When
    inMemoryDatabase.saveInvoice(invoice1);
    inMemoryDatabase.saveInvoice(invoice2);
    inMemoryDatabase.deleteAllInvoices();

    //Then
    assertTrue(invoiceStorage.isEmpty());
  }

  @Test
  void shouldReturnTrueForExistingInvoice() throws DatabaseOperationException {
    //Given
    Invoice invoice = new InvoiceGenerator().getRandomInvoice();

    //When
    inMemoryDatabase.saveInvoice(invoice);
    System.out.println();

    //Then
    assertTrue(inMemoryDatabase.invoiceExists(1L));
  }

  @Test
  void shouldReturnFalseForNonExistingInvoice() throws DatabaseOperationException {
    //Given
    Invoice invoice1 = new InvoiceGenerator().getRandomInvoice();

    //When
    inMemoryDatabase.saveInvoice(invoice1);

    //Then
    assertFalse(inMemoryDatabase.invoiceExists(2L));
  }

  @Test
   void shouldReturnCorrectNumberOfInvoicesFromInMemoryDatabase() throws DatabaseOperationException {
    //Given
    Invoice invoice1 = new InvoiceGenerator().getRandomInvoice();
    Invoice invoice2 = new InvoiceGenerator().getRandomInvoice();
    long expectedNumberOfInvoices = 2;

    //When
    inMemoryDatabase.saveInvoice(invoice1);
    inMemoryDatabase.saveInvoice(invoice2);
    long actualNumberOfInvoices = inMemoryDatabase.countInvoices();

    //Then
    assertEquals(expectedNumberOfInvoices, actualNumberOfInvoices);
  }
}
