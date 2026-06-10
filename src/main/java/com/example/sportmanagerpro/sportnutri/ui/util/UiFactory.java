package com.example.sportmanagerpro.sportnutri.ui.util;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public final class UiFactory {

    private UiFactory() {
    }

    public static VBox panel(String title) {
        VBox panel = new VBox(8);
        panel.getStyleClass().add("panel");
        panel.setPadding(new Insets(18));

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("panel-title");

        panel.getChildren().add(titleLabel);
        return panel;
    }

    public static Label link(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("panel-link");
        return label;
    }
}