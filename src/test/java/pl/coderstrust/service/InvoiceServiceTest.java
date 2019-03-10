package pl.coderstrust.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @InjectMocks
    private InvoiceService invoiceService;
    @Mock
    private Database database;
    private Invoice invoice1;
    private Invoice invoice2;

    @BeforeEach
    void setUp() {
        invoice1 = InvoiceGenerator.getRandomInvoice();
        invoice2 = InvoiceGenerator.getRandomInvoice();
    }

    @Test
    void shouldThrowExceptionForNullAsDatabase() {
        assertThrows(IllegalArgumentException.class, () -> new InvoiceService(null));
    }

    @Test
    void shouldReturnAllInvoices() throws DatabaseOperationException, ServiceOperationException {
        //Given
        List<Invoice> expectedInvoiceList = Arrays.asList(invoice1, invoice2);
        when(database.getAllInvoices()).thenReturn(expectedInvoiceList);

        //When
        Collection<Invoice> resultInvoiceList = invoiceService.getAllInvoices();

        //Then
        assertEquals(expectedInvoiceList, resultInvoiceList);
        verify(database).getAllInvoices();
    }

    @Test
    void shouldThrowServiceOperationExceptionWhileGettingAllInvoices() throws DatabaseOperationException {
        //When
        when(database.getAllInvoices()).thenThrow(DatabaseOperationException.class);

        //Then
        assertThrows(ServiceOperationException.class, () -> invoiceService.getAllInvoices());
    }

    @Test
    void shouldReturnAllInvoicesFromGivenBuyer() throws DatabaseOperationException, ServiceOperationException {
        //Given
        List<Invoice> invoiceList = Arrays.asList(invoice1, invoice2, invoice1);
        List<Invoice> expectedInvoiceList = Arrays.asList(invoice1, invoice1);
        when(database.getAllInvoices()).thenReturn(invoiceList);

        //When
        Collection<Invoice> resultInvoiceList = invoiceService.getAllInvoicesByBuyer(invoice1.getBuyer().getId());

        //Then
        assertEquals(expectedInvoiceList, resultInvoiceList);
        verify(database).getAllInvoices();
    }

    @Test
    void shouldThrowServiceOperationExceptionWhileGetAllInvoicesByBuyer() throws DatabaseOperationException {
        //When
        when(database.getAllInvoices()).thenThrow(DatabaseOperationException.class);

        //Then
        assertThrows(ServiceOperationException.class, () -> invoiceService.getAllInvoicesByBuyer(invoice1.getBuyer().getId()));
    }

    @Test
    void shouldReturnAllInvoicesFromGivenSeller() throws DatabaseOperationException, ServiceOperationException {
        //Given
        List<Invoice> invoiceList = Arrays.asList(invoice1, invoice2, invoice1);
        List<Invoice> expectedInvoiceList = Arrays.asList(invoice1, invoice1);
        when(database.getAllInvoices()).thenReturn(invoiceList);

        //When
        Collection<Invoice> resultInvoiceList = invoiceService.getAllInvoicesBySeller(invoice1.getSeller().getId());

        //Then
        assertEquals(expectedInvoiceList, resultInvoiceList);
        verify(database).getAllInvoices();
    }

    @Test
    void shouldThrowServiceOperationExceptionWhileGetAllInvoicesBySeller() throws DatabaseOperationException {
        //When
        when(database.getAllInvoices()).thenThrow(DatabaseOperationException.class);

        //Then
        assertThrows(ServiceOperationException.class, () -> invoiceService.getAllInvoicesBySeller(invoice1.getSeller().getId()));
    }

    @Test
    void shouldReturnAllInvoicesFromGivenDateRage() throws DatabaseOperationException, ServiceOperationException {
        //Given
        Invoice invoice3 = new Invoice(invoice1.getId(), invoice1.getNumber(), LocalDate.of(2016, 9, 13), invoice1.getDueDate(), invoice1.getSeller(), invoice1.getBuyer(), invoice1.getEntries());
        Invoice invoice4 = new Invoice(invoice2.getId(), invoice2.getNumber(), LocalDate.of(2017, 5, 23), invoice2.getDueDate(), invoice2.getSeller(), invoice2.getBuyer(), invoice1.getEntries());
        List<Invoice> invoiceList = Arrays.asList(invoice3, invoice4, invoice3);
        List<Invoice> expectedInvoiceList = Arrays.asList(invoice3, invoice3);
        when(database.getAllInvoices()).thenReturn(invoiceList);

        //When
        Collection<Invoice> resultInvoiceList = invoiceService.getAllInvoicesByDate(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 12, 31));

        //Then
        assertEquals(expectedInvoiceList, resultInvoiceList);
        verify(database).getAllInvoices();
    }

    @Test
    void shouldThrowExceptionForNullIFromDate() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.getAllInvoicesByDate(null, LocalDate.of(2018, 4, 11)));
    }

    @Test
    void shouldThrowExceptionForNullIToDate() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.getAllInvoicesByDate(LocalDate.of(2016, 4, 21), null));
    }

    @Test
    void shouldThrowExceptionForInvalidDates() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.getAllInvoicesByDate(LocalDate.of(2017, 1, 20), LocalDate.of(2016, 6, 13)));
    }

    @Test
    void shouldThrowServiceOperationExceptionWhileGettingAllInvoiceFromGivenDataRange() throws DatabaseOperationException {
        //When
        when(database.getAllInvoices()).thenThrow(DatabaseOperationException.class);

        //Then
        assertThrows(ServiceOperationException.class, () -> invoiceService.getAllInvoicesByDate(LocalDate.of(2016, 4, 21), LocalDate.of(2017, 1, 20)));
    }

    @Test
    void shouldReturnInvoice() throws DatabaseOperationException, ServiceOperationException {
        //Given
        when(database.getInvoice(1L)).thenReturn(invoice1);

        //When
        Invoice resultInvoice = invoiceService.getInvoice(1L);

        //Then
        assertEquals(invoice1, resultInvoice);
        verify(database).getInvoice(1L);
    }

    @Test
    void shouldThrowExceptionForNullIdWhileGettingInvoice() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.getInvoice(null));
    }

    @Test
    void shouldThrowServiceOperationExceptionWhileGettingInvoice() throws DatabaseOperationException {
        //When
        when(database.getInvoice(2L)).thenThrow(DatabaseOperationException.class);

        //Then
        assertThrows(ServiceOperationException.class, () -> invoiceService.getInvoice(2L));
    }

    @Test
    void shouldSaveInvoice() throws DatabaseOperationException, ServiceOperationException {
        //Given
        when(database.saveInvoice(invoice1)).thenReturn(invoice2);

        //When
        Invoice resultInvoice = invoiceService.saveInvoice(invoice1);

        //Then
        assertEquals(invoice2, resultInvoice);
        verify(database).saveInvoice(invoice1);
    }

    @Test
    void shouldThrowExceptionForNullInvoiceWhileSavingNullInvoice() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.saveInvoice(null));
    }

    @Test
    void shouldThrowServiceOperationExceptionWhileSavingInvoice() throws DatabaseOperationException {
        //When
        when(database.saveInvoice(invoice1)).thenThrow(DatabaseOperationException.class);

        //Then
        assertThrows(ServiceOperationException.class, () -> invoiceService.saveInvoice(invoice1));
    }

    @Test
    void shouldDeleteInvoice() throws DatabaseOperationException, ServiceOperationException {
        //When
        invoiceService.deleteInvoice(1L);

        //Then
        verify(database).deleteInvoice(1L);
    }

    @Test
    void shouldThrowExceptionForNullIdWhileDeletingInvoice() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.deleteInvoice(null));
    }

    @Test
    void shouldThrowServiceOperationExceptionWhileDeletingInvoice() throws DatabaseOperationException {
        //When
        doThrow(DatabaseOperationException.class)
            .when(database)
            .deleteInvoice(1L);

        //Then
        assertThrows(ServiceOperationException.class, () -> invoiceService.deleteInvoice(1L));
    }

    @Test
    void shouldDeleteAllInvoices() throws ServiceOperationException, DatabaseOperationException {
        //When
        invoiceService.deleteAllInvoices();

        //Then
        verify(database).deleteAllInvoices();
    }

    @Test
    void shouldThrowServiceOperationExceptionWhileDeletingAllInvoices() throws DatabaseOperationException {
        //When
        doThrow(DatabaseOperationException.class)
            .when(database)
            .deleteAllInvoices();

        //Then
        assertThrows(ServiceOperationException.class, () -> invoiceService.deleteAllInvoices());
    }
}
