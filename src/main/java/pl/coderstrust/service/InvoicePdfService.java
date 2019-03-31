package pl.coderstrust.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import pl.coderstrust.model.Company;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.InvoiceEntry;
import pl.coderstrust.model.Vat;


@Service
public class InvoicePdfService {

    InvoicePdfService() {
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
            addInvoiceInformation(invoicePdf, invoice);
            addSellerAndBuyerSegment(invoicePdf, invoice);
            addEntriesPart(invoicePdf, invoice);
            invoicePdf.close();
        } catch (DocumentException ex) {
            throw new ServiceOperationException("An error occurred during getting all invoices.", ex);
        }
        return baos.toByteArray();
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
        paragraph.setAlignment(Element.ALIGN_CENTER);
        invoicePdf.add(paragraph);
    }

    private static void addSellerAndBuyerSegment(Document invoicePdf, Invoice invoice) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        addEmptyLine(invoicePdf, paragraph, 2);
        PdfPTable table = new PdfPTable(2);
        setNewTableFormat(table, 100, new int[]{5, 5});
        table.setWidthPercentage(100);
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

    private static void addEntriesPart(Document invoicePdf, Invoice invoice) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        addEmptyLine(invoicePdf, paragraph, 2);
        initializeTableHeader(invoicePdf);
        addEntries(invoicePdf, invoice.getEntries());
    }

    private static PdfPCell addCompanyInformation(Company company) {
        List companyInformationAsList = new List();
        companyInformationAsList.add(new ListItem(" Name: " + company.getName(), smallBold));
        companyInformationAsList.add(new ListItem(" TaxId: " + company.getTaxId(), smallBold));
        companyInformationAsList.add(new ListItem(" Address: " + company.getAddress(), smallBold));
        companyInformationAsList.add(new ListItem(" Phone number: " + company.getPhoneNumber(), smallBold));
        companyInformationAsList.add(new ListItem(" Email: " + company.getEmail(), smallBold));
        companyInformationAsList.add(new ListItem(" Account number: " + company.getAccountNumber(), smallBold));
        PdfPCell companyInformation = new PdfPCell();
        companyInformation.addElement(companyInformationAsList);
        companyInformation.setBorder(Rectangle.NO_BORDER);
        return companyInformation;
    }

    private static void initializeTableHeader(Document invoicePdf) throws DocumentException {
        PdfPTable table = new PdfPTable(8);
        setNewTableFormat(table, 100, new int[]{1, 2, 1, 1, 1, 1, 1, 1});
        table.addCell("Id");
        table.addCell("Product name");
        table.addCell("Quantity");
        table.addCell("Unit");
        table.addCell("Price");
        table.addCell("VAT rate");
        table.addCell("Nett value");
        table.addCell("Gross value");
        invoicePdf.add(table);
    }

    private static void addEntries(Document invoicePdf, java.util.List<InvoiceEntry> entries) throws DocumentException {
        BigDecimal totalNettValue = new BigDecimal(0);
        BigDecimal totalGrossValue = new BigDecimal(0);
        for (InvoiceEntry entry : entries) {
            PdfPTable table = new PdfPTable(8);
            setNewTableFormat(table, 100, new int[]{1, 2, 1, 1, 1, 1, 1, 1});
            table.addCell(String.valueOf(entry.getId()));
            table.addCell(entry.getProductName());
            table.addCell(String.valueOf(entry.getQuantity()));
            table.addCell(String.valueOf(entry.getUnit()));
            table.addCell(String.valueOf(entry.getPrice()));
            Vat vatRate = entry.getVatRate();
            table.addCell(vatRate.getValue() * 100 + "%");
            table.addCell(String.valueOf(entry.getNettValue()));
            table.addCell(String.valueOf(entry.getGrossValue()));
            invoicePdf.add(table);
            totalNettValue = totalNettValue.add(entry.getNettValue());
            totalGrossValue = totalGrossValue.add(entry.getGrossValue());
        }
        addEntriesSummary(invoicePdf, totalNettValue, totalGrossValue);
    }

    private static void addEntriesSummary(Document invoicePdf, BigDecimal totalNettValue, BigDecimal totalGrossValue) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        setNewTableFormat(table, 100, new int[]{1, 6, 1, 1});
        table.addCell(new Phrase("Total:", smallBold));
        table.addCell("");
        table.addCell(new Phrase(String.valueOf(totalNettValue), smallBold));
        table.addCell(new Phrase(String.valueOf(totalGrossValue), smallBold));
        invoicePdf.add(table);
    }

    private static void setNewTableFormat(PdfPTable table, int widthPercentage, int[] spacing) throws DocumentException {
        table.setWidths(spacing);
        table.setWidthPercentage(widthPercentage);
    }

    private static void addEmptyLine(Document invoicePdf, Paragraph paragraph, int number) throws DocumentException {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
        invoicePdf.add(paragraph);
    }

    private static Font bigBold = new Font(Font.FontFamily.TIMES_ROMAN, 16,
        Font.BOLD);

    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 13,
        Font.BOLD);
}
