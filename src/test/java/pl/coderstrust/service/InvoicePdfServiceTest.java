package pl.coderstrust.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.constraints.AssertTrue;

import org.junit.jupiter.api.Test;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

class InvoicePdfServiceTest {

    private InvoicePdfService invoicePdfService = new InvoicePdfService();

    @Test
    void shouldReturnTrueForNotEmptyInvoice() throws ServiceOperationException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoice();

        //When
        byte[] someBytes = invoicePdfService.getInvoiceAsPdf(invoice);

        //Then
        assertNotNull(someBytes);
    }
}
