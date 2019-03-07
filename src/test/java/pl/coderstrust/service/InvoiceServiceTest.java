package pl.coderstrust.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    private InvoiceService invoiceService;
    private Database database;
    private Invoice invoice1;
    private Invoice invoice2;

    @BeforeEach
    void setUp() {
        database = mock(Database.class);
        invoiceService = new InvoiceService(database);
        invoice1 = InvoiceGenerator.getRandomInvoice();
        invoice2 = InvoiceGenerator.getRandomInvoice();
    }

    @Test
    void shouldReturnAllInvoices() throws DatabaseOperationException {
        //Given
        List<Invoice> expectedInvoiceList = Arrays.asList(invoice1, invoice2);
        when(database.getAllInvoices()).thenReturn(expectedInvoiceList);

        //When
        Collection<Invoice> resultInvoiceList = invoiceService.getAllInvoices();

        //Then
        verify(database).getAllInvoices();
        assertEquals(expectedInvoiceList, resultInvoiceList);
    }

    @Test
    void shouldReturnAllInvoicesFromGivenBuyer() throws DatabaseOperationException {
        //Given
        List<Invoice> invoiceList = Arrays.asList(invoice1, invoice2, invoice1);
        List<Invoice> expectedInvoiceList = Arrays.asList(invoice1, invoice1);
        when(database.getAllInvoices()).thenReturn(invoiceList);

        //When
        Collection<Invoice> resultInvoiceList = invoiceService.getAllInvoices(invoice1.getSeller());

        //Then
        verify(database).getAllInvoices();
        assertEquals(expectedInvoiceList, resultInvoiceList);
    }

    @Test
    void shouldReturnAllInvoicesFromGivenDate() throws DatabaseOperationException {
        //Given
        Invoice invoice3 = new Invoice(invoice1.getId(), invoice1.getNumber(), LocalDate.of(2016, 9, 13), invoice1.getDueDate(), invoice1.getSeller(), invoice1.getBuyer(), invoice1.getEntries());
        List<Invoice> invoiceList = Arrays.asList(invoice1, invoice3, invoice3);
        List<Invoice> expectedInvoiceList = Arrays.asList(invoice3, invoice3);
        when(database.getAllInvoices()).thenReturn(invoiceList);

        //When
        Collection<Invoice> resultInvoiceList = invoiceService.getAllInvoices(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 12, 31));

        //Then
        verify(database).getAllInvoices();
        assertEquals(expectedInvoiceList, resultInvoiceList);
    }

    @Test
    void getInvoice() throws DatabaseOperationException {
        //Given
        when(database.getInvoice(1L)).thenReturn(invoice1);

        //When
        Invoice resultInvoice = invoiceService.getInvoice(1L);

        //Then
        verify(database).getInvoice(1L);
        assertEquals(invoice1, resultInvoice);
    }

    @Test
    void addInvoice() throws DatabaseOperationException {
        //Given
        when(database.saveInvoice(invoice1)).thenReturn(invoice1);

        //When
        Invoice resultInvoice = invoiceService.addInvoice(invoice1);

        //Then
        verify(database).saveInvoice(invoice1);
        assertEquals(invoice1, resultInvoice);
    }

    @Test
    void deleteInvoice() throws DatabaseOperationException {
        //Given
        List<Invoice> resultInvoiceList = new ArrayList<>();
        resultInvoiceList.add(invoice1);
        resultInvoiceList.add(invoice2);

        //When
        doAnswer(invocationOnMock -> {
            resultInvoiceList.remove(invoice1);
            return null;
        }).when(database).deleteInvoice(1L);
        invoiceService.deleteInvoice(1L);

        //Then
        List<Invoice> expectedInvoiceList = new ArrayList<>();
        expectedInvoiceList.add(invoice2);
        verify(database).deleteInvoice(1L);
        assertEquals(expectedInvoiceList, resultInvoiceList);
    }

    @Test
    void deleteAllInvoices() throws DatabaseOperationException {
        //Given
        List<Invoice> resultInvoiceList = new ArrayList<>();
        resultInvoiceList.add(invoice1);
        resultInvoiceList.add(invoice2);

        //When
        doAnswer(invocationOnMock -> {
            resultInvoiceList.clear();
            return null;
        }).when(database).deleteAllInvoices();
        invoiceService.deleteAllInvoices();

        //Then
        List<Invoice> expectedInvoiceList = new ArrayList<>();
        verify(database).deleteAllInvoices();
        assertEquals(expectedInvoiceList, resultInvoiceList);
    }
}
