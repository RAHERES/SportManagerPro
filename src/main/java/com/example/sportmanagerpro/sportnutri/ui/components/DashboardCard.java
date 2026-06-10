package com.example.sportmanagerpro.sportnutri.ui.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class DashboardCard extends HBox {

    public DashboardCard(String icon, String number, String title, String link, String colorClass) {
        getStyleClass().add("dashboard-card");
        setSpacing(16);
        setPadding(new Insets(20));
        setPrefHeight(125);
        HBox.setHgrow(this, Priority.ALWAYS);

        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().addAll("card-icon", colorClass);

        VBox textBox = new VBox(5);

        Label numberLabel = new Label(number);
        numberLabel.getStyleClass().add("card-number");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");

        Label linkLabel = new Label(link);
        linkLabel.getStyleClass().add("card-link");

        textBox.getChildren().addAll(numberLabel, titleLabel, linkLabel);

        getChildren().addAll(iconLabel, textBox);
    }
}