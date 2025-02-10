package org.example.utils;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Utility class for displaying help information in different areas.
 * This class provides methods to show help dialogs for habits, footprints, and environmental impact calculations.
 */
public class HelpsUtils {

    /**
     * Displays a help dialog for habits.
     */
    public static void showHabitsHelp() {
        Stage helpStage = new Stage();
        helpStage.setTitle("Ayuda sobre Hábitos");

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setStyle("-fx-padding: 10;");

        Label helpLabel = new Label("Los hábitos son actividades regulares como el uso de transporte, consumo de energía " +
                "o hábitos alimenticios.\n\n" +
                "Los usuarios pueden registrar estas actividades con frecuencias y fechas específicas.\n\n" +
                "Basado en estos hábitos, los usuarios pueden recibir recomendaciones.");
        vbox.getChildren().add(helpLabel);

        Scene scene = new Scene(vbox, 600, 600);
        helpStage.setScene(scene);
        helpStage.show();
    }

    /**
     * Displays a help dialog for footprints.
     */
    public static void showFootprintsHelp() {
        Stage helpStage = new Stage();
        helpStage.setTitle("Ayuda sobre Huellas");

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setStyle("-fx-padding: 10;");

        Label helpLabel = new Label("Las huellas representan el impacto ambiental de las actividades.\n\n" +
                "Cada huella se calcula en base a la actividad y su factor de emisión.\n\n" +
                "Los usuarios pueden ver y gestionar sus huellas para reducir su impacto.");
        vbox.getChildren().add(helpLabel);

        Scene scene = new Scene(vbox, 600, 600);
        helpStage.setScene(scene);
        helpStage.show();
    }

    /**
     * Displays a help dialog for environmental impact calculations.
     */
    public static void showEnvironmentalImpactHelp() {
        Stage helpStage = new Stage();
        helpStage.setTitle("Ayuda sobre Cálculo de Impacto Medioambiental");

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setStyle("-fx-padding: 10;");

        Label helpLabel = new Label("El cálculo del impacto medioambiental se basa en las huellas registradas.\n\n" +
                "Cada huella tiene un valor y un factor de emisión asociado a la actividad.\n\n" +
                "El impacto total se calcula multiplicando el valor de la huella por su factor de emisión y sumando todos los resultados.");
        vbox.getChildren().add(helpLabel);

        Scene scene = new Scene(vbox, 600, 600);
        helpStage.setScene(scene);
        helpStage.show();
    }
}