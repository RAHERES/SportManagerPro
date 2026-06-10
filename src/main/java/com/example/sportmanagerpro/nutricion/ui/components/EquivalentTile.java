package com.example.sportmanagerpro.nutricion.ui.components;

import javafx.geometry.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;

/** Tarjeta para equivalentes del SMAE. */
public class EquivalentTile extends VBox {
    public EquivalentTile(String group, String amount) {
        setAlignment(Pos.CENTER);
        setSpacing(5);
        setPrefSize(105, 86);
        setMinSize(95, 86);
        setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
        setBorder(new Border(new BorderStroke(Ui.BORDER, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(1))));
        getChildren().addAll(Ui.label(group, 11, FontWeight.NORMAL, Color.web("#334155")), Ui.label(amount, 24, FontWeight.EXTRA_BOLD, Ui.TEXT), Ui.label("equiv.", 11, FontWeight.NORMAL, Ui.MUTED));
    }
}
