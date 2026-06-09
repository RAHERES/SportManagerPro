package com.example.sportmanagerpro.pacientes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Kit visual reutilizable sin archivos CSS externos.
 *
 * <p>Centraliza colores, tarjetas, campos, botones e iconos para mantener una
 * apariencia consistente en toda la pantalla.</p>
 */
public final class UiKit {

    public static final Color NAVY = Color.web("#071A2F");
    public static final Color GREEN = Color.web("#22C55E");
    public static final Color TEXT = Color.web("#0F172A");
    public static final Color MUTED = Color.web("#64748B");
    public static final Color BORDER = Color.web("#D8E1EE");
    public static final Color BACKGROUND = Color.web("#F4F7FB");

    private UiKit() {}

    public static VBox card(double spacing, double padding) {
        VBox box = new VBox(spacing);
        box.setPadding(new Insets(padding));
        box.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(16), Insets.EMPTY)));
        box.setBorder(new Border(new BorderStroke(BORDER, BorderStrokeStyle.SOLID, new CornerRadii(16), new BorderWidths(1))));
        box.setEffect(new DropShadow(18, Color.rgb(15, 23, 42, 0.06)));
        return box;
    }

    public static Label title(String text, double size) {
        Label label = new Label(text);
        label.setTextFill(TEXT);
        label.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, size));
        return label;
    }

    public static Label muted(String text, double size) {
        Label label = new Label(text);
        label.setTextFill(MUTED);
        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, size));
        return label;
    }

    public static Label sectionTitle(String text, String iconSvg) {
        Label label = new Label(text);
        label.setTextFill(TEXT);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        label.setGraphic(icon(iconSvg, Color.web("#334155"), 18));
        label.setGraphicTextGap(12);
        return label;
    }

    public static TextField input(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setPrefHeight(42);
        field.setFont(Font.font("Segoe UI", 13));
        field.setStyle("-fx-background-color: white; -fx-background-radius: 9; -fx-border-radius: 9; -fx-border-color: #CBD5E1; -fx-padding: 0 12;");
        return field;
    }

    public static TextArea textArea(String prompt) {
        TextArea area = new TextArea();
        area.setPromptText(prompt);
        area.setPrefRowCount(2);
        area.setWrapText(true);
        area.setFont(Font.font("Segoe UI", 13));
        area.setStyle("-fx-background-color: white; -fx-background-radius: 9; -fx-border-radius: 9; -fx-border-color: #CBD5E1; -fx-padding: 4 8;");
        return area;
    }

    public static ComboBox<String> combo(String prompt, String... items) {
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll(items);
        combo.setPromptText(prompt);
        combo.setPrefHeight(42);
        combo.setMaxWidth(Double.MAX_VALUE);
        combo.setStyle("-fx-background-color: white; -fx-background-radius: 9; -fx-border-radius: 9; -fx-border-color: #CBD5E1;");
        return combo;
    }

    public static DatePicker datePicker() {
        DatePicker picker = new DatePicker();
        picker.setPromptText("dd/mm/aaaa");
        picker.setPrefHeight(42);
        picker.setMaxWidth(Double.MAX_VALUE);
        picker.setStyle("-fx-background-color: white; -fx-background-radius: 9; -fx-border-radius: 9; -fx-border-color: #CBD5E1;");
        return picker;
    }

    public static Button primaryButton(String text, String iconSvg) {
        Button button = new Button(text);
        button.setGraphic(icon(iconSvg, Color.WHITE, 18));
        button.setGraphicTextGap(10);
        button.setTextFill(Color.WHITE);
        button.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        button.setPrefHeight(46);
        button.setStyle("-fx-background-color: linear-gradient(to right, #22C55E, #16A34A); -fx-background-radius: 10; -fx-cursor: hand;");
        return button;
    }

    public static Button outlineButton(String text, Color color, String iconSvg) {
        Button button = new Button(text);
        button.setGraphic(icon(iconSvg, color, 18));
        button.setGraphicTextGap(10);
        button.setTextFill(color);
        button.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        button.setPrefHeight(46);
        button.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: " + toRgb(color) + "; -fx-cursor: hand;");
        return button;
    }

    public static StackPane icon(String svg, Color color, double size) {
        SVGPath path = new SVGPath();
        path.setContent(svg);
        path.setFill(Color.TRANSPARENT);
        path.setStroke(color);
        path.setStrokeWidth(2);
        StackPane pane = new StackPane(path);
        pane.setMinSize(size, size);
        pane.setPrefSize(size, size);
        pane.setMaxSize(size, size);
        return pane;
    }

    public static StackPane coloredIcon(String svg, Color color) {
        StackPane container = icon(svg, color, 20);
        container.setPadding(new Insets(8));
        container.setMinSize(40, 40);
        container.setPrefSize(40, 40);
        container.setMaxSize(40, 40);
        container.setBackground(new Background(new BackgroundFill(Color.rgb((int)(color.getRed()*255), (int)(color.getGreen()*255), (int)(color.getBlue()*255), 0.13), new CornerRadii(9), Insets.EMPTY)));
        return container;
    }

    public static HBox fieldWithLabel(String label, Node field) {
        VBox box = new VBox(8);
        Label l = muted(label, 13);
        l.setTextFill(Color.web("#243247"));
        box.getChildren().addAll(l, field);
        HBox wrapper = new HBox(box);
        HBox.setHgrow(box, Priority.ALWAYS);
        return wrapper;
    }

    public static Circle avatar(double radius) {
        Circle circle = new Circle(radius);
        circle.setFill(Color.web("#DBEAFE"));
        return circle;
    }

    public static String toRgb(Color color) {
        return String.format("#%02X%02X%02X", (int)(color.getRed()*255), (int)(color.getGreen()*255), (int)(color.getBlue()*255));
    }

    public static final String ICON_USER = "M12 12 A4 4 0 1 0 12 4 A4 4 0 1 0 12 12 M4 22 C4 17 8 15 12 15 C16 15 20 17 20 22";
    public static final String ICON_CLIPBOARD = "M8 5 L16 5 M9 3 L15 3 L15 7 L9 7 Z M6 5 L5 5 L5 23 L19 23 L19 5 L18 5";
    public static final String ICON_CALENDAR = "M5 7 L19 7 L19 21 L5 21 Z M8 3 L8 8 M16 3 L16 8 M5 11 L19 11";
    public static final String ICON_SAVE = "M5 4 L17 4 L21 8 L21 22 L5 22 Z M8 4 L8 10 L16 10 L16 4 M8 18 L18 18";
    public static final String ICON_TRASH = "M7 7 L17 7 M10 7 L10 20 M14 7 L14 20 M8 7 L9 22 L15 22 L16 7 M10 4 L14 4 L15 7 L9 7 Z";
    public static final String ICON_REFRESH = "M20 7 L20 13 L14 13 M4 17 A8 8 0 0 0 18 20 M20 7 A8 8 0 0 0 6 4";
    public static final String ICON_CAMERA = "M7 8 L9 5 L15 5 L17 8 L20 8 L20 20 L4 20 L4 8 Z M12 17 A4 4 0 1 0 12 9 A4 4 0 1 0 12 17";
    public static final String ICON_WEIGHT = "M6 8 L18 8 L18 22 L6 22 Z M9 8 C9 4 15 4 15 8 M10 13 L14 13";
    public static final String ICON_HEIGHT = "M7 3 L7 23 M17 3 L17 23 M5 5 L9 5 M15 5 L19 5 M5 21 L9 21 M15 21 L19 21";
    public static final String ICON_FIRE = "M12 22 C8 20 6 17 7 13 C8 9 12 7 11 3 C16 7 19 11 18 16 C17 20 15 22 12 22 Z";
    public static final String ICON_PLUS_USER = "M10 11 A4 4 0 1 0 10 3 A4 4 0 1 0 10 11 M3 21 C3 16 6 14 10 14 C14 14 17 16 17 21 M19 8 L19 16 M15 12 L23 12";
}
