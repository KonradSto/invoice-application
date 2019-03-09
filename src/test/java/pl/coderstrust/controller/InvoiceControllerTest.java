package pl.coderstrust.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

@ExtendWith(SpringExtension.class)
@WebMvcTest(InvoiceController.class)
@AutoConfigureMockMvc
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;

    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    void shouldReturnInvoice() throws Exception {
        Invoice invoice = InvoiceGenerator.getRandomInvoice();
        when(invoiceService.getInvoice(1L)).thenReturn(invoice);

        MvcResult result = mockMvc.perform(
            get("/invoices/{id}", 1L).accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();

        int actualHttpStatus = result.getResponse().getStatus();
        Invoice actualInvoice = mapper.readValue(result.getResponse().getContentAsString(), Invoice.class);

        //Assert
        assertEquals(HttpStatus.OK.value(), actualHttpStatus);
        assertEquals(invoice, actualInvoice);
        verify(invoiceService).getInvoice(1L);
    }
}