package pl.coderstrust.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Optional;

import com.itextpdf.text.Document;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;

@Service
public class InvoicePdfService {

    public byte[] getInvoiceAsPdf(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        Document invoicePdf = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();


        return baos.toByteArray();
    }

    private static void addMetaData(Document invoicePdf, Invoice invoice) {
        invoicePdf.addTitle("Invoice id: " + invoice.getId());
    }

    private static void addSeller(Document invoicePdf, Invoice invoice) {

    }

    private static void addBuyer(Document invoicePdf, Invoice invoice) {

    }

    private static void addEntries(Document invoicePdf, Invoice invoice) {

    }

}
