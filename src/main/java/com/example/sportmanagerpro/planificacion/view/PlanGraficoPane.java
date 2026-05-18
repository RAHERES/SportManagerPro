package com.example.sportmanagerpro.planificacion.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Componente visual reutilizable para mostrar el plan gráfico de planificación deportiva.
 * Esta clase NO extiende Application, por lo tanto puede insertarse dentro de cualquier vista JavaFX.
 */
public class PlanGraficoPane extends BorderPane {

    private final GridPane grid = new GridPane();

    private final String[] semanas = {
            "16-22\n1", "23-29\n2", "30-05\n3", "06-12\n4",
            "13-19\n5", "20-26\n6", "27-03\n7", "04-10\n8",
            "11-17\n9", "18-24\n10", "25-31\n11", "01-07\n12", "08-11\n13"
    };

    public PlanGraficoPane() {
        construirVista();
    }

    private void construirVista() {
        setStyle("-fx-background-color: #f4f7fb;");
        setLeft(crearMenuLateral());
        setTop(crearEncabezado());
        setCenter(crearCentro());
        setRight(crearPanelDerecho());
        setBottom(crearFooter());
    }

    private VBox crearMenuLateral() {
        VBox menu = new VBox(20);
        menu.setPrefWidth(80);
        menu.setPadding(new Insets(20, 8, 20, 8));
        menu.setAlignment(Pos.TOP_CENTER);
        menu.setStyle("-fx-background-color: linear-gradient(to bottom, #06213d, #0a3b68);");

        menu.getChildren().addAll(
                itemMenu("📅", "Plan\ngráfico", true),
                itemMenu("🗓", "Calendario", false),
                itemMenu("📋", "Cargas", false),
                itemMenu("🔍", "Análisis", false),
                itemMenu("📊", "Reportes", false),
                itemMenu("⚙", "Config.", false)
        );

        return menu;
    }

    private VBox itemMenu(String icono, String texto, boolean activo) {
        Label icon = new Label(icono);
        icon.setFont(Font.font(24));
        icon.setTextFill(Color.WHITE);

        Label label = new Label(texto);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font(11));
        label.setAlignment(Pos.CENTER);

        VBox box = new VBox(5, icon, label);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(65);
        box.setPadding(new Insets(8));

        if (activo) {
            box.setStyle("-fx-background-color: #0875c9; -fx-background-radius: 8;");
        }

        return box;
    }

    private VBox crearEncabezado() {
        VBox contenedor = new VBox(12);
        contenedor.setPadding(new Insets(18, 20, 12, 20));
        contenedor.setStyle("-fx-background-color: white;");

        Label titulo = new Label("PLAN GRÁFICO");
        titulo.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #08294a;");

        HBox datos = new HBox(30);
        datos.setAlignment(Pos.CENTER_LEFT);
        datos.getChildren().addAll(
                dato("Temporada:", "2026"),
                dato("Deporte:", "Fútbol"),
                dato("Categoría:", "Sub 17 Femenil"),
                dato("Modelo:", "CLÁSICO"),
                dato("Objetivo:", "Estatal CONADEMS")
        );

        HBox controles = new HBox(12);
        controles.setAlignment(Pos.CENTER_LEFT);

        DatePicker inicio = new DatePicker();
        DatePicker fin = new DatePicker();

        controles.getChildren().addAll(
                new Label("Inicio:"), inicio,
                new Label("Fin:"), fin,
                botonAzul("Generar estructura"),
                new Separator(),
                botonNormal("←"),
                botonNormal("→"),
                botonNormal("Hoy"),
                botonNormal("Vista semanas"),
                botonVerde("Vista meses"),
                botonNormal("Configuración días"),
                botonNormal("Exportar ▾")
        );

        contenedor.getChildren().addAll(titulo, datos, controles);
        return contenedor;
    }

    private HBox dato(String etiqueta, String valor) {
        Label e = new Label(etiqueta);
        e.setStyle("-fx-font-weight: bold; -fx-text-fill: #22364f;");

        Label v = new Label(valor);
        v.setStyle("-fx-font-weight: bold; -fx-text-fill: #0a4c8a;");

        return new HBox(6, e, v);
    }

    private Button botonAzul(String texto) {
        Button b = new Button(texto);
        b.setStyle("-fx-background-color: #006bb6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        return b;
    }

    private Button botonVerde(String texto) {
        Button b = new Button(texto);
        b.setStyle("-fx-background-color: #35a853; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        return b;
    }

    private Button botonNormal(String texto) {
        Button b = new Button(texto);
        b.setStyle("-fx-background-color: #f5f7fa; -fx-border-color: #d7dde6; -fx-background-radius: 5; -fx-border-radius: 5;");
        return b;
    }

    private ScrollPane crearCentro() {
        VBox wrapper = new VBox();
        wrapper.setPadding(new Insets(18));
        wrapper.setStyle("-fx-background-color: #f4f7fb;");

        construirPlanGrafico();

        wrapper.getChildren().addAll(grid, crearLeyenda());

        ScrollPane scroll = new ScrollPane(wrapper);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        return scroll;
    }

    private void construirPlanGrafico() {
        grid.getChildren().clear();
        grid.setHgap(0);
        grid.setVgap(0);
        grid.setStyle("-fx-background-color: white; -fx-border-color: #d7dde6;");

        double anchoSemana = 82;

        grid.add(celdaMes("CONCEPTO", 170, 58), 0, 0);
        grid.add(bloque("Mayo", "#06213d", 3 * anchoSemana, 28, true), 1, 0, 3, 1);
        grid.add(bloque("Junio", "#06213d", 8 * anchoSemana, 28, true), 4, 0, 8, 1);
        grid.add(bloque("Julio", "#06213d", 2 * anchoSemana, 28, true), 12, 0, 2, 1);

        for (int i = 0; i < semanas.length; i++) {
            grid.add(celdaSemana(semanas[i], anchoSemana, 58), i + 1, 1);
        }

        int row = 2;

        filaBloques(row++, "PERÍODO",
                new Segmento("PREPARATORIO", 1, 6, "#d7e8ff"),
                new Segmento("COMPETITIVO", 7, 11, "#d9f3d2"),
                new Segmento("TRANSITORIO", 12, 13, "#ffe2aa")
        );

        filaBloques(row++, "ETAPA",
                new Segmento("Preparación General", 1, 3, "#a8ccff"),
                new Segmento("Preparación Especial", 4, 6, "#83b7ff"),
                new Segmento("Competitivo", 7, 11, "#9ee39a"),
                new Segmento("Transición", 12, 13, "#ffc768")
        );

        filaBloques(row++, "MESOCICLO",
                new Segmento("Acondicionamiento", 1, 3, "#7eb4ff"),
                new Segmento("Desarrollo", 4, 6, "#5aa3ff"),
                new Segmento("Puesta a punto", 7, 8, "#8fe27a"),
                new Segmento("Competencias", 9, 11, "#61cc65"),
                new Segmento("Descarga", 12, 13, "#f2af3d")
        );

        filaMicrociclos(row++);

        filaNumerica(row++, "VOLUMEN (%)", new int[]{60, 75, 90, 50, 60, 80, 95, 60, 80, 95, 40, 30, 40});
        filaNumerica(row++, "INTENSIDAD (%)", new int[]{65, 70, 75, 60, 70, 75, 85, 60, 70, 90, 75, 85, 50});
        filaIconos(row++, "COMPETENCIAS", new String[]{"", "", "", "", "", "", "", "", "", "⚽", "⚽", "", ""});
        filaIconos(row++, "CONTROLES / TEST", new String[]{"", "📋", "📋", "", "", "📋", "", "📋", "", "", "📋", "📋", ""});
        filaNumerica(row++, "SESIONES", new int[]{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1});
        filaNumerica(row++, "MINUTOS PLAN.", new int[]{450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 450, 180});
        filaNumericaConColor(row++, "MINUTOS REAL.", new int[]{420, 450, 430, 400, 450, 410, 380, 420, 440, 430, 400, 0, 0});
        filaCumplimiento(row++, "CUMPLIMIENTO (%)", new int[]{93, 100, 96, 89, 100, 91, 84, 93, 98, 96, 89, 0, 0});
        filaNumerica(row++, "CARGA PLAN. (MIN x RPE)", new int[]{2700, 2880, 3240, 2160, 2700, 3240, 3780, 2160, 2700, 3240, 1800, 1260, 720});
        filaNumerica(row++, "CARGA REAL (MIN x RPE)", new int[]{2520, 2880, 3090, 1920, 2700, 2460, 2850, 2520, 2990, 3090, 2880, 0, 0});

        filaBarras(row++, "PREP. FÍSICA", "#276ef1", new int[]{20, 40, 60, 30, 50, 80, 95, 50, 70, 90, 75, 35, 20});
        filaBarras(row++, "PREP. TÉCNICO-TÁCTICA", "#1f9d46", new int[]{30, 45, 55, 60, 70, 75, 85, 65, 75, 80, 85, 55, 25});
        filaBarras(row++, "PREP. PSICOLÓGICA", "#8e44ad", new int[]{10, 20, 30, 35, 50, 55, 60, 45, 55, 60, 50, 25, 15});
        filaBarras(row++, "PREP. TEÓRICA", "#d49a00", new int[]{15, 25, 40, 45, 55, 65, 70, 55, 70, 75, 45, 25, 10});
    }

    private void filaBloques(int row, String titulo, Segmento... segmentos) {
        grid.add(celdaTitulo(titulo), 0, row);

        for (Segmento s : segmentos) {
            grid.add(
                    bloque(s.texto, s.color, (s.fin - s.inicio + 1) * 82, 38, false),
                    s.inicio,
                    row,
                    s.fin - s.inicio + 1,
                    1
            );
        }
    }

    private void filaMicrociclos(int row) {
        grid.add(celdaTitulo("MICROCICLO"), 0, row);

        String[] tipos = {"A", "C", "I", "A", "C", "R", "R", "A", "C", "I", "R", "PC", "REC"};
        String[] colores = {
                "#bfd9ff", "#3c86ef", "#143da8", "#a9e3d0",
                "#3c86ef", "#a9e3d0", "#a9e3d0", "#3c86ef",
                "#143da8", "#143da8", "#a9e3d0", "#8e44ad", "#ef4136"
        };

        for (int i = 0; i < tipos.length; i++) {
            grid.add(bloque(tipos[i], colores[i], 82, 38, false), i + 1, row);
        }
    }

    private void filaNumerica(int row, String titulo, int[] valores) {
        grid.add(celdaTitulo(titulo), 0, row);

        for (int i = 0; i < valores.length; i++) {
            grid.add(celda(String.valueOf(valores[i]), 82, 34), i + 1, row);
        }
    }

    private void filaNumericaConColor(int row, String titulo, int[] valores) {
        grid.add(celdaTitulo(titulo), 0, row);

        for (int i = 0; i < valores.length; i++) {
            Label c = celda(String.valueOf(valores[i]), 82, 34);

            if (valores[i] == 0 || valores[i] < 400) {
                c.setStyle(estiloCelda() + "-fx-text-fill: #e3342f; -fx-font-weight: bold;");
            } else {
                c.setStyle(estiloCelda() + "-fx-text-fill: #188038; -fx-font-weight: bold;");
            }

            grid.add(c, i + 1, row);
        }
    }

    private void filaCumplimiento(int row, String titulo, int[] valores) {
        grid.add(celdaTitulo(titulo), 0, row);

        for (int i = 0; i < valores.length; i++) {
            StackPane cont = new StackPane();
            cont.setPrefSize(82, 34);
            cont.setStyle(estiloCelda());

            ProgressBar barra = new ProgressBar(valores[i] / 100.0);
            barra.setPrefWidth(60);

            String color = valores[i] >= 90 ? "#35a853" : valores[i] >= 70 ? "#f6b73c" : "#e3342f";
            barra.setStyle("-fx-accent: " + color + ";");

            Label texto = new Label(valores[i] + "%");
            texto.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");

            cont.getChildren().addAll(barra, texto);
            grid.add(cont, i + 1, row);
        }
    }

    private void filaIconos(int row, String titulo, String[] iconos) {
        grid.add(celdaTitulo(titulo), 0, row);

        for (int i = 0; i < iconos.length; i++) {
            Label c = celda(iconos[i], 82, 34);
            c.setStyle(estiloCelda() + "-fx-font-size: 16px;");
            grid.add(c, i + 1, row);
        }
    }

    private void filaBarras(int row, String titulo, String color, int[] valores) {
        grid.add(celdaTitulo(titulo), 0, row);

        for (int i = 0; i < valores.length; i++) {
            StackPane cont = new StackPane();
            cont.setAlignment(Pos.CENTER_LEFT);
            cont.setPadding(new Insets(0, 8, 0, 8));
            cont.setPrefSize(82, 34);
            cont.setStyle(estiloCelda());

            Region barra = new Region();
            barra.setPrefSize(Math.max(8, valores[i] * 0.65), 14);
            barra.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 2; -fx-opacity: 0.85;");

            cont.getChildren().add(barra);
            grid.add(cont, i + 1, row);
        }
    }

    private Label celdaTitulo(String texto) {
        Label l = celda(texto, 170, 38);
        l.setAlignment(Pos.CENTER_LEFT);
        l.setPadding(new Insets(0, 0, 0, 18));
        l.setStyle(estiloCelda() + "-fx-font-weight: bold; -fx-text-fill: #123456;");
        return l;
    }

    private Label celdaMes(String texto, double w, double h) {
        Label l = celda(texto, w, h);
        l.setStyle("-fx-background-color: white; -fx-border-color: #d7dde6; -fx-font-weight: bold; -fx-text-fill: #123456;");
        return l;
    }

    private Label celdaSemana(String texto, double w, double h) {
        Label l = celda(texto, w, h);
        l.setStyle(estiloCelda() + "-fx-font-size: 11px; -fx-font-weight: bold;");
        return l;
    }

    private Label celda(String texto, double w, double h) {
        Label l = new Label(texto);
        l.setPrefSize(w, h);
        l.setMinSize(w, h);
        l.setAlignment(Pos.CENTER);
        l.setStyle(estiloCelda());
        return l;
    }

    private Label bloque(String texto, String color, double w, double h, boolean oscuro) {
        Label l = new Label(texto);
        l.setPrefSize(w, h);
        l.setMinSize(w, h);
        l.setAlignment(Pos.CENTER);
        l.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-border-color: #d7dde6;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + (oscuro ? "white" : "#0b2442") + ";"
        );
        return l;
    }

    private String estiloCelda() {
        return "-fx-background-color: white; -fx-border-color: #e1e6ef; -fx-font-size: 12px;";
    }

    private VBox crearPanelDerecho() {
        VBox panel = new VBox(14);
        panel.setPrefWidth(250);
        panel.setPadding(new Insets(18));
        panel.setStyle("-fx-background-color: #f4f7fb;");

        panel.getChildren().addAll(
                tarjetaResumen("RESUMEN GENERAL",
                        "Sesiones planificadas:     49\n" +
                                "Sesiones realizadas:       44\n\n" +
                                "Minutos planificados:   5,580\n" +
                                "Minutos realizados:     4,910\n\n" +
                                "Cumplimiento general:   88.0 %\n\n" +
                                "Carga planificada:      32,400\n" +
                                "Carga realizada:        28,910\n" +
                                "Cumplimiento carga:     89.3 %"
                ),
                tarjetaResumen("MICROCICLO ACTUAL",
                        "Semana:                    7\n" +
                                "Fechas:          27/06 al 03/07\n" +
                                "Tipo:               IMPACTO\n\n" +
                                "Sesiones plan.:           4\n" +
                                "Minutos plan.:          450\n" +
                                "Minutos real.:          380\n" +
                                "Cumplimiento:         84.4 %\n\n" +
                                "Carga plan.:          3,780\n" +
                                "Carga real.:          2,850"
                ),
                indicadores()
        );

        return panel;
    }

    private VBox tarjetaResumen(String titulo, String contenido) {
        Label header = new Label(titulo);
        header.setPrefWidth(220);
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #06213d; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8;");

        Label body = new Label(contenido);
        body.setPrefWidth(220);
        body.setStyle("-fx-background-color: white; -fx-padding: 12; -fx-border-color: #d7dde6; -fx-font-size: 12px;");

        return new VBox(header, body);
    }

    private VBox indicadores() {
        VBox box = new VBox(10);

        Label header = new Label("INDICADORES RÁPIDOS");
        header.setPrefWidth(220);
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #06213d; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8;");

        GridPane g = new GridPane();
        g.setHgap(12);
        g.setVgap(12);
        g.setPadding(new Insets(12));
        g.setStyle("-fx-background-color: white; -fx-border-color: #d7dde6;");

        g.add(indicador("Volumen", "-12%"), 0, 0);
        g.add(indicador("Intensidad", "+5%"), 1, 0);
        g.add(indicador("Monotonía", "1.35"), 0, 1);
        g.add(indicador("Fatiga", "320"), 1, 1);

        box.getChildren().addAll(header, g);
        return box;
    }

    private VBox indicador(String titulo, String valor) {
        Label v = new Label(valor);
        v.setPrefSize(70, 70);
        v.setAlignment(Pos.CENTER);
        v.setStyle("-fx-background-color: white; -fx-border-color: #d7dde6; -fx-border-width: 3; -fx-background-radius: 50; -fx-border-radius: 50; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label t = new Label(titulo);
        t.setAlignment(Pos.CENTER);
        t.setStyle("-fx-font-size: 11px;");

        VBox box = new VBox(4, v, t);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private HBox crearLeyenda() {
        HBox leyenda = new HBox(18);
        leyenda.setPadding(new Insets(12));
        leyenda.setStyle("-fx-background-color: white;");
        leyenda.getChildren().addAll(
                leyendaItem("#bfd9ff", "A: Ajuste"),
                leyendaItem("#3c86ef", "C: Carga"),
                leyendaItem("#143da8", "I: Impacto"),
                leyendaItem("#a9e3d0", "R: Recuperación"),
                leyendaItem("#8e44ad", "PC: Precompetitivo"),
                leyendaItem("#ef4136", "REC: Recuperación final")
        );
        return leyenda;
    }

    private HBox leyendaItem(String color, String texto) {
        Region r = new Region();
        r.setPrefSize(18, 14);
        r.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 3;");

        Label l = new Label(texto);
        l.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        return new HBox(6, r, l);
    }

    private HBox crearFooter() {
        HBox footer = new HBox();
        footer.setPadding(new Insets(8, 18, 8, 18));
        footer.setStyle("-fx-background-color: #06213d;");
        footer.setAlignment(Pos.CENTER_LEFT);

        Label usuario = new Label("Usuario: Entrenador Principal");
        usuario.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label plan = new Label("Plan: Plan anual 2026 - Sub 17 Femenil");
        plan.setTextFill(Color.WHITE);

        footer.getChildren().addAll(usuario, spacer, plan);
        return footer;
    }

    private static class Segmento {
        String texto;
        int inicio;
        int fin;
        String color;

        Segmento(String texto, int inicio, int fin, String color) {
            this.texto = texto;
            this.inicio = inicio;
            this.fin = fin;
            this.color = color;
        }
    }
}