// src/main/java/org/example/utils/ChartUtils.java
package org.example.utils;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import org.example.entities.Huella;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Utility class for displaying various types of bar charts.
 */
public class ChartUtils {

    /**
     * Displays a bar chart for fingerprints.
     *
     * @param container the VBox container to add the chart to.
     * @param huellas   the list of Huella entities to display.
     */
    public static void showBarChartForFingerprints(VBox container, List<Huella> huellas) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Impacto por Huella");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Huella huella : huellas) {
            BigDecimal factorEmision = BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision());
            BigDecimal impacto = huella.getValor().multiply(factorEmision);
            series.getData().add(new XYChart.Data<>(huella.getIdActividad().getNombre(), impacto));
        }

        barChart.getData().add(series);
        container.getChildren().add(barChart);
    }

    /**
     * Displays a bar chart for categories.
     *
     * @param container         the VBox container to add the chart to.
     * @param categoryImpactMap the map of category names to their impact values.
     */
    public static void showBarChartForCategories(VBox container, Map<String, BigDecimal> categoryImpactMap) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Impacto por Categoría");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, BigDecimal> entry : categoryImpactMap.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);
        container.getChildren().add(barChart);
    }

    /**
     * Displays a monthly impact bar chart.
     *
     * @param container       the VBox container to add the chart to.
     * @param weeklyImpactMap the map of week numbers to their impact values.
     */
    public static void showMonthlyImpactChart(VBox container, Map<Integer, BigDecimal> weeklyImpactMap) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Impacto Mensual");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<Integer, BigDecimal> entry : weeklyImpactMap.entrySet()) {
            series.getData().add(new XYChart.Data<>("Semana " + entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);
        container.getChildren().add(barChart);
    }

    /**
     * Displays a weekly impact bar chart.
     *
     * @param container the VBox container to add the chart to.
     * @param huellas   the list of Huella entities to display.
     */
    public static void showWeeklyImpactChart(VBox container, List<Huella> huellas) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Impacto Semanal");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Huella huella : huellas) {
            BigDecimal factorEmision = BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision());
            BigDecimal impacto = huella.getValor().multiply(factorEmision);
            series.getData().add(new XYChart.Data<>(huella.getIdActividad().getNombre(), impacto));
        }

        barChart.getData().add(series);
        container.getChildren().add(barChart);
    }
}