package pl.coderstrust.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    // TODO: 01/03/2019 what to do with exceptions?
    @GetMapping
    ResponseEntity<List<Invoice>> getAllInvoices() {
        List<Invoice> allInvoices;
        try {
            allInvoices = invoiceService.getAllInvoices();
            return new ResponseEntity<>(allInvoices, HttpStatus.OK);
        } catch (DatabaseOperationException e) {                // TODO: 05/03/2019 at the current Application state this will not happen (maybe can happen when real DB implementation is injected
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{company}")
    ResponseEntity<List<Invoice>> getInvoicesByCompany(@PathVariable Company company) {
        List<Invoice> allInvoicesByCompany;
        try {
            allInvoicesByCompany = invoiceService.getAllInvoices(company);
            return new ResponseEntity<>(allInvoicesByCompany, HttpStatus.OK);
        } catch (DatabaseOperationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);          // FIXME: 05/03/2019 ldksgjakdg
        }
    }

    @GetMapping("/{fromDate}/{toDate}")
    ResponseEntity<List<Invoice>> getInvoicesByDateRange(@PathVariable LocalDate fromDate, @PathVariable LocalDate toDate) {
        List<Invoice> allInvoicesByDates;
        try {
            allInvoicesByDates = invoiceService.getAllInvoices(fromDate, toDate);
            return new ResponseEntity<>(allInvoicesByDates, HttpStatus.OK);
        } catch (DatabaseOperationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        Invoice invoice;
        try {
            invoice = invoiceService.getInvoice(id);
            return new ResponseEntity<>(invoice, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DatabaseOperationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    ResponseEntity<Invoice> addInvoice(@RequestBody Invoice invoice) {
        try {
            invoiceService.addInvoice(invoice);
            return new ResponseEntity<>(invoice, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DatabaseOperationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping()
    ResponseEntity<Invoice> updateInvoice(@RequestBody Invoice invoice) {
        try {
            invoiceService.updateInvoice(invoice);
            return new ResponseEntity<>(invoice, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
