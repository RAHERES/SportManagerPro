package com.example.sportmanagerpro.sportnutri.ui.personas;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AgregarPersonaView extends BorderPane {

    public AgregarPersonaView() {
        getStyleClass().add("dashboard-root");

        VBox main = new VBox(16);
        main.setPadding(new Insets(22, 26, 28, 26));

        main.getChildren().addAll(
                header(),
                steps(),
                content()
        );

        ScrollPane scroll = new ScrollPane(main);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.getStyleClass().add("content-scroll");

        setCenter(scroll);
    }

    private HBox header() {
        HBox header = new HBox(16);
        header.setAlignment(Pos.CENTER_LEFT);

        Label icon = new Label("👤+");
        icon.getStyleClass().add("add-person-main-icon");

        VBox titleBox = new VBox(3);
        Label title = new Label("Agregar persona");
        title.getStyleClass().add("main-title");

        Label subtitle = new Label("Registra un nuevo atleta o usuario en el sistema.");
        subtitle.getStyleClass().add("muted-text");

        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button cancel = new Button("✕  Cancelar");
        cancel.getStyleClass().add("secondary-button");

        Button save = new Button("Guardar persona");
        save.getStyleClass().add("primary-button");

        Label icons = new Label("🔔   ?   👤");
        icons.getStyleClass().add("top-icons");

        header.getChildren().addAll(icon, titleBox, spacer, cancel, save, icons);
        return header;
    }

    private HBox steps() {
        HBox box = new HBox(34);
        box.getStyleClass().add("add-person-steps");
        box.setAlignment(Pos.CENTER_LEFT);

        box.getChildren().addAll(
                step("1", "Información personal", true),
                arrow(),
                step("2", "Datos físicos", false),
                arrow(),
                step("3", "Objetivos y notas", false),
                arrow(),
                step("4", "Revisar y guardar", false)
        );

        return box;
    }

    private HBox step(String number, String text, boolean active) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);

        Label circle = new Label(number);
        circle.getStyleClass().add(active ? "step-circle-active" : "step-circle");

        Label label = new Label(text);
        label.getStyleClass().add(active ? "step-text-active" : "step-text");

        box.getChildren().addAll(circle, label);
        return box;
    }

    private Label arrow() {
        Label label = new Label("→");
        label.getStyleClass().add("step-arrow");
        return label;
    }

    private HBox content() {
        HBox root = new HBox(18);

        VBox left = new VBox(16);
        HBox.setHgrow(left, Priority.ALWAYS);

        left.getChildren().addAll(
                personalInfo(),
                physicalData(),
                additionalDetails(),
                bottomActions()
        );

        VBox right = new VBox(16);
        right.setMinWidth(340);
        right.setPrefWidth(350);
        right.setMaxWidth(360);

        right.getChildren().addAll(
                previewCard(),
                goalsCard(),
                quickInfoCard()
        );

        root.getChildren().addAll(left, right);
        return root;
    }

    private VBox personalInfo() {
        VBox card = sectionCard("👤", "Información personal");

        GridPane grid = new GridPane();
        grid.setHgap(22);
        grid.setVgap(16);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(31);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(31);
        ColumnConstraints c3 = new ColumnConstraints();
        c3.setPercentWidth(38);

        grid.getColumnConstraints().addAll(c1, c2, c3);

        grid.add(input("Nombre completo *", "Ej. Juan Pérez García"), 0, 0);
        grid.add(combo("Género", "Masculino", "Masculino", "Femenino"), 1, 0);
        grid.add(uploadPhoto(), 2, 0, 1, 4);

        grid.add(input("Correo electrónico", "Ej. juan.perez@email.com"), 0, 1);
        grid.add(combo("Rol", "Atleta", "Atleta", "Paciente", "Entrenador", "Nutriólogo"), 1, 1);

        grid.add(phoneInput(), 0, 2);
        grid.add(combo("Estado", "Activo", "Activo", "Inactivo"), 1, 2);

        grid.add(dateAndAge(), 0, 3);
        grid.add(combo("Grupo / Equipo", "Seleccionar grupo", "Seleccionar grupo", "Sub 17 Femenil", "Sub 15 Femenil", "Fuerza Élite"), 1, 3);

        card.getChildren().add(grid);
        return card;
    }

    private VBox physicalData() {
        VBox card = sectionCard("🧍", "Datos físicos básicos");

        HBox units = new HBox(8);
        units.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label label = new Label("Unidades");
        label.getStyleClass().add("muted-text");

        Button metric = new Button("Métrico");
        metric.getStyleClass().add("tab-pill-active");

        Button imperial = new Button("Imperial");
        imperial.getStyleClass().add("tab-pill");

        units.getChildren().addAll(spacer, label, metric, imperial);

        GridPane grid = new GridPane();
        grid.setHgap(22);
        grid.setVgap(16);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(25);
            grid.getColumnConstraints().add(col);
        }

        grid.add(input("Estatura (cm)", "178"), 0, 0);
        grid.add(input("Peso (kg)", "78.5"), 1, 0);
        grid.add(input("IMC", "24.8"), 2, 0);
        grid.add(input("% Grasa corporal (%)", "14.5"), 3, 0);

        grid.add(input("Masa muscular (kg)", "36.2"), 0, 1);
        grid.add(input("Frecuencia cardiaca (ppm)", "72"), 1, 1);
        grid.add(input("Presión arterial", "120 / 80"), 2, 1);
        grid.add(combo("Nivel de actividad", "Moderado", "Bajo", "Moderado", "Alto"), 3, 1);

        card.getChildren().addAll(units, grid);
        return card;
    }

    private VBox additionalDetails() {
        VBox card = sectionCard("📋", "Detalles adicionales");

        GridPane grid = new GridPane();
        grid.setHgap(22);
        grid.setVgap(16);

        for (int i = 0; i < 3; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(33.33);
            grid.getColumnConstraints().add(col);
        }

        grid.add(combo("Deporte / Disciplina", "Entrenamiento de fuerza", "Entrenamiento de fuerza", "Fútbol", "Nutrición deportiva"), 0, 0);
        grid.add(combo("Experiencia", "Intermedio", "Principiante", "Intermedio", "Avanzado"), 1, 0);
        grid.add(combo("Disponibilidad semanal", "5 días", "3 días", "4 días", "5 días", "6 días"), 2, 0);

        VBox notesBox = new VBox(7);
        Label notesLabel = new Label("Notas (opcional)");
        notesLabel.getStyleClass().add("form-label");

        TextArea notes = new TextArea();
        notes.setPromptText("Escribe cualquier información relevante sobre esta persona...");
        notes.getStyleClass().add("input-area");
        notes.setPrefRowCount(2);

        notesBox.getChildren().addAll(notesLabel, notes);
        grid.add(notesBox, 0, 1, 3, 1);

        card.getChildren().add(grid);
        return card;
    }

    private HBox bottomActions() {
        HBox box = new HBox();
        box.setPadding(new Insets(0, 0, 0, 0));

        Button cancel = new Button("Cancelar");
        cancel.getStyleClass().add("secondary-button");
        cancel.setPrefWidth(120);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button next = new Button("Siguiente  →");
        next.getStyleClass().add("primary-button");
        next.setPrefWidth(360);

        box.getChildren().addAll(cancel, spacer, next);
        return box;
    }

    private VBox previewCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("add-person-side-card");
        card.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Vista previa");
        title.getStyleClass().add("panel-title-dark");
        title.setMaxWidth(Double.MAX_VALUE);

        Label avatar = new Label("👨");
        avatar.getStyleClass().add("preview-avatar");

        Label name = new Label("Juan Pérez García");
        name.getStyleClass().add("preview-name");

        Label active = new Label("Activo");
        active.getStyleClass().add("status-active-pill");

        VBox rows = new VBox(10);
        rows.setMaxWidth(Double.MAX_VALUE);
        rows.getChildren().addAll(
                previewRow("📅", "Edad", "24 años"),
                previewRow("⚥", "Género", "Masculino"),
                previewRow("🏃", "Rol", "Atleta"),
                previewRow("🧩", "Grupo", "Fuerza Élite")
        );

        card.getChildren().addAll(title, avatar, name, active, rows);
        return card;
    }

    private VBox goalsCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("add-person-side-card");

        HBox header = new HBox();
        Label title = new Label("🎯  Objetivos principales");
        title.getStyleClass().add("panel-title-dark");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label add = new Label("+  Agregar");
        add.getStyleClass().add("card-link");

        header.getChildren().addAll(title, spacer, add);

        card.getChildren().addAll(
                header,
                goal("Aumento de masa muscular", "green-goal"),
                goal("Mejorar fuerza", "blue-goal"),
                goal("Definición corporal", "orange-goal")
        );

        return card;
    }

    private VBox quickInfoCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("add-person-side-card");

        Label title = new Label("📊  Información rápida");
        title.getStyleClass().add("panel-title-dark");

        card.getChildren().addAll(
                title,
                previewRow("📅", "Fecha de registro", "25/05/2024 - 10:30 AM"),
                previewRow("👤", "Agregado por", "Carlos Ramírez"),
                previewRow("⏱", "Última actualización", "–"),
                previewRow("✅", "Estado", "Activo")
        );

        return card;
    }

    private VBox sectionCard(String icon, String title) {
        VBox card = new VBox(16);
        card.getStyleClass().add("add-person-card");

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("section-icon-purple");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("panel-title-dark");

        header.getChildren().addAll(iconLabel, titleLabel);
        card.getChildren().add(header);

        return card;
    }

    private VBox input(String label, String prompt) {
        VBox box = new VBox(7);

        Label l = new Label(label);
        l.getStyleClass().add("form-label");

        TextField field = new TextField();
        field.setPromptText(prompt);
        field.getStyleClass().add("add-input");

        box.getChildren().addAll(l, field);
        return box;
    }

    private VBox combo(String label, String selected, String... values) {
        VBox box = new VBox(7);

        Label l = new Label(label);
        l.getStyleClass().add("form-label");

        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll(values);
        combo.setValue(selected);
        combo.getStyleClass().add("add-combo");
        combo.setMaxWidth(Double.MAX_VALUE);

        box.getChildren().addAll(l, combo);
        return box;
    }

    private VBox phoneInput() {
        VBox box = new VBox(7);

        Label label = new Label("Teléfono");
        label.getStyleClass().add("form-label");

        HBox row = new HBox(8);

        ComboBox<String> code = new ComboBox<>();
        code.getItems().addAll("🇲🇽  +52", "+1", "+34");
        code.setValue("🇲🇽  +52");
        code.getStyleClass().add("add-combo");
        code.setPrefWidth(105);

        TextField phone = new TextField();
        phone.setPromptText("55 1234 5678");
        phone.getStyleClass().add("add-input");
        HBox.setHgrow(phone, Priority.ALWAYS);

        row.getChildren().addAll(code, phone);
        box.getChildren().addAll(label, row);

        return box;
    }

    private HBox dateAndAge() {
        HBox box = new HBox(10);
        box.getChildren().addAll(
                input("Fecha de nacimiento *", "25/05/2000"),
                input("Edad", "24 años")
        );
        return box;
    }

    private VBox uploadPhoto() {
        VBox box = new VBox(7);

        Label label = new Label("Foto de perfil");
        label.getStyleClass().add("form-label");

        VBox upload = new VBox(8);
        upload.getStyleClass().add("upload-box");
        upload.setAlignment(Pos.CENTER);
        upload.setPrefHeight(150);
        upload.setMaxWidth(Double.MAX_VALUE);

        Label camera = new Label("📷");
        camera.getStyleClass().add("upload-icon");

        Label text = new Label("Subir foto");
        text.getStyleClass().add("card-link");

        Label hint = new Label("JPG o PNG (máx. 5MB)");
        hint.getStyleClass().add("muted-text");

        upload.getChildren().addAll(camera, text, hint);
        box.getChildren().addAll(label, upload);
        return box;
    }

    private HBox previewRow(String icon, String label, String value) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Label i = new Label(icon);
        i.getStyleClass().add("preview-row-icon");

        Label l = new Label(label);
        l.getStyleClass().add("muted-text");
        l.setPrefWidth(120);

        Label v = new Label(value);
        v.getStyleClass().add("table-cell-bold");

        row.getChildren().addAll(i, l, v);
        return row;
    }

    private Label goal(String text, String style) {
        Label label = new Label(text + "   ×");
        label.getStyleClass().add(style);
        return label;
    }
}