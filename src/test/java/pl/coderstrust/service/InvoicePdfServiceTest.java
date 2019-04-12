package pl.coderstrust.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.itextpdf.text.DocumentException;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

class InvoicePdfServiceTest {

    private InvoicePdfService invoicePdfService;

    @BeforeEach
    private void setup() {
        invoicePdfService = new InvoicePdfService();
    }

    @Test
    void shouldReturnTrueForFilledInvoice() throws ServiceOperationException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoice();

        //When
        byte[] invoicePdfAsBytes = invoicePdfService.getInvoiceAsPdf(invoice);

        //Then
        assertNotNull(invoicePdfAsBytes);
        assertTrue(invoicePdfAsBytes.length > 0);
    }

    @Test
    void getInvoiceAsPdfMethodShouldThrowExceptionForNullInvoice() {
        assertThrows(IllegalArgumentException.class, () -> invoicePdfService.getInvoiceAsPdf(null));
    }
}
