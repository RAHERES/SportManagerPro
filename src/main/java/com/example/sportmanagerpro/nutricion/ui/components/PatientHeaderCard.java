package com.example.sportmanagerpro.nutricion.ui.components;

import javafx.geometry.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.FontWeight;

/** Encabezado del expediente del paciente. */
public class PatientHeaderCard extends VBox {
    public PatientHeaderCard() {
        setSpacing(18);
        setPadding(new Insets(22));
        setMinHeight(210);
        setMaxWidth(Double.MAX_VALUE);
        Ui.card(this, 18, 0.07);

        HBox content = new HBox(24);
        content.setAlignment(Pos.CENTER_LEFT);
        Circle photo = new Circle(58, Color.web("#DBEAFE"));
        VBox data = createPatientData();
        HBox.setHgrow(data, Priority.ALWAYS);

        HBox metrics = new HBox(14);
        metrics.setAlignment(Pos.CENTER_RIGHT);
        metrics.setMinWidth(445);
        metrics.setPrefWidth(445);
        metrics.setMaxWidth(445);

        MetricTile weight = new MetricTile("Peso actual", 68.4, "kg", "IMC: 26.1", MetricTile.IconType.WEIGHT, Color.web("#7C3AED"));
        MetricTile height = new MetricTile("Estatura", 162, "cm", "", MetricTile.IconType.HEIGHT, Color.web("#22C55E"));
        height.setDecimals(0);
        MetricTile target = new MetricTile("Objetivo", 60, "kg", "", MetricTile.IconType.TARGET, Color.web("#F97316"));
        target.setDecimals(0);
        fixed(weight, 145, 112);
        fixed(height, 135, 112);
        fixed(target, 135, 112);
        metrics.getChildren().addAll(weight, height, target);
        content.getChildren().addAll(photo, data, metrics);

        getChildren().addAll(content, createTabs());
    }

    private void fixed(Region node, double w, double h) {
        node.setMinSize(w, h);
        node.setPrefSize(w, h);
        node.setMaxSize(w, h);
    }

    private VBox createPatientData() {
        VBox box = new VBox(7);
        box.setMinWidth(330);
        box.setPrefWidth(360);
        box.setMaxWidth(Double.MAX_VALUE);
        HBox nameRow = new HBox(10);
        nameRow.setAlignment(Pos.CENTER_LEFT);
        Label name = Ui.label("Ana López", 28, FontWeight.EXTRA_BOLD, Ui.TEXT);
        Label status = Ui.label("Activa", 11, FontWeight.EXTRA_BOLD, Color.web("#15803D"));
        status.setPadding(new Insets(4, 10, 4, 10));
        status.setBackground(new Background(new BackgroundFill(Color.web("#DCFCE7"), new CornerRadii(20), Insets.EMPTY)));
        nameRow.getChildren().addAll(name, status);
        Label info = Ui.label("23 años (15/04/2001)     Femenino\n55 1234 5678     ana.lopez@email.com\nObjetivo: Pérdida de grasa y tonificación\nIngreso: 10 Feb 2024 · Expediente: 000145", 12.5, FontWeight.NORMAL, Color.web("#334155"));
        info.setWrapText(true);
        box.getChildren().addAll(nameRow, info);
        return box;
    }

    private HBox createTabs() {
        HBox tabs = new HBox(28);
        tabs.setAlignment(Pos.CENTER_LEFT);
        tabs.getChildren().addAll(tab("Datos generales", false), tab("Evaluación", false), tab("Cálculo dietético", true), tab("Equivalentes", false), tab("Menú", false), tab("Evolución", false), tab("Documentos", false));
        return tabs;
    }

    private Label tab(String text, boolean active) {
        Label l = Ui.label(text, 13, active ? FontWeight.EXTRA_BOLD : FontWeight.NORMAL, active ? Ui.GREEN_DARK : Color.web("#334155"));
        l.setPadding(new Insets(0, 0, 9, 0));
        if (active) {
            l.setBorder(new Border(new BorderStroke(Ui.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,0,3,0))));
        }
        return l;
    }
}
