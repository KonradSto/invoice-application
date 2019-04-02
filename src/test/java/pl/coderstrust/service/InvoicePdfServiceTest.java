package pl.coderstrust.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        byte[] someBytes = invoicePdfService.getInvoiceAsPdf(invoice);

        //Then
        assertNotNull(someBytes);
    }

    @Test
    void getInvoiceAsPdfMethodShouldThrowExceptionForNullInvoice() {
        assertThrows(IllegalArgumentException.class, () -> invoicePdfService.getInvoiceAsPdf(null));
    }
}
