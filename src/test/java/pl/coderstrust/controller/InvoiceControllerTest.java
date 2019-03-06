package pl.coderstrust.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllInvoice() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
            .get("/invoices"))
            .andDo(print())
            .andExpect(content().string(containsString("hello")))
            .andExpect(status().isOk());
    }

    @Test
    public void getAllInvoices() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
            .get("/invoices/all"))
            .andDo(print())
            //  .andExpect(content().string(containsString("hello")))
            .andExpect(status().isOk());
    }

}

    /*@GetMapping(value = "/all")
    ResponseEntity<Collection<Invoice>> getAllInvoices() {
        Collection<Invoice> allInvoices;
        try {
            allInvoices = invoiceServiceImpl.getAllInvoices();
            return new ResponseEntity<>(allInvoices, HttpStatus.OK);
        } catch (DatabaseOperationException e) {                // TODO: 05/03/2019 at the current Application state this will not happen (maybe can happen when real DB implementation is injected
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
*/