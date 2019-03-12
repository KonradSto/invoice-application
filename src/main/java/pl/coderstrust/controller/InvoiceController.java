package pl.coderstrust.controller;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Invoice;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getInvoiceById(@PathVariable Long id) {
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

    @GetMapping()
    ResponseEntity<?> getAllInvoices() {
        Collection<Invoice> allInvoices;
        try {
            allInvoices = invoiceService.getAllInvoices();
            return ResponseEntity.status(HttpStatus.OK).body(allInvoices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byDate")
    ResponseEntity<?> getInvoicesByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        if (fromDate == null || toDate == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Collection<Invoice> allInvoicesByDates;
        try {
            allInvoicesByDates = invoiceService.getAllInvoicesByDate(fromDate, toDate);
            return ResponseEntity.status(HttpStatus.OK).body(allInvoicesByDates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byBuyer")
    ResponseEntity<Collection<Invoice>> getAllInvoicesByBuyer(@RequestParam Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Collection<Invoice> allInvoicesByBuyerId;
        try {
            allInvoicesByBuyerId = invoiceService.getAllInvoicesByBuyer(id);
            return ResponseEntity.status(HttpStatus.OK).body(allInvoicesByBuyerId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/bySeller")
    ResponseEntity<Collection<Invoice>> getAllInvoicesBySeller(@RequestParam Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Collection<Invoice> allInvoicesBySellerId;
        try {
            allInvoicesBySellerId = invoiceService.getAllInvoicesBySeller(id);
            return ResponseEntity.status(HttpStatus.OK).body(allInvoicesBySellerId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // TODO: 10/03/2019 not sure if that method should be implemented
    // TODO: 11/03/2019  PUT or POST?
   /* @PutMapping()
    ResponseEntity<?> updateInvoice(@RequestBody Invoice invoice) {
        if (invoice == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            invoiceService.saveInvoice(invoice);
            return ResponseEntity.status(HttpStatus.OK).body(invoice);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteInvoice(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            invoiceService.deleteInvoice(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // TODO: 11/03/2019  what id deleteInvoice called with null id - make sure it will not fallout here to delete all invoices
   /* @DeleteMapping()
    ResponseEntity<?> deleteAllInvoices() {
        try {
            invoiceService.deleteAllInvoices();
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/
    // TODO: 10/03/2019 not sure if that method should be implemented  (see updateInvoice)

    /*
    @PostMapping("/saveInvoice")
    ResponseEntity<Invoice> saveInvoice(@RequestBody Invoice invoice) {
        try {
            invoiceService.saveInvoice(invoice);
            return new ResponseEntity<>(invoice, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }*/
}
