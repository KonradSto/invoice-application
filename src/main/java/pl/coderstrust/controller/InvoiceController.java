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
@RequestMapping("/invoice")
public class InvoiceController {

    private InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    // TODO: 01/03/2019 what to do with exceptions?
    @GetMapping("/invoice")
    ResponseEntity<List<Invoice>> allInvoices() {
        List<Invoice> allInvoices;
        try {
            allInvoices = invoiceService.getAllInvoices();
            return new ResponseEntity<>(allInvoices, HttpStatus.OK);
        } catch (DatabaseOperationException e) {                // TODO: 05/03/2019 at the current Application state this will not happen (maybe can happen when real DB implementation is injected
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{company}")
    List<Invoice> getInvoicesByDateRange(@PathVariable LocalDate fromDate, LocalDate toDate) throws DatabaseOperationException {
        return invoiceService.getAllInvoices(fromDate, toDate);
    }

    @GetMapping("/{company}")
    List<Invoice> getInvoicesByCompany(@PathVariable Company company) throws DatabaseOperationException {
        return invoiceService.getAllInvoices(company);
    }

    @GetMapping("/{id}")
    Invoice getInvoiceById(@PathVariable Long id) throws DatabaseOperationException {
        return invoiceService.getInvoice(id);
    }

    @PostMapping()
    Invoice addInvoice(@RequestBody Invoice invoice) throws DatabaseOperationException {
        return invoiceService.addInvoice(invoice);
    }

    @PostMapping()
    Invoice updateInvoice(@RequestBody Invoice invoice) throws DatabaseOperationException {
        return invoiceService.updateInvoice(invoice.getId());
    }
}

 /* @GetMapping("/{id}")
    public ResponseEntity<?> getBazz(@PathVariable String id){
        return new ResponseEntity<>(new Bazz(id, "Bazz"+id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBazz(@PathVariable String id){
        return new ResponseEntity<>(new Bazz(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> newBazz(@RequestParam("name") String name){
        return new ResponseEntity<>(new Bazz("5", name), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBazz(
        @PathVariable String id,
        @RequestParam("name") String name) {
        return new ResponseEntity<>(new Bazz(id, name), HttpStatus.OK);
    }*/
