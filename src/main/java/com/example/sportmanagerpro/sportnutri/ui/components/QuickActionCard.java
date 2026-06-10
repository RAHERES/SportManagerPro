package com.example.sportmanagerpro.sportnutri.ui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class QuickActionCard extends VBox {

    public QuickActionCard(String icon, String title, String colorClass) {
        getStyleClass().addAll("quick-card", colorClass);
        setAlignment(Pos.CENTER);
        setSpacing(10);
        setPrefSize(145, 110);

        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("quick-icon");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("quick-title");

        getChildren().addAll(iconLabel, titleLabel);
    }
}