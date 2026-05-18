package com.example.sportmanagerpro.planificacion.model;

import com.example.sportmanagerpro.planificacion.enums.*;
import com.example.sportmanagerpro.planificacion.service.EtapasPorPeriodizacionService;
import com.example.sportmanagerpro.planificacion.service.PeriodizacionService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class PlanGraficoView extends Application {
    private final List<SesionMicrocicloPlanificada> sesionesMicrociclo = new ArrayList<>();
    private boolean sesionesInicializadas = false;

    private Button btnAplicarCambios;

    private boolean permitirEncimarMicrociclos = false;

    private final List<MicrocicloGraficoPlanificado> microciclosPlanificados = new ArrayList<>();
    private boolean microciclosInicializados = false;

    private final List<MesocicloPlanificado> mesociclosPlanificados = new ArrayList<>();
    private boolean mesociclosInicializados = false;

    private ComboBox<TipoPeriodizacion> cbTipoPeriodizacion;

    private final EtapasPorPeriodizacionService etapasService = new EtapasPorPeriodizacionService();

    private List<EtapaPlanificada> etapasPlanificadas = new ArrayList<>();

    private boolean modoPeriodosManual = false;
    private final PeriodizacionService periodizacionService = new PeriodizacionService();

    private List<PeriodoPlanificado> periodosPlanificados = new ArrayList<>();

    private TipoPeriodizacion tipoPeriodizacionActual = TipoPeriodizacion.TRADICIONAL;

    private String deporteActual = "Fútbol";

    private DatePicker dpFechaInicio;
    private DatePicker dpFechaFin;

    private LocalDate fechaInicioPlan = LocalDate.of(2026, 5, 16);
    private LocalDate fechaFinPlan = LocalDate.of(2026, 7, 11);

    private final List<SemanaPlanificacion> semanasPlan = new ArrayList<>();

    private final Map<String, CeldaPlanGrafico> celdasPlan = new LinkedHashMap<>();
    private final Map<String, Label> labelsPlan = new HashMap<>();

    private final VBox panelEdicion = new VBox(10);

    private CeldaPlanGrafico celdaSeleccionada;

    private final TextField txtValor = new TextField();
    private final ColorPicker colorPicker = new ColorPicker();
    private final Label lblCeldaSeleccionada = new Label("Sin celda seleccionada");

    private final GridPane grid = new GridPane();

    private final String[] semanas = {
            "16-22\n1", "23-29\n2", "30-05\n3", "06-12\n4",
            "13-19\n5", "20-26\n6", "27-03\n7", "04-10\n8",
            "11-17\n9", "18-24\n10", "25-31\n11", "01-07\n12", "08-11\n13"
    };

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f7fb;");

        root.setLeft(crearMenuLateral());
        root.setTop(crearEncabezado());

        generarSemanasPlanificacion();
        root.setCenter(crearCentro());
        root.setRight(crearPanelDerecho());
        root.setBottom(crearFooter());

        Scene scene = new Scene(root, 1600, 900);
        stage.setTitle("Planificación y Periodización");
        stage.setScene(scene);
        stage.show();
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

    private void inicializarSesionesSiEsNecesario() {
        if (sesionesInicializadas || semanasPlan.isEmpty()) {
            return;
        }

        sesionesMicrociclo.clear();

        for (SemanaPlanificacion semana : semanasPlan) {
            int numSemana = semana.getNumeroSemana();

            sesionesMicrociclo.add(new SesionMicrocicloPlanificada(
                    numSemana,
                    DayOfWeek.TUESDAY,
                    LocalTime.of(16, 30),
                    90,
                    false,
                    "Sesión regular"
            ));

            sesionesMicrociclo.add(new SesionMicrocicloPlanificada(
                    numSemana,
                    DayOfWeek.THURSDAY,
                    LocalTime.of(16, 30),
                    90,
                    false,
                    "Sesión regular"
            ));

            sesionesMicrociclo.add(new SesionMicrocicloPlanificada(
                    numSemana,
                    DayOfWeek.SATURDAY,
                    LocalTime.of(14, 0),
                    120,
                    false,
                    "Sesión regular"
            ));
        }

        sesionesInicializadas = true;
    }

    private int contarSesionesSemana(int semana) {
        inicializarSesionesSiEsNecesario();

        return (int) sesionesMicrociclo.stream()
                .filter(s -> s.getSemana() == semana)
                .count();
    }

    private int calcularMinutosSemana(int semana) {
        inicializarSesionesSiEsNecesario();

        return sesionesMicrociclo.stream()
                .filter(s -> s.getSemana() == semana)
                .mapToInt(SesionMicrocicloPlanificada::getDuracionMinutos)
                .sum();
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

        dpFechaInicio = new DatePicker(fechaInicioPlan);
        dpFechaFin = new DatePicker(fechaFinPlan);

        cbTipoPeriodizacion = new ComboBox<>();
        cbTipoPeriodizacion.getItems().setAll(TipoPeriodizacion.values());
        cbTipoPeriodizacion.setValue(tipoPeriodizacionActual);
        cbTipoPeriodizacion.setPrefWidth(180);

        cbTipoPeriodizacion.setOnAction(e -> {
            tipoPeriodizacionActual = cbTipoPeriodizacion.getValue();

            periodosPlanificados = periodizacionService.generarPeriodos(
                    semanasPlan,
                    tipoPeriodizacionActual,
                    deporteActual
            );

            etapasPlanificadas = etapasService.generarEtapas(
                    tipoPeriodizacionActual,
                    periodosPlanificados,
                    semanasPlan
            );

            modoPeriodosManual = false;

            mesociclosInicializados = false;
            mesociclosPlanificados.clear();

            microciclosInicializados = false;
            microciclosPlanificados.clear();

            construirPlanGrafico();
        });

        Button generar = botonAzul("Generar estructura");
        generar.setOnAction(e -> generarEstructuraPorFechas());

        Button btnEditarPeriodos = botonNormal("Editar períodos");
        btnEditarPeriodos.setOnAction(e -> abrirEditorPeriodos());

        Button btnEditarMesociclos = botonNormal("Editar mesociclos");
        btnEditarMesociclos.setOnAction(e -> abrirEditorMesociclos());

        Button btnEditarMicrociclos = botonNormal("Editar microciclos");
        btnEditarMicrociclos.setOnAction(e -> abrirEditorMicrociclos());


        Button semanas = botonNormal("Vista semanas");
        Button meses = botonVerde("Vista meses");
        Button config = botonNormal("Configuración días");
        Button exportar = botonNormal("Exportar ▾");

        controles.getChildren().addAll(
                new Label("Inicio:"), dpFechaInicio,
                new Label("Fin:"), dpFechaFin,
                cbTipoPeriodizacion,
                generar,
                btnEditarPeriodos,
                btnEditarMesociclos,
                btnEditarMicrociclos,
                new Separator(),
                botonNormal("←"),
                botonNormal("→"),
                botonNormal("Hoy"),
                semanas,
                meses,
                config,
                exportar
        );

        contenedor.getChildren().addAll(titulo, datos, controles);
        return contenedor;
    }

    private void inicializarMicrociclosSiEsNecesario() {
        if (microciclosInicializados || semanasPlan.isEmpty()) {
            return;
        }

        microciclosPlanificados.clear();

        for (int semana = 1; semana <= semanasPlan.size(); semana++) {
            TipoMicrociclo tipo = sugerirTipoMicrocicloPorSemana(semana);

            microciclosPlanificados.add(crearMicrocicloGrafico(
                    tipo,
                    abreviaturaMicrociclo(tipo),
                    semana,
                    1,
                    colorMicrociclo(tipo)
            ));
        }

        microciclosInicializados = true;
    }

    private TipoMicrociclo sugerirTipoMicrocicloPorSemana(int semana) {
        int posicion = semana % 4;

        if (posicion == 1) return TipoMicrociclo.AJUSTE;
        if (posicion == 2) return TipoMicrociclo.CARGA;
        if (posicion == 3) return TipoMicrociclo.IMPACTO;

        return TipoMicrociclo.RECUPERACION;
    }

    private String abreviaturaMicrociclo(TipoMicrociclo tipo) {
        return switch (tipo) {
            case AJUSTE -> "A";
            case CARGA -> "C";
            case IMPACTO -> "I";
            case RECUPERACION -> "R";
            case PRECOMPETITIVO -> "PC";
            case COMPETENCIA -> "COMP";
        };
    }

    private String nombreMicrociclo(TipoMicrociclo tipo) {
        return switch (tipo) {
            case AJUSTE -> "Ajuste";
            case CARGA -> "Carga";
            case IMPACTO -> "Impacto";
            case RECUPERACION -> "Recuperación";
            case PRECOMPETITIVO -> "Precompetitivo";
            case COMPETENCIA -> "Competencia";
        };
    }

    private String colorMicrociclo(TipoMicrociclo tipo) {
        return switch (tipo) {
            case AJUSTE -> "#bfd9ff";
            case CARGA -> "#3c86ef";
            case IMPACTO -> "#143da8";
            case RECUPERACION -> "#a9e3d0";
            case PRECOMPETITIVO -> "#8e44ad";
            case COMPETENCIA -> "#ef4136";
        };
    }

    private MicrocicloGraficoPlanificado crearMicrocicloGrafico(TipoMicrociclo tipo,
                                                                String nombre,
                                                                int semanaInicio,
                                                                int duracion,
                                                                String colorHex) {

        int semanaFin = semanaInicio + duracion - 1;

        if (semanaFin > semanasPlan.size()) {
            semanaFin = semanasPlan.size();
            duracion = semanaFin - semanaInicio + 1;
        }

        SemanaPlanificacion primera = semanasPlan.get(semanaInicio - 1);
        SemanaPlanificacion ultima = semanasPlan.get(semanaFin - 1);

        return new MicrocicloGraficoPlanificado(
                tipo,
                nombre,
                semanaInicio,
                duracion,
                primera.getFechaInicio(),
                ultima.getFechaFin(),
                colorHex
        );
    }

    /*private void filaMicrociclosCalculados(int row) {
        grid.add(celdaTitulo("MICROCICLO"), 0, row);

        inicializarMicrociclosSiEsNecesario();

        for (MicrocicloGraficoPlanificado microciclo : microciclosPlanificados) {
            grid.add(
                    celdaEditable(
                            "MICROCICLO",
                            microciclo.getSemanaInicio(),
                            microciclo.getNombre(),
                            microciclo.getColorHex(),
                            microciclo.getDuracionSemanas() * 82,
                            38
                    ),
                    microciclo.getSemanaInicio(),
                    row,
                    microciclo.getDuracionSemanas(),
                    1
            );
        }
    }*/

    private void filaMicrociclosCalculados(int row) {
        grid.add(celdaTitulo("MICROCICLO"), 0, row);

        inicializarMicrociclosSiEsNecesario();

        boolean[] semanasOcupadas = new boolean[semanasPlan.size() + 1];

        for (MicrocicloGraficoPlanificado microciclo : microciclosPlanificados) {
            for (int s = microciclo.getSemanaInicio(); s <= microciclo.getSemanaFin(); s++) {
                if (s >= 1 && s <= semanasPlan.size()) {
                    semanasOcupadas[s] = true;
                }
            }

            grid.add(
                    celdaEditable(
                            "MICROCICLO",
                            microciclo.getSemanaInicio(),
                            microciclo.getNombre(),
                            microciclo.getColorHex(),
                            microciclo.getDuracionSemanas() * 82,
                            38
                    ),
                    microciclo.getSemanaInicio(),
                    row,
                    microciclo.getDuracionSemanas(),
                    1
            );
        }

        for (int semana = 1; semana <= semanasPlan.size(); semana++) {
            if (!semanasOcupadas[semana]) {
                grid.add(celdaMicrocicloVacia(semana), semana, row);
            }
        }
    }

    private Label celdaMicrocicloVacia(int semana) {
        Label label = celda("+", 82, 38);

        label.setStyle("""
            -fx-background-color: #f8fafc;
            -fx-border-color: #d7dde6;
            -fx-border-style: dashed;
            -fx-border-width: 1.5;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-text-fill: #6b7280;
            -fx-cursor: hand;
            """);

        label.setOnMouseEntered(e -> label.setStyle("""
            -fx-background-color: #e8f1ff;
            -fx-border-color: #006bb6;
            -fx-border-style: dashed;
            -fx-border-width: 2;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-text-fill: #006bb6;
            -fx-cursor: hand;
            """));

        label.setOnMouseExited(e -> label.setStyle("""
            -fx-background-color: #f8fafc;
            -fx-border-color: #d7dde6;
            -fx-border-style: dashed;
            -fx-border-width: 1.5;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-text-fill: #6b7280;
            -fx-cursor: hand;
            """));

        label.setOnMouseClicked(e -> abrirEditorNuevoMicrocicloDesdeSemana(semana));

        return label;
    }

    private void abrirEditorNuevoMicrocicloDesdeSemana(int semana) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Agregar microciclo");
        dialog.setHeaderText("Crear microciclo desde la semana " + semana);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(15));

        ComboBox<TipoMicrociclo> cbTipo = new ComboBox<>();
        cbTipo.getItems().setAll(TipoMicrociclo.values());
        cbTipo.setValue(TipoMicrociclo.CARGA);

        TextField txtNombre = new TextField(abreviaturaMicrociclo(cbTipo.getValue()));

        Spinner<Integer> spSemanaInicio = new Spinner<>(1, semanasPlan.size(), semana);
        Spinner<Integer> spDuracion = new Spinner<>(1, semanasPlan.size(), 1);

        spSemanaInicio.setEditable(true);
        spDuracion.setEditable(true);

        ColorPicker cpColor = new ColorPicker(Color.web(colorMicrociclo(cbTipo.getValue())));

        cbTipo.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                txtNombre.setText(abreviaturaMicrociclo(newValue));
                cpColor.setValue(Color.web(colorMicrociclo(newValue)));
            }
        });

        form.add(new Label("Tipo:"), 0, 0);
        form.add(cbTipo, 1, 0);

        form.add(new Label("Nombre:"), 0, 1);
        form.add(txtNombre, 1, 1);

        form.add(new Label("Semana inicio:"), 0, 2);
        form.add(spSemanaInicio, 1, 2);

        form.add(new Label("Duración:"), 0, 3);
        form.add(spDuracion, 1, 3);

        form.add(new Label("Color:"), 0, 4);
        form.add(cpColor, 1, 4);

        dialog.getDialogPane().setContent(form);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                TipoMicrociclo tipoSeleccionado = cbTipo.getValue();
                String nombreSeleccionado = txtNombre.getText();
                String colorSeleccionado = toHex(cpColor.getValue());

                MicrocicloGraficoPlanificado nuevo = crearMicrocicloGrafico(
                        tipoSeleccionado,
                        nombreSeleccionado,
                        spSemanaInicio.getValue(),
                        spDuracion.getValue(),
                        colorSeleccionado
                );

                if (!resolverEncimamientoMicrociclo(nuevo, null)) {
                    return;
                }

                microciclosPlanificados.add(nuevo);
                actualizarFechasMicrociclos();
                limpiarSeleccionCelda();
                construirPlanGrafico();

            }
        });
    }

    private boolean resolverEncimamientoMicrociclo(MicrocicloGraficoPlanificado microcicloEditado,
                                                   MicrocicloGraficoPlanificado microcicloOriginal) {

        List<MicrocicloGraficoPlanificado> encimados = microciclosPlanificados.stream()
                .filter(m -> m != microcicloOriginal)
                .filter(m -> seEnciman(m, microcicloEditado))
                .toList();

        if (encimados.isEmpty()) {
            return true;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Microciclos encimados");
        alerta.setHeaderText("El microciclo se encima con otro.");
        alerta.setContentText("Elige qué deseas hacer.");

        ButtonType btnPermitir = new ButtonType("Permitir encimar");
        ButtonType btnEliminar = new ButtonType("Eliminar el que se encima");
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alerta.getButtonTypes().setAll(btnPermitir, btnEliminar, btnCancelar);

        Optional<ButtonType> respuesta = alerta.showAndWait();

        if (respuesta.isEmpty() || respuesta.get() == btnCancelar) {
            return false;
        }

        if (respuesta.get() == btnPermitir) {
            permitirEncimarMicrociclos = true;
            return true;
        }

        if (respuesta.get() == btnEliminar) {
            microciclosPlanificados.removeAll(encimados);
            return true;
        }

        return false;
    }

    private boolean seEnciman(MicrocicloGraficoPlanificado a,
                              MicrocicloGraficoPlanificado b) {

        return a.getSemanaInicio() <= b.getSemanaFin()
                && b.getSemanaInicio() <= a.getSemanaFin();
    }

    private void abrirEditorMesociclos() {
        inicializarMesociclosSiEsNecesario();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar mesociclos");
        dialog.setHeaderText("Modifica tipo, semana de inicio, duración y nombre de cada mesociclo");

        VBox contenido = new VBox(10);
        contenido.setPadding(new Insets(15));

        TableView<MesocicloPlanificado> tabla = new TableView<>();
        tabla.setEditable(true);
        tabla.setPrefHeight(360);

        TableColumn<MesocicloPlanificado, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        colNombre.setOnEditCommit(e -> e.getRowValue().setNombre(e.getNewValue()));

        TableColumn<MesocicloPlanificado, TipoMesociclo> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTipoMesociclo()));
        colTipo.setCellFactory(ComboBoxTableCell.forTableColumn(TipoMesociclo.values()));
        colTipo.setOnEditCommit(e -> {
            e.getRowValue().setTipoMesociclo(e.getNewValue());
            e.getRowValue().setColorHex(colorMesociclo(e.getNewValue()));
            e.getRowValue().setNombre(nombreMesociclo(e.getNewValue()));
        });

        TableColumn<MesocicloPlanificado, Integer> colInicio = new TableColumn<>("Semana inicio");
        colInicio.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getSemanaInicio()));
        colInicio.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colInicio.setOnEditCommit(e -> e.getRowValue().setSemanaInicio(e.getNewValue()));

        TableColumn<MesocicloPlanificado, Integer> colDuracion = new TableColumn<>("Duración");
        colDuracion.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDuracionSemanas()));
        colDuracion.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colDuracion.setOnEditCommit(e -> e.getRowValue().setDuracionSemanas(e.getNewValue()));

        tabla.getColumns().addAll(colNombre, colTipo, colInicio, colDuracion);
        tabla.getItems().setAll(mesociclosPlanificados);

        Button btnAgregar = botonNormal("Agregar mesociclo");
        btnAgregar.setOnAction(e -> {
            int inicio = mesociclosPlanificados.isEmpty()
                    ? 1
                    : mesociclosPlanificados.get(mesociclosPlanificados.size() - 1).getSemanaFin() + 1;

            if (inicio > semanasPlan.size()) {
                mostrarAlerta("No se puede agregar", "Ya no hay semanas disponibles.");
                return;
            }

            MesocicloPlanificado nuevo = crearMesociclo(
                    TipoMesociclo.PERSONALIZADO,
                    "Nuevo mesociclo",
                    inicio,
                    1,
                    colorMesociclo(TipoMesociclo.PERSONALIZADO)
            );

            mesociclosPlanificados.add(nuevo);
            tabla.getItems().setAll(mesociclosPlanificados);
        });

        Button btnEliminar = botonNormal("Eliminar seleccionado");
        btnEliminar.setOnAction(e -> {
            MesocicloPlanificado seleccionado = tabla.getSelectionModel().getSelectedItem();

            if (seleccionado != null) {
                mesociclosPlanificados.remove(seleccionado);
                tabla.getItems().setAll(mesociclosPlanificados);
            }
        });

        HBox acciones = new HBox(10, btnAgregar, btnEliminar);

        contenido.getChildren().addAll(tabla, acciones);

        dialog.getDialogPane().setContent(contenido);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                if (!validarMesociclos()) {
                    return;
                }

                actualizarFechasMesociclos();
                construirPlanGrafico();
            }
        });
    }

    private boolean validarMesociclos() {
        boolean[] ocupadas = new boolean[semanasPlan.size() + 1];

        for (MesocicloPlanificado mesociclo : mesociclosPlanificados) {
            if (mesociclo.getSemanaInicio() < 1 || mesociclo.getSemanaFin() > semanasPlan.size()) {
                mostrarAlerta("Error en mesociclos", "Un mesociclo está fuera del rango de semanas.");
                return false;
            }

            if (mesociclo.getDuracionSemanas() < 1) {
                mostrarAlerta("Error en mesociclos", "La duración debe ser de al menos una semana.");
                return false;
            }

            for (int semana = mesociclo.getSemanaInicio(); semana <= mesociclo.getSemanaFin(); semana++) {
                if (ocupadas[semana]) {
                    mostrarAlerta("Error en mesociclos", "Hay mesociclos encimados.");
                    return false;
                }

                ocupadas[semana] = true;
            }
        }

        return true;
    }

    private void actualizarFechasMesociclos() {
        for (MesocicloPlanificado mesociclo : mesociclosPlanificados) {
            int semanaInicio = mesociclo.getSemanaInicio();
            int semanaFin = mesociclo.getSemanaFin();

            SemanaPlanificacion primera = semanasPlan.get(semanaInicio - 1);
            SemanaPlanificacion ultima = semanasPlan.get(semanaFin - 1);

            mesociclo.setFechaInicio(primera.getFechaInicio());
            mesociclo.setFechaFin(ultima.getFechaFin());
        }
    }
    private void inicializarMesociclosSiEsNecesario() {
        if (mesociclosInicializados || semanasPlan.isEmpty()) {
            return;
        }

        mesociclosPlanificados.clear();

        int semanaActual = 1;
        int totalSemanas = semanasPlan.size();

        while (semanaActual <= totalSemanas) {
            int duracion = Math.min(4, totalSemanas - semanaActual + 1);

            TipoMesociclo tipo = sugerirTipoMesociclo(semanaActual);

            mesociclosPlanificados.add(crearMesociclo(
                    tipo,
                    nombreMesociclo(tipo),
                    semanaActual,
                    duracion,
                    colorMesociclo(tipo)
            ));

            semanaActual += duracion;
        }

        mesociclosInicializados = true;
    }

    private TipoMesociclo sugerirTipoMesociclo(int semanaInicio) {
        if (tipoPeriodizacionActual == TipoPeriodizacion.ATR) {
            int posicion = ((semanaInicio - 1) / 4) % 3;

            return switch (posicion) {
                case 0 -> TipoMesociclo.ACUMULACION;
                case 1 -> TipoMesociclo.TRANSFORMACION;
                default -> TipoMesociclo.REALIZACION;
            };
        }

        if (tipoPeriodizacionActual == TipoPeriodizacion.TRADICIONAL) {
            if (semanaInicio <= semanasPlan.size() * 0.35) {
                return TipoMesociclo.BASE;
            }

            if (semanaInicio <= semanasPlan.size() * 0.70) {
                return TipoMesociclo.DESARROLLADOR;
            }

            return TipoMesociclo.COMPETITIVO;
        }

        return TipoMesociclo.DESARROLLADOR;
    }

    private String nombreMesociclo(TipoMesociclo tipo) {
        return switch (tipo) {
            case INTRODUCTORIO -> "Introductorio";
            case BASE -> "Base";
            case DESARROLLADOR -> "Desarrollador";
            case ESTABILIZADOR -> "Estabilizador";
            case CONTROL -> "Control";
            case PRECOMPETITIVO -> "Precompetitivo";
            case COMPETITIVO -> "Competitivo";
            case RECUPERACION -> "Recuperación";
            case TRANSICION -> "Transición";
            case ACUMULACION -> "Acumulación";
            case TRANSFORMACION -> "Transformación";
            case REALIZACION -> "Realización";
            case PERSONALIZADO -> "Personalizado";
        };
    }

    private String colorMesociclo(TipoMesociclo tipo) {
        return switch (tipo) {
            case INTRODUCTORIO -> "#dbeafe";
            case BASE -> "#93c5fd";
            case DESARROLLADOR -> "#60a5fa";
            case ESTABILIZADOR -> "#bfdbfe";
            case CONTROL -> "#fef08a";
            case PRECOMPETITIVO -> "#c084fc";
            case COMPETITIVO -> "#86efac";
            case RECUPERACION -> "#bbf7d0";
            case TRANSICION -> "#fed7aa";
            case ACUMULACION -> "#7eb4ff";
            case TRANSFORMACION -> "#66d19e";
            case REALIZACION -> "#f2c94c";
            case PERSONALIZADO -> "#e5e7eb";
        };
    }

    private MesocicloPlanificado crearMesociclo(TipoMesociclo tipo,
                                                String nombre,
                                                int semanaInicio,
                                                int duracion,
                                                String colorHex) {

        int semanaFin = semanaInicio + duracion - 1;

        if (semanaFin > semanasPlan.size()) {
            semanaFin = semanasPlan.size();
            duracion = semanaFin - semanaInicio + 1;
        }

        SemanaPlanificacion primera = semanasPlan.get(semanaInicio - 1);
        SemanaPlanificacion ultima = semanasPlan.get(semanaFin - 1);

        return new MesocicloPlanificado(
                tipo,
                nombre,
                semanaInicio,
                duracion,
                primera.getFechaInicio(),
                ultima.getFechaFin(),
                colorHex
        );
    }

    /*private void abrirEditorPeriodos() {
        if (periodosPlanificados == null || periodosPlanificados.isEmpty()) {
            periodosPlanificados = periodizacionService.generarPeriodos(
                    semanasPlan,
                    tipoPeriodizacionActual,
                    deporteActual
            );
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar períodos");
        dialog.setHeaderText("Modifica manualmente las semanas de cada período");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(15));

        List<Spinner<Integer>> spInicio = new ArrayList<>();
        List<Spinner<Integer>> spFin = new ArrayList<>();

        form.add(new Label("Período"), 0, 0);
        form.add(new Label("Semana inicio"), 1, 0);
        form.add(new Label("Semana fin"), 2, 0);

        int totalSemanas = semanasPlan.size();

        for (int i = 0; i < periodosPlanificados.size(); i++) {
            PeriodoPlanificado periodo = periodosPlanificados.get(i);

            Label lblPeriodo = new Label(periodo.getTipoPeriodo().name());

            Spinner<Integer> inicio = new Spinner<>(1, totalSemanas, periodo.getSemanaInicio());
            Spinner<Integer> fin = new Spinner<>(1, totalSemanas, periodo.getSemanaFin());

            inicio.setEditable(true);
            fin.setEditable(true);

            spInicio.add(inicio);
            spFin.add(fin);

            int row = i + 1;
            form.add(lblPeriodo, 0, row);
            form.add(inicio, 1, row);
            form.add(fin, 2, row);
        }

        dialog.getDialogPane().setContent(form);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                aplicarPeriodosManuales(spInicio, spFin);
            }
        });
    }*/

    private void abrirEditorPeriodos() {
        if (periodosPlanificados == null || periodosPlanificados.isEmpty()) {
            periodosPlanificados = periodizacionService.generarPeriodos(
                    semanasPlan,
                    tipoPeriodizacionActual,
                    deporteActual
            );
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar períodos por porcentaje");
        dialog.setHeaderText("Modifica el porcentaje de duración de cada período");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(15));

        Spinner<Double> spPreparatorio = new Spinner<>(0.0, 100.0, obtenerPorcentajePeriodo(TipoPeriodoPlanificacion.PREPARATORIO), 1.0);
        Spinner<Double> spCompetitivo = new Spinner<>(0.0, 100.0, obtenerPorcentajePeriodo(TipoPeriodoPlanificacion.COMPETITIVO), 1.0);
        Spinner<Double> spTransitorio = new Spinner<>(0.0, 100.0, obtenerPorcentajePeriodo(TipoPeriodoPlanificacion.TRANSITORIO), 1.0);

        spPreparatorio.setEditable(true);
        spCompetitivo.setEditable(true);
        spTransitorio.setEditable(true);

        form.add(new Label("Período"), 0, 0);
        form.add(new Label("Porcentaje"), 1, 0);

        form.add(new Label("Preparatorio"), 0, 1);
        form.add(spPreparatorio, 1, 1);

        form.add(new Label("Competitivo"), 0, 2);
        form.add(spCompetitivo, 1, 2);

        form.add(new Label("Transitorio"), 0, 3);
        form.add(spTransitorio, 1, 3);

        dialog.getDialogPane().setContent(form);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                double prep = spPreparatorio.getValue();
                double comp = spCompetitivo.getValue();
                double trans = spTransitorio.getValue();

                double suma = prep + comp + trans;

                if (Math.abs(suma - 100.0) > 0.01) {
                    mostrarAlerta("Porcentajes incorrectos", "La suma de los porcentajes debe ser 100%.");
                    return;
                }

                periodosPlanificados = generarPeriodosPorPorcentaje(prep, comp, trans);
                modoPeriodosManual = true;
                construirPlanGrafico();
            }
        });
    }

    private double obtenerPorcentajePeriodo(TipoPeriodoPlanificacion tipo) {
        for (PeriodoPlanificado periodo : periodosPlanificados) {
            if (periodo.getTipoPeriodo() == tipo) {
                return periodo.getPorcentaje();
            }
        }

        return switch (tipo) {
            case PREPARATORIO -> 55.0;
            case COMPETITIVO -> 35.0;
            case TRANSITORIO -> 10.0;
        };
    }

    private void aplicarPeriodosManuales(List<Spinner<Integer>> spInicio,
                                         List<Spinner<Integer>> spFin) {

        List<PeriodoPlanificado> nuevosPeriodos = new ArrayList<>();

        for (int i = 0; i < periodosPlanificados.size(); i++) {
            PeriodoPlanificado periodoAnterior = periodosPlanificados.get(i);

            int inicio = spInicio.get(i).getValue();
            int fin = spFin.get(i).getValue();

            if (fin < inicio) {
                mostrarAlerta("Error en períodos", "La semana final no puede ser menor que la semana inicial.");
                return;
            }

            if (inicio < 1 || fin > semanasPlan.size()) {
                mostrarAlerta("Error en períodos", "Las semanas están fuera del rango del plan.");
                return;
            }

            nuevosPeriodos.add(crearPeriodoManual(
                    periodoAnterior.getTipoPeriodo(),
                    inicio,
                    fin, 0
            ));
        }

        if (hayTraslapes(nuevosPeriodos)) {
            mostrarAlerta("Error en períodos", "Hay períodos que se enciman. Revisa las semanas.");
            return;
        }

        periodosPlanificados = nuevosPeriodos;
        modoPeriodosManual = true;
        construirPlanGrafico();
    }

    private PeriodoPlanificado crearPeriodoManual(TipoPeriodoPlanificacion tipo,
                                                  int semanaInicio,
                                                  int semanaFin,
                                                  double porcentaje) {

        SemanaPlanificacion primera = semanasPlan.get(semanaInicio - 1);
        SemanaPlanificacion ultima = semanasPlan.get(semanaFin - 1);

        return new PeriodoPlanificado(
                tipo,
                semanaInicio,
                semanaFin,
                primera.getFechaInicio(),
                ultima.getFechaFin(),
                porcentaje
        );
    }

    private boolean hayTraslapes(List<PeriodoPlanificado> periodos) {
        boolean[] ocupadas = new boolean[semanasPlan.size() + 1];

        for (PeriodoPlanificado periodo : periodos) {
            for (int semana = periodo.getSemanaInicio(); semana <= periodo.getSemanaFin(); semana++) {
                if (ocupadas[semana]) {
                    return true;
                }
                ocupadas[semana] = true;
            }
        }

        return false;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(mensaje);
        alert.showAndWait();
    }

   /* private void generarEstructuraPorFechas() {
        if (dpFechaInicio.getValue() == null || dpFechaFin.getValue() == null) {
            return;
        }

        if (dpFechaFin.getValue().isBefore(dpFechaInicio.getValue())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fechas inválidas");
            alert.setHeaderText("La fecha final no puede ser anterior a la fecha inicial.");
            alert.showAndWait();
            return;
        }

        fechaInicioPlan = dpFechaInicio.getValue();
        fechaFinPlan = dpFechaFin.getValue();

        generarSemanasPlanificacion();
        construirPlanGrafico();
    }*/

    private void generarEstructuraPorFechas() {
        if (dpFechaInicio.getValue() == null || dpFechaFin.getValue() == null) {
            return;
        }

        if (dpFechaFin.getValue().isBefore(dpFechaInicio.getValue())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fechas inválidas");
            alert.setHeaderText("La fecha final no puede ser anterior a la fecha inicial.");
            alert.showAndWait();
            return;
        }

        fechaInicioPlan = dpFechaInicio.getValue();
        fechaFinPlan = dpFechaFin.getValue();

        modoPeriodosManual = false;


        generarSemanasPlanificacion();

        if (!modoPeriodosManual) {
            periodosPlanificados = periodizacionService.generarPeriodos(
                    semanasPlan,
                    tipoPeriodizacionActual,
                    deporteActual
            );
        }

        etapasPlanificadas = etapasService.generarEtapas(
                tipoPeriodizacionActual,
                periodosPlanificados,
                semanasPlan
        );

        mesociclosInicializados = false;
        mesociclosPlanificados.clear();

        microciclosInicializados = false;
        microciclosPlanificados.clear();

        sesionesInicializadas = false;
        sesionesMicrociclo.clear();

        limpiarSeleccionCelda();

        construirPlanGrafico();
    }

    private List<PeriodoPlanificado> generarPeriodosPorPorcentaje(double porcentajePreparatorio,
                                                                  double porcentajeCompetitivo,
                                                                  double porcentajeTransitorio) {

        List<PeriodoPlanificado> periodos = new ArrayList<>();

        int totalSemanas = semanasPlan.size();

        int semanasPreparatorio = (int) Math.round(totalSemanas * porcentajePreparatorio / 100.0);
        int semanasCompetitivo = (int) Math.round(totalSemanas * porcentajeCompetitivo / 100.0);
        int semanasTransitorio = (int) Math.round(totalSemanas * porcentajeTransitorio / 100.0);

        int suma = semanasPreparatorio + semanasCompetitivo + semanasTransitorio;
        int diferencia = totalSemanas - suma;

        semanasCompetitivo += diferencia;

        if (semanasPreparatorio < 1) semanasPreparatorio = 1;
        if (semanasCompetitivo < 1) semanasCompetitivo = 1;
        if (semanasTransitorio < 1) semanasTransitorio = 1;

        int inicioPrep = 1;
        int finPrep = semanasPreparatorio;

        int inicioComp = finPrep + 1;
        int finComp = inicioComp + semanasCompetitivo - 1;

        int inicioTrans = finComp + 1;
        int finTrans = totalSemanas;

        periodos.add(crearPeriodoDesdePorcentaje(
                TipoPeriodoPlanificacion.PREPARATORIO,
                inicioPrep,
                finPrep,
                porcentajePreparatorio
        ));

        periodos.add(crearPeriodoDesdePorcentaje(
                TipoPeriodoPlanificacion.COMPETITIVO,
                inicioComp,
                finComp,
                porcentajeCompetitivo
        ));

        periodos.add(crearPeriodoDesdePorcentaje(
                TipoPeriodoPlanificacion.TRANSITORIO,
                inicioTrans,
                finTrans,
                porcentajeTransitorio
        ));

        return periodos;
    }

    private PeriodoPlanificado crearPeriodoDesdePorcentaje(TipoPeriodoPlanificacion tipo,
                                                           int semanaInicio,
                                                           int semanaFin,
                                                           double porcentaje) {

        SemanaPlanificacion primera = semanasPlan.get(semanaInicio - 1);
        SemanaPlanificacion ultima = semanasPlan.get(semanaFin - 1);

        return new PeriodoPlanificado(
                tipo,
                semanaInicio,
                semanaFin,
                primera.getFechaInicio(),
                ultima.getFechaFin(),
                porcentaje
        );
    }

    private void pintarFilaMeses(double anchoSemana) {
        grid.add(celdaMes("Mes", 170, 28), 0, 0);

        int columnaInicio = 1;
        YearMonth mesActual = null;
        int cantidadSemanas = 0;

        for (int i = 0; i < semanasPlan.size(); i++) {
            YearMonth mesSemana = obtenerMesDominante(semanasPlan.get(i));

            if (mesActual == null) {
                mesActual = mesSemana;
                cantidadSemanas = 1;
            } else if (mesActual.equals(mesSemana)) {
                cantidadSemanas++;
            } else {
                grid.add(
                        bloque(nombreMes(mesActual), "#06213d", cantidadSemanas * anchoSemana, 28, true),
                        columnaInicio,
                        0,
                        cantidadSemanas,
                        1
                );

                columnaInicio += cantidadSemanas;
                mesActual = mesSemana;
                cantidadSemanas = 1;
            }
        }

        if (mesActual != null) {
            grid.add(
                    bloque(nombreMes(mesActual), "#06213d", cantidadSemanas * anchoSemana, 28, true),
                    columnaInicio,
                    0,
                    cantidadSemanas,
                    1
            );
        }
    }

    private String nombreMes(YearMonth mes) {
        String nombre = mes.getMonth()
                .getDisplayName(TextStyle.FULL, new Locale("es", "MX"));

        return nombre.substring(0, 1).toUpperCase() + nombre.substring(1);
    }

    private YearMonth obtenerMesDominante(SemanaPlanificacion semana) {
        Map<YearMonth, Integer> conteoDias = new LinkedHashMap<>();

        LocalDate fecha = semana.getFechaInicio();

        while (!fecha.isAfter(semana.getFechaFin())) {
            YearMonth mes = YearMonth.from(fecha);
            conteoDias.put(mes, conteoDias.getOrDefault(mes, 0) + 1);
            fecha = fecha.plusDays(1);
        }

        return conteoDias.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(YearMonth.from(semana.getFechaInicio()));
    }

    private void generarSemanasPlanificacion() {
        semanasPlan.clear();

        LocalDate inicio = fechaInicioPlan.with(DayOfWeek.MONDAY);
        LocalDate finGeneral = fechaFinPlan.with(DayOfWeek.SUNDAY);

        int numeroSemana = 1;

        while (!inicio.isAfter(finGeneral)) {
            LocalDate fin = inicio.plusDays(6);

            semanasPlan.add(new SemanaPlanificacion(numeroSemana, inicio, fin));

            inicio = inicio.plusWeeks(1);
            numeroSemana++;
        }
    }

    /*private void generarSemanasPlanificacion() {
        semanasPlan.clear();

        LocalDate inicio = fechaInicioPlan;
        int numeroSemana = 1;

        while (!inicio.isAfter(fechaFinPlan)) {
            LocalDate fin = inicio.plusDays(6);

            if (fin.isAfter(fechaFinPlan)) {
                fin = fechaFinPlan;
            }

            semanasPlan.add(new SemanaPlanificacion(numeroSemana, inicio, fin));

            inicio = fin.plusDays(1);
            numeroSemana++;
        }
    }*/

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

        HBox leyenda = crearLeyenda();

        wrapper.getChildren().addAll(grid, leyenda);

        ScrollPane scroll = new ScrollPane(wrapper);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent;");
        return scroll;
    }

    private void construirPlanGrafico() {

        grid.getChildren().clear();
        labelsPlan.clear();

        grid.setHgap(0);
        grid.setVgap(0);
        grid.setStyle("-fx-background-color: white; -fx-border-color: #d7dde6;");

        double columnaConcepto = 170;
        double anchoSemana = 82;

      /*  grid.add(celdaMes("CONCEPTO", columnaConcepto, 58), 0, 0);
        grid.add(bloque("Mayo", "#06213d", 3 * anchoSemana, 28, true), 1, 0, 3, 1);
        grid.add(bloque("Junio", "#06213d", 8 * anchoSemana, 28, true), 4, 0, 8, 1);
        grid.add(bloque("Julio", "#06213d", 2 * anchoSemana, 28, true), 12, 0, 2, 1);
*/

        pintarFilaMeses(anchoSemana);

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd");

        grid.add(celdaMes("FECHAS", columnaConcepto, 34), 0, 1);
        for (int i = 0; i < semanasPlan.size(); i++) {
            SemanaPlanificacion semana = semanasPlan.get(i);

            String textoFecha = semana.getFechaInicio().format(formato)
                    + " - "
                    + semana.getFechaFin().format(formato);

            grid.add(celdaSemana(textoFecha, anchoSemana, 34), i + 1, 1);
        }

        grid.add(celdaMes("SEMANA", columnaConcepto, 34), 0, 2);
        for (int i = 0; i < semanasPlan.size(); i++) {
            SemanaPlanificacion semana = semanasPlan.get(i);
            grid.add(celdaSemana(String.valueOf(semana.getNumeroSemana()), anchoSemana, 34), i + 1, 2);
        }

        int row = 3;

      /*  filaBloques(row++, "PERÍODO",
                new Segmento("PREPARATORIO", 1, 6, "#d7e8ff"),
                new Segmento("COMPETITIVO", 7, 11, "#d9f3d2"),
                new Segmento("TRANSITORIO", 12, 13, "#ffe2aa")
        );*/
        filaPeriodosCalculados(row++);

        filaEtapasCalculadas(row++);

        /*filaBloques(row++, "ETAPA",
                new Segmento("Preparación General", 1, 3, "#a8ccff"),
                new Segmento("Preparación Especial", 4, 6, "#83b7ff"),
                new Segmento("Competitivo", 7, 11, "#9ee39a"),
                new Segmento("Transición", 12, 13, "#ffc768")
        );
*/
        filaMesociclosCalculados(row++);
      /*  filaBloques(row++, "MESOCICLO",
                new Segmento("Acondicionamiento", 1, 3, "#7eb4ff"),
                new Segmento("Desarrollo", 4, 6, "#5aa3ff"),
                new Segmento("Puesta a punto", 7, 8, "#8fe27a"),
                new Segmento("Competencias", 9, 11, "#61cc65"),
                new Segmento("Descarga", 12, 13, "#f2af3d")
        );*/

        filaMicrociclosCalculados(row++);

        filaGraficoCarga(row++);

        row += 4;

        int totalSemanas = semanasPlan.size();

        filaNumerica(row++, "VOLUMEN (%)", generarValores(totalSemanas, 60, 75, 90, 50));
        filaNumerica(row++, "INTENSIDAD (%)", new int[]{65, 70, 75, 60, 70, 75, 85, 60, 70, 90, 75, 85, 50});
        filaIconos(row++, "COMPETENCIAS", new String[]{"", "", "", "", "", "", "", "", "", "⚽", "⚽", "", ""});
        filaIconos(row++, "CONTROLES / TEST", new String[]{"", "📋", "📋", "", "", "📋", "", "📋", "", "", "📋", "📋", ""});
        filaSesionesCalculadas(row++);
        filaMinutosPlanificadosCalculados(row++);
        filaNumericaConColor(row++, "MINUTOS REAL.", new int[]{420, 450, 430, 400, 450, 410, 380, 420, 440, 430, 400, 0, 0});
        filaCumplimiento(row++, "CUMPLIMIENTO (%)", new int[]{93, 100, 96, 89, 100, 91, 84, 93, 98, 96, 89, 0, 0});
        filaNumerica(row++, "CARGA PLAN. (MIN x RPE)", new int[]{2700, 2880, 3240, 2160, 2700, 3240, 3780, 2160, 2700, 3240, 1800, 1260, 720});
        filaNumerica(row++, "CARGA REAL (MIN x RPE)", new int[]{2520, 2880, 3090, 1920, 2700, 2460, 2850, 2520, 2990, 3090, 2880, 0, 0});

        filaBarras(row++, "PREP. FÍSICA", "#276ef1", new int[]{20, 40, 60, 30, 50, 80, 95, 50, 70, 90, 75, 35, 20});
        /*filaBarras(row++, "PREP. TÉCNICO-TÁCTICA", "#1f9d46", new int[]{30, 45, 55, 60, 70, 75, 85, 65, 75, 80, 85, 55, 25});*/
        filaNumerica(row++, "PREP. TÉCNICO-TÁCTICA", generarValores(totalSemanas, 30, 45, 55, 60, 70, 75, 85));
        filaBarras(row++, "PREP. PSICOLÓGICA", "#8e44ad", new int[]{10, 20, 30, 35, 50, 55, 60, 45, 55, 60, 50, 25, 15});
        filaBarras(row++, "PREP. TEÓRICA", "#d49a00", new int[]{15, 25, 40, 45, 55, 65, 70, 55, 70, 75, 45, 25, 10});
    }

    private void filaSesionesCalculadas(int row) {
        grid.add(celdaTitulo("SESIONES"), 0, row);
        inicializarSesionesSiEsNecesario();

        for (int semana = 1; semana <= semanasPlan.size(); semana++) {
            int sesiones = contarSesionesSemana(semana);

            grid.add(
                    celdaEditable(
                            "SESIONES",
                            semana,
                            String.valueOf(sesiones),
                            "#ffffff",
                            82,
                            34
                    ),
                    semana,
                    row
            );
        }
    }

    private void filaMinutosPlanificadosCalculados(int row) {
        grid.add(celdaTitulo("MINUTOS PLAN."), 0, row);
        inicializarSesionesSiEsNecesario();

        for (int semana = 1; semana <= semanasPlan.size(); semana++) {
            int minutos = calcularMinutosSemana(semana);

            grid.add(
                    celdaEditable(
                            "MINUTOS PLAN.",
                            semana,
                            String.valueOf(minutos),
                            "#ffffff",
                            82,
                            34
                    ),
                    semana,
                    row
            );
        }
    }

    private void abrirEditorMicrociclos() {
        inicializarMicrociclosSiEsNecesario();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar microciclos");
        dialog.setHeaderText("Modifica tipo, semana de inicio, duración y nombre de cada microciclo");

        VBox contenido = new VBox(10);
        contenido.setPadding(new Insets(15));

        TableView<MicrocicloGraficoPlanificado> tabla = new TableView<>();
        tabla.setEditable(true);
        tabla.setPrefHeight(420);

        TableColumn<MicrocicloGraficoPlanificado, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        colNombre.setOnEditCommit(e -> e.getRowValue().setNombre(e.getNewValue()));

        TableColumn<MicrocicloGraficoPlanificado, TipoMicrociclo> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTipoMicrociclo()));
        colTipo.setCellFactory(ComboBoxTableCell.forTableColumn(TipoMicrociclo.values()));
        colTipo.setOnEditCommit(e -> {
            e.getRowValue().setTipoMicrociclo(e.getNewValue());
            e.getRowValue().setNombre(abreviaturaMicrociclo(e.getNewValue()));
            e.getRowValue().setColorHex(colorMicrociclo(e.getNewValue()));
            tabla.refresh();
        });

        TableColumn<MicrocicloGraficoPlanificado, Integer> colInicio = new TableColumn<>("Semana inicio");
        colInicio.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getSemanaInicio()));
        colInicio.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colInicio.setOnEditCommit(e -> e.getRowValue().setSemanaInicio(e.getNewValue()));

        TableColumn<MicrocicloGraficoPlanificado, Integer> colDuracion = new TableColumn<>("Duración");
        colDuracion.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDuracionSemanas()));
        colDuracion.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colDuracion.setOnEditCommit(e -> e.getRowValue().setDuracionSemanas(e.getNewValue()));

        tabla.getColumns().addAll(colNombre, colTipo, colInicio, colDuracion);
        tabla.getItems().setAll(microciclosPlanificados);

        Button btnAgregar = botonNormal("Agregar microciclo");
        btnAgregar.setOnAction(e -> {
            int inicio = microciclosPlanificados.isEmpty()
                    ? 1
                    : microciclosPlanificados.get(microciclosPlanificados.size() - 1).getSemanaFin() + 1;

            if (inicio > semanasPlan.size()) {
                mostrarAlerta("No se puede agregar", "Ya no hay semanas disponibles.");
                return;
            }

            MicrocicloGraficoPlanificado nuevo = crearMicrocicloGrafico(
                    TipoMicrociclo.CARGA,
                    abreviaturaMicrociclo(TipoMicrociclo.CARGA),
                    inicio,
                    1,
                    colorMicrociclo(TipoMicrociclo.CARGA)
            );

            microciclosPlanificados.add(nuevo);
            tabla.getItems().setAll(microciclosPlanificados);
        });

        Button btnEliminar = botonNormal("Eliminar seleccionado");
        btnEliminar.setOnAction(e -> {
            MicrocicloGraficoPlanificado seleccionado = tabla.getSelectionModel().getSelectedItem();

            if (seleccionado != null) {
                microciclosPlanificados.remove(seleccionado);
                tabla.getItems().setAll(microciclosPlanificados);
            }
        });

        HBox acciones = new HBox(10, btnAgregar, btnEliminar);

        contenido.getChildren().addAll(tabla, acciones);

        dialog.getDialogPane().setContent(contenido);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                if (!validarMicrociclos()) {
                    return;
                }

                actualizarFechasMicrociclos();
                construirPlanGrafico();
            }
        });
    }

    private boolean validarMicrociclos() {
        boolean[] ocupadas = new boolean[semanasPlan.size() + 1];

        for (MicrocicloGraficoPlanificado microciclo : microciclosPlanificados) {
            if (microciclo.getSemanaInicio() < 1 || microciclo.getSemanaFin() > semanasPlan.size()) {
                mostrarAlerta("Error en microciclos", "Un microciclo está fuera del rango de semanas.");
                return false;
            }

            if (microciclo.getDuracionSemanas() < 1) {
                mostrarAlerta("Error en microciclos", "La duración debe ser de al menos una semana.");
                return false;
            }

            for (int semana = microciclo.getSemanaInicio(); semana <= microciclo.getSemanaFin(); semana++) {
                if (ocupadas[semana]) {
                    mostrarAlerta("Error en microciclos", "Hay microciclos encimados.");
                    return false;
                }

                ocupadas[semana] = true;
            }
        }

        return true;
    }

    private void actualizarFechasMicrociclos() {
        for (MicrocicloGraficoPlanificado microciclo : microciclosPlanificados) {
            int semanaInicio = microciclo.getSemanaInicio();
            int semanaFin = microciclo.getSemanaFin();

            SemanaPlanificacion primera = semanasPlan.get(semanaInicio - 1);
            SemanaPlanificacion ultima = semanasPlan.get(semanaFin - 1);

            microciclo.setFechaInicio(primera.getFechaInicio());
            microciclo.setFechaFin(ultima.getFechaFin());
        }
    }



    private void filaGraficoCarga(int row) {
        grid.add(celdaTitulo("GRÁFICO"), 0, row);

        Canvas canvas = new Canvas(semanasPlan.size() * 82, 150);
        dibujarGraficoCarga(canvas);

        StackPane contenedor = new StackPane(canvas);
        contenedor.setPrefSize(semanasPlan.size() * 82, 150);
        contenedor.setMinSize(semanasPlan.size() * 82, 150);
        contenedor.setStyle("-fx-background-color: white; -fx-border-color: #d7dde6;");

        grid.add(contenedor, 1, row, semanasPlan.size(), 1);
    }

    private void dibujarGraficoCarga(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        double w = canvas.getWidth();
        double h = canvas.getHeight();

        gc.clearRect(0, 0, w, h);

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, w, h);

        gc.setStroke(Color.web("#e1e6ef"));
        gc.setLineWidth(1);

        for (int i = 0; i <= 4; i++) {
            double y = 20 + i * ((h - 40) / 4);
            gc.strokeLine(0, y, w, y);
        }

        for (int i = 0; i < semanasPlan.size(); i++) {
            double x = i * 82;
            gc.strokeLine(x, 0, x, h);
        }

        int[] volumen = obtenerValoresFila("VOLUMEN (%)");
        int[] intensidad = obtenerValoresFila("INTENSIDAD (%)");
        int[] tecnica = obtenerValoresFila("PREP. TÉCNICO-TÁCTICA");

        dibujarLinea(gc, volumen, "#e74c3c", w, h);
        dibujarLinea(gc, intensidad, "#f39c12", w, h);
        dibujarLinea(gc, tecnica, "#27ae60", w, h);

        gc.setFill(Color.web("#123456"));
        gc.fillText("Volumen", 15, 18);
        gc.setFill(Color.web("#e74c3c"));
        gc.fillText("●", 70, 18);

        gc.setFill(Color.web("#123456"));
        gc.fillText("Intensidad", 95, 18);
        gc.setFill(Color.web("#f39c12"));
        gc.fillText("●", 165, 18);

        gc.setFill(Color.web("#123456"));
        gc.fillText("Técnica", 190, 18);
        gc.setFill(Color.web("#27ae60"));
        gc.fillText("●", 240, 18);
    }

    private void dibujarLinea(GraphicsContext gc, int[] valores, String colorHex, double w, double h) {
        if (valores.length == 0) {
            return;
        }

        gc.setStroke(Color.web(colorHex));
        gc.setLineWidth(2.5);

        double margenSuperior = 25;
        double margenInferior = 20;
        double altoUtil = h - margenSuperior - margenInferior;

        for (int i = 0; i < valores.length - 1; i++) {
            double x1 = i * 82 + 41;
            double x2 = (i + 1) * 82 + 41;

            double y1 = margenSuperior + altoUtil - ((valores[i] / 100.0) * altoUtil);
            double y2 = margenSuperior + altoUtil - ((valores[i + 1] / 100.0) * altoUtil);

            gc.strokeLine(x1, y1, x2, y2);
        }
    }

    private int[] obtenerValoresFila(String fila) {
        int total = semanasPlan.size();
        int[] valores = new int[total];

        for (int semana = 1; semana <= total; semana++) {
            CeldaPlanGrafico celda = celdasPlan.get(fila + "-" + semana);

            if (celda == null) {
                valores[semana - 1] = 0;
                continue;
            }

            try {
                valores[semana - 1] = Integer.parseInt(celda.getValor().trim());
            } catch (NumberFormatException e) {
                valores[semana - 1] = 0;
            }
        }

        return valores;
    }

    private double calcularCurvaVolumen(double progreso) {
        return 85 - (progreso * 65);
    }

    private double calcularCurvaIntensidad(double progreso) {
        if (progreso < 0.75) {
            return 30 + (progreso * 85);
        }

        return 85 - ((progreso - 0.75) * 80);
    }

    private double calcularCurvaTecnica(double progreso) {
        double centro = 0.55;
        double amplitud = 75;
        double base = 20;

        double valor = base + amplitud * Math.exp(-Math.pow((progreso - centro) / 0.22, 2));

        return Math.min(100, valor);
    }
    private void filaMesociclosCalculados(int row) {
        grid.add(celdaTitulo("MESOCICLO"), 0, row);

        inicializarMesociclosSiEsNecesario();

        for (MesocicloPlanificado mesociclo : mesociclosPlanificados) {
            grid.add(
                    celdaEditable(
                            "MESOCICLO",
                            mesociclo.getSemanaInicio(),
                            mesociclo.getNombre() + "\n" + mesociclo.getDuracionSemanas() + " sem.",
                            mesociclo.getColorHex(),
                            mesociclo.getDuracionSemanas() * 82,
                            38
                    ),
                    mesociclo.getSemanaInicio(),
                    row,
                    mesociclo.getDuracionSemanas(),
                    1
            );
        }
    }

    private void filaEtapasCalculadas(int row) {
        grid.add(celdaTitulo("ETAPA"), 0, row);

        if (etapasPlanificadas == null || etapasPlanificadas.isEmpty()) {
            etapasPlanificadas = etapasService.generarEtapas(
                    tipoPeriodizacionActual,
                    periodosPlanificados,
                    semanasPlan
            );
        }

        for (EtapaPlanificada etapa : etapasPlanificadas) {
            grid.add(
                    celdaEditable(
                            "ETAPA",
                            etapa.getSemanaInicio(),
                            nombreEtapa(etapa.getTipoEtapa()) + "\n" + etapa.getPorcentajeDentroPeriodo() + "%",
                            colorEtapa(etapa.getTipoEtapa()),
                            etapa.getDuracionSemanas() * 82,
                            38
                    ),
                    etapa.getSemanaInicio(),
                    row,
                    etapa.getDuracionSemanas(),
                    1
            );
        }
    }

    private String colorEtapa(TipoEtapaPlanificacion tipo) {
        return switch (tipo) {
            case PREPARACION_GENERAL -> "#a8ccff";
            case PREPARACION_ESPECIAL -> "#83b7ff";
            case PRECOMPETITIVA -> "#bdf2b3";
            case COMPETITIVA -> "#85df78";
            case MANTENIMIENTO -> "#c8f7c5";
            case RECUPERACION -> "#ffe2aa";
            case REGENERACION -> "#ffd37a";

            case ACUMULACION -> "#7eb4ff";
            case TRANSFORMACION -> "#66d19e";
            case REALIZACION -> "#f2c94c";

            case CONCENTRACION_CARGA -> "#ffb86b";
            case TRANSFERENCIA -> "#9be7c3";
            case PUESTA_A_PUNTO -> "#f7d774";

            case CARGA_GENERAL -> "#c7d2fe";
            case CARGA_ESPECIAL -> "#93c5fd";
            case CARGA_COMPETITIVA -> "#60a5fa";

            case ADQUISICION_MODELO_JUEGO -> "#d8b4fe";
            case ESTABILIZACION_MODELO_JUEGO -> "#c084fc";
            case OPTIMIZACION_MODELO_JUEGO -> "#a855f7";
            case MORFOCICLO_PATRON -> "#f0abfc";

            case PERSONALIZADA -> "#e5e7eb";
        };
    }

    private String nombreEtapa(TipoEtapaPlanificacion tipo) {
        return switch (tipo) {
            case PREPARACION_GENERAL -> "Prep. general";
            case PREPARACION_ESPECIAL -> "Prep. especial";
            case PRECOMPETITIVA -> "Precompetitiva";
            case COMPETITIVA -> "Competitiva";
            case MANTENIMIENTO -> "Mantenimiento";
            case RECUPERACION -> "Recuperación";
            case REGENERACION -> "Regeneración";

            case ACUMULACION -> "Acumulación";
            case TRANSFORMACION -> "Transformación";
            case REALIZACION -> "Realización";

            case CONCENTRACION_CARGA -> "Concentración";
            case TRANSFERENCIA -> "Transferencia";
            case PUESTA_A_PUNTO -> "Puesta a punto";

            case CARGA_GENERAL -> "Carga general";
            case CARGA_ESPECIAL -> "Carga especial";
            case CARGA_COMPETITIVA -> "Carga competitiva";

            case ADQUISICION_MODELO_JUEGO -> "Adquisición";
            case ESTABILIZACION_MODELO_JUEGO -> "Estabilización";
            case OPTIMIZACION_MODELO_JUEGO -> "Optimización";
            case MORFOCICLO_PATRON -> "Morfociclo";

            case PERSONALIZADA -> "Personalizada";
        };
    }

    private void filaPeriodosCalculados(int row) {
        grid.add(celdaTitulo("PERÍODO"), 0, row);

        if (periodosPlanificados == null || periodosPlanificados.isEmpty()) {
            periodosPlanificados = periodizacionService.generarPeriodos(
                    semanasPlan,
                    tipoPeriodizacionActual,
                    deporteActual
            );
        }

        for (PeriodoPlanificado periodo : periodosPlanificados) {
            String color = colorPeriodo(periodo.getTipoPeriodo());

            grid.add(
                    celdaEditable(
                            "PERÍODO",
                            periodo.getSemanaInicio(),
                            periodo.getTipoPeriodo().name() + "\n" + periodo.getPorcentaje() + "%",
                            color,
                            periodo.getDuracionSemanas() * 82,
                            38
                    ),
                    periodo.getSemanaInicio(),
                    row,
                    periodo.getDuracionSemanas(),
                    1
            );
        }
    }

    private String colorPeriodo(TipoPeriodoPlanificacion tipo) {
        return switch (tipo) {
            case PREPARATORIO -> "#d7e8ff";
            case COMPETITIVO -> "#d9f3d2";
            case TRANSITORIO -> "#ffe2aa";
        };
    }


    private String[] generarIconos(int total) {
        String[] iconos = new String[total];

        for (int i = 0; i < total; i++) {
            iconos[i] = "";
        }

        return iconos;
    }

    private int[] generarValores(int total, int... patron) {
        int[] valores = new int[total];

        for (int i = 0; i < total; i++) {
            valores[i] = patron[i % patron.length];
        }

        return valores;
    }

    private void filaBloques(int row, String titulo, Segmento... segmentos) {
        grid.add(celdaTitulo(titulo), 0, row);

        for (Segmento s : segmentos) {
            grid.add(bloque(s.texto, s.color, (s.fin - s.inicio + 1) * 82, 38, false),
                    s.inicio, row, s.fin - s.inicio + 1, 1);
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

  /*  private void filaNumerica(int row, String titulo, int[] valores) {
        grid.add(celdaTitulo(titulo), 0, row);
        for (int i = 0; i < valores.length; i++) {
            grid.add(celda(String.valueOf(valores[i]), 82, 34), i + 1, row);
        }
    }*/

   /* private void filaNumerica(int row, String titulo, int[] valores) {
        grid.add(celdaTitulo(titulo), 0, row);

        for (int i = 0; i < valores.length; i++) {
            CeldaPlanGrafico dato = new CeldaPlanGrafico(
                    titulo,
                    i + 1,
                    String.valueOf(valores[i]),
                    "#ffffff",
                    true
            );

            grid.add(celdaEditable(dato, 82, 34), i + 1, row);
        }
    }*/

    private void filaNumerica(int row, String titulo, int[] valores) {
        grid.add(celdaTitulo(titulo), 0, row);

        for (int i = 0; i < valores.length; i++) {
            grid.add(
                    celdaEditable(titulo, i + 1, String.valueOf(valores[i]), "#ffffff", 82, 34),
                    i + 1,
                    row
            );
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
            barra.setStyle("-fx-accent: " + (valores[i] >= 90 ? "#35a853" : valores[i] >= 70 ? "#f6b73c" : "#e3342f") + ";");

            Label texto = new Label(valores[i] + "%");
            texto.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");

            cont.getChildren().addAll(barra, texto);
            grid.add(cont, i + 1, row);
        }
    }

    private void filaIconos(int row, String titulo, String[] iconos) {
        grid.add(celdaTitulo(titulo), 0, row);

        for (int i = 0; i < iconos.length; i++) {
            grid.add(
                    celdaEditable(titulo, i + 1, iconos[i], "#ffffff", 82, 34),
                    i + 1,
                    row
            );
        }
    }

   /* private void filaIconos(int row, String titulo, String[] iconos) {
        grid.add(celdaTitulo(titulo), 0, row);

        for (int i = 0; i < iconos.length; i++) {
            CeldaPlanGrafico dato = new CeldaPlanGrafico(
                    titulo,
                    i + 1,
                    iconos[i],
                    "#ffffff",
                    true
            );

            Label c = celdaEditable(dato, 82, 34);
            c.setStyle(c.getStyle() + "-fx-font-size: 16px;");
            grid.add(c, i + 1, row);
        }
    }*/
    /*private void filaIconos(int row, String titulo, String[] iconos) {
        grid.add(celdaTitulo(titulo), 0, row);
        for (int i = 0; i < iconos.length; i++) {
            Label c = celda(iconos[i], 82, 34);
            c.setStyle(estiloCelda() + "-fx-font-size: 16px;");
            grid.add(c, i + 1, row);
        }
    }*/

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

    private VBox crearPanelEdicion() {
        Label titulo = new Label("EDITAR CELDA");
        titulo.setPrefWidth(240);
        titulo.setAlignment(Pos.CENTER);
        titulo.setStyle("-fx-background-color: #06213d; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8;");

        lblCeldaSeleccionada.setStyle("-fx-font-weight: bold; -fx-text-fill: #123456;");

        txtValor.setPromptText("Nuevo valor");

        colorPicker.setValue(Color.web("#ffffff"));

        btnAplicarCambios = botonAzul("Aplicar cambios");
        btnAplicarCambios.setMaxWidth(Double.MAX_VALUE);
        btnAplicarCambios.setOnAction(e -> aplicarCambiosCelda());

        Button btnCompetencia = botonNormal("Agregar competencia ⚽");
        btnCompetencia.setMaxWidth(Double.MAX_VALUE);
        btnCompetencia.setOnAction(e -> asignarValorRapido("⚽"));

        Button btnControl = botonNormal("Agregar control 📋");
        btnControl.setMaxWidth(Double.MAX_VALUE);
        btnControl.setOnAction(e -> asignarValorRapido("📋"));

        Button btnLimpiar = botonNormal("Limpiar celda");
        btnLimpiar.setMaxWidth(Double.MAX_VALUE);
        btnLimpiar.setOnAction(e -> asignarValorRapido(""));

        panelEdicion.setPadding(new Insets(12));
        panelEdicion.setStyle("-fx-background-color: white; -fx-border-color: #d7dde6;");
        panelEdicion.getChildren().addAll(
                lblCeldaSeleccionada,
                new Label("Texto o valor:"),
                txtValor,
                new Label("Color:"),
                colorPicker,
                btnAplicarCambios,
                btnCompetencia,
                btnControl,
                btnLimpiar
        );

        return new VBox(titulo, panelEdicion);
    }

    /*private Label celdaEditable(CeldaPlanGrafico dato, double w, double h) {
        Label label = celda(dato.getValor(), w, h);

        label.setStyle(
                "-fx-background-color: " + dato.getColorHex() + ";" +
                        "-fx-border-color: #e1e6ef;" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;"
        );

        label.setUserData(dato);

        if (dato.isEditable()) {
            label.setOnMouseClicked(e -> seleccionarCelda(label));
            label.setOnMouseEntered(e -> label.setStyle(label.getStyle() + "-fx-border-color: #006bb6; -fx-border-width: 2;"));
            label.setOnMouseExited(e -> {
                CeldaPlanGrafico c = (CeldaPlanGrafico) label.getUserData();
                label.setStyle(
                        "-fx-background-color: " + c.getColorHex() + ";" +
                                "-fx-border-color: #e1e6ef;" +
                                "-fx-font-size: 12px;" +
                                "-fx-font-weight: bold;"
                );
            });
        }

        return label;
    }*/

    private Label celdaEditable(String fila, int semana, String valorInicial, String colorInicial, double w, double h) {
        String key = fila + "-" + semana;

        CeldaPlanGrafico dato = celdasPlan.computeIfAbsent(
                key,
                k -> new CeldaPlanGrafico(fila, semana, valorInicial, colorInicial, true)
        );

        Label label = celda(dato.getValor(), w, h);
        aplicarEstiloEditable(label, dato, false);

        label.setUserData(dato);
        labelsPlan.put(key, label);

        label.setOnMouseClicked(e -> {
            seleccionarCelda(label);

            if (e.getClickCount() == 2) {
                CeldaPlanGrafico celda = (CeldaPlanGrafico) label.getUserData();

                if ("MICROCICLO".equals(celda.getFila())) {
                    abrirEditorMicrocicloDesdeCelda(celda);
                } else if ("SESIONES".equals(celda.getFila()) || "MINUTOS PLAN.".equals(celda.getFila())) {
                    abrirEditorSesionesSemana(celda.getSemana());
                } else {
                    editarCeldaConDialogo(label);
                }
            }
        });

        label.setOnMouseEntered(e -> aplicarEstiloEditable(label, dato, true));
        label.setOnMouseExited(e -> aplicarEstiloEditable(label, dato, false));

        return label;
    }

    /*private void abrirEditorSesionesSemana(int semana) {
        inicializarSesionesSiEsNecesario();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar sesiones");
        dialog.setHeaderText("Semana " + semana + " | Configuración de unidades de entrenamiento");

        VBox contenido = new VBox(10);
        contenido.setPadding(new Insets(15));

        TableView<SesionMicrocicloPlanificada> tabla = new TableView<>();
        tabla.setEditable(true);
        tabla.setPrefHeight(360);

        TableColumn<SesionMicrocicloPlanificada, DayOfWeek> colDia = new TableColumn<>("Día");
        colDia.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDiaSemana()));
        colDia.setCellFactory(ComboBoxTableCell.forTableColumn(DayOfWeek.values()));
        colDia.setOnEditCommit(e -> e.getRowValue().setDiaSemana(e.getNewValue()));

        TableColumn<SesionMicrocicloPlanificada, String> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getHoraInicio().toString()));
        colHora.setCellFactory(TextFieldTableCell.forTableColumn());
        colHora.setOnEditCommit(e -> {
            try {
                e.getRowValue().setHoraInicio(LocalTime.parse(e.getNewValue()));
            } catch (Exception ex) {
                mostrarAlerta("Hora inválida", "Usa el formato HH:mm, por ejemplo 16:30.");
                tabla.refresh();
            }
        });

        TableColumn<SesionMicrocicloPlanificada, Integer> colDuracion = new TableColumn<>("Minutos");
        colDuracion.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDuracionMinutos()));
        colDuracion.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colDuracion.setOnEditCommit(e -> e.getRowValue().setDuracionMinutos(e.getNewValue()));

        TableColumn<SesionMicrocicloPlanificada, Boolean> colExtra = new TableColumn<>("Extra");
        colExtra.setCellValueFactory(data ->
                new javafx.beans.property.SimpleBooleanProperty(data.getValue().isExtra()));
        colExtra.setCellFactory(CheckBoxTableCell.forTableColumn(colExtra));
        colExtra.setOnEditCommit(e -> e.getRowValue().setExtra(e.getNewValue()));

        TableColumn<SesionMicrocicloPlanificada, String> colObs = new TableColumn<>("Observaciones");
        colObs.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getObservaciones()));
        colObs.setCellFactory(TextFieldTableCell.forTableColumn());
        colObs.setOnEditCommit(e -> e.getRowValue().setObservaciones(e.getNewValue()));

        tabla.getColumns().addAll(colDia, colHora, colDuracion, colExtra, colObs);

        tabla.getItems().setAll(
                sesionesMicrociclo.stream()
                        .filter(s -> s.getSemana() == semana)
                        .toList()
        );

        Button btnAgregar = botonNormal("Agregar unidad");
        btnAgregar.setOnAction(e -> {
            SesionMicrocicloPlanificada nueva = new SesionMicrocicloPlanificada(
                    semana,
                    DayOfWeek.MONDAY,
                    LocalTime.of(16, 30),
                    60,
                    true,
                    "Sesión extra"
            );

            sesionesMicrociclo.add(nueva);
            tabla.getItems().setAll(
                    sesionesMicrociclo.stream()
                            .filter(s -> s.getSemana() == semana)
                            .toList()
            );
        });

        Button btnEliminar = botonNormal("Eliminar unidad");
        btnEliminar.setOnAction(e -> {
            SesionMicrocicloPlanificada seleccionada = tabla.getSelectionModel().getSelectedItem();

            if (seleccionada != null) {
                sesionesMicrociclo.remove(seleccionada);
                tabla.getItems().setAll(
                        sesionesMicrociclo.stream()
                                .filter(s -> s.getSemana() == semana)
                                .toList()
                );
            }
        });

        HBox acciones = new HBox(10, btnAgregar, btnEliminar);

        Label resumen = new Label();
        resumen.setStyle("-fx-font-weight: bold; -fx-text-fill: #123456;");

        Runnable actualizarResumen = () -> resumen.setText(
                "Sesiones: " + contarSesionesSemana(semana)
                        + " | Minutos: " + calcularMinutosSemana(semana)
        );

        actualizarResumen.run();

        tabla.getItems().addListener((javafx.collections.ListChangeListener<SesionMicrocicloPlanificada>) c -> actualizarResumen.run());

        contenido.getChildren().addAll(tabla, acciones, resumen);

        dialog.getDialogPane().setContent(contenido);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                limpiarCeldasSesionSemana(semana);
                construirPlanGrafico();
            }
        });
    }*/

    private void abrirEditorSesionesSemana(int semana) {
        inicializarSesionesSiEsNecesario();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Semana " + semana);
        dialog.setHeaderText("Distribución semanal de unidades de entrenamiento");

        HBox semanaBox = new HBox(10);
        semanaBox.setPadding(new Insets(15));

        for (DayOfWeek dia : DayOfWeek.values()) {
            VBox columnaDia = crearColumnaDiaSemana(semana, dia);
            semanaBox.getChildren().add(columnaDia);
        }

        ScrollPane scroll = new ScrollPane(semanaBox);
        scroll.setFitToHeight(true);
        scroll.setPrefViewportWidth(1050);
        scroll.setPrefViewportHeight(420);

        dialog.getDialogPane().setContent(scroll);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                limpiarCeldasSesionSemana(semana);
                construirPlanGrafico();
            }
        });
    }

    private VBox crearColumnaDiaSemana(int semana, DayOfWeek dia) {
        VBox columna = new VBox(8);
        columna.setPrefWidth(140);
        columna.setPadding(new Insets(10));
        columna.setStyle("""
            -fx-background-color: white;
            -fx-border-color: #d7dde6;
            -fx-background-radius: 6;
            -fx-border-radius: 6;
            """);

        Label titulo = new Label(nombreDia(dia));
        titulo.setMaxWidth(Double.MAX_VALUE);
        titulo.setAlignment(Pos.CENTER);
        titulo.setStyle("""
            -fx-background-color: #06213d;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-padding: 6;
            -fx-background-radius: 4;
            """);

        VBox listaUnidades = new VBox(6);

        Runnable refrescar = () -> {
            listaUnidades.getChildren().clear();

            sesionesMicrociclo.stream()
                    .filter(s -> s.getSemana() == semana)
                    .filter(s -> s.getDiaSemana() == dia)
                    .forEach(s -> listaUnidades.getChildren().add(crearTarjetaUnidad(s, listaUnidades, semana, dia)));
        };

        Button btnAgregar = botonNormal("+ Unidad");
        btnAgregar.setMaxWidth(Double.MAX_VALUE);
        btnAgregar.setOnAction(e -> {
            SesionMicrocicloPlanificada nueva = new SesionMicrocicloPlanificada(
                    semana,
                    dia,
                    LocalTime.of(16, 30),
                    60,
                    true,
                    "Unidad de entrenamiento"
            );

            sesionesMicrociclo.add(nueva);
            refrescar.run();
        });

        refrescar.run();

        columna.getChildren().addAll(titulo, listaUnidades, btnAgregar);

        return columna;
    }

    private VBox crearTarjetaUnidad(SesionMicrocicloPlanificada sesion,
                                    VBox listaUnidades,
                                    int semana,
                                    DayOfWeek dia) {

        VBox card = new VBox(6);
        card.setPadding(new Insets(8));
        card.setStyle("""
            -fx-background-color: #f8fafc;
            -fx-border-color: #cbd5e1;
            -fx-background-radius: 5;
            -fx-border-radius: 5;
            """);

        TextField txtHora = new TextField(sesion.getHoraInicio().toString());
        txtHora.setPromptText("HH:mm");

        Spinner<Integer> spDuracion = new Spinner<>(1, 300, sesion.getDuracionMinutos());
        spDuracion.setEditable(true);

        TextField txtObs = new TextField(sesion.getObservaciones());
        txtObs.setPromptText("Observación");

        CheckBox chkExtra = new CheckBox("Extra");
        chkExtra.setSelected(sesion.isExtra());

        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setMaxWidth(Double.MAX_VALUE);
        btnEliminar.setStyle("""
            -fx-background-color: #d9534f;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            """);

        txtHora.focusedProperty().addListener((obs, oldVal, focused) -> {
            if (!focused) {
                try {
                    sesion.setHoraInicio(LocalTime.parse(txtHora.getText()));
                } catch (Exception ex) {
                    mostrarAlerta("Hora inválida", "Usa el formato HH:mm, por ejemplo 16:30.");
                    txtHora.setText(sesion.getHoraInicio().toString());
                }
            }
        });

        spDuracion.valueProperty().addListener((obs, oldVal, newVal) ->
                sesion.setDuracionMinutos(newVal));

        txtObs.textProperty().addListener((obs, oldVal, newVal) ->
                sesion.setObservaciones(newVal));

        chkExtra.selectedProperty().addListener((obs, oldVal, newVal) ->
                sesion.setExtra(newVal));

        btnEliminar.setOnAction(e -> {
            sesionesMicrociclo.remove(sesion);
            listaUnidades.getChildren().remove(card);
        });

        card.getChildren().addAll(
                new Label("Hora"),
                txtHora,
                new Label("Duración min"),
                spDuracion,
                chkExtra,
                new Label("Observación"),
                txtObs,
                btnEliminar
        );

        return card;
    }

    private String nombreDia(DayOfWeek dia) {
        return switch (dia) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sábado";
            case SUNDAY -> "Domingo";
        };
    }

    private void limpiarCeldasSesionSemana(int semana) {
        eliminarCeldaPlan("SESIONES", semana);
        eliminarCeldaPlan("MINUTOS PLAN.", semana);
    }

    private void abrirEditorMicrocicloDesdeCelda(CeldaPlanGrafico celda) {
        inicializarMicrociclosSiEsNecesario();

        MicrocicloGraficoPlanificado microciclo = microciclosPlanificados.stream()
                .filter(m -> celda.getSemana() >= m.getSemanaInicio()
                        && celda.getSemana() <= m.getSemanaFin())
                .findFirst()
                .orElse(null);

        if (microciclo == null) {
            mostrarAlerta("Microciclo no encontrado", "No se encontró el microciclo de esta semana.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar microciclo");
        dialog.setHeaderText("Semana " + microciclo.getSemanaInicio() + " a " + microciclo.getSemanaFin());

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(15));

        TextField txtNombre = new TextField(microciclo.getNombre());

        ComboBox<TipoMicrociclo> cbTipo = new ComboBox<>();
        cbTipo.getItems().setAll(TipoMicrociclo.values());
        cbTipo.setValue(microciclo.getTipoMicrociclo());

        Spinner<Integer> spSemanaInicio = new Spinner<>(1, semanasPlan.size(), microciclo.getSemanaInicio());
        Spinner<Integer> spDuracion = new Spinner<>(1, semanasPlan.size(), microciclo.getDuracionSemanas());

        spSemanaInicio.setEditable(true);
        spDuracion.setEditable(true);

        ColorPicker cpColor = new ColorPicker(Color.web(microciclo.getColorHex()));

        Button btnUsarNombreTipo = botonNormal("Usar abreviatura del tipo");
        btnUsarNombreTipo.setOnAction(e -> txtNombre.setText(abreviaturaMicrociclo(cbTipo.getValue())));

        Button btnEliminar = new Button("Eliminar microciclo");
        btnEliminar.setStyle("""
        -fx-background-color: #d9534f;
        -fx-text-fill: white;
        -fx-font-weight: bold;
        """);

        btnEliminar.setOnAction(e -> {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Eliminar microciclo");
            confirmacion.setHeaderText("¿Deseas eliminar este microciclo?");
            confirmacion.setContentText(
                    microciclo.getNombre()
                            + " | Semana "
                            + microciclo.getSemanaInicio()
                            + " - "
                            + microciclo.getSemanaFin()
            );

            confirmacion.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.OK) {

                    int semanaInicioEliminada = microciclo.getSemanaInicio();

                    microciclosPlanificados.remove(microciclo);

                    eliminarCeldaPlan("MICROCICLO", semanaInicioEliminada);

                    limpiarSeleccionCelda();

                    dialog.setResult(ButtonType.CANCEL);
                    dialog.close();

                    construirPlanGrafico();
                }
            });
        });

        cbTipo.setOnAction(e -> {
            TipoMicrociclo tipo = cbTipo.getValue();

            if (tipo != null) {
                txtNombre.setText(abreviaturaMicrociclo(tipo));
                cpColor.setValue(Color.web(colorMicrociclo(tipo)));
            }
        });

        form.add(new Label("Nombre:"), 0, 0);
        form.add(txtNombre, 1, 0);

        form.add(new Label("Tipo:"), 0, 1);
        form.add(cbTipo, 1, 1);

        form.add(new Label("Semana inicio:"), 0, 2);
        form.add(spSemanaInicio, 1, 2);

        form.add(new Label("Duración semanas:"), 0, 3);
        form.add(spDuracion, 1, 3);

        form.add(new Label("Color:"), 0, 4);
        form.add(cpColor, 1, 4);

        form.add(btnUsarNombreTipo, 1, 5);

        form.add(btnEliminar, 1, 6);



        dialog.getDialogPane().setContent(form);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {

                TipoMicrociclo nuevoTipo = cbTipo.getValue();
                String nuevoNombre = txtNombre.getText();
                int nuevaSemanaInicio = spSemanaInicio.getValue();
                int nuevaDuracion = spDuracion.getValue();
                String nuevoColor = toHex(cpColor.getValue());

                MicrocicloGraficoPlanificado copia = crearMicrocicloGrafico(
                        nuevoTipo,
                        nuevoNombre,
                        nuevaSemanaInicio,
                        nuevaDuracion,
                        nuevoColor
                );

                if (!resolverEncimamientoMicrociclo(copia, microciclo)) {
                    return;
                }

                int semanaAnterior = microciclo.getSemanaInicio();

                microciclo.setTipoMicrociclo(nuevoTipo);
                microciclo.setNombre(nuevoNombre);
                microciclo.setSemanaInicio(nuevaSemanaInicio);
                microciclo.setDuracionSemanas(nuevaDuracion);
                microciclo.setColorHex(nuevoColor);

                actualizarFechasMicrociclos();

                eliminarCeldaPlan("MICROCICLO", semanaAnterior);
                eliminarCeldaPlan("MICROCICLO", nuevaSemanaInicio);

                limpiarSeleccionCelda();

                construirPlanGrafico();
            }
        });
    }

    private void reconstruirSemanasMicrociclos() {
        microciclosPlanificados.sort(
                Comparator.comparingInt(MicrocicloGraficoPlanificado::getSemanaInicio)
        );

        int semanaActual = 1;

        for (MicrocicloGraficoPlanificado microciclo : microciclosPlanificados) {

            microciclo.setSemanaInicio(semanaActual);

            int nuevaDuracion = microciclo.getDuracionSemanas();

            if (semanaActual + nuevaDuracion - 1 > semanasPlan.size()) {
                nuevaDuracion = semanasPlan.size() - semanaActual + 1;
                microciclo.setDuracionSemanas(nuevaDuracion);
            }

            int semanaFin = microciclo.getSemanaFin();

            SemanaPlanificacion primera = semanasPlan.get(microciclo.getSemanaInicio() - 1);
            SemanaPlanificacion ultima = semanasPlan.get(semanaFin - 1);

            microciclo.setFechaInicio(primera.getFechaInicio());
            microciclo.setFechaFin(ultima.getFechaFin());

            semanaActual = semanaFin + 1;

            if (semanaActual > semanasPlan.size()) {
                break;
            }
        }
    }

    private void aplicarEstiloEditable(Label label, CeldaPlanGrafico dato, boolean hover) {
        label.setStyle(
                "-fx-background-color: " + dato.getColorHex() + ";" +
                        "-fx-border-color: " + (hover ? "#006bb6" : "#e1e6ef") + ";" +
                        "-fx-border-width: " + (hover ? "2" : "1") + ";" +
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
        );
    }

    private void editarCeldaConDialogo(Label label) {
        CeldaPlanGrafico dato = (CeldaPlanGrafico) label.getUserData();

        TextInputDialog dialog = new TextInputDialog(dato.getValor());
        dialog.setTitle("Editar celda");
        dialog.setHeaderText(dato.getFila() + " | Semana " + dato.getSemana());
        dialog.setContentText("Nuevo valor:");

        dialog.showAndWait().ifPresent(nuevoValor -> {
            dato.setValor(nuevoValor);
            label.setText(nuevoValor);
            seleccionarCelda(label);
        });
    }

    private void actualizarCeldaVisual(CeldaPlanGrafico celda) {
        String key = celda.getFila() + "-" + celda.getSemana();
        Label label = labelsPlan.get(key);

        if (label != null) {
            label.setText(celda.getValor());
            aplicarEstiloEditable(label, celda, false);
        }
    }

    private void seleccionarCelda(Label label) {
        celdaSeleccionada = (CeldaPlanGrafico) label.getUserData();



        lblCeldaSeleccionada.setText(
                celdaSeleccionada.getFila() + " | Semana " + celdaSeleccionada.getSemana()
        );

        txtValor.setText(celdaSeleccionada.getValor());
        colorPicker.setValue(Color.web(celdaSeleccionada.getColorHex()));

        boolean esMicrociclo = "MICROCICLO".equals(celdaSeleccionada.getFila());

        btnAplicarCambios.setText(esMicrociclo ? "Editar microciclo" : "Aplicar cambios");

        txtValor.setDisable(esMicrociclo);
        colorPicker.setDisable(esMicrociclo);
    }

    private void limpiarSeleccionCelda() {
        celdaSeleccionada = null;

        lblCeldaSeleccionada.setText("Sin celda seleccionada");

        txtValor.clear();
        txtValor.setDisable(false);

        colorPicker.setValue(Color.web("#ffffff"));
        colorPicker.setDisable(false);

        if (btnAplicarCambios != null) {
            btnAplicarCambios.setText("Aplicar cambios");
        }
    }

    private void eliminarCeldaPlan(String fila, int semana) {
        String key = fila + "-" + semana;
        celdasPlan.remove(key);
        labelsPlan.remove(key);
    }
   /* private void aplicarCambiosCelda() {
        if (celdaSeleccionada == null) {
            return;
        }

        celdaSeleccionada.setValor(txtValor.getText());
        celdaSeleccionada.setColorHex(toHex(colorPicker.getValue()));

        construirPlanGrafico();
    }*/

    private void aplicarCambiosCelda() {
        if (celdaSeleccionada == null) {
            return;
        }

        if ("MICROCICLO".equals(celdaSeleccionada.getFila())) {
            abrirEditorMicrocicloDesdeCelda(celdaSeleccionada);
            return;
        }

        celdaSeleccionada.setValor(txtValor.getText());
        celdaSeleccionada.setColorHex(toHex(colorPicker.getValue()));

        construirPlanGrafico();
    }

    /*private void asignarValorRapido(String valor) {
        if (celdaSeleccionada == null) {
            return;
        }

        celdaSeleccionada.setValor(valor);
        txtValor.setText(valor);

        construirPlanGrafico();
    }*/

    private void asignarValorRapido(String valor) {
        if (celdaSeleccionada == null) {
            return;
        }

        if ("MICROCICLO".equals(celdaSeleccionada.getFila())) {
            abrirEditorMicrocicloDesdeCelda(celdaSeleccionada);
            return;
        }

        celdaSeleccionada.setValor(valor);
        txtValor.setText(valor);

        construirPlanGrafico();
    }

    private String toHex(Color color) {
        return String.format(
                "#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255)
        );
    }

    private VBox crearPanelDerecho() {
        VBox panel = new VBox(14);
        panel.setPrefWidth(280);
        panel.setPadding(new Insets(18));
        panel.setStyle("-fx-background-color: #f4f7fb;");

        panel.getChildren().addAll(
                crearPanelEdicion(),
                tarjetaResumen("RESUMEN GENERAL",
                        "Sesiones planificadas:     49\n" +
                                "Sesiones realizadas:       44\n\n" +
                                "Minutos planificados:   5,580\n" +
                                "Minutos realizados:     4,910\n\n" +
                                "Cumplimiento general:   88.0 %"
                ),
                indicadores()
        );

        return panel;
    }
/*
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
*/

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

    public static void main(String[] args) {
        launch(args);
    }
}