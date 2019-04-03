package pl.coderstrust.service;

import static org.junit.jupiter.api.Assertions.*;

import com.itextpdf.text.DocumentException;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

class InvoicePdfServiceTest {

    private InvoicePdfService invoicePdfService = new InvoicePdfService();

    InvoicePdfServiceTest() throws IOException, DocumentException {
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
