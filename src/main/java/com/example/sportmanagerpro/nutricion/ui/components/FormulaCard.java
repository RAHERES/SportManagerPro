package com.example.sportmanagerpro.nutricion.ui.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;

/** Tarjeta de fórmula dietética. */
public class FormulaCard extends VBox {
    public FormulaCard(String title, String value, String unit, boolean active) {
        setSpacing(8);
        setPadding(new Insets(16));
        setPrefSize(140, 120);
        setMinSize(140, 120);
        Color bg = active ? Color.web("#ECFDF5") : Color.WHITE;
        Color border = active ? Ui.GREEN : Ui.BORDER;
        setBackground(new Background(new BackgroundFill(bg, new CornerRadii(13), Insets.EMPTY)));
        setBorder(new Border(new BorderStroke(border, BorderStrokeStyle.SOLID, new CornerRadii(13), new BorderWidths(1.2))));
        getChildren().addAll(Ui.label(title, 12, FontWeight.NORMAL, Color.web("#334155")), Ui.label(value, 27, FontWeight.EXTRA_BOLD, Ui.TEXT), Ui.label(unit, 12, FontWeight.NORMAL, Ui.TEXT));
    }
}
