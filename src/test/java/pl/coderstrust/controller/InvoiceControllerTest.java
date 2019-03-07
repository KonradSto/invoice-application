package pl.coderstrust.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class InvoiceControllerTest {

    @BeforeEach
    public void setUp() {
        //@MockBean(InvoiceService.class)
      /*  @MockBean
        InvoiceService invoiceService;*/
    }


    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllInvoices() throws Exception {

    }


    /*
    @Test
    public void getAllInvoice() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
            .get("/invoices"))
            .andDo(print())
            .andExpect(content().string(containsString("hello")))
            .andExpect(status().isOk());
    }*/

   /* @Test
    public void getAllInvoices() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
            .get("/invoices/all"))
            .andDo(print())
            //  .andExpect(content().string(containsString("hello")))
            .andExpect(status().isOk());
    }*/

}
