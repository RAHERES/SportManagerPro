package com.example.sportmanagerpro.nutricion.ui;

import com.example.sportmanagerpro.nutricion.ui.components.*;
import javafx.geometry.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;

/**
 * Vista integral del expediente de pacientes/clientes.
 * No utiliza CSS externo.
 */
public class PatientsModuleView extends BorderPane {
    public PatientsModuleView() {
        setBackground(new Background(new BackgroundFill(Ui.APP_BG, CornerRadii.EMPTY, Insets.EMPTY)));
        setLeft(new NavigationSidebar());
        setTop(new TopBar());
        setCenter(createCenter());
    }

    private HBox createCenter() {
        HBox content = new HBox(15);
        content.setPadding(new Insets(15));
        content.setAlignment(Pos.TOP_LEFT);
        PatientSearchPanel left = new PatientSearchPanel();
        VBox expediente = createRecord();
        VBox right = createRightSummary();
        HBox.setHgrow(expediente, Priority.ALWAYS);
        content.getChildren().addAll(left, expediente, right);
        return content;
    }

    private VBox createRecord() {
        VBox box = new VBox(14);
        box.setMinWidth(800);
        box.setPrefWidth(900);
        box.setMaxWidth(Double.MAX_VALUE);
        box.getChildren().addAll(new PatientHeaderCard(), createGetCard(), createMacroCard(), createEquivalentsCard(), createActions());
        return box;
    }

    private VBox createGetCard() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(18));
        Ui.card(card, 16, 0.05);
        Label title = Ui.label("Gasto energético total (GET)", 15, FontWeight.EXTRA_BOLD, Ui.TEXT);
        HBox formula = new HBox(16);
        formula.setAlignment(Pos.CENTER_LEFT);
        formula.getChildren().addAll(
                new FormulaCard("GEB", "1,450", "kcal", false), op("+"),
                new FormulaCard("ETA", "145", "kcal", false), op("+"),
                new FormulaCard("AF", "406", "kcal", false), op("="),
                new FormulaCard("GET", "2,001", "kcal/día", true)
        );
        card.getChildren().addAll(title, formula);
        return card;
    }

    private Label op(String value) {
        return Ui.label(value, 28, FontWeight.EXTRA_BOLD, Ui.TEXT);
    }

    private VBox createMacroCard() {
        VBox card = new VBox(12);
        card.setPadding(new Insets(18));
        Ui.card(card, 16, 0.05);
        Label title = Ui.label("Distribución de macronutrientes", 15, FontWeight.EXTRA_BOLD, Ui.TEXT);
        PieChart chart = new PieChart();
        chart.setLegendVisible(false);
        chart.setLabelsVisible(false);
        chart.setPrefSize(250, 220);
        chart.getData().addAll(new PieChart.Data("Proteína", 20), new PieChart.Data("Lípidos", 30), new PieChart.Data("Carbohidratos", 50));
        GridPane table = new GridPane();
        table.setHgap(42);
        table.setVgap(13);
        addRow(table, 0, "Nutrimento", "g/día", "kcal", "%", true);
        addRow(table, 1, "Proteína", "100 g", "400 kcal", "20%", false);
        addRow(table, 2, "Lípidos", "67 g", "603 kcal", "30%", false);
        addRow(table, 3, "Carbohidratos", "250 g", "998 kcal", "50%", false);
        addRow(table, 4, "Total", "", "2,001 kcal", "100%", false);
        HBox row = new HBox(40, chart, table);
        row.setAlignment(Pos.CENTER_LEFT);
        card.getChildren().addAll(title, row);
        return card;
    }

    private void addRow(GridPane table, int row, String a, String b, String c, String d, boolean head) {
        FontWeight w = head ? FontWeight.SEMI_BOLD : FontWeight.NORMAL;
        table.add(Ui.label(a, 12, w, head ? Color.web("#334155") : Ui.TEXT), 0, row);
        table.add(Ui.label(b, 12, w, Ui.TEXT), 1, row);
        table.add(Ui.label(c, 12, w, Ui.TEXT), 2, row);
        table.add(Ui.label(d, 12, w, Ui.TEXT), 3, row);
    }

    private VBox createEquivalentsCard() {
        VBox card = new VBox(12);
        card.setPadding(new Insets(18));
        Ui.card(card, 16, 0.05);
        HBox row = new HBox(10);
        row.getChildren().addAll(new EquivalentTile("Cereales", "11"), new EquivalentTile("Frutas", "3"), new EquivalentTile("Verduras", "4"), new EquivalentTile("Lácteos", "2"), new EquivalentTile("AOA", "6"), new EquivalentTile("Grasas", "4"));
        card.getChildren().addAll(Ui.label("Sistema de equivalentes", 15, FontWeight.EXTRA_BOLD, Ui.TEXT), row);
        return card;
    }

    private HBox createActions() {
        HBox actions = new HBox(15);
        actions.getChildren().addAll(
                Ui.button("Registrar peso", Ui.GREEN, false),
                Ui.button("Nueva consulta", Color.web("#3B82F6"), false),
                Ui.button("Crear dieta", Ui.GREEN, true),
                Ui.button("Crear entrenamiento", Color.web("#8B5CF6"), false),
                Ui.button("Exportar PDF", Color.web("#EF4444"), false)
        );
        actions.getChildren().forEach(n -> HBox.setHgrow(n, Priority.ALWAYS));
        return actions;
    }

    private VBox createRightSummary() {
        VBox right = new VBox(12);
        right.setMinWidth(310);
        right.setPrefWidth(310);
        right.setMaxWidth(310);
        right.getChildren().addAll(adherenceCard(), appointmentCard(), weightEvolutionCard(), alertsCard());
        return right;
    }

    private VBox adherenceCard() {
        VBox card = new VBox(8);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(18));
        Ui.card(card, 16, 0.05);
        ProgressIndicator progress = new ProgressIndicator(.85);
        progress.setPrefSize(86, 86);
        card.getChildren().addAll(Ui.label("Resumen rápido", 15, FontWeight.EXTRA_BOLD, Ui.TEXT), progress, Ui.label("85%", 28, FontWeight.EXTRA_BOLD, Ui.TEXT), Ui.label("Adherencia al plan\nBuena", 12, FontWeight.NORMAL, Ui.TEXT));
        return card;
    }

    private VBox appointmentCard() {
        VBox card = new VBox(8);
        card.setPadding(new Insets(18));
        Ui.card(card, 16, 0.05);
        card.getChildren().addAll(Ui.label("Próxima cita", 12, FontWeight.NORMAL, Color.web("#334155")), Ui.label("15 Jun 2024", 27, FontWeight.EXTRA_BOLD, Ui.TEXT), Ui.label("10:00 AM", 12, FontWeight.NORMAL, Ui.TEXT));
        return card;
    }

    private VBox weightEvolutionCard() {
        VBox card = new VBox(8);
        card.setPadding(new Insets(18));
        Ui.card(card, 16, 0.05);
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis(0, 75, 25);
        LineChart<String, Number> chart = new LineChart<>(x, y);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setPrefHeight(170);
        chart.setCreateSymbols(true);
        XYChart.Series<String, Number> s = new XYChart.Series<>();
        s.getData().add(new XYChart.Data<>("Feb", 71));
        s.getData().add(new XYChart.Data<>("Mar", 69));
        s.getData().add(new XYChart.Data<>("Abr", 69));
        s.getData().add(new XYChart.Data<>("May", 66));
        s.getData().add(new XYChart.Data<>("Jun", 64));
        chart.getData().add(s);
        card.getChildren().addAll(Ui.label("Evolución de peso", 12, FontWeight.NORMAL, Color.web("#334155")), chart);
        return card;
    }

    private VBox alertsCard() {
        VBox card = new VBox(12);
        card.setPadding(new Insets(18));
        card.setBackground(new Background(new BackgroundFill(Color.web("#FFFBEB"), new CornerRadii(16), Insets.EMPTY)));
        card.setBorder(new Border(new BorderStroke(Color.web("#FACC15"), BorderStrokeStyle.SOLID, new CornerRadii(16), new BorderWidths(1))));
        card.getChildren().addAll(Ui.label("Alertas", 12, FontWeight.NORMAL, Ui.TEXT), Ui.label("⚠ Estudios bioquímicos pendientes\nSolicitados el 03/06/2024", 12, FontWeight.NORMAL, Ui.TEXT), Ui.label("ℹ Medición de pliegues pendiente\nÚltima medición: 10/02/2024", 12, FontWeight.NORMAL, Ui.TEXT));
        return card;
    }
}
