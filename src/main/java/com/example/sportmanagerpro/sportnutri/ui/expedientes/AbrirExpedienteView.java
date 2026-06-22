package com.example.sportmanagerpro.sportnutri.ui.expedientes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AbrirExpedienteView extends BorderPane {

    public AbrirExpedienteView() {
        getStyleClass().add("dashboard-root");

        VBox main = new VBox(18);
        main.setPadding(new Insets(22, 26, 28, 26));

        main.getChildren().addAll(
                header(),
                searchSection()
        );

        setCenter(main);
    }

    private HBox header() {
        HBox box = new HBox(16);
        box.setAlignment(Pos.CENTER_LEFT);

        Label icon = new Label("📁");
        icon.getStyleClass().add("open-exp-main-icon");

        VBox titles = new VBox(4);
        Label title = new Label("Abrir expediente");
        title.getStyleClass().add("main-title");

        Label subtitle = new Label("Busca y selecciona una persona para ver su información completa");
        subtitle.getStyleClass().add("muted-text");

        titles.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button help = new Button("ⓘ  Ayuda");
        help.getStyleClass().add("secondary-button");

        box.getChildren().addAll(icon, titles, spacer, help);
        return box;
    }

    private HBox searchSection() {
        HBox root = new HBox(18);

        VBox left = new VBox(18);
        HBox.setHgrow(left, Priority.ALWAYS);

        left.getChildren().addAll(
                searchPanel(),
                resultsPanel(),
                bottomCards()
        );

        VBox right = previewPanel();
        right.setMinWidth(390);
        right.setPrefWidth(410);
        right.setMaxWidth(430);

        root.getChildren().addAll(left, right);
        return root;
    }

    private VBox searchPanel() {
        VBox panel = new VBox(12);
        panel.getStyleClass().add("open-exp-panel");

        Label title = new Label("Buscar persona");
        title.getStyleClass().add("panel-title-dark");

        HBox searchRow = new HBox(14);
        searchRow.setAlignment(Pos.CENTER_LEFT);

        TextField search = new TextField();
        search.setPromptText("Buscar por nombre, apellido, correo o teléfono...");
        search.getStyleClass().add("open-exp-search");
        HBox.setHgrow(search, Priority.ALWAYS);

        Button filters = new Button("⚱  Filtros");
        filters.getStyleClass().add("primary-outline-button");
        filters.setPrefWidth(105);

        searchRow.getChildren().addAll(search, filters);

        HBox filtersRow = new HBox(14);
        filtersRow.getChildren().addAll(
                filterButton("Estado: Activo"),
                filterButton("Tipo: Todos"),
                filterButton("Entrenador: Todos"),
                clearFilters()
        );

        panel.getChildren().addAll(title, searchRow, filtersRow);
        return panel;
    }

    private Button filterButton(String text) {
        Button button = new Button(text + " ⌄");
        button.getStyleClass().add("open-exp-filter-button");
        return button;
    }

    private Label clearFilters() {
        Label label = new Label("Limpiar filtros");
        label.getStyleClass().add("card-link");
        label.setPadding(new Insets(10, 0, 0, 6));
        return label;
    }

    private VBox resultsPanel() {
        VBox panel = new VBox(0);
        panel.getStyleClass().add("open-exp-panel");

        HBox header = new HBox();
        header.setPadding(new Insets(0, 0, 12, 0));
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Resultados (24)");
        title.getStyleClass().add("panel-title-dark");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label sort = new Label("Ordenar por:  Nombre A-Z ⌄");
        sort.getStyleClass().add("muted-text");

        header.getChildren().addAll(title, spacer, sort);

        VBox list = new VBox(0);
        list.getChildren().addAll(
                personRow("👩", "María Fernanda López", "28 años  •  Nutrición y Entrenamiento", "Activo", true),
                personRow("👨", "Juan Carlos Pérez", "32 años  •  Entrenamiento", "Activo", false),
                personRow("👩", "Ana Sofía Martínez", "24 años  •  Nutrición", "Activo", false),
                personRow("👨", "Luis Alberto García", "30 años  •  Entrenamiento", "Activo", false),
                personRow("👩", "Carla Rodríguez", "27 años  •  Nutrición", "Inactivo", false),
                personRow("👨", "Diego Ramírez", "35 años  •  Entrenamiento", "Activo", false)
        );

        HBox pages = pagination();

        panel.getChildren().addAll(header, list, pages);
        return panel;
    }

    private HBox personRow(String avatar, String name, String detail, String status, boolean selected) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add(selected ? "open-exp-result-selected" : "open-exp-result");

        Label photo = new Label(avatar);
        photo.getStyleClass().add("open-exp-avatar");

        VBox texts = new VBox(4);
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("open-exp-name");

        HBox detailLine = new HBox(8);
        Label detailLabel = new Label(detail);
        detailLabel.getStyleClass().add("muted-text");

        Label statusBadge = new Label(status);
        statusBadge.getStyleClass().add(status.equals("Activo") ? "status-active-pill" : "status-closed-pill");

        detailLine.getChildren().addAll(detailLabel, statusBadge);
        texts.getChildren().addAll(nameLabel, detailLine);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button open = new Button("📁  Abrir expediente");
        open.getStyleClass().add("open-exp-open-button");

        Label arrow = new Label("›");
        arrow.getStyleClass().add("open-exp-arrow");

        row.getChildren().addAll(photo, texts, spacer, open, arrow);
        return row;
    }

    private HBox pagination() {
        HBox box = new HBox(8);
        box.setPadding(new Insets(16, 0, 0, 0));
        box.setAlignment(Pos.CENTER);

        Button prev = page("‹  Anterior", false);
        Button one = page("1", true);
        Button two = page("2", false);
        Button three = page("3", false);
        Button dots = page("...", false);
        Button five = page("5", false);
        Button next = page("Siguiente  →", false);

        box.getChildren().addAll(prev, one, two, three, dots, five, next);
        return box;
    }

    private Button page(String text, boolean active) {
        Button b = new Button(text);
        b.getStyleClass().add(active ? "page-active" : "page-button");
        return b;
    }

    private HBox bottomCards() {
        HBox box = new HBox(16);

        VBox missing = bottomCard(
                "¿No encuentras a la persona?",
                "Puedes agregar una nueva persona al sistema",
                "👤  Agregar nueva persona",
                "open-exp-bottom-purple"
        );

        VBox advanced = bottomCard(
                "Búsqueda avanzada",
                "Usa filtros adicionales para encontrar\nresultados más específicos",
                "⚱  Búsqueda avanzada",
                "open-exp-bottom-blue"
        );

        HBox.setHgrow(missing, Priority.ALWAYS);
        HBox.setHgrow(advanced, Priority.ALWAYS);

        box.getChildren().addAll(missing, advanced);
        return box;
    }

    private VBox bottomCard(String title, String text, String buttonText, String style) {
        VBox card = new VBox(12);
        card.getStyleClass().add(style);
        card.setPadding(new Insets(18));

        Label t = new Label(title);
        t.getStyleClass().add("panel-title-dark");

        Label d = new Label(text);
        d.getStyleClass().add("muted-text");

        Button b = new Button(buttonText);
        b.getStyleClass().add("primary-outline-button");
        b.setMaxWidth(Double.MAX_VALUE);

        card.getChildren().addAll(t, d, b);
        return card;
    }

    private VBox previewPanel() {
        VBox panel = new VBox(18);
        panel.getStyleClass().add("open-exp-panel");

        Label title = new Label("Vista previa del expediente");
        title.getStyleClass().add("panel-title-dark");

        HBox identity = new HBox(16);
        identity.setAlignment(Pos.CENTER_LEFT);

        Label avatar = new Label("👩");
        avatar.getStyleClass().add("open-exp-preview-avatar");

        VBox data = new VBox(6);

        HBox nameLine = new HBox(8);
        nameLine.setAlignment(Pos.CENTER_LEFT);

        Label name = new Label("María Fernanda López");
        name.getStyleClass().add("open-exp-preview-name");

        Label active = new Label("Activo");
        active.getStyleClass().add("status-active-pill");

        nameLine.getChildren().addAll(name, active);

        data.getChildren().addAll(
                nameLine,
                small("👤  28 años  •  Femenino"),
                small("✉  maria.lopez@email.com"),
                small("☎  55 1234 5678")
        );

        identity.getChildren().addAll(avatar, data);

        GridPane miniCards = new GridPane();
        miniCards.setHgap(8);
        miniCards.setVgap(8);

        miniCards.add(mini("📋", "Tipo de cliente", "Nutrición y\nEntrenamiento"), 0, 0);
        miniCards.add(mini("📅", "Fecha de alta", "12/01/2024"), 1, 0);
        miniCards.add(mini("〽", "Última actividad", "20/05/2024"), 0, 1);
        miniCards.add(mini("👤", "Entrenador", "Carlos Ramírez"), 1, 1);

        Label infoTitle = new Label("Información registrada");
        infoTitle.getStyleClass().add("panel-title-dark");

        VBox info = new VBox(6);
        info.getChildren().addAll(
                infoRow("📋", "Evaluaciones físicas", "Última: 15/05/2024", "3"),
                infoRow("🏋", "Planes de entrenamiento", "Activo: Fuerza - intermedio", "1"),
                infoRow("🍎", "Planes nutricionales", "Activo: Plan hipocalórico", "1"),
                infoRow("📊", "Mediciones corporales", "Última: 15/05/2024", "4"),
                infoRow("📅", "Citas y sesiones", "Próxima: 27/05/2024", "2"),
                infoRow("📝", "Notas y observaciones", "Notas registradas", "6")
        );

        Button openFull = new Button("📁  Abrir expediente completo   →");
        openFull.getStyleClass().add("primary-button");
        openFull.setMaxWidth(Double.MAX_VALUE);
        openFull.setPrefHeight(46);

        panel.getChildren().addAll(title, identity, miniCards, infoTitle, info, openFull);
        return panel;
    }

    private VBox mini(String icon, String title, String value) {
        VBox box = new VBox(5);
        box.getStyleClass().add("open-exp-mini-card");
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(180);
        box.setMinHeight(86);

        Label i = new Label(icon);
        i.getStyleClass().add("preview-row-icon");

        Label t = new Label(title);
        t.getStyleClass().add("muted-text");
        t.setWrapText(true);

        Label v = new Label(value);
        v.getStyleClass().add("open-exp-mini-value");
        v.setWrapText(true);

        box.getChildren().addAll(i, t, v);
        return box;
    }

    private Label small(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("muted-text");
        return label;
    }

    /*private VBox mini(String icon, String title, String value) {
        VBox box = new VBox(6);
        box.getStyleClass().add("open-exp-mini-card");
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(112);

        Label i = new Label(icon);
        i.getStyleClass().add("preview-row-icon");

        Label t = new Label(title);
        t.getStyleClass().add("muted-text");
        t.setWrapText(true);

        Label v = new Label(value);
        v.getStyleClass().add("table-cell-bold");
        v.setWrapText(true);

        box.getChildren().addAll(i, t, v);
        return box;
    }*/

    private HBox infoRow(String icon, String title, String subtitle, String count) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("open-exp-info-row");

        Label i = new Label(icon);
        i.getStyleClass().add("preview-row-icon");

        VBox texts = new VBox(3);
        Label t = new Label(title);
        t.getStyleClass().add("open-exp-name");

        Label s = new Label(subtitle);
        s.getStyleClass().add("muted-text");

        texts.getChildren().addAll(t, s);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label badge = new Label(count);
        badge.getStyleClass().add("open-exp-count");

        Label arrow = new Label("›");
        arrow.getStyleClass().add("open-exp-arrow");

        row.getChildren().addAll(i, texts, spacer, badge, arrow);
        return row;
    }
}