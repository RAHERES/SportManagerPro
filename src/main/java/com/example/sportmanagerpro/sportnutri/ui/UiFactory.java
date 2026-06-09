package com.example.sportmanagerpro.sportnutri.ui;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public final class UiFactory {

    private UiFactory() {
    }

    public static Label tituloSeccion(String texto) {
        Label label = new Label(texto);
        label.getStyleClass().add("section-title");
        return label;
    }

    public static Label ayuda(String texto) {
        Label label = new Label(texto);
        label.getStyleClass().add("muted");
        label.setWrapText(true);
        return label;
    }

    public static TextField campoTexto(String valorInicial) {
        TextField field = new TextField(valorInicial);
        field.getStyleClass().add("field");
        return field;
    }

    public static TextArea areaTexto(String valorInicial) {
        TextArea area = new TextArea(valorInicial);
        area.getStyleClass().add("area");
        area.setWrapText(true);
        area.setPrefRowCount(4);
        return area;
    }

    public static VBox campo(String etiqueta, TextField field) {
        VBox box = new VBox(5);
        Label label = new Label(etiqueta);
        label.getStyleClass().add("field-label");
        box.getChildren().addAll(label, field);
        return box;
    }

    public static VBox area(String etiqueta, TextArea area) {
        VBox box = new VBox(5);
        Label label = new Label(etiqueta);
        label.getStyleClass().add("field-label");
        box.getChildren().addAll(label, area);
        return box;
    }

    public static ComboBox<String> combo(String valor, String... opciones) {
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll(opciones);
        combo.setValue(valor);
        combo.getStyleClass().add("field");
        return combo;
    }

    public static VBox card() {
        VBox box = new VBox(12);
        box.getStyleClass().add("card");
        return box;
    }
}