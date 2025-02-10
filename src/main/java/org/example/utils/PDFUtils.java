package org.example.utils;

import org.example.entities.Habito;
import org.example.entities.Huella;
import org.example.entities.Recomendacion;
import org.example.entities.Usuario;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Utility class for generating PDF documents.
 */
public class PDFUtils {

    /**
     * Generates a PDF document with user recommendations, fingerprints, and habits.
     *
     * @param user the user for whom the PDF is generated.
     * @param recommendations the list of recommendations.
     * @param fingerprints the list of fingerprints.
     * @param habits the list of habits.
     * @param filePath the file path where the PDF will be saved.
     * @throws FileNotFoundException if the file path is invalid.
     * @throws DocumentException if there is an error in the document creation.
     */
    public static void generateRecommendationsPdf(Usuario user, List<Recomendacion> recommendations, List<Huella> fingerprints, List<Habito> habits, String filePath) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        document.add(new Paragraph("Usuario: " + user.getNombre()));

        document.add(new Paragraph("Recomendaciones:"));
        addRecommendationsTable(document, recommendations);
        document.add(new Paragraph("Huellas:"));
        addFingerprintsTable(document, fingerprints);
        document.add(new Paragraph("Hábitos:"));
        addHabitsTable(document, habits);

        document.close();
    }

    /**
     * Adds a table of recommendations to the PDF document.
     *
     * @param document the PDF document.
     * @param recommendations the list of recommendations.
     * @throws DocumentException if there is an error in adding the table.
     */
    private static void addRecommendationsTable(Document document, List<Recomendacion> recommendations) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.addCell("Descripcion");
        table.addCell("Impacto estimado");

        for (Recomendacion recommendation : recommendations) {
            table.addCell(recommendation.getDescripcion());
            table.addCell(String.valueOf(recommendation.getImpactoEstimado()));
        }

        document.add(table);
    }

    /**
     * Adds a table of fingerprints to the PDF document.
     *
     * @param document the PDF document.
     * @param fingerprints the list of fingerprints.
     * @throws DocumentException if there is an error in adding the table.
     */
    private static void addFingerprintsTable(Document document, List<Huella> fingerprints) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.addCell("Actividad");
        table.addCell("Valor");
        table.addCell("Fecha");

        for (Huella fingerprint : fingerprints) {
            table.addCell(fingerprint.getIdActividad().getNombre());
            table.addCell(fingerprint.getValor().toString());
            table.addCell(fingerprint.getFecha().toString());
        }

        document.add(table);
    }

    /**
     * Adds a table of habits to the PDF document.
     *
     * @param document the PDF document.
     * @param habits the list of habits.
     * @throws DocumentException if there is an error in adding the table.
     */
    private static void addHabitsTable(Document document, List<Habito> habits) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.addCell("Actividades");
        table.addCell("Frecuencia");
        table.addCell("Tipo");

        for (Habito habit : habits) {
            table.addCell(habit.getIdActividad().getNombre());
            table.addCell(habit.getFrecuencia());
            table.addCell(habit.getTipo());
        }

        document.add(table);
    }
}