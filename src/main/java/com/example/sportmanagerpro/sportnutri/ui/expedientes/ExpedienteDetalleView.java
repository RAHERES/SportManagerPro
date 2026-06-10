package com.example.sportmanagerpro.sportnutri.ui.expedientes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ExpedienteDetalleView extends BorderPane {

    public ExpedienteDetalleView() {
        getStyleClass().add("dashboard-root");

        VBox main = new VBox(18);
        main.setPadding(new Insets(20, 26, 30, 26));

        main.getChildren().addAll(
                topHeader(),
                personHeader(),
                infoStrip(),
                tabs(),
                contentGrid()
        );

        ScrollPane scrollPane = new ScrollPane(main);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("content-scroll");

        setCenter(scrollPane);
    }


    private HBox topHeader() {
        HBox box = new HBox(16);
        box.setAlignment(Pos.CENTER_LEFT);

        Label breadcrumb = new Label("Expedientes  ›  Detalle del expediente");
        breadcrumb.getStyleClass().add("muted-text");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        TextField search = new TextField();
        search.setPromptText("Buscar en expediente...");
        search.getStyleClass().add("input-search");
        search.setPrefWidth(330);

        Label icons = new Label("🔔  📅  ?  👩");
        icons.getStyleClass().add("top-icons");

        box.getChildren().addAll(breadcrumb, spacer, search, icons);
        return box;
    }

    private HBox personHeader() {
        HBox box = new HBox(18);
        box.setAlignment(Pos.CENTER_LEFT);

        Label avatar = new Label("👩");
        avatar.getStyleClass().add("detail-avatar");

        VBox nameBox = new VBox(8);

        HBox nameRow = new HBox(12);
        nameRow.setAlignment(Pos.CENTER_LEFT);

        Label name = new Label("María López Hernández");
        name.getStyleClass().add("detail-name");

        Label status = new Label("Activo");
        status.getStyleClass().add("status-active-pill");

        nameRow.getChildren().addAll(name, status);

        HBox meta = new HBox(26);
        meta.getChildren().addAll(
                smallText("♀  Femenino"),
                smallText("📅  15/03/2007 (17 años)"),
                smallText("☎  55 1234 5678"),
                smallText("✉  maria.lopez@email.com")
        );

        nameBox.getChildren().addAll(nameRow, meta);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button print = new Button("🖨  Imprimir expediente");
        print.getStyleClass().add("secondary-button");

        Button edit = new Button("✎  Editar expediente");
        edit.getStyleClass().add("primary-button");

        box.getChildren().addAll(avatar, nameBox, spacer, print, edit);
        return box;
    }

    private HBox infoStrip() {
        HBox strip = new HBox();
        strip.getStyleClass().add("panel");
        strip.setPadding(new Insets(16));
        strip.setSpacing(24);

        strip.getChildren().addAll(
                infoBlock("Institución", "Instituto Villa de los Niños"),
                infoBlock("Grado / Grupo", "2° Secundaria / B"),
                infoBlock("Categoría deportiva", "Sub 17 Femenil"),
                infoBlock("Posición", "Mediocampista"),
                infoBlock("Fecha de ingreso", "10/01/2023"),
                infoBlock("Expedientes activos", "Deportivo   Nutricional   Clínico   Competitivo")
        );

        return strip;
    }

    private VBox infoBlock(String title, String value) {
        VBox box = new VBox(6);
        box.setPrefWidth(170);

        Label t = new Label(title);
        t.getStyleClass().add("muted-text");

        Label v = new Label(value);
        v.getStyleClass().add("table-cell-bold");
        v.setWrapText(true);

        box.getChildren().addAll(t, v);
        return box;
    }

    private HBox tabs() {
        HBox tabs = new HBox(34);
        tabs.getStyleClass().add("tabs-bar");

        tabs.getChildren().addAll(
                tab("Resumen general", true),
                tab("Información personal", false),
                tab("Historial", false),
                tab("Evaluaciones", false),
                tab("Planes", false),
                tab("Seguimientos", false),
                tab("Documentos", false)
        );

        return tabs;
    }

    private Label tab(String text, boolean active) {
        Label label = new Label(text);
        label.getStyleClass().add(active ? "tab-active" : "tab");
        return label;
    }

    private GridPane contentGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(16);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(24);

        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(24);

        ColumnConstraints c3 = new ColumnConstraints();
        c3.setPercentWidth(24);

        ColumnConstraints c4 = new ColumnConstraints();
        c4.setPercentWidth(28);

        grid.getColumnConstraints().addAll(c1, c2, c3, c4);

        grid.add(personalInfoCard(), 0, 0);
        grid.add(rolesCard(), 1, 0);
        grid.add(notesCard(), 2, 0);
        grid.add(actionsCard(), 3, 0);

        grid.add(sportCard(), 0, 1);
        grid.add(nutritionCard(), 1, 1);
        grid.add(clinicalCard(), 2, 1);
        grid.add(competitiveCard(), 3, 1);

        return grid;
    }

    private VBox personalInfoCard() {
        VBox card = card("👤  Información personal");

        card.getChildren().addAll(
                row("Fecha de nacimiento:", "15/03/2007"),
                row("Edad:", "17 años"),
                row("Sexo:", "Femenino"),
                row("Teléfono:", "55 1234 5678"),
                row("Correo:", "maria.lopez@email.com"),
                row("Dirección:", "Av. Central 123, Col. Centro,\nCiudad de México"),
                row("Tutor / Responsable:", "Laura Hernández"),
                row("Tel. Tutor:", "55 8765 4321"),
                outlineButton("Ver información completa")
        );

        return card;
    }

    private VBox rolesCard() {
        VBox card = card("👥  Roles y perfiles");

        card.getChildren().addAll(
                role("Deportista", "Activo", "green"),
                role("Paciente nutricional", "Activo", "blue"),
                role("Alumna", "Activo", "orange"),
                role("Seleccionada Sub 17", "Activo", "blue"),
                role("Capitana", "Inactivo", "red"),
                outlineButton("Gestionar roles")
        );

        return card;
    }

    private VBox notesCard() {
        VBox card = card("📋  Notas importantes");

        card.getChildren().addAll(
                note("Entrenadora", "22/05/2024", "Excelente actitud en entrenamientos. Destaca en resistencia y visión de juego."),
                note("Nutrióloga", "20/05/2024", "Buena adherencia al plan alimentario. Reforzar hidratación en entrenamientos."),
                outlineButton("Ver todas las notas")
        );

        return card;
    }

    private VBox actionsCard() {
        VBox card = card("⚡  Acciones rápidas");

        card.getChildren().addAll(
                action("Nueva evaluación"),
                action("Nueva cita nutricional"),
                action("Nuevo plan alimentario"),
                action("Nueva sesión de entrenamiento"),
                action("Agregar documento"),
                action("Registrar lesión / incidencia"),
                action("Generar reporte"),
                action("Ver calendario de esta persona")
        );

        return card;
    }

    private VBox sportCard() {
        VBox card = card("🏃  Expediente Deportivo     Activo");

        card.getChildren().addAll(
                row("Entrenadora responsable", "Ana Torres"),
                row("Categoría / Equipo", "Sub 17 Femenil"),
                row("Posición principal", "Mediocampista"),
                row("Fecha apertura", "10/01/2023"),
                greenButton("Ver expediente deportivo")
        );

        return card;
    }

    private VBox nutritionCard() {
        VBox card = card("🍎  Expediente Nutricional     Activo");

        card.getChildren().addAll(
                row("Nutrióloga responsable", "Lic. Andrea Núñez"),
                row("Objetivo nutricional", "Rendimiento deportivo"),
                row("Última actualización", "21/05/2024"),
                row("Próxima cita", "28/05/2024 - 11:00 AM"),
                blueButton("Ver expediente nutricional")
        );

        return card;
    }

    private VBox clinicalCard() {
        VBox card = card("⚕  Expediente Clínico     Activo");

        card.getChildren().addAll(
                row("Médico responsable", "Dr. Carlos Méndez"),
                row("Estado de salud", "Sin restricciones"),
                row("Última revisión", "18/04/2024"),
                row("Próxima revisión", "18/07/2024"),
                orangeButton("Ver expediente clínico")
        );

        return card;
    }

    private VBox competitiveCard() {
        VBox card = card("🏆  Expediente Competitivo     Activo");

        card.getChildren().addAll(
                row("Participaciones", "8 torneos"),
                row("Partidos jugados", "24"),
                row("Goles anotados", "6"),
                row("Asistencias", "5"),
                purpleButton("Ver expediente competitivo")
        );

        return card;
    }

    private VBox card(String title) {
        VBox card = new VBox(12);
        card.getStyleClass().add("panel");
        card.setPadding(new Insets(16));

        Label label = new Label(title);
        label.getStyleClass().add("panel-title-dark");

        card.getChildren().add(label);
        return card;
    }

    private HBox row(String label, String value) {
        HBox row = new HBox(10);

        Label l = new Label(label);
        l.getStyleClass().add("muted-text");
        l.setPrefWidth(145);

        Label v = new Label(value);
        v.getStyleClass().add("table-cell-bold");
        v.setWrapText(true);

        row.getChildren().addAll(l, v);
        return row;
    }

    private HBox role(String role, String status, String color) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Label name = new Label("●  " + role);
        name.getStyleClass().add("table-cell-bold");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label badge = new Label(status);
        badge.getStyleClass().add(switch (color) {
            case "green" -> "status-active-pill";
            case "blue" -> "badge-blue-pill";
            case "orange" -> "badge-orange-pill";
            case "red" -> "status-inactive-pill";
            default -> "badge-blue-pill";
        });

        row.getChildren().addAll(name, spacer, badge);
        return row;
    }

    private VBox note(String author, String date, String text) {
        VBox note = new VBox(6);
        note.getStyleClass().add("note-card");
        note.setPadding(new Insets(12));

        HBox top = new HBox();

        Label a = new Label(author);
        a.getStyleClass().add("table-cell-bold");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label d = new Label(date);
        d.getStyleClass().add("muted-text");

        top.getChildren().addAll(a, spacer, d);

        Label body = new Label(text);
        body.setWrapText(true);
        body.getStyleClass().add("table-cell");

        note.getChildren().addAll(top, body);
        return note;
    }

    private Label action(String text) {
        Label label = new Label(text + "    ›");
        label.getStyleClass().add("side-action");
        return label;
    }

    private Button outlineButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("primary-outline-button");
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private Button greenButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("success-soft-button");
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private Button blueButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("blue-soft-button");
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private Button orangeButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("orange-soft-button");
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private Button purpleButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("purple-soft-button");
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private Label smallText(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("muted-text");
        return label;
    }
}