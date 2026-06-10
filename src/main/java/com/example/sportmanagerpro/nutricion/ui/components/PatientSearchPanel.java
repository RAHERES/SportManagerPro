package com.example.sportmanagerpro.nutricion.ui.components;

import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.FontWeight;

/**
 * Panel izquierdo de búsqueda, filtros y listado de pacientes.
 */
public class PatientSearchPanel extends VBox {
    public PatientSearchPanel() {
        setPrefWidth(280);
        setMinWidth(280);
        setMaxWidth(280);
        setSpacing(16);
        setPadding(new Insets(18));
        Ui.card(this, 18, 0.06);

        getChildren().addAll(
                createSearch(),
                createFilters(),
                createPatientsTitle(),
                createPatientList(),
                createShowAll()
        );
    }

    private VBox createSearch() {
        VBox box = new VBox(9);
        Label title = Ui.label("Buscar paciente", 14, FontWeight.EXTRA_BOLD, Ui.TEXT);
        TextField input = new TextField();
        input.setPromptText("Nombre, teléfono o ID...");
        input.setPrefHeight(38);
        styleInput(input);
        Button filter = new Button("⌁");
        filter.setPrefSize(38, 38);
        styleWhiteButton(filter);
        HBox row = new HBox(8, input, filter);
        HBox.setHgrow(input, Priority.ALWAYS);
        box.getChildren().addAll(title, row);
        return box;
    }

    private VBox createFilters() {
        VBox box = new VBox(10);
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label title = Ui.label("Filtros", 14, FontWeight.EXTRA_BOLD, Ui.TEXT);
        Label clear = Ui.label("Borrar", 13, FontWeight.SEMI_BOLD, Ui.GREEN_DARK);
        header.getChildren().addAll(title, Ui.spacerH(), clear);
        box.getChildren().addAll(header, combo("Todos los estados"), combo("Todos los tipos"), combo("Todos los objetivos"));
        return box;
    }

    private ComboBox<String> combo(String value) {
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().add(value);
        combo.setValue(value);
        combo.setPrefHeight(38);
        combo.setMaxWidth(Double.MAX_VALUE);
        combo.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(8), Insets.EMPTY)));
        combo.setBorder(new Border(new BorderStroke(Color.web("#CBD5E1"), BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(1))));
        return combo;
    }

    private HBox createPatientsTitle() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);
        Label title = Ui.label("Pacientes (24)", 14, FontWeight.EXTRA_BOLD, Ui.TEXT);
        Button add = new Button("+");
        add.setPrefSize(34, 34);
        add.setCursor(Cursor.HAND);
        add.setFont(javafx.scene.text.Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 22));
        add.setTextFill(Color.WHITE);
        add.setBackground(new Background(new BackgroundFill(Color.web("#65D84E"), new CornerRadii(9), Insets.EMPTY)));
        add.setEffect(new DropShadow(10, Color.rgb(34,197,94,.25)));
        box.getChildren().addAll(title, Ui.spacerH(), add);
        return box;
    }

    private VBox createPatientList() {
        VBox list = new VBox(0);
        list.getChildren().addAll(
                item("Ana López", "23 años · Femenino", "ID: 000145", true, true), sep(),
                item("María Pérez", "28 años · Femenino", "ID: 000146", false, false), sep(),
                item("Carlos Ruiz", "31 años · Masculino", "ID: 000147", false, true), sep(),
                item("Luis Mendoza", "26 años · Masculino", "ID: 000148", false, true), sep(),
                item("Sofía Martínez", "24 años · Femenino", "ID: 000149", false, false), sep(),
                item("Diego Torres", "27 años · Masculino", "ID: 000150", false, true)
        );
        return list;
    }

    private HBox item(String name, String data, String id, boolean selected, boolean green) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        item.setCursor(Cursor.HAND);
        item.setPrefHeight(78);
        item.setMaxWidth(Double.MAX_VALUE);
        item.setBackground(new Background(new BackgroundFill(selected ? Color.web("#ECFDF5") : Color.TRANSPARENT, new CornerRadii(10), Insets.EMPTY)));
        item.setBorder(new Border(new BorderStroke(selected ? Ui.GREEN : Color.TRANSPARENT, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(selected ? 1.3 : 0))));

        Circle photo = new Circle(22);
        photo.setFill(Color.web("#DBEAFE"));
        VBox texts = new VBox(3,
                Ui.label(name, 13, FontWeight.EXTRA_BOLD, Ui.TEXT),
                Ui.label(data, 12, FontWeight.NORMAL, Color.web("#334155")),
                Ui.label(id, 12, FontWeight.NORMAL, Color.web("#334155"))
        );
        Circle state = new Circle(5, green ? Color.web("#65C83B") : Color.web("#F59E0B"));
        item.getChildren().addAll(photo, texts, Ui.spacerH(), state);
        return item;
    }

    private Region sep() {
        Region line = new Region();
        line.setPrefHeight(1);
        line.setBackground(new Background(new BackgroundFill(Ui.BORDER, CornerRadii.EMPTY, new Insets(0, 6, 0, 58))));
        return line;
    }

    private Label createShowAll() {
        Label label = Ui.label("Ver todos", 13, FontWeight.NORMAL, Color.web("#334155"));
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setPadding(new Insets(6, 0, 0, 0));
        return label;
    }

    private void styleInput(TextField input) {
        input.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(8), Insets.EMPTY)));
        input.setBorder(new Border(new BorderStroke(Color.web("#CBD5E1"), BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(1))));
        input.setPadding(new Insets(0, 12, 0, 12));
    }

    private void styleWhiteButton(Button button) {
        button.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(8), Insets.EMPTY)));
        button.setBorder(new Border(new BorderStroke(Color.web("#CBD5E1"), BorderStrokeStyle.SOLID, new CornerRadii(8), new BorderWidths(1))));
        button.setTextFill(Color.web("#334155"));
        button.setCursor(Cursor.HAND);
    }
}
