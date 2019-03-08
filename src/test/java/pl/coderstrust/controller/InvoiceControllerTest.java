package pl.coderstrust.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;

//@RunWith(MockitoJUnitRunner.class)
@ExtendWith(SpringExtension.class)
//@SpringBootTest
@WebMvcTest
@AutoConfigureMockMvc

@MockBean(InvoiceService.class)
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private InvoiceController invoiceController;
    private InvoiceService invoiceService;

    @BeforeEach
    void setUp() {
        invoiceService = mock(InvoiceService.class);
        InvoiceController invoiceController = new InvoiceController(invoiceService);

    }

    @Test
    public void shouldReturnEmptyInvoiceListWhenGetAllInvoicesCalledOnEmptyDatabase() throws Exception {

       /* invoiceService = mock(InvoiceService.class);
        InvoiceController invoiceController = new InvoiceController(invoiceService);

        Collection<Invoice> allInvoices = Arrays.asList(InvoiceGenerator.getRandomInvoice(), InvoiceGenerator.getRandomInvoice());
        ResponseEntity<Collection<Invoice>> returnValue = new ResponseEntity<>(allInvoices, HttpStatus.OK);*/

        mockMvc.perform(get("/invoices/all"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$", hasSize(0)))
            .andExpect(MockMvcResultMatchers.status().isOk());
/*
        when(invoiceController.getAllInvoicesC()).thenReturn(returnValue);
        when(invoiceService.getAllInvoices()).thenReturn(allInvoices);
        verify(invoiceService).getAllInvoices();*/
    }

    @Test
    public void shouldReturnEmptyInvoiceListWhenCompanyNotContainedInInvoices() throws Exception {
        Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
        Company buyer = invoice1.getBuyer();
        Collection<Invoice> allInvoices = new ArrayList<>(Collections.singleton(invoice1));
        when(invoiceService.getAllInvoices(invoice1.getBuyer())).thenReturn(allInvoices);

        mockMvc.perform(get("/invoices"))
            .andDo(print())
            .andExpect(content().string(buyer.toString()));


        /*
        mockMvc.perform(get("/invoices/buyer"))
            .andExpect(handler().handlerType(InvoiceController.class))
            .andExpect(handler().methodName("getInvoicesByCompany"));*/

    }

    // FIXME: 07/03/2019 below i invoked getAllInvoices but it expects HTTP input so cannot be mocked like below
   /* @Test
    public void getAllInvoices() throws Exception {

        invoiceService = mock(InvoiceService.class);
        InvoiceController invoiceController = new InvoiceController(invoiceService);

        Collection<Invoice> allInvoices = Arrays.asList(InvoiceGenerator.getRandomInvoice(), InvoiceGenerator.getRandomInvoice());
        ResponseEntity<Collection<Invoice>> returnValue = new ResponseEntity<>(allInvoices, HttpStatus.OK);

        when(invoiceController.getAllInvoicesC()).thenReturn(returnValue);
        when(invoiceService.getAllInvoices()).thenReturn(allInvoices);
        verify(invoiceService).getAllInvoices();
    }*/
}
