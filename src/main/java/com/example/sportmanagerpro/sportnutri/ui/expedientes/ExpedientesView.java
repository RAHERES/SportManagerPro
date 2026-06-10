package com.example.sportmanagerpro.sportnutri.ui.expedientes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ExpedientesView extends BorderPane {

    private final java.util.function.Consumer<String> router;

    public ExpedientesView(java.util.function.Consumer<String> router) {
        this.router = router;

        getStyleClass().add("dashboard-root");

        HBox layout = new HBox(20);
        layout.setPadding(new Insets(22, 26, 18, 26));

        VBox main = new VBox(20);
        HBox.setHgrow(main, Priority.ALWAYS);

        main.getChildren().addAll(
                header(),
                tabs(),
                summaryCards(),
                tablePanel()
        );


        layout.getChildren().addAll(main, rightPanel());
        setCenter(layout);
    }

    private HBox header() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);

        Label title = new Label("📁  Expedientes");
        title.getStyleClass().add("main-title");

        Label subtitle = new Label("Consulta y administra todos los expedientes del sistema");
        subtitle.getStyleClass().add("muted-text");

        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        TextField search = new TextField();
        search.setPromptText("Buscar personas, expedientes, planes, evaluaciones...");
        search.getStyleClass().add("input-search");
        search.setPrefWidth(420);

        Label icons = new Label("🔔  📅  ?  ●");
        icons.getStyleClass().add("top-icons");

        header.getChildren().addAll(titleBox, spacer, search, icons);
        return header;
    }

    private HBox tabs() {
        HBox tabs = new HBox(34);
        tabs.getStyleClass().add("tabs-bar");

        tabs.getChildren().addAll(
                tab("Todos", true),
                tab("Deportivos", false),
                tab("Nutricionales", false),
                tab("Clínicos", false),
                tab("Competitivos", false)
        );

        return tabs;
    }

    private Label tab(String text, boolean active) {
        Label label = new Label(text);
        label.getStyleClass().add(active ? "tab-active" : "tab");
        return label;
    }

    private HBox summaryCards() {
        HBox cards = new HBox(12);

        cards.getChildren().addAll(
                statCard("📁", "84", "Total expedientes", "Ver todos", "purple"),
                statCard("🏋", "38", "Deportivos", "Ver todos", "green"),
                statCard("🍎", "29", "Nutricionales", "Ver todos", "cyan"),
                statCard("⚕", "11", "Clínicos", "Ver todos", "orange"),
                statCard("🏆", "6", "Competitivos", "Ver todos", "purple")
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

    private VBox tablePanel() {
        VBox wrapper = new VBox(0);
        wrapper.getStyleClass().add("panel");

        HBox controls = new HBox(14);
        controls.setPadding(new Insets(18));
        controls.setAlignment(Pos.CENTER_LEFT);

        TextField search = new TextField();
        search.setPromptText("Buscar por nombre, tipo o ID de expediente...");
        search.getStyleClass().add("input-search");
        search.setPrefWidth(340);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button limpiar = new Button("⛌ Limpiar filtros");
        limpiar.getStyleClass().add("secondary-button");

        Button nuevo = new Button("+ Nuevo Expediente");
        nuevo.getStyleClass().add("primary-button");

        controls.getChildren().addAll(search, spacer, limpiar, nuevo);

        GridPane table = new GridPane();
        table.getStyleClass().add("data-table");
        table.setPadding(new Insets(0, 16, 0, 16));
        table.setHgap(10);

        addHeaderRow(table);

        addExpediente(table, 1, "👩", "María López Hernández\n17 años", "EXP-D-0001", "Deportivo", "15/01/2024", "22/05/2024", "Activo", "Entrenadora");
        addExpediente(table, 2, "👩", "Ana Sofía Pérez Ruiz\n15 años", "EXP-N-0002", "Nutricional", "20/02/2024", "21/05/2024", "Activo", "Nutrióloga");
        addExpediente(table, 3, "👩", "Fernanda García Morales\n17 años", "EXP-D-0003", "Deportivo", "10/01/2024", "19/05/2024", "Activo", "Entrenadora");
        addExpediente(table, 4, "👩", "Valeria Martínez López\n14 años", "EXP-N-0004", "Nutricional", "05/03/2024", "18/05/2024", "Activo", "Nutrióloga");
        addExpediente(table, 5, "👩", "Diana Laura Torres Vega\n17 años", "EXP-C-0005", "Clínico", "18/04/2024", "17/05/2024", "En seguimiento", "Médico");
        addExpediente(table, 6, "👩", "Carolina Ramírez Salas\n16 años", "EXP-D-0006", "Deportivo", "30/01/2024", "16/05/2024", "Activo", "Entrenadora");
        addExpediente(table, 7, "👩", "José Manuel Ortega\n19 años", "EXP-N-0007", "Nutricional", "12/02/2024", "15/05/2024", "Activo", "Nutrióloga");
        addExpediente(table, 8, "👩", "Andrea Núñez Sánchez\n34 años", "EXP-COMP-0001", "Competitivo", "25/03/2024", "14/05/2024", "Cerrado", "Entrenadora");

        HBox pagination = new HBox(10);
        pagination.setPadding(new Insets(16));
        pagination.setAlignment(Pos.CENTER_RIGHT);

        Label info = new Label("Mostrando 1 a 8 de 84 expedientes");
        info.getStyleClass().add("muted-text");

        Region pSpacer = new Region();
        HBox.setHgrow(pSpacer, Priority.ALWAYS);

        Button prev = page("‹", false);
        Button one = page("1", true);
        Button two = page("2", false);
        Button three = page("3", false);
        Button next = page("›", false);

        ComboBox<String> perPage = new ComboBox<>();
        perPage.getItems().add("8 por página");
        perPage.setValue("8 por página");
        perPage.getStyleClass().add("combo-filter");

        pagination.getChildren().addAll(info, pSpacer, prev, one, two, three, next, perPage);

        wrapper.getChildren().addAll(controls, table, pagination);
        return wrapper;
    }

    private Button page(String text, boolean active) {
        Button button = new Button(text);
        button.getStyleClass().add(active ? "page-active" : "page-button");
        return button;
    }

    private void addHeaderRow(GridPane table) {
        String[] headers = {
                "Persona", "ID Expediente", "Tipo", "Fecha apertura",
                "Última actualización", "Estado", "Responsable", "Acciones"
        };

        for (int i = 0; i < headers.length; i++) {
            Label label = new Label(headers[i]);
            label.getStyleClass().add("table-header");
            table.add(label, i, 0);
        }
    }

    private void addExpediente(
            GridPane table,
            int row,
            String photo,
            String persona,
            String id,
            String tipo,
            String apertura,
            String actualizacion,
            String estado,
            String responsable
    ) {
        table.add(cell(photo + "  " + persona, "table-cell-bold"), 0, row);
        table.add(cell(id, "table-cell"), 1, row);
        table.add(cell(tipo, badgeClass(tipo)), 2, row);
        table.add(cell(apertura, "table-cell"), 3, row);
        table.add(cell(actualizacion, "table-cell"), 4, row);
        table.add(cell(estado, estadoClass(estado)), 5, row);
        table.add(cell(responsable, "table-cell"), 6, row);
       // table.add(cell("👁   ✎   ⋮", "table-actions"), 7, row);
        HBox actions = new HBox(8);
        Button ver = new Button("👁");
        ver.getStyleClass().add("table-icon-button");
        ver.setOnAction(e -> router.accept("detalle-expediente"));

        Button editar = new Button("✎");
        editar.getStyleClass().add("table-icon-button");

        Button mas = new Button("⋮");
        mas.getStyleClass().add("table-icon-button");

        actions.getChildren().addAll(ver, editar, mas);
        table.add(actions, 7, row);
    }

    private String badgeClass(String tipo) {
        return switch (tipo) {
            case "Deportivo" -> "badge-green";
            case "Nutricional" -> "badge-blue";
            case "Clínico" -> "badge-orange";
            case "Competitivo" -> "badge-purple";
            default -> "table-cell";
        };
    }

    private String estadoClass(String estado) {
        return switch (estado) {
            case "Activo" -> "status-active";
            case "En seguimiento" -> "status-warning";
            case "Cerrado" -> "status-closed";
            default -> "table-cell";
        };
    }

    private Label cell(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        label.setMinHeight(58);
        label.setWrapText(true);
        return label;
    }

    private VBox rightPanel() {
        VBox side = new VBox(18);
        side.setPrefWidth(245);

        side.getChildren().addAll(
                filterPanel(),
                quickPanel()
        );

        return side;
    }

    private VBox filterPanel() {
        VBox panel = new VBox(12);
        panel.getStyleClass().add("panel");
        panel.setPadding(new Insets(18));

        Label title = new Label("⚱  Filtros");
        title.getStyleClass().add("panel-title-dark");

        TextField buscar = new TextField();
        buscar.setPromptText("Buscar en expedientes...");
        buscar.getStyleClass().add("input-search");

        ComboBox<String> tipo = combo("Tipo de expediente", "Todos");
        ComboBox<String> estado = combo("Estado", "Todos");
        ComboBox<String> responsable = combo("Responsable", "Todos");

        Button aplicar = new Button("Aplicar filtros");
        aplicar.getStyleClass().add("primary-outline-button");

        Button limpiar = new Button("⛌ Limpiar filtros");
        limpiar.getStyleClass().add("secondary-button");

        panel.getChildren().addAll(title, label("Buscar"), buscar, label("Tipo de expediente"), tipo, label("Estado"), estado, label("Responsable"), responsable, aplicar, limpiar);
        return panel;
    }

    private VBox quickPanel() {
        VBox panel = new VBox(10);
        panel.getStyleClass().add("panel");
        panel.setPadding(new Insets(18));

        Label title = new Label("⚡ Acciones rápidas");
        title.getStyleClass().add("panel-title-dark");

        panel.getChildren().addAll(
                title,
                action("📄 Exportar lista"),
                action("🖨 Imprimir lista"),
                action("📊 Reporte general"),
                action("🕘 Historial de cambios")
        );

        return panel;
    }

    private Label action(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("side-action");
        return label;
    }

    private Label label(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("filter-label");
        return label;
    }

    private ComboBox<String> combo(String prompt, String value) {
        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().add(value);
        combo.setValue(value);
        combo.setPromptText(prompt);
        combo.getStyleClass().add("combo-filter");
        combo.setMaxWidth(Double.MAX_VALUE);
        return combo;
    }
}