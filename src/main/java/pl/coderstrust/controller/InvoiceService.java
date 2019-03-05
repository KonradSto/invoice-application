package pl.coderstrust.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;

public interface InvoiceService {


    default List<Invoice> getAllInvoices() throws DatabaseOperationException {
        return Collections.emptyList();
    }

    default List getAllInvoices(LocalDate fromDate, LocalDate toDate) throws DatabaseOperationException {
        return Collections.emptyList();
    }

    default List getAllInvoices(Company company) throws DatabaseOperationException {
        return Collections.emptyList();
    }

    default Invoice getInvoice(Long id) throws DatabaseOperationException {
        return new Invoice(null, null, null, null, null, null, null);
    }

    default Invoice addInvoice(Invoice invoice) throws DatabaseOperationException {
        return new Invoice(null, null, null, null, null, null, null);
    }

    default Invoice updateInvoice(Long id) {
        return new Invoice(null, null, null, null, null, null, null);
    }

    default Invoice deleteInvoice(Long id) {
        return new Invoice(null, null, null, null, null, null, null);
    }

    default Invoice deleteAllInvoices() {
        return new Invoice(null, null, null, null, null, null, null);
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
