package util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

public class PDFGenerator {

    private static final String ROOT_FOLDER = "PDF_Invoices";

    public static void generateInvoice(InvoiceInstance invoice) {
        try {
            // Ensure the root folder exists
            File rootDir = new File(ROOT_FOLDER);
            if (!rootDir.exists()) rootDir.mkdirs();

            // Create sub-folder for the activity
            String activityFolderName = invoice.getActivity().replaceAll("[^a-zA-Z0-9\\s]", "").replace(" ", "_");
            File activityDir = new File(rootDir, activityFolderName);
            if (!activityDir.exists()) activityDir.mkdirs();

            // Create a descriptive PDF file name
            String fileName = invoice.getSponsorName().replaceAll("[^a-zA-Z0-9\\s]", "").replace(" ", "_") +
                    "_" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".pdf";

            File pdfFile = new File(activityDir, fileName);

            // Setup PDF writer
            PdfWriter writer = new PdfWriter(pdfFile.getAbsolutePath());
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Title
            Paragraph title = new Paragraph("INVOICE")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(24)
                    .setBold();
            document.add(title);

            document.add(new Paragraph("\n"));

            // Invoice Header Details
            Table headerTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                    .useAllAvailableWidth();

            headerTable.addCell(new Cell()
                    .add(new Paragraph("Invoice ID: " + invoice.getInvoiceId()))
                    .setBorder(null)
                    .setTextAlignment(TextAlignment.LEFT));

            headerTable.addCell(new Cell()
                    .add(new Paragraph("Date: " + invoice.getIssueDate()))
                    .setBorder(null)
                    .setTextAlignment(TextAlignment.RIGHT));

            document.add(headerTable);

            document.add(new Paragraph("Concept: Sponsorship for \"" + invoice.getActivity() + "\"\n"));

            // Sponsor Details
            document.add(new Paragraph("Sponsor Information").setBold().setFontSize(14));
            document.add(new Paragraph(invoice.getSponsorName()));
            document.add(new Paragraph(invoice.getSponsorAddress()));
            document.add(new Paragraph("VAT Number: " + invoice.getSponsorVAT()));
            document.add(new Paragraph("\n"));

            // Recipient Details
            document.add(new Paragraph("Recipient Information").setBold().setFontSize(14));
            document.add(new Paragraph(invoice.getRecipientName()));
            document.add(new Paragraph(invoice.getRecipientAddress()));
            document.add(new Paragraph("VAT Number: " + invoice.getRecipientVAT()));
            document.add(new Paragraph("\n"));

            // Amount Breakdown Table
            document.add(new Paragraph("Amount Summary").setBold().setFontSize(14));
            Table amountTable = new Table(UnitValue.createPercentArray(new float[]{70, 30}))
                    .useAllAvailableWidth();

            amountTable.addHeaderCell(new Cell()
                    .add(new Paragraph("Description")).setBold()
                    .setTextAlignment(TextAlignment.CENTER));
            amountTable.addHeaderCell(new Cell()
                    .add(new Paragraph("Amount (€)")).setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            amountTable.addCell(new Cell()
                    .add(new Paragraph("Subtotal (before tax)"))
                    .setTextAlignment(TextAlignment.LEFT));
            amountTable.addCell(new Cell()
                    .add(new Paragraph(String.format("%.2f", invoice.getSubtotal())))
                    .setTextAlignment(TextAlignment.RIGHT));

            amountTable.addCell(new Cell()
                    .add(new Paragraph(String.format("Tax (%.0f%%)", invoice.getTaxRate())))
                    .setTextAlignment(TextAlignment.LEFT));
            amountTable.addCell(new Cell()
                    .add(new Paragraph(String.format("%.2f", invoice.getTaxAmount())))
                    .setTextAlignment(TextAlignment.RIGHT));

            amountTable.addCell(new Cell()
                    .add(new Paragraph("Total")).setBold()
                    .setTextAlignment(TextAlignment.LEFT));
            amountTable.addCell(new Cell()
                    .add(new Paragraph(String.format("%.2f", invoice.getTotal()))).setBold()
                    .setTextAlignment(TextAlignment.RIGHT));


            document.add(amountTable);

            // Footer
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Please note that this invoice is issued for billing purposes only and does not serve as proof of payment.").setItalic().setFontSize(10));

            // Close the document
            document.close();

            System.out.println("Invoice PDF generated at: " + pdfFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error generating PDF: " + e.getMessage());
        }
    }
}