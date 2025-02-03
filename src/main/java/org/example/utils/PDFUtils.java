// src/main/java/org/example/utils/PdfUtils.java
package org.example.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.example.entities.Recomendacion;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class PDFUtils {

    public static void generateRecommendationsPdf(List<Recomendacion> recommendations) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("RecomendacionesPersonalizadas.pdf"));
            document.open();
            document.add(new Paragraph("Recomendaciones Personalizadas"));
            for (Recomendacion recommendation : recommendations) {
                document.add(new Paragraph(recommendation.getDescripcion()));
            }
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}