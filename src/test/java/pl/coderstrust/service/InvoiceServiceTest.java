package pl.coderstrust.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

@RunWith(MockitoJUnitRunner.class)
class InvoiceServiceTest {

    private InvoiceService invoiceService;
    private Database database;

    @Test
    void shouldReturnAllInvoices() throws DatabaseOperationException {
        //Given
        database = mock(Database.class);
        invoiceService = new InvoiceService(database);
        Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
        Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
        List<Invoice> invoiceList = Arrays.asList(invoice1, invoice2);
        when(database.getAllInvoices()).thenReturn(invoiceList);

        //When
        Collection<Invoice> allInvoices = invoiceService.getAllInvoices();

        //Then
        verify(database).getAllInvoices();
        assertEquals(invoiceList, allInvoices);
    }

    @Test
    void shouldReturnAllInvoicesFromGivenBuyer() throws DatabaseOperationException {
        //Given
        database = mock(Database.class);
        invoiceService = new InvoiceService(database);
        Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
        Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
        List<Invoice> invoiceList = Arrays.asList(invoice1, invoice2, invoice1);
        List<Invoice> expectedInvoiceList = Arrays.asList(invoice1, invoice1);
        when(database.getAllInvoices()).thenReturn(invoiceList);

        //When
        Collection<Invoice> allInvoices = invoiceService.getAllInvoices(invoice1.getSeller());

        //Then
        verify(database).getAllInvoices();
        assertEquals(expectedInvoiceList, allInvoices);
    }

    @Test
    void shouldReturnAllInvoicesFromGivenDate() throws DatabaseOperationException {
        //Given
        database = mock(Database.class);
        invoiceService = new InvoiceService(database);
        Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
        Invoice invoice2 = new Invoice(invoice1.getId(),invoice1.getNumber(), LocalDate.of(2016,9,13),invoice1.getDueDate(),invoice1.getSeller(),invoice1.getBuyer(),invoice1.getEntries());

        List<Invoice> invoiceList = Arrays.asList(invoice1, invoice2, invoice2);
        List<Invoice> expectedInvoiceList = Arrays.asList(invoice2, invoice2);
        when(database.getAllInvoices()).thenReturn(invoiceList);

        //When
        Collection<Invoice> allInvoices = invoiceService.getAllInvoices(LocalDate.of(2016,1,1), LocalDate.of(2016,12,31));

        //Then
        verify(database).getAllInvoices();
        assertEquals(expectedInvoiceList, allInvoices);
    }

    @Test
    void getInvoice() throws DatabaseOperationException {
        //Given
        database = mock(Database.class);
        invoiceService = new InvoiceService(database);
        Invoice invoice = InvoiceGenerator.getRandomInvoice();
        when(database.getInvoice((long) 1)).thenReturn(invoice);

        //When
        Invoice resultInvoice = invoiceService.getInvoice((long) 1);

        //Then
        verify(database).getInvoice((long) 1);
        assertEquals(invoice, resultInvoice);
    }

    @Test
    void addInvoice() throws DatabaseOperationException {
        //Given
        database = mock(Database.class);
        invoiceService = new InvoiceService(database);
        Invoice invoice = InvoiceGenerator.getRandomInvoice();
        when(database.saveInvoice(invoice)).thenReturn(invoice);

        //When
        Invoice resultInvoice = invoiceService.addInvoice(invoice);

        //Then
        verify(database).saveInvoice(invoice);
        assertEquals(invoice, resultInvoice);
    }

    @Test
    void deleteInvoice() throws DatabaseOperationException {
        //Given
        database = mock(Database.class);
        invoiceService = new InvoiceService(database);
        Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
        Invoice invoice2 = InvoiceGenerator.getRandomInvoice();

        List<Invoice> invoiceList = Arrays.asList(invoice1, invoice2);
        List<Invoice> expectedInvoiceList = Arrays.asList(invoice2);
        when(database.deleteInvoice((long) 1)).then(invoiceList.remove(invoice1));

        //When
        Collection<Invoice> allInvoices = invoiceService.deleteInvoice(1);

        //Then
        verify(database).getAllInvoices();
        assertEquals(expectedInvoiceList, allInvoices);
    }

    @Test
    void deleteAllInvoices() {
    }
}