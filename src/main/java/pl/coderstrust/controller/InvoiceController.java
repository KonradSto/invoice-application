package pl.coderstrust.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.InvoiceService;
import pl.coderstrust.utils.ArgumentValidator;

@RestController
@RequestMapping("/invoices")
@Api(value = "/invoices", description = "Available operations for invoice application", tags = {"Invoices"})
public class InvoiceController {

    private InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        ArgumentValidator.ensureNotNull(invoiceService, "invoiceService");
        this.invoiceService = invoiceService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single invoice", notes = "Gets an invoice by id", response = Invoice.class)
    @ApiImplicitParam(name = "id", value = "Only digits possible, e.g. 7565", example = "7865", dataType = "Long")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "Invoice not found for passed id."),
        @ApiResponse(code = 500, message = "Internal server error.")})
    ResponseEntity<?> getInvoiceById(@PathVariable Long id) {
        try {
            Optional<Invoice> invoice = invoiceService.getInvoice(id);
            if (!invoice.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok().body(invoice.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all invoices", response = Invoice.class, responseContainer = "List")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 500, message = "Internal server error.")})
    ResponseEntity<?> getAllInvoices() {
        try {
            Collection<Invoice> invoices = invoiceService.getAllInvoices();
            return ResponseEntity.status(HttpStatus.OK).body(invoices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byDate")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all invoices by dates", notes = "Gets all invoices issued between specified dates (inclusive) fromDate and toDate.", response = Invoice.class, responseContainer = "List")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Passed dates are invalid."),
        @ApiResponse(code = 500, message = "Internal server error.")})
    ResponseEntity<?> getInvoicesByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        if (fromDate == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fromDate parameter cannot be null.");
        }
        if (toDate == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("toDate parameter cannot be null.");
        }
        if (fromDate.isAfter(toDate)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fromDate cannot be after toDate.");
        }
        try {
            Collection<Invoice> invoices = invoiceService.getAllInvoicesByDate(fromDate, toDate);
            return ResponseEntity.status(HttpStatus.OK).body(invoices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byBuyer")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all invoices by buyer", notes = "Gets all invoices issued to specified buyer.", response = Invoice.class, responseContainer = "List")
    @ApiImplicitParam(name = "id", value = "Only digits possible, e.g. 7565", example = "7865", dataType = "Long")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Passed buyer id is invalid."),
        @ApiResponse(code = 500, message = "Internal server error.")})
    ResponseEntity<?> getInvoicesByBuyer(@RequestParam Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id cannot be null.");
        }
        try {
            Collection<Invoice> invoices = invoiceService.getAllInvoicesByBuyer(id);
            return ResponseEntity.status(HttpStatus.OK).body(invoices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/bySeller")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all invoices by seller", notes = "Gets all invoices issued to specified seller.",response = Invoice.class, responseContainer = "List")
    @ApiImplicitParam(name = "id", value = "Only digits possible, e.g. 7565", example = "7865", dataType = "Long")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Passed seller id is invalid."),
        @ApiResponse(code = 500, message = "Internal server error.")})
    ResponseEntity<?> getInvoicesBySeller(@RequestParam Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id cannot be null.");
        }
        try {
            Collection<Invoice> invoices = invoiceService.getAllInvoicesBySeller(id);
            return ResponseEntity.status(HttpStatus.OK).body(invoices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete an invoice by id", notes = "Deletes invoice by specified id from database.")
    @ApiImplicitParam(name = "id", value = "Only digits possible, e.g. 7565", example = "7865", dataType = "Long")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "Invoice not found for passed id."),
        @ApiResponse(code = 500, message = "Internal server error.")})
    ResponseEntity<?> deleteInvoice(@PathVariable Long id) {
        try {
            Optional<Invoice> invoice = invoiceService.getInvoice(id);
            if (!invoice.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            invoiceService.deleteInvoice(id);
            return ResponseEntity.status(HttpStatus.OK).body(invoice.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete ALL invoices", notes = "WARNING!!! This operation deletes ALL available invoices from database.")
    @ApiResponses({
        @ApiResponse(code = 204, message = "OK"),
        @ApiResponse(code = 500, message = "Internal server error.")})
    ResponseEntity<?> deleteAllInvoices() {
        try {
            invoiceService.deleteAllInvoices();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "When invoice id field is not set application saves the invoice to database as new invoice, otherwise updates existing invoice.", response = Invoice.class)
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Passed invoice is invalid."),
        @ApiResponse(code = 500, message = "Internal server error.")})
    ResponseEntity<?> saveInvoice(@RequestBody(required = false) Invoice invoice) {
        if (invoice == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invoice cannot be null.");
        }
        try {
            Invoice savedInvoice = invoiceService.saveInvoice(invoice);
            return ResponseEntity.status(HttpStatus.OK).body(savedInvoice);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
