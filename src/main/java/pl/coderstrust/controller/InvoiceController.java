package pl.coderstrust.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import pl.coderstrust.service.InvoiceEmailService;
import pl.coderstrust.service.InvoicePdfService;
import pl.coderstrust.service.InvoiceService;
import pl.coderstrust.utils.ArgumentValidator;

@RestController
@RequestMapping("/invoices")
@Api(value = "/invoices", description = "Available operations for invoice application", tags = {"Invoices"})
public class InvoiceController {

    private static Logger log = LoggerFactory.getLogger(InvoiceController.class);
    private InvoiceService invoiceService;
    private InvoicePdfService invoicePdfService;
    private InvoiceEmailService invoiceEmailService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService, InvoicePdfService invoicePdfService, InvoiceEmailService invoiceEmailService) {
        log.debug("Launching to REST service");
        ArgumentValidator.ensureNotNull(invoiceService, "invoiceService");
        ArgumentValidator.ensureNotNull(invoicePdfService, "invoicePdfService");
        ArgumentValidator.ensureNotNull(invoiceEmailService, "invoiceEmailService");
        this.invoiceService = invoiceService;
        this.invoicePdfService = invoicePdfService;
        this.invoiceEmailService = invoiceEmailService;
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
            log.debug("Getting an invoice by id: {}", id);
            Optional<Invoice> invoice = invoiceService.getInvoice(id);
            if (!invoice.isPresent()) {
                log.error("An error occurred during getting an invoice, invoice not found for passed id: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok().body(invoice.get());
        } catch (Exception e) {
            log.error("An error occurred during getting invoice.", e);
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
            log.debug("Getting all invoices");
            Collection<Invoice> invoices = invoiceService.getAllInvoices();
            return ResponseEntity.status(HttpStatus.OK).body(invoices);
        } catch (Exception e) {
            log.error("An error occurred during getting all invoices.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byDate")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all invoices by dates", notes = "Gets all invoices issued between specified dates (inclusive) fromDate and toDate.", response = Invoice.class, responseContainer = "List")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "fromDate", value = "YYYY-MM-DD", example = "2019-02-04", dataType = "date"),
        @ApiImplicitParam(name = "toDate", value = "YYYY-MM-DD", example = "2019-03-04", dataType = "date")})
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Passed dates are invalid."),
        @ApiResponse(code = 500, message = "Internal server error.")})
    ResponseEntity<?> getInvoicesByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        if (fromDate == null) {
            String message = "fromDate parameter cannot be null.";
            log.error(message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if (toDate == null) {
            String message = "toDate parameter cannot be null.";
            log.error(message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if (fromDate.isAfter(toDate)) {
            String message = "fromDate cannot be after toDate.";
            log.error(message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        try {
            log.debug("Getting all invoices by dates: from {} to {}", fromDate, toDate);
            Collection<Invoice> invoices = invoiceService.getAllInvoicesByDate(fromDate, toDate);
            return ResponseEntity.status(HttpStatus.OK).body(invoices);
        } catch (Exception e) {
            log.error("An error occurred during getting all invoices by dates.", e);
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
            String message = "Buyer id cannot be null.";
            log.error(message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        try {
            log.debug("Getting all invoices by buyer: {}", id);
            Collection<Invoice> invoices = invoiceService.getAllInvoicesByBuyer(id);
            return ResponseEntity.status(HttpStatus.OK).body(invoices);
        } catch (Exception e) {
            log.error("An error occurred during getting invoices by buyer.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/bySeller")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all invoices by seller", notes = "Gets all invoices issued to specified seller.", response = Invoice.class, responseContainer = "List")
    @ApiImplicitParam(name = "id", value = "Only digits possible, e.g. 7565", example = "7865", dataType = "Long")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Passed seller id is invalid."),
        @ApiResponse(code = 500, message = "Internal server error.")})
    ResponseEntity<?> getInvoicesBySeller(@RequestParam Long id) {
        if (id == null) {
            String message = "Seller id cannot be null.";
            log.error(message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        try {
            log.debug("Getting all invoices by seller: {}", id);
            Collection<Invoice> invoices = invoiceService.getAllInvoicesBySeller(id);
            return ResponseEntity.status(HttpStatus.OK).body(invoices);
        } catch (Exception e) {
            log.error("An error occurred during getting invoices by seller.", e);
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
            log.debug("Deleting invoice by id: {}", id);
            Optional<Invoice> invoice = invoiceService.getInvoice(id);
            if (!invoice.isPresent()) {
                log.error("An error occurred during deleting an invoice, invoice not found for passed id: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            invoiceService.deleteInvoice(id);
            return ResponseEntity.status(HttpStatus.OK).body(invoice.get());
        } catch (Exception e) {
            log.error("An error occurred during deleting an invoice.", e);
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
            log.debug("Deleting all invoices");
            invoiceService.deleteAllInvoices();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            log.error("An error occurred during deleting all invoices.", e);
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
            String message = "Invoice cannot be null.";
            log.error(message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        try {
            log.debug("Saving invoice: {}", invoice);
            Invoice savedInvoice = invoiceService.saveInvoice(invoice);
            log.debug("Sending an e-mail with invoice: {}", savedInvoice);
            invoiceEmailService.sendEmailWithInvoice(savedInvoice);
            return ResponseEntity.status(HttpStatus.OK).body(savedInvoice);
        } catch (Exception e) {
            log.error("An error occurred during saving an invoice.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/pdf/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get a single invoice as PDF file.", notes = "Gets a PDF file of invoice by given id", response = Invoice.class)
    @ApiImplicitParam(name = "id", value = "Only digits possible, e.g. 7565", example = "7865", dataType = "Long")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 404, message = "Invoice not found for passed id."),
        @ApiResponse(code = 500, message = "Internal server error.")})
    ResponseEntity<?> getInvoiceAsPdf(@PathVariable Long id) {
        try {
            log.debug("Getting an invoice as PDF by id: {}", id);
            Optional<Invoice> invoice = invoiceService.getInvoice(id);
            if (!invoice.isPresent()) {
                log.error("An error occurred during getting an invoice as PDF, invoice not found for passed id: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            byte[] invoiceAsPdf = invoicePdfService.getInvoiceAsPdf(invoice.get());
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.APPLICATION_PDF);
            return ResponseEntity.ok().headers(responseHeaders).body(invoiceAsPdf);
        } catch (Exception e) {
            log.error("An error occurred during getting invoice as PDF.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
