package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
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
    Invoice invoice = InvoiceGenerator.getInvoice();

    //When
    Invoice addedInvoice = inMemoryDatabase.saveInvoice(invoice);

    //Then
    assertEquals(addedInvoice, invoiceStorage.get(addedInvoice.getId()));
  }

  @Test
  void shouldUpdateInvoiceWithinInMemoryDatabase() throws DatabaseOperationException {
    //Given
    Invoice invoice = new Invoice(null, "123", LocalDate.now(), LocalDate.of(2021, 4, 3), null, null, null);
    Invoice invoiceToUpdate = new Invoice(1L, "456", LocalDate.now(), LocalDate.of(2021, 4, 3), null, null, null);

    //When
    inMemoryDatabase.saveInvoice(invoice);
    Invoice updatedInvoice = inMemoryDatabase.saveInvoice(invoiceToUpdate);

    //Then
    assertEquals(updatedInvoice, invoiceStorage.get(updatedInvoice.getId()));
  }

  @Test
  void shouldThrowDatabaseOperationExceptionForNonExistingInvoiceId() {
    Assertions.assertThrows(DatabaseOperationException.class, () -> {
      Invoice invoiceToUpdate = new Invoice(1L, "123", LocalDate.now(), LocalDate.of(2021, 4, 3), null, null, null);
      inMemoryDatabase.saveInvoice(invoiceToUpdate);
    });
  }

  @Test
  void shouldDeleteInvoiceByIdFromInMemoryDatabase() throws DatabaseOperationException {
    //Given
    Invoice invoice = new Invoice(null, "123", LocalDate.now(), LocalDate.of(2021, 4, 3), null, null, null);

    //When
    inMemoryDatabase.saveInvoice(invoice);
    System.out.println(invoiceStorage.containsKey(1L));
    inMemoryDatabase.deleteInvoice(1L);

    //Then
    assertFalse(invoiceStorage.containsKey(1L));
  }

  @Test
  void shouldReturnAllInvoicesStoredWithinInMemoryDatabase(){
    //Given

    //When

    //Then

  }

  @Test
  void shouldDeleteAllInvoicesFromInMemoryDatabase(){
    //Given

    //When

    //Then

  }

  @Test
  void shouldReturnTrueForExistingInvoice(){
    //Given

    //When

    //Then

  }

  @Test
  void shouldReturnFalseForExistingInvoice(){
    //Given

    //When

    //Then

  }

  @Test
  public void shouldReturnCorrectNumberOfInvoicesFromInMemoryDatabase(){
    //Given

    //When

    //Then

  }
}