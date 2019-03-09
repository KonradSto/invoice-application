package pl.coderstrust.controller;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Invoice;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping()
    ResponseEntity<Collection<Invoice>> getAllInvoicesC() {
        Collection<Invoice> allInvoices;
        try {
            allInvoices = invoiceService.getAllInvoices();
            return new ResponseEntity<>(allInvoices, HttpStatus.OK);
        } catch (DatabaseOperationException e) {                // TODO: 07/03/2019 cannot make exception to be thrown in case of InmemoryDatabase
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

   /* @GetMapping("/{company}")
    ResponseEntity<Collection<Invoice>> getInvoicesByCompany(@PathVariable Company company) {
        Collection<Invoice> allInvoicesByCompany;
        try {
            allInvoicesByCompany = invoiceService.getAllInvoices(company);
            return new ResponseEntity<>(allInvoicesByCompany, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (DatabaseOperationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }*/

    @GetMapping("/byDate")
        //inoices/byData?fromdate=123&todate-123
    ResponseEntity<?> getInvoicesByDateRange(@RequestParam LocalDate fromDate, @PathVariable LocalDate toDate) {
        Collection<Invoice> allInvoicesByDates;
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
        try {
            Invoice invoice = invoiceService.getInvoice(id);
            if (invoice == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok().body(invoice);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping()
    ResponseEntity<Invoice> updateInvoice(@RequestBody Invoice invoice) {
        try {
            invoiceService.addInvoice(invoice);
            return new ResponseEntity<>(invoice, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DatabaseOperationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
/*
    @PostMapping("/updateInvoice")
    ResponseEntity<Invoice> updateInvoice(@RequestBody Invoice invoice) {
        try {
            invoiceService.updateInvoice(invoice);
            return new ResponseEntity<>(invoice, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (DatabaseOperationException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);   // FIXME: 05/03/2019 some other status code
        }
    }*/
}
