package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderstrust.database.hibernate.HibernateDatabase;
import pl.coderstrust.database.hibernate.InvoiceRepository;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

@ExtendWith(MockitoExtension.class)
class HibernateDatabaseTest {

    @InjectMocks
    private HibernateDatabase hibernateDatabase;

    @Mock
    private InvoiceRepository invoiceRepository;
    private Invoice invoice1;
    private Invoice invoice2;

    @BeforeEach
    void setUp() {
        invoice1 = InvoiceGenerator.getRandomInvoice();
        invoice2 = InvoiceGenerator.getRandomInvoice();
    }

    @Test
    void shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new HibernateDatabase(null));
    }

    @Test
    void shouldSaveInvoice() {
        //Given
        when(invoiceRepository.save(invoice1)).thenReturn(invoice2);

        //When
        Invoice savedInvoice = hibernateDatabase.saveInvoice(invoice1);

        //Then
        assertEquals(invoice2, savedInvoice);
        verify(invoiceRepository).save(invoice1);
    }

    @Test
    void saveInvoiceMethodShouldThrowExceptionForNullInvoice() {
        assertThrows(IllegalArgumentException.class, () -> hibernateDatabase.saveInvoice(null));
    }

    @Test
    void shouldDeleteInvoice() throws DatabaseOperationException {
        //Given
        when(invoiceRepository.existsById(invoice1.getId())).thenReturn(true);

        //When
        hibernateDatabase.deleteInvoice(invoice1.getId());

        //Then
        verify(invoiceRepository).deleteById(invoice1.getId());
    }

    @Test
    void deleteInvoiceMethodShouldThrowExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> hibernateDatabase.deleteInvoice(null));
    }

    @Test
    void deleteInvoiceMethodShouldThrowExceptionDuringDeletingNotExistingInvoice() {
        assertThrows(DatabaseOperationException.class, () -> hibernateDatabase.deleteInvoice(1L));
    }

    @Test
    void shouldReturnInvoice()  {
        //Given
        Optional<Invoice> expectedInvoice = Optional.ofNullable(invoice1);
        when(invoiceRepository.findById(invoice1.getId())).thenReturn(expectedInvoice);

        //When
        Optional<Invoice> returnedInvoice = hibernateDatabase.getInvoice(invoice1.getId());

        //Then
        assertEquals(expectedInvoice, returnedInvoice);
        verify(invoiceRepository).findById(invoice1.getId());
    }

    @Test
    void getInvoiceMethodShouldThrowExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> hibernateDatabase.getInvoice(null));
    }

    @Test
    void shouldReturnEmptyOptionalWhenInvoiceDoesNotExist() {
        //When
        Optional<Invoice> invoice = hibernateDatabase.getInvoice(1L);

        //Then
        assertFalse(invoice.isPresent());
    }

    @Test
    void shouldReturnAllInvoices() {
        //Given
        List<Invoice> expectedInvoices = Arrays.asList(invoice1, invoice2);
        when(invoiceRepository.findAll()).thenReturn(expectedInvoices);

        //When
        Collection<Invoice> returnedInvoices = hibernateDatabase.getAllInvoices();

        //Then
        assertEquals(expectedInvoices, returnedInvoices);
        verify(invoiceRepository).findAll();
    }

    @Test
    void shouldDeleteAllInvoices() {
        //When
        hibernateDatabase.deleteAllInvoices();

        //Then
        verify(invoiceRepository).deleteAll();
    }

    @Test
    void shouldReturnTrueForExistingInvoice() {
        //Given
        when(invoiceRepository.existsById(invoice1.getId())).thenReturn(true);

        //Then
        assertTrue(hibernateDatabase.invoiceExists(invoice1.getId()));
        verify(invoiceRepository).existsById(invoice1.getId());
    }

    @Test
    void shouldReturnFalseForNotExistingInvoice() {
        //Given
        when(invoiceRepository.existsById(invoice1.getId())).thenReturn(false);

        //Then
        assertFalse(hibernateDatabase.invoiceExists(invoice1.getId()));
        verify(invoiceRepository).existsById(invoice1.getId());
    }

    @Test
    void invoiceExistsMethodShouldThrowExceptionForNullId() {
        assertThrows(IllegalArgumentException.class, () -> hibernateDatabase.invoiceExists(null));
    }

    @Test
    void shouldReturnNumberOfInvoices() {
        //Given
        long expectedNumberOfInvoices = 2L;
        when(invoiceRepository.count()).thenReturn(2L);

        //When
        long actualNumberOfInvoices = hibernateDatabase.countInvoices();

        //Then
        assertEquals(expectedNumberOfInvoices, actualNumberOfInvoices);
        verify(invoiceRepository).count();
    }
}
