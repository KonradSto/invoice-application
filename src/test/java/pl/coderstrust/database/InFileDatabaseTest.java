package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class InFileDatabaseTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private InFileDataBase inFileDataBase;

    @Test
    void shouldAddInvoiceForNullInvoiceId() throws IOException, DatabaseOperationException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice returned;
        when(inFileDataBase.saveInvoice(invoice)).thenReturn(invoice);

        //When
        returned = inFileDataBase.saveInvoice(invoice);

        //Then
        verify(inFileDataBase).saveInvoice(invoice);
        assertEquals(invoice, returned);
    }


    @Test
    void shouldThrowExceptioneForNullInvoiceId() throws IOException, DatabaseOperationException {
        //When
        when(inFileDataBase.saveInvoice(null)).thenThrow(IllegalArgumentException.class);

        //Then
        assertThrows(IllegalArgumentException.class, () -> inFileDataBase.saveInvoice(null));
    }

    String toJson(Invoice invoice) throws JsonProcessingException {
        return mapper.writeValueAsString(invoice);
    }

    Invoice getInvoiceFromJson(String invoiceAsJson) throws IOException {
        return mapper.readValue(invoiceAsJson, Invoice.class);
    }

}
