package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.coderstrust.model.Invoice;

//@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class InFileDatabaseTest {
    //@Autowired
    @MockBean
    private FileHelper fileHelper;
    @Autowired
    private InFileDatabase inFileDataBase;
    @Autowired
    private ObjectMapper mapper;


/*
    @Test
    void shouldAddInvoiceForNullInvoiceId() throws IOException, DatabaseOperationException {
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoiceWithoutId();
        Invoice returned;
       // when(fileHelper.writeLine(toJson(invoice))).thenReturn(invoice);
        //when(fileHelper.saveInvoice(invoice)).thenReturn(invoice);
doAnswer()
        //When
        returned = inFileDataBase.saveInvoice(invoice);

        //Then
        verify(inFileDataBase).saveInvoice(invoice);
        assertEquals(invoice, returned);
    }*/


    @Test
    void shouldThrowExceptionForNullInvoice() throws IOException, DatabaseOperationException {
        assertThrows(org.springframework.dao.InvalidDataAccessApiUsageException.class, () -> inFileDataBase.saveInvoice(null));
    }

    @Test
    void shouldInsertInvoiceForNullId() throws IOException, DatabaseOperationException {


    }


    String toJson(Invoice invoice) throws JsonProcessingException {
        return mapper.writeValueAsString(invoice);
    }

    Invoice getInvoiceFromJson(String invoiceAsJson) throws IOException {
        return mapper.readValue(invoiceAsJson, Invoice.class);
    }

}
