package com.example.sportmanagerpro.sportnutri.ui.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class EventRow extends HBox {

    public EventRow(String icon, String title, String subtitle, String tag) {
        getStyleClass().add("event-row");
        setPadding(new Insets(12));
        setSpacing(14);

        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("event-icon");

        VBox textBox = new VBox(4);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("event-title");

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.getStyleClass().add("event-subtitle");

        textBox.getChildren().addAll(titleLabel, subtitleLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label tagLabel = new Label(tag);
        tagLabel.getStyleClass().add("event-tag");

        getChildren().addAll(iconLabel, textBox, spacer, tagLabel);
    }
}