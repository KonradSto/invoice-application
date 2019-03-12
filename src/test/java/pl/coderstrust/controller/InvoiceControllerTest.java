package pl.coderstrust.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
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
        //Given
        Invoice invoice = InvoiceGenerator.getRandomInvoice();
        when(invoiceService.getInvoice(1L)).thenReturn(invoice);

        //When
        MvcResult result = mockMvc.perform(
            get("/invoices/{id}", 1L).accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();
        int actualHttpStatus = result.getResponse().getStatus();
        Invoice actualInvoice = mapper.readValue(result.getResponse().getContentAsString(), Invoice.class);
        //Then
        assertEquals(HttpStatus.OK.value(), actualHttpStatus);
        assertEquals(invoice, actualInvoice);
        verify(invoiceService).getInvoice(1L);
    }

    @Test
//    get("/invoices/{id}", null).accept(MediaType.APPLICATION_JSON_UTF8))
    void shouldReturnHttpNotFoundStatusWhenNullReturnedFromInvoiceServiceMethod() throws Exception {
        //Given
        Long nonExistentId = 10L;
        when(invoiceService.getInvoice(nonExistentId)).thenReturn(null);

        //When
        MvcResult result = mockMvc.perform(
            get("/invoices/{id}", nonExistentId).accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();
        int actualHttpStatus = result.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.NOT_FOUND.value(), actualHttpStatus);
        verify(invoiceService).getInvoice(nonExistentId);
    }

    @Test
        // FIXME: 10/03/2019 shoulsreturninternalserver error during getting invoice when sth wetn wrong on server
    void shouldReturnHttpInternalServerErrorWhenExceptionThrownByInvoiceServiceMethod() throws Exception {
        //Given
        // TODO: 10/03/2019 should I procure ecxeption like below or in some other way
        Long id = 10L;
        when(invoiceService.getInvoice(id)).thenThrow(ServiceOperationException.class);

        //When
        MvcResult result = mockMvc.perform(
            get("/invoices/{id}", id).accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();
        int actualHttpStatus = result.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), actualHttpStatus);
        verify(invoiceService).getInvoice(id);
    }

    @Test
    void shouldReturnAllInvoices() throws Exception {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoice();
        Invoice invoice2 = InvoiceGenerator.getRandomInvoice();
        Collection<Invoice> invoices = Arrays.asList(invoice1, invoice2);
        when(invoiceService.getAllInvoices()).thenReturn(invoices);

        //When
        MvcResult result = mockMvc.perform(
            get("/invoices").accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();
        int actualHttpStatus = result.getResponse().getStatus();
        List<Invoice> actualInvoices = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Invoice>>() {
        });

        //Then
        assertEquals(HttpStatus.OK.value(), actualHttpStatus);
        assertEquals(invoices, actualInvoices);
        verify(invoiceService).getAllInvoices();
    }

    @Test
        // TODO: 10/03/2019  how to name tests like below
        // TODO: 10/03/2019  when Bad request when  internalserver error   ( illegalArgumentEx vs serviceOperationEx ?? )
    void shouldReturnInternalServerErrorForGetAllInvoices() throws Exception {
        //Given
        when(invoiceService.getAllInvoices()).thenThrow(ServiceOperationException.class);

        //When
        MvcResult result = mockMvc.perform(
            get("/invoices").accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();
        int actualHttpStatus = result.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), actualHttpStatus);
        verify(invoiceService).getAllInvoices();
    }

    @Test
    void shouldReturnInvoicesIssuedWithinGivenDates() throws Exception {
        //Given
        String fromDate = "2018-01-01";
        String toDate = "2018-01-31";
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificIssueDate(LocalDate.parse(fromDate));
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificIssueDate(LocalDate.parse("2018-01-15"));
        Invoice invoice3 = InvoiceGenerator.getRandomInvoiceWithSpecificIssueDate(LocalDate.parse(toDate));
        Invoice invoice4 = InvoiceGenerator.getRandomInvoiceWithSpecificIssueDate(LocalDate.parse("2018-02-01"));
        List<Invoice> expected = Arrays.asList(invoice1, invoice2, invoice3);
        when(invoiceService.getAllInvoicesByDate(LocalDate.parse(fromDate), LocalDate.parse(toDate))).thenReturn(expected);

        //When
        MvcResult result = mockMvc.perform(
            get("/invoices/byDate")
                .param("fromDate", fromDate)
                .param("toDate", toDate)
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();
        int actualHttpStatus = result.getResponse().getStatus();
        List<Invoice> actualInvoices = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Invoice>>() {
        });

        //Then
        assertEquals(HttpStatus.OK.value(), actualHttpStatus);
        assertEquals(expected, actualInvoices);
        verify(invoiceService).getAllInvoicesByDate(LocalDate.parse(fromDate), LocalDate.parse(toDate));
    }

    @Test
    void shouldReturnInternalServerErrorWhenWhenExceptionThrownByGetAllInvoicesByDate() throws Exception {
        //Given
        String fromDate = "2018-01-01";
        String toDate = "2018-01-31";
        when(invoiceService.getAllInvoicesByDate(LocalDate.parse(fromDate), LocalDate.parse(toDate))).thenThrow(ServiceOperationException.class);

        //When
        MvcResult result = mockMvc.perform(
            get("/invoices/byDate")
                .param("fromDate", fromDate)
                .param("toDate", toDate)
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();
        int actualHttpStatus = result.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), actualHttpStatus);
        verify(invoiceService).getAllInvoicesByDate(LocalDate.parse(fromDate), LocalDate.parse(toDate));
    }

    @Test
    void shouldReturnBadRequestWhenFromDateParsedValueIsNull() throws Exception {
        //Given
        String fromDate = "2018-01-01";
        String toDate = "2018-01-31";

        //When
        MvcResult result = mockMvc.perform(
            get("/invoices/byDate")
                .param("fromDate", "")
                .param("toDate", toDate)
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();
        int actualHttpStatus = result.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.BAD_REQUEST.value(), actualHttpStatus);
        verify(invoiceService, never()).getAllInvoicesByDate(LocalDate.parse(fromDate), LocalDate.parse(toDate));
    }

    @Test
    void shouldReturnBadRequestWhenToDateParsedValueIsNull() throws Exception {
        //Given
        String fromDate = "2018-01-01";
        String toDate = "2018-01-31";

        //When
        MvcResult result = mockMvc.perform(
            get("/invoices/byDate")
                .param("fromDate", fromDate)
                .param("toDate", "")
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();
        int actualHttpStatus = result.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.BAD_REQUEST.value(), actualHttpStatus);
        verify(invoiceService, never()).getAllInvoicesByDate(LocalDate.parse(fromDate), LocalDate.parse(toDate));
    }

    @Test
    void shouldReturnInvoicesByGivenBuyerId() throws Exception {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerId(1L);
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificBuyerId(1L);
        List<Invoice> expected = Arrays.asList(invoice1, invoice2);
        when(invoiceService.getAllInvoicesByBuyer(1L)).thenReturn(expected);

        //When
        MvcResult result = mockMvc.perform(
            get("/invoices/byBuyer")
                .param("id", "1")
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();
        int actualHttpStatus = result.getResponse().getStatus();
        List<Invoice> actualInvoices = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Invoice>>() {
        });

        //Then
        assertEquals(HttpStatus.OK.value(), actualHttpStatus);
        assertEquals(expected, actualInvoices);
        verify(invoiceService).getAllInvoicesByBuyer(1L);
    }

    @Test
    void shouldReturnInternalServerErrorWhenExceptionThrownByGetAllInvoicesByBuyerId() throws Exception {
        //Given
        when(invoiceService.getAllInvoicesByBuyer(1L)).thenThrow(ServiceOperationException.class);

        //When
        MvcResult result = mockMvc.perform(
            get("/invoices/byBuyer")
                .param("id", "1")
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();
        int actualHttpStatus = result.getResponse().getStatus();

        //Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), actualHttpStatus);
        verify(invoiceService).getAllInvoicesByBuyer(1L);
    }

    @Test
    void shouldReturnInvoicesByGivenSellerId() throws Exception {
        //Given
        Invoice invoice1 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerId(1L);
        Invoice invoice2 = InvoiceGenerator.getRandomInvoiceWithSpecificSellerId(1L);
        List<Invoice> expected = Arrays.asList(invoice1, invoice2);
        when(invoiceService.getAllInvoicesBySeller(1L)).thenReturn(expected);

        //When
        MvcResult result = mockMvc.perform(
            get("/invoices/bySeller")
                .param("id", "1")
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();
        int actualHttpStatus = result.getResponse().getStatus();
        List<Invoice> actualInvoices = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Invoice>>() {
        });

        //Then
        assertEquals(HttpStatus.OK.value(), actualHttpStatus);
        assertEquals(expected, actualInvoices);
        verify(invoiceService).getAllInvoicesBySeller(1L);
    }

    @Test
    void shouldReturnInternalServerErrorWhenExceptionThrownByGetAllInvoicesBySellerId() throws Exception {
        //Given
        when(invoiceService.getAllInvoicesBySeller(1L)).thenThrow(ServiceOperationException.class);

        //When
        MvcResult result = mockMvc.perform(
            get("/invoices/bySeller")
                .param("id", "1")
                .accept(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();
        int actualHttpStatus = result.getResponse().getStatus();

        //Then
        //verify(invoiceService, never()).getAllInvoicesByDate("", null);
        verify(invoiceService, never()).getAllInvoicesByBuyer(1L);
        verify(invoiceService).getAllInvoicesBySeller(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), actualHttpStatus);
    }

}
