package pl.coderstrust.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.coderstrust.generators.InvoiceGenerator;
import pl.coderstrust.model.Invoice;

//@RunWith(MockitoJUnitRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc

@MockBean(InvoiceService.class)
class InvoiceControllerTest {

    @Autowired
    MockMvc mockMvc;
    private InvoiceController invoiceController;
    private InvoiceService invoiceService;

    @BeforeEach
    void setUp() {
        invoiceService = mock(InvoiceService.class);
        InvoiceController invoiceController = new InvoiceController(invoiceService);

    }

    @Test
    public void shouldgetAllInvoices() throws Exception {

        invoiceService = mock(InvoiceService.class);
        InvoiceController invoiceController = new InvoiceController(invoiceService);

        Collection<Invoice> allInvoices = Arrays.asList(InvoiceGenerator.getRandomInvoice(), InvoiceGenerator.getRandomInvoice());
        ResponseEntity<Collection<Invoice>> returnValue = new ResponseEntity<>(allInvoices, HttpStatus.OK);

        mockMvc.perform(get("/invoices/all"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$", hasSize(0)))
            .andExpect(MockMvcResultMatchers.status().isOk());
/*
        when(invoiceController.getAllInvoicesC()).thenReturn(returnValue);
        when(invoiceService.getAllInvoices()).thenReturn(allInvoices);
        verify(invoiceService).getAllInvoices();*/
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
