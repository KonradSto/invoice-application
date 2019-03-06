package pl.coderstrust.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

class InvoiceControllerTest {

    private MockMvc mockMvc;

    @Test
    public void getAllInvoices() throws Exception {

        mockMvc.perform(get("/invoices/all").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}

 /*   @Override
    public Collection<Invoice> getAllInvoices() throws DatabaseOperationException {
        return database.getAllInvoices();
    }*/