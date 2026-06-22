package com.example.sportmanagerpro.sportnutri.ui.util.entrenamiento;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class EntrenamientoView extends BorderPane {

    public EntrenamientoView() {
        getStyleClass().add("dashboard-root");

        VBox main = new VBox(18);
        main.setPadding(new Insets(20, 26, 30, 26));

        main.getChildren().addAll(
                header(),
                tabs(),
                summaryCards(),
                mainContent(),
                indicators()
        );

        ScrollPane scroll = new ScrollPane(main);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.getStyleClass().add("content-scroll");

        setCenter(scroll);
    }

    private HBox header() {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);
        Label title = new Label("👥  Entrenamiento");
        title.getStyleClass().add("main-title");

        Label subtitle = new Label("Planifica, organiza y da seguimiento al entrenamiento deportivo");
        subtitle.getStyleClass().add("muted-text");

        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        TextField search = new TextField();
        search.setPromptText("Buscar personas, planes, sesiones, ejercicios...");
        search.getStyleClass().add("input-search");
        search.setPrefWidth(410);

        Label icons = new Label("🔔  📅  ?  👩  Entrenadora");
        icons.getStyleClass().add("top-icons");

        box.getChildren().addAll(titleBox, spacer, search, icons);
        return box;
    }

    private HBox tabs() {
        HBox tabs = new HBox(34);
        tabs.getStyleClass().add("tabs-bar");

        tabs.getChildren().addAll(
                tab("Resumen", true),
                tab("Planes", false),
                tab("Macrociclos", false),
                tab("Mesociclos", false),
                tab("Microciclos", false),
                tab("Sesiones", false),
                tab("Ejercicios", false),
                tab("Asistencia", false),
                tab("Reportes", false)
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
                statCard("🧩", "3", "Planes activos", "Ver planes", "purple"),
                statCard("👥", "2", "Macrociclos activos", "Ver detalle", "green"),
                statCard("🗓", "5", "Microciclos activos", "Ver detalle", "orange"),
                statCard("🏃", "18", "Sesiones esta semana", "Ver calendario", "blue"),
                statCard("☑", "92%", "Asistencia promedio", "Ver reporte", "cyan"),
                statCard("👤", "24", "Ejercicios utilizados", "Ver biblioteca", "red")
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

    private HBox mainContent() {
        HBox layout = new HBox(18);
        layout.setFillHeight(true);


        VBox left = new VBox(18);
        left.setMinWidth(850);
        HBox.setHgrow(left, Priority.ALWAYS);


        left.getChildren().addAll(
                planGrafico(),
                lowerTrainingPanels()
        );

        VBox right = new VBox(18);
        right.setPrefWidth(330);
        right.setPrefWidth(380);
        right.setMaxWidth(400);

        right.getChildren().addAll(
                nextSessions(),
                exerciseLibrary()
        );

        layout.getChildren().addAll(left, right);
        return layout;
    }

    private VBox planGrafico() {
        VBox panel = panel("☷  PLAN GRÁFICO GENERAL");

        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ComboBox<String> plan = new ComboBox<>();
        plan.getItems().add("Plan anual 2024 - 2025");
        plan.setValue("Plan anual 2024 - 2025");
        plan.getStyleClass().add("combo-filter");

        Button prev = new Button("‹");
        prev.getStyleClass().add("page-button");

        Button next = new Button("›");
        next.getStyleClass().add("page-button");

        ComboBox<String> period = new ComboBox<>();
        period.getItems().add("Ago 2024 - Jul 2025");
        period.setValue("Ago 2024 - Jul 2025");
        period.getStyleClass().add("combo-filter");

        Button custom = new Button("⚙ Personalizar vista");
        custom.getStyleClass().add("secondary-button");

        controls.getChildren().addAll(spacer, plan, prev, next, period, custom);

        GridPane graph = new GridPane();
        graph.getStyleClass().add("training-graph");
        graph.setHgap(6);
        graph.setVgap(12);
        graph.setPadding(new Insets(14, 0, 10, 0));

        addMonths(graph);
        addPlanRow(graph, 1, "PERIODOS", new String[]{"PREPARATORIO", "COMPETITIVO", "TRANSICIÓN"}, new int[]{6, 4, 1}, "period");
        addPlanRow(graph, 2, "ETAPAS", new String[]{"GENERAL", "ESPECÍFICA", "PRECOMPETITIVA", "COMPETITIVA", "TRANSICIÓN"}, new int[]{2, 2, 2, 3, 1}, "stage");
        addPlanRow(graph, 3, "MESOCICLOS", new String[]{"I1", "I2", "D1", "D2", "D3", "PC1", "PC2", "C1", "C2", "C3", "T"}, new int[]{1,1,1,1,1,1,1,1,1,1,1}, "meso");
        addMicrocycles(graph);

        HBox legend = new HBox(18);
        legend.getChildren().addAll(
                legend("●", "Introductorio", "green-text"),
                legend("●", "Desarrollador", "green-text"),
                legend("●", "Precompetitivo", "blue-text"),
                legend("●", "Competitivo", "blue-text"),
                legend("●", "Recuperador", "pink-text"),
                legend("●", "Transición", "orange-text"),
                legend("▢", "Control / Evaluación", "muted-text")
        );

        panel.getChildren().addAll(controls, graph, legend);
        return panel;
    }

    private void addMonths(GridPane graph) {
        graph.add(emptyCell(""), 0, 0);

        String[] months = {"AGO", "SEP", "OCT", "NOV", "DIC", "ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL"};
        for (int i = 0; i < months.length; i++) {
            Label month = graphCell(months[i], "graph-month");
            graph.add(month, i + 1, 0);
        }
    }

    private void addPlanRow(GridPane graph, int row, String title, String[] values, int[] spans, String type) {
        graph.add(rowTitle(title), 0, row);

        int column = 1;
        for (int i = 0; i < values.length; i++) {
            Label label = graphCell(values[i], switch (type) {
                case "period" -> i == values.length - 1 ? "graph-orange" : i == 0 ? "graph-green" : "graph-blue";
                case "stage" -> i == values.length - 1 ? "graph-orange" : i < 2 ? "graph-green" : "graph-blue";
                default -> i < 5 ? "graph-green" : i == values.length - 1 ? "graph-orange" : "graph-blue";
            });
            graph.add(label, column, row, spans[i], 1);
            column += spans[i];
        }
    }

    private void addMicrocycles(GridPane graph) {
        graph.add(rowTitle("MICROCICLOS"), 0, 4);

        for (int i = 1; i <= 52; i++) {
            Label label = graphCell(String.valueOf(i), i <= 20 ? "micro-green" : i <= 46 ? "micro-blue" : "micro-orange");
            label.setMinWidth(18);
            label.setPrefWidth(18);
            graph.add(label, i, 4);
        }
    }

    private Label rowTitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("graph-row-title");
        return label;
    }

    private Label graphCell(String text, String style) {
        Label label = new Label(text);
        label.getStyleClass().add(style);
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setMinHeight(32);
        return label;
    }

    private Label emptyCell(String text) {
        Label label = new Label(text);
        label.setMinWidth(85);
        return label;
    }

    private Label legend(String symbol, String text, String style) {
        Label label = new Label(symbol + " " + text);
        label.getStyleClass().add(style);
        return label;
    }

    private HBox lowerTrainingPanels() {
        HBox box = new HBox(18);

        VBox micro = panel("MICROCICLOS ACTIVOS");
        micro.getChildren().addAll(
                microRow("MC-18", "Carga", "20 - 26 May", "5", "Alta", "Desarrollo de fuerza máxima", "En curso"),
                microRow("MC-19", "Competitivo", "27 May - 02 Jun", "6", "Media", "Afianzamiento táctico", "Próximo"),
                microRow("MC-20", "Choque", "03 - 09 Jun", "5", "Alta", "Estímulo de intensidad", "Programado"),
                microRow("MC-21", "Recuperación", "10 - 16 Jun", "4", "Baja", "Recuperación y descarga", "Programado")
        );

        VBox carga = panel("DISTRIBUCIÓN DE CARGA (ÚLTIMAS 4 SEMANAS)");
        carga.getChildren().add(simpleChart());

        HBox.setHgrow(micro, Priority.ALWAYS);
        HBox.setHgrow(carga, Priority.ALWAYS);

        box.getChildren().addAll(micro, carga);
        return box;
    }

    private HBox microRow(String micro, String tipo, String fechas, String sesiones, String carga, String objetivo, String estado) {
        HBox row = new HBox(12);
        row.getStyleClass().add("event-row");
        row.setPadding(new Insets(10));

        row.getChildren().addAll(
                smallCell(micro, 65),
                smallCell(tipo, 80),
                smallCell(fechas, 95),
                smallCell(sesiones, 60),
                smallCell(carga, 65),
                smallCell(objetivo, 180),
                smallCell(estado, 80)
        );

        return row;
    }

    private Label smallCell(String text, int width) {
        Label label = new Label(text);
        label.getStyleClass().add("table-cell");
        label.setPrefWidth(width);
        return label;
    }

    private VBox simpleChart() {
        VBox box = new VBox(12);
        box.setAlignment(Pos.CENTER);

        HBox bars = new HBox(24);
        bars.setAlignment(Pos.BOTTOM_CENTER);
        bars.setPadding(new Insets(20));

        bars.getChildren().addAll(
                bar("Semana 15", 90),
                bar("Semana 16", 120),
                bar("Semana 17", 170),
                bar("Semana 18", 140)
        );

        Button detail = new Button("Ver análisis completo");
        detail.getStyleClass().add("primary-outline-button");
        detail.setMaxWidth(Double.MAX_VALUE);

        box.getChildren().addAll(bars, detail);
        return box;
    }

    private VBox bar(String label, int height) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.BOTTOM_CENTER);

        Region bar = new Region();
        bar.getStyleClass().add("chart-bar");
        bar.setPrefSize(36, height);

        Label l = new Label(label);
        l.getStyleClass().add("muted-text");

        box.getChildren().addAll(bar, l);
        return box;
    }

    private VBox nextSessions() {
        VBox panel = panel("PRÓXIMAS SESIONES");

        panel.getChildren().addAll(
                session("MAY\n24\nVie", "Fuerza máxima - Tren inferior", "07:00 - 08:30 AM\nGimnasio"),
                session("MAY\n25\nSáb", "Táctica ofensiva + Posesión", "09:00 - 10:30 AM\nCancha 1"),
                session("MAY\n27\nLun", "Resistencia aeróbica", "07:00 - 08:00 AM\nPista"),
                session("MAY\n28\nMar", "Juego aplicado 11v11", "09:00 - 10:30 AM\nCancha 1"),
                session("MAY\n30\nJue", "Fuerza explosiva + Core", "07:00 - 08:15 AM\nGimnasio"),
                link("Ver todas las sesiones →")
        );

        return panel;
    }

    private HBox session(String date, String title, String detail) {
        HBox row = new HBox(12);
        row.getStyleClass().add("event-row");
        row.setPadding(new Insets(10));

        Label d = new Label(date);
        d.getStyleClass().add("date-box");
        d.setMinWidth(58);

        VBox info = new VBox(4);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label t = new Label(title);
        t.getStyleClass().add("table-cell-bold");
        t.setWrapText(true);
        t.setMaxWidth(240);

        Label sub = new Label(detail);
        sub.getStyleClass().add("muted-text");
        sub.setWrapText(true);
        sub.setMaxWidth(240);

        Label tag = new Label("Programada");
        tag.getStyleClass().add("status-active-pill");

        info.getChildren().addAll(t, sub, tag);
        row.getChildren().addAll(d, info);

        return row;
    }

    private VBox exerciseLibrary() {
        VBox panel = panel("BIBLIOTECA RÁPIDA");

        panel.getChildren().addAll(
                exercise("Sentadilla trasera", "Fuerza"),
                exercise("Pase en triángulo", "Técnico"),
                exercise("Juego de posesión 6v3", "Táctico"),
                exercise("Plancha frontal", "Fuerza"),
                exercise("Sprints 30 m", "Velocidad"),
                link("Ir a biblioteca completa  ›")
        );

        return panel;
    }

    private HBox exercise(String name, String type) {
        HBox row = new HBox(10);
        row.getStyleClass().add("event-row");
        row.setPadding(new Insets(8));

        Label img = new Label("▧");
        img.getStyleClass().add("event-icon");

        Label n = new Label(name);
        n.getStyleClass().add("table-cell-bold");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label tag = new Label(type);
        tag.getStyleClass().add("badge-blue-pill");

        row.getChildren().addAll(img, n, spacer, tag);
        return row;
    }

    private HBox indicators() {
        HBox panel = new HBox(28);
        panel.getStyleClass().add("panel");
        panel.setPadding(new Insets(18));

        panel.getChildren().addAll(
                indicator("Volumen semanal", "1,245 u.a.", "+12% vs semana anterior"),
                indicator("Intensidad promedio", "7.2 / 10", "Adecuada"),
                indicator("Carga aguda", "485 u.a.", "Óptima"),
                indicator("Carga crónica", "1,920 u.a.", "Óptima"),
                indicator("Relación carga", "0.85", "Óptima"),
                indicator("Monotonía", "1.32", "Adecuada"),
                indicator("Fatiga", "Baja", "Sin riesgo")
        );

        return panel;
    }

    private VBox indicator(String title, String value, String status) {
        VBox box = new VBox(5);
        HBox.setHgrow(box, Priority.ALWAYS);

        Label t = new Label(title);
        t.getStyleClass().add("muted-text");

        Label v = new Label(value);
        v.getStyleClass().add("card-number");

        Label s = new Label(status);
        s.getStyleClass().add("status-active-pill");

        box.getChildren().addAll(t, v, s);
        return box;
    }

    private VBox panel(String title) {
        VBox panel = new VBox(12);
        panel.getStyleClass().add("panel");
        panel.setPadding(new Insets(16));

        Label label = new Label(title);
        label.getStyleClass().add("panel-title-dark");

        panel.getChildren().add(label);
        return panel;
    }

    private Label link(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("panel-link");
        return label;
    }
}