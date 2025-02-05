package org.example.utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationUtil {

    public static void addHoverAnimation(Node node) {
        node.setOnMouseEntered(event -> {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(node.scaleXProperty(), 1),
                            new KeyValue(node.scaleYProperty(), 1)
                    ),
                    new KeyFrame(Duration.seconds(0.5),
                            new KeyValue(node.scaleXProperty(), 1.1),
                            new KeyValue(node.scaleYProperty(), 1.1)
                    )
            );
            timeline.play();
        });

        node.setOnMouseExited(event -> {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(node.scaleXProperty(), 1.1),
                            new KeyValue(node.scaleYProperty(), 1.1)
                    ),
                    new KeyFrame(Duration.seconds(0.5),
                            new KeyValue(node.scaleXProperty(), 1),
                            new KeyValue(node.scaleYProperty(), 1)
                    )
            );
            timeline.play();
        });
    }
}