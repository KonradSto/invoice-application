package pl.coderstrust.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.Vat;
import pl.coderstrust.utils.ArgumentValidator;


@Service
public class InvoicePdfService {

    InvoicePdfService() throws IOException, DocumentException {
    }

    public byte[] getInvoiceAsPdf(Invoice invoice) throws ServiceOperationException {
        ArgumentValidator.ensureNotNull(invoice, "Invoice cannot be null");
        Document invoicePdf = new Document();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(invoicePdf, stream);
            invoicePdf.open();
            addInvoiceInformation(invoicePdf, invoice);
            addSellerAndBuyerSection(invoicePdf, invoice);
            addEntriesSection(invoicePdf, invoice);
            invoicePdf.close();
        } catch (DocumentException ex) {
            throw new ServiceOperationException("An error occurred during getting PDF file of invoice", ex);
        }
        return stream.toByteArray();
    }

    private void addInvoiceInformation(Document invoicePdf, Invoice invoice) throws DocumentException {
        Paragraph paragraph;
        paragraph = new Paragraph("Issue date: " + invoice.getIssuedDate(), smallBold);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        invoicePdf.add(paragraph);
        paragraph = new Paragraph("Due date: " + invoice.getDueDate(), smallBold);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        invoicePdf.add(paragraph);
        paragraph = new Paragraph("Invoice number: " + invoice.getNumber(), bigBold);
        invoicePdf.add(paragraph);
    }

    private void addSellerAndBuyerSection(Document invoicePdf, Invoice invoice) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        addEmptyLine(invoicePdf, paragraph, 2);
        PdfPTable table = new PdfPTable(2);
        setNewTableFormat(table, 100, new int[]{5, 5});
        PdfPCell seller = new PdfPCell(new Phrase("Seller:", bigBold));
        seller.setBorder(Rectangle.NO_BORDER);
        table.addCell(seller);
        PdfPCell buyer = new PdfPCell(new Phrase("Buyer:", bigBold));
        buyer.setBorder(Rectangle.NO_BORDER);
        table.addCell(buyer);
        table.addCell(addCompanyInformation(invoice.getSeller()));
        table.addCell(addCompanyInformation(invoice.getBuyer()));
        invoicePdf.add(table);
    }

    private void addEntriesSection(Document invoicePdf, Invoice invoice) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        addEmptyLine(invoicePdf, paragraph, 2);
        initializeTableHeader(invoicePdf);
        addEntries(invoicePdf, invoice.getEntries());
    }

    private PdfPCell addCompanyInformation(Company company) {
        PdfPCell companyInformation = new PdfPCell();
        companyInformation.addElement(new Phrase(" Name: " + company.getName(), smallBold));
        companyInformation.addElement(new Phrase(" TaxId: " + company.getTaxId(), smallBold));
        companyInformation.addElement(new Phrase(" Address: " + company.getAddress(), smallBold));
        companyInformation.addElement(new Phrase(" Phone number: " + company.getPhoneNumber(), smallBold));
        companyInformation.addElement(new Phrase(" Email: " + company.getEmail(), smallBold));
        companyInformation.addElement(new Phrase(" Account number: " + company.getAccountNumber(), smallBold));
        companyInformation.setBorder(Rectangle.NO_BORDER);
        return companyInformation;
    }

    private void initializeTableHeader(Document invoicePdf) throws DocumentException {
        PdfPTable table = new PdfPTable(7);
        setNewTableFormat(table, 100, new int[]{2, 1, 1, 1, 1, 1, 1});
        table.addCell("Product name");
        table.addCell("Quantity");
        table.addCell("Unit");
        table.addCell("Price");
        table.addCell("VAT");
        table.addCell("Nett value");
        table.addCell("Gross value");
        invoicePdf.add(table);
    }

    private void addEntries(Document invoicePdf, java.util.List<InvoiceEntry> entries) throws DocumentException {
        BigDecimal totalNettValue = new BigDecimal(0);
        BigDecimal totalGrossValue = new BigDecimal(0);
        for (InvoiceEntry entry : entries) {
            PdfPTable table = new PdfPTable(7);
            setNewTableFormat(table, 100, new int[]{2, 1, 1, 1, 1, 1, 1});
            table.addCell(new Phrase(entry.getProductName(), smallBold));
            table.addCell(new Phrase(String.valueOf(entry.getQuantity()), smallBold));
            table.addCell(new Phrase(String.valueOf(entry.getUnit()), smallBold));
            table.addCell(new Phrase(entry.getPrice() + " zł", smallBold));
            Vat vatRate = entry.getVatRate();
            table.addCell(new Phrase(vatRate.getValue() * 100 + "%", smallBold));
            table.addCell(new Phrase(entry.getNettValue() + " zł", smallBold));
            table.addCell(new Phrase(entry.getGrossValue() + " zł", smallBold));
            invoicePdf.add(table);
            totalNettValue = totalNettValue.add(entry.getNettValue());
            totalGrossValue = totalGrossValue.add(entry.getGrossValue());
        }
        addEntriesSummary(invoicePdf, totalNettValue, totalGrossValue);
    }

    private void addEntriesSummary(Document invoicePdf, BigDecimal totalNettValue, BigDecimal totalGrossValue) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        setNewTableFormat(table, 100, new int[]{6, 1, 1});
        table.addCell(new Phrase("Total:", smallBold));
        table.addCell(new Phrase(totalNettValue + " zł", smallBold));
        table.addCell(new Phrase(totalGrossValue + " zł", smallBold));
        invoicePdf.add(table);
    }

    private void setNewTableFormat(PdfPTable table, int widthPercentage, int[] spacing) throws DocumentException {
        table.setWidths(spacing);
        table.setWidthPercentage(widthPercentage);
    }

    private void addEmptyLine(Document invoicePdf, Paragraph paragraph, int number) throws DocumentException {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
        invoicePdf.add(paragraph);
    }

    private BaseFont polishCharTimesRoman = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1250, BaseFont.EMBEDDED);

    private Font bigBold = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);

    private Font smallBold = new Font(polishCharTimesRoman, 12, Font.BOLD);
}
