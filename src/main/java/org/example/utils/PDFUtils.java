package org.example.utils;

import org.example.entities.Recomendacion;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

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
}