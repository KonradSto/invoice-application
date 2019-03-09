package pl.coderstrust.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

@RunWith(SpringRunner.class)
@WebMvcTest(InvoiceController.class)
public class ControllerApiTest {

    @MockBean
    InvoiceService invoiceService;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void init() {
        Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
        Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
        Invoice invoice3 = InvoiceGenerator.getRandomInvoice();
    }

    @Test
    public void shouldGetInvoiceNo1() throws Exception {
        mvc.perform(MockMvcRequestBuilders
            .get("/invoices/{1}")
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.employees").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.employees[*].employeeId").isNotEmpty());

        mvc.perform(MockMvcRequestBuilders
            .get("/employees")
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.employees").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.employees[*].employeeId").isNotEmpty());
    }
}
