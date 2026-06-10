package com.example.sportmanagerpro.nutricion.ui.components;

import javafx.geometry.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;

/** Barra superior de navegación y búsqueda global. */
public class TopBar extends HBox {
    public TopBar() {
        setMinHeight(84);
        setPadding(new Insets(18, 26, 18, 26));
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(20);
        setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        setBorder(new Border(new BorderStroke(Ui.BORDER, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,0,1,0))));

        VBox titles = new VBox(3, Ui.label("Paciente", 25, FontWeight.EXTRA_BOLD, Ui.TEXT), Ui.label("Pacientes / Ana López", 12, FontWeight.NORMAL, Color.web("#334155")));
        TextField search = new TextField();
        search.setPromptText("Buscar (Ctrl + K)");
        search.setPrefSize(300, 38);
        search.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
        search.setBorder(new Border(new BorderStroke(Color.web("#D8E1EF"), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(1))));
        search.setPadding(new Insets(0, 12, 0, 12));
        Label user = Ui.label("Lic. Valeria M.\nNutrióloga", 13, FontWeight.EXTRA_BOLD, Ui.TEXT);
        getChildren().addAll(titles, Ui.spacerH(), search, Ui.label("♟", 12, FontWeight.BOLD, Ui.TEXT), Ui.label("?", 12, FontWeight.BOLD, Ui.TEXT), user);
    }
}
