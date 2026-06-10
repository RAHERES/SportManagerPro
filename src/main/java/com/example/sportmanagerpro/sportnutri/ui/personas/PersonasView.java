package com.example.sportmanagerpro.sportnutri.ui.personas;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PersonasView extends BorderPane {

    public PersonasView() {
        getStyleClass().add("dashboard-root");

        VBox main = new VBox(20);
        main.setPadding(new Insets(22, 26, 18, 26));

        main.getChildren().addAll(
                header(),
                tabs(),
                filters(),
                summaryCards(),
                tableSection()
        );

        setCenter(main);
    }

    private HBox header() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);

        Label title = new Label("👥  Personas");
        title.getStyleClass().add("main-title");

        Label subtitle = new Label("Gestiona todas las personas registradas en el sistema");
        subtitle.getStyleClass().add("muted-text");

        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        TextField search = new TextField();
        search.setPromptText("Buscar personas...");
        search.getStyleClass().add("input-search");
        search.setPrefWidth(340);

        Label icons = new Label("🔔  📅  ?  ●");
        icons.getStyleClass().add("top-icons");

        header.getChildren().addAll(titleBox, spacer, search, icons);
        return header;
    }

    private HBox tabs() {
        HBox tabs = new HBox(34);
        tabs.getStyleClass().add("tabs-bar");

        Label listado = tab("Listado", true);
        Label nuevo = tab("Nuevo Registro", false);
        Label importar = tab("Importar", false);

        tabs.getChildren().addAll(listado, nuevo, importar);
        return tabs;
    }

    private Label tab(String text, boolean active) {
        Label label = new Label(text);
        label.getStyleClass().add(active ? "tab-active" : "tab");
        return label;
    }

    private HBox filters() {
        HBox box = new HBox(16);
        box.getStyleClass().add("panel");
        box.setPadding(new Insets(18));
        box.setAlignment(Pos.CENTER_LEFT);

        TextField search = new TextField();
        search.setPromptText("Buscar por nombre, teléfono o correo...");
        search.getStyleClass().add("input-search");
        search.setPrefWidth(290);

        ComboBox<String> categoria = combo("Categoría", "Todas");
        ComboBox<String> rol = combo("Rol", "Todos");
        ComboBox<String> estado = combo("Estado", "Todos");
        ComboBox<String> grupo = combo("Grupo / Equipo", "Todos");

        Button limpiar = new Button("⛌ Limpiar filtros");
        limpiar.getStyleClass().add("secondary-button");

        Button nueva = new Button("+ Nueva Persona");
        nueva.getStyleClass().add("primary-button");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        box.getChildren().addAll(search, categoria, rol, estado, grupo, limpiar, nueva);
        return box;
    }

    private ComboBox<String> combo(String prompt, String value) {
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().add(value);
        combo.setValue(value);
        combo.setPromptText(prompt);
        combo.getStyleClass().add("combo-filter");
        combo.setPrefWidth(145);
        return combo;
    }

    private HBox summaryCards() {
        HBox cards = new HBox(12);
        cards.getChildren().addAll(
                statCard("👥", "128", "Total personas", "Ver todos", "blue"),
                statCard("●", "64", "Deportistas", "Ver todos", "green"),
                statCard("🍎", "47", "Pacientes", "Ver todos", "purple"),
                statCard("🎓", "12", "Alumnos", "Ver todos", "orange"),
                statCard("⚕", "5", "Entrenadores", "Ver todos", "cyan"),
                statCard("👤", "3", "Administrativos", "Ver todos", "red")
        );
        return cards;
    }

    private HBox statCard(String icon, String number, String title, String link, String color) {
        HBox card = new HBox(14);
        card.getStyleClass().add("dashboard-card");
        card.setPadding(new Insets(18));
        card.setPrefHeight(105);
        HBox.setHgrow(card, Priority.ALWAYS);

        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().addAll("card-icon", color);

        VBox texts = new VBox(4);

        Label n = new Label(number);
        n.getStyleClass().add("card-number");

        Label t = new Label(title);
        t.getStyleClass().add("card-title");

        Label l = new Label(link);
        l.getStyleClass().add("card-link");

        texts.getChildren().addAll(n, t, l);
        card.getChildren().addAll(iconLabel, texts);

        return card;
    }

    private VBox tableSection() {
        VBox wrapper = new VBox(0);
        wrapper.getStyleClass().add("panel");

        HBox tableHeader = new HBox(12);
        tableHeader.setPadding(new Insets(18));
        tableHeader.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Listado de Personas");
        title.getStyleClass().add("panel-title-dark");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button exportar = new Button("⇩ Exportar");
        exportar.getStyleClass().add("secondary-button");

        Button columnas = new Button("⚙ Columnas");
        columnas.getStyleClass().add("secondary-button");

        tableHeader.getChildren().addAll(title, spacer, exportar, columnas);

        GridPane table = new GridPane();
        table.getStyleClass().add("data-table");
        table.setPadding(new Insets(0, 16, 0, 16));
        table.setHgap(10);

        addHeaderRow(table);
        addPerson(table, 1, "👩", "María López Hernández", "15/03/2007\n17 años", "Femenino", "Deportista   Paciente", "Sub 17 Femenil", "55 1234 5678", "Activo");
        addPerson(table, 2, "👩", "Ana Sofía Pérez Ruiz", "22/06/2008\n15 años", "Femenino", "Deportista", "Sub 15 Femenil", "55 2345 6789", "Activo");
        addPerson(table, 3, "👩", "Fernanda García Morales", "10/11/2006\n17 años", "Femenino", "Deportista   Paciente", "Sub 17 Femenil", "55 3456 7890", "Activo");
        addPerson(table, 4, "👩", "Valeria Martínez López", "05/09/2009\n14 años", "Femenino", "Deportista   Alumna", "Sub 15 Femenil", "55 4567 8901", "Activo");
        addPerson(table, 5, "👩", "Diana Laura Torres Vega", "18/02/2007\n17 años", "Femenino", "Paciente", "Nutrición Deportiva", "55 5678 9012", "Activo");
        addPerson(table, 6, "👩", "Carolina Ramírez Salas", "30/07/2007\n16 años", "Femenino", "Deportista   Paciente", "Sub 17 Femenil", "55 6789 0123", "Inactivo");
        addPerson(table, 7, "👨", "Carlos Alberto Vega", "12/04/1985\n39 años", "Masculino", "Entrenador", "Fuerza y Acondicionamiento", "55 7890 1234", "Activo");
        addPerson(table, 8, "👩", "Lic. Andrea Núñez", "25/01/1990\n34 años", "Femenino", "Nutrióloga   Administrativo", "Nutrición", "55 8901 2345", "Activo");

        HBox pagination = new HBox(10);
        pagination.setPadding(new Insets(16));
        pagination.setAlignment(Pos.CENTER_RIGHT);

        Label info = new Label("Mostrando 1 a 8 de 128 registros");
        info.getStyleClass().add("muted-text");

        Region pSpacer = new Region();
        HBox.setHgrow(pSpacer, Priority.ALWAYS);

        Button prev = new Button("‹");
        Button one = new Button("1");
        Button two = new Button("2");
        Button three = new Button("3");
        Button next = new Button("›");

        one.getStyleClass().add("page-active");
        prev.getStyleClass().add("page-button");
        two.getStyleClass().add("page-button");
        three.getStyleClass().add("page-button");
        next.getStyleClass().add("page-button");

        ComboBox<String> perPage = new ComboBox<>();
        perPage.getItems().add("8 por página");
        perPage.setValue("8 por página");
        perPage.getStyleClass().add("combo-filter");

        pagination.getChildren().addAll(info, pSpacer, prev, one, two, three, next, perPage);

        wrapper.getChildren().addAll(tableHeader, table, pagination);
        return wrapper;
    }

    private void addHeaderRow(GridPane table) {
        String[] headers = {"Foto", "Nombre completo", "Fecha nacimiento", "Sexo", "Categoría / Rol", "Grupo / Equipo", "Teléfono", "Estado", "Acciones"};
        for (int i = 0; i < headers.length; i++) {
            Label label = new Label(headers[i]);
            label.getStyleClass().add("table-header");
            table.add(label, i, 0);
        }
    }

    private void addPerson(GridPane table, int row, String photo, String name, String birth, String sex, String role, String group, String phone, String status) {
        table.add(cell(photo, "photo-cell"), 0, row);
        table.add(cell(name, "table-cell-bold"), 1, row);
        table.add(cell(birth, "table-cell"), 2, row);
        table.add(cell(sex, "table-cell"), 3, row);
        table.add(cell(role, "table-cell"), 4, row);
        table.add(cell(group, "table-cell"), 5, row);
        table.add(cell(phone, "table-cell"), 6, row);
        table.add(cell(status, status.equals("Activo") ? "status-active" : "status-inactive"), 7, row);
        table.add(cell("👁   ✎   ⋮", "table-actions"), 8, row);
    }

    private Label cell(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        label.setMinHeight(54);
        return label;
    }
}