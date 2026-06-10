package com.example.sportmanagerpro.nutricion.ui.components;

import javafx.geometry.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;

/** Barra lateral principal de módulos. */
public class NavigationSidebar extends VBox {
    public NavigationSidebar() {
        setPrefWidth(220);
        setMinWidth(220);
        setMaxWidth(220);
        setPadding(new Insets(26, 24, 20, 24));
        setSpacing(16);
        setBackground(new Background(new BackgroundFill(Ui.NAVY, CornerRadii.EMPTY, Insets.EMPTY)));

        VBox logo = new VBox(0,
                Ui.label("SPORTNUTRI", 22, FontWeight.EXTRA_BOLD, Color.WHITE),
                Ui.label("PRO", 22, FontWeight.EXTRA_BOLD, Color.WHITE)
        );
        logo.setPadding(new Insets(0, 0, 10, 0));

        getChildren().addAll(
                logo,
                menu("☘  Pacientes", true),
                menu("▣  Evaluación", false),
                menu("⚕  Diagnóstico", false),
                menu("◖  Cálculo dietético", false),
                menu("☕  Menú", false),
                menu("◧  Reportes", false),
                menu("▦  Agenda", false),
                menu("◴  Recordatorios", false),
                separator(),
                menu("◈  Alimentos", false),
                menu("◫  Plantillas", false),
                menu("⚙  Parámetros", false),
                menu("♟  Usuarios", false),
                Ui.spacerV(),
                quickBox()
        );
    }

    private Button menu(String text, boolean active) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setMinHeight(44);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setFont(javafx.scene.text.Font.font("Segoe UI", active ? FontWeight.EXTRA_BOLD : FontWeight.MEDIUM, 14));
        b.setTextFill(Color.WHITE);
        b.setBackground(new Background(new BackgroundFill(active ? Ui.GREEN_DARK : Color.TRANSPARENT, new CornerRadii(10), Insets.EMPTY)));
        b.setBorder(Border.EMPTY);
        return b;
    }

    private Region separator() {
        Region r = new Region();
        r.setPrefHeight(1);
        r.setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255,.75), CornerRadii.EMPTY, Insets.EMPTY)));
        VBox.setMargin(r, new Insets(8, 0, 8, 0));
        return r;
    }

    private VBox quickBox() {
        VBox box = new VBox(12);
        box.setPadding(new Insets(16));
        box.setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255,.08), new CornerRadii(12), Insets.EMPTY)));
        box.getChildren().addAll(
                Ui.label("Accesos rápidos", 13, FontWeight.NORMAL, Color.WHITE),
                quick("Nuevo paciente"), quick("Nueva evaluación"), quick("Crear menú"), quick("Reporte PDF")
        );
        return box;
    }

    private Label quick(String text) {
        Label l = Ui.label(text, 13, FontWeight.MEDIUM, Color.web("#E5E7EB"));
        l.setPadding(new Insets(4, 0, 4, 0));
        return l;
    }
}
