package org.example.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.example.entities.Recomendacion;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PDFUtils {

    public static void generateRecommendationsPdf(List<Recomendacion> recommendations, String filePath) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            document.add(new Paragraph("Recomendaciones Personalizadas"));
            for (Recomendacion recommendation : recommendations) {
                document.add(new Paragraph(recommendation.getDescripcion()));
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();        } finally {
            document.close();
        }
    }
    public static void saveChartAsPDF(File chartFile, String title, String userName, String userEmail) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        PDImageXObject pdImage = PDImageXObject.createFromFile(chartFile.getAbsolutePath(), document);

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.newLineAtOffset(100, 700);
        contentStream.showText("User: " + userName);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Email: " + userEmail);
        contentStream.endText();

        contentStream.drawImage(pdImage, 100, 400, 400, 300);
        contentStream.close();

        document.save(title + ".pdf");
        document.close();
    }
}