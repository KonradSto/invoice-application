package pl.coderstrust.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
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
            addSellerAndBuyerInformation(invoicePdf, invoice);
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
        Paragraph paragraph;
        paragraph = new Paragraph("Issue date: " + invoice.getIssuedDate(), smallBold);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        invoicePdf.add(paragraph);
        paragraph = new Paragraph("Due date: " + invoice.getDueDate(), smallBold);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        invoicePdf.add(paragraph);
        paragraph = new Paragraph("Invoice number: " + invoice.getNumber(), subFont);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        invoicePdf.add(paragraph);
/*        Paragraph issueDate = new Paragraph();
        Paragraph dueDate = new Paragraph();
        Paragraph invoiceNumber = new Paragraph();
        issueDate.setAlignment(Element.ALIGN_RIGHT);
        dueDate.setAlignment(Element.ALIGN_RIGHT);
        invoiceNumber.setAlignment(Element.ALIGN_CENTER);
        issueDate.add(new Chunk("Issue date: " + invoice.getIssuedDate(), smallBold));
        dueDate.add(new Chunk("Due date: " + invoice.getDueDate(), smallBold));
        invoiceNumber.add(new Chunk("Invoice number: " + invoice.getNumber(), subFont));
        invoicePdf.add(issueDate);
        invoicePdf.add(dueDate);
        invoicePdf.add(invoiceNumber);*/
    }

    private static void addSellerAndBuyerInformation(Document invoicePdf, Invoice invoice) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        addEmptyLine(invoicePdf, paragraph, 2);
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        PdfPCell seller = new PdfPCell(new Phrase("Seller: ", subFont));
        seller.setBorder(Rectangle.NO_BORDER);
        table.addCell(seller);
        PdfPCell buyer = new PdfPCell(new Phrase("Buyer: ", subFont));
        buyer.setBorder(Rectangle.NO_BORDER);
        table.addCell(buyer);
        invoicePdf.add(table);
    }

    private static void addBuyer(Document invoicePdf, Invoice invoice) {

    }

    private static void addEntries(Document invoicePdf, Invoice invoice) {

    }

    private static void addEmptyLine(Document invoicePdf, Paragraph paragraph, int number) throws DocumentException {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
        invoicePdf.add(paragraph);
    }

    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
        Font.BOLD);

    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
        Font.BOLD);
}
