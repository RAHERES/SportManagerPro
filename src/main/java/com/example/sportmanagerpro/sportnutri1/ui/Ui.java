package com.example.sportmanagerpro.sportnutri1.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;

/**
 * Utilidades visuales sin CSS externo para construir una interfaz moderna en JavaFX.
 */
public final class Ui {
    public static final Color BG = Color.web("#F5F8FC");
    public static final Color NAVY = Color.web("#071C34");
    public static final Color TEXT = Color.web("#0F172A");
    public static final Color MUTED = Color.web("#64748B");
    public static final Color GREEN = Color.web("#10B981");
    public static final Color BLUE = Color.web("#2563EB");
    public static final Color PURPLE = Color.web("#7C3AED");
    public static final Color BORDER = Color.web("#DDE7F2");

    private Ui() {}

    public static Label label(String text, double size, boolean bold, Color color) {
        Label l = new Label(text);
        l.setTextFill(color);
        l.setStyle(
                "-fx-font-size:" + size + "px;" +
                        "-fx-font-weight:" + (bold ? "800" : "500") + ";" +
                        "-fx-text-fill:" + toHex(color) + ";"
        );
        return l;
    }

    private static String toHex(Color color) {
        int r = (int) Math.round(color.getRed() * 255);
        int g = (int) Math.round(color.getGreen() * 255);
        int b = (int) Math.round(color.getBlue() * 255);
        return String.format("#%02X%02X%02X", r, g, b);
    }

    public static VBox card(Node... children) {
        VBox box = new VBox(18, children);
        box.setPadding(new Insets(22));
        box.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(18), Insets.EMPTY)));
        box.setBorder(new Border(new BorderStroke(BORDER, BorderStrokeStyle.SOLID, new CornerRadii(18), new BorderWidths(1))));
        box.setEffect(new DropShadow(18, Color.rgb(15, 23, 42, 0.06)));
        return box;
    }

    public static TextField input(String prompt) {
        TextField t = new TextField();
        t.setPromptText(prompt);
        t.setPrefHeight(46);
        t.setStyle(inputStyle());
        return t;
    }

    public static TextArea area(String prompt, double height) {
        TextArea a = new TextArea();
        a.setPromptText(prompt);
        a.setPrefHeight(height);
        a.setWrapText(true);
        a.setStyle(inputStyle());
        return a;
    }

    public static DatePicker datePicker(String prompt) {
        DatePicker d = new DatePicker();
        d.setPromptText(prompt);
        d.setPrefHeight(46);
        d.setMaxWidth(Double.MAX_VALUE);
        d.setStyle(inputStyle());
        return d;
    }

    public static ComboBox<String> combo(String prompt, String... values) {
        ComboBox<String> c = new ComboBox<>();
        c.getItems().addAll(values);
        c.setPromptText(prompt);
        c.setPrefHeight(46);
        c.setMaxWidth(Double.MAX_VALUE);
        c.setStyle(inputStyle());
        return c;
    }

    public static VBox field(String label, Control control) {
        VBox box = new VBox(7);
        Label l = label(label, 13, true, Color.web("#334155"));
        control.setMaxWidth(Double.MAX_VALUE);
        box.getChildren().addAll(l, control);
        return box;
    }

    public static GridPane grid(int columns) {
        GridPane g = new GridPane();
        g.setHgap(18);
        g.setVgap(18);
        for (int i = 0; i < columns; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / columns);
            cc.setHgrow(Priority.ALWAYS);
            g.getColumnConstraints().add(cc);
        }
        return g;
    }

    public static Button button(String text, String type) {
        Button b = new Button(text);
        b.setPrefHeight(48);
        b.setMinWidth(150);
        b.setStyle(switchButton(type));
        return b;
    }

    public static HBox step(String text, String icon, Color color, boolean selected) {
        Label i = label(icon, 17, true, color);
        Label t = label(text, 14, true, selected ? color : Color.web("#64748B"));
        HBox box = new HBox(9, i, t);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(12, 18, 12, 18));
        box.setMinHeight(52);
        box.setBackground(new Background(new BackgroundFill(selected ? alpha(color, .10) : Color.WHITE, new CornerRadii(14), Insets.EMPTY)));
        box.setBorder(new Border(new BorderStroke(selected ? alpha(color, .75) : BORDER, BorderStrokeStyle.SOLID, new CornerRadii(14), new BorderWidths(1))));
        return box;
    }

    public static StackPane iconCircle(String svg, Color color, double size) {
        Circle bg = new Circle(size / 2, alpha(color, 0.12));
        SVGPath path = new SVGPath();
        path.setContent(svg);
        path.setStroke(color);
        path.setFill(Color.TRANSPARENT);
        path.setStrokeWidth(2.3);
        StackPane p = new StackPane(bg, path);
        p.setMinSize(size, size);
        p.setPrefSize(size, size);
        p.setMaxSize(size, size);
        return p;
    }

    public static String inputStyle() {
        return "-fx-background-color:white;" +
                "-fx-border-color:#D6E0EC;" +
                "-fx-border-radius:10;" +
                "-fx-background-radius:10;" +
                "-fx-font-size:14;" +
                "-fx-text-fill:#0F172A;" +
                "-fx-prompt-text-fill:#94A3B8;" +
                "-fx-padding:0 12;";
    }

    private static String switchButton(String type) {
        switch (type) {
            case "primary":
                return "-fx-background-color:linear-gradient(to right,#10B981,#16A34A);-fx-text-fill:white;-fx-font-weight:800;-fx-background-radius:12;";
            case "blue":
                return "-fx-background-color:white;-fx-border-color:#3B82F6;-fx-text-fill:#2563EB;-fx-font-weight:800;-fx-border-radius:12;-fx-background-radius:12;";
            case "danger":
                return "-fx-background-color:white;-fx-border-color:#EF4444;-fx-text-fill:#DC2626;-fx-font-weight:800;-fx-border-radius:12;-fx-background-radius:12;";
            default:
                return "-fx-background-color:white;-fx-border-color:#CBD5E1;-fx-text-fill:#334155;-fx-font-weight:800;-fx-border-radius:12;-fx-background-radius:12;";
        }
    }

    public static Color alpha(Color color, double opacity) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }

    public static final String SVG_USER = "M12 12 A4 4 0 1 0 11.9 12 M4 22 C5 17 19 17 20 22";
    public static final String SVG_CAMERA = "M5 8 L9 8 L11 5 L15 5 L17 8 L21 8 L21 20 L5 20 Z M13 17 A4 4 0 1 0 12.9 17";
    public static final String SVG_CLIP = "M8 6 L18 6 L18 22 L6 22 L6 8 Z M10 4 L16 4 L16 8 L10 8 Z";
    public static final String SVG_CAL = "M6 7 L20 7 L20 21 L6 21 Z M9 4 L9 9 M17 4 L17 9 M6 12 L20 12";
    public static final String SVG_SHIELD = "M12 3 L20 7 L20 12 C20 17 16 20 12 22 C8 20 4 17 4 12 L4 7 Z M9 12 L11 14 L15 10";
}
