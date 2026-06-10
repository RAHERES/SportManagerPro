package com.example.sportmanagerpro.nutricion.ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Utilidades visuales centralizadas para construir la interfaz sin CSS externo.
 */
public final class Ui {
    public static final Color APP_BG = Color.web("#F4F7FB");
    public static final Color NAVY = Color.web("#071A2F");
    public static final Color NAVY_2 = Color.web("#0B223D");
    public static final Color TEXT = Color.web("#0F172A");
    public static final Color MUTED = Color.web("#64748B");
    public static final Color BORDER = Color.web("#E2E8F0");
    public static final Color GREEN = Color.web("#22C55E");
    public static final Color GREEN_DARK = Color.web("#16A34A");

    private Ui() {}

    public static Label label(String text, double size, FontWeight weight, Color color) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", weight, size));
        label.setTextFill(color);
        return label;
    }

    public static void card(Region region, double radius, double shadowAlpha) {
        region.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(radius), Insets.EMPTY)));
        region.setBorder(new Border(new BorderStroke(BORDER, BorderStrokeStyle.SOLID, new CornerRadii(radius), new BorderWidths(1))));
        region.setEffect(new DropShadow(18, Color.rgb(15, 23, 42, shadowAlpha)));
    }

    public static Button button(String text, Color color, boolean filled) {
        Button b = new Button(text);
        b.setMinHeight(44);
        b.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        b.setBackground(new Background(new BackgroundFill(filled ? color : Color.WHITE, new CornerRadii(11), Insets.EMPTY)));
        b.setTextFill(filled ? Color.WHITE : color);
        b.setBorder(new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, new CornerRadii(11), new BorderWidths(1.4))));
        return b;
    }

    public static Region spacerH() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }

    public static Region spacerV() {
        Region r = new Region();
        VBox.setVgrow(r, Priority.ALWAYS);
        return r;
    }

    public static HBox row(Node... children) {
        HBox box = new HBox(12, children);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }
}
