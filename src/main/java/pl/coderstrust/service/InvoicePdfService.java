package pl.coderstrust.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import pl.coderstrust.model.Invoice;


@Service
public class InvoicePdfService {

    public InvoicePdfService() {
    }

    public byte[] getInvoiceAsPdf(Invoice invoice) throws ServiceOperationException {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice cannot be null");
        }
        Document invoicePdf = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(invoicePdf, baos);
            invoicePdf.open();
            addMetaData(invoicePdf, invoice);
            addInvoiceInformation(invoicePdf, invoice);
            addSeller(invoicePdf, invoice);
            addBuyer(invoicePdf, invoice);
            addEntries(invoicePdf, invoice);
            invoicePdf.close();
        } catch (DocumentException ex) {
            throw new ServiceOperationException("An error occurred during getting all invoices.", ex);
        }
        return baos.toByteArray();
    }

    private static void addMetaData(Document invoicePdf, Invoice invoice) {
        invoicePdf.addTitle("Invoice id: " + invoice.getId());
    }

    private void addInvoiceInformation(Document invoicePdf, Invoice invoice) throws DocumentException {
        Paragraph preface1 = new Paragraph();
        Paragraph preface2 = new Paragraph();
        preface1.setAlignment(Element.ALIGN_RIGHT);
        preface2.setAlignment(Element.ALIGN_CENTER);
        preface1.add(new Chunk("Issue date: " + invoice.getIssuedDate(), smallBold));
        preface1.add(new Chunk("Due date: " + invoice.getDueDate(), smallBold));
        preface2.add(new Chunk("Invoice number: " + invoice.getNumber(), subFont));
        invoicePdf.add(preface1);
        invoicePdf.add(preface2);
    }

    private static void addSeller(Document invoicePdf, Invoice invoice) {
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 2);
    }

    private static void addBuyer(Document invoicePdf, Invoice invoice) {

    }

    private static void addEntries(Document invoicePdf, Invoice invoice) {

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(""));
        }
    }

    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
        Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
        Font.BOLD);

}
