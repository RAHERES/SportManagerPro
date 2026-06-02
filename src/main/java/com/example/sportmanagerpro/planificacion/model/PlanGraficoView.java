package com.example.sportmanagerpro.planificacion.model;

import com.example.sportmanagerpro.planificacion.configuracion.*;
import com.example.sportmanagerpro.planificacion.enums.*;
import com.example.sportmanagerpro.planificacion.persistencia.PlanGrafico;
import com.example.sportmanagerpro.planificacion.persistencia.PlanGraficoRepository;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class PlanGraficoView extends Application {


    private final List<FilaPlanGraficoPersonalizada> filasPersonalizadas = new ArrayList<>();

    private final PlanGraficoRepository planGraficoRepository = new PlanGraficoRepository();

    private ConfiguracionPlanificacion configuracionPlanificacion;

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
        configuracionPlanificacion = ConfiguracionPlanificacionStore.getConfiguracionActiva();

        fechaInicioPlan = configuracionPlanificacion.getFechaInicio();
        fechaFinPlan = configuracionPlanificacion.getFechaFin();
        deporteActual = configuracionPlanificacion.getDeporte();

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

        Button btnGuardarPlan = botonVerde("Guardar plan");
        btnGuardarPlan.setOnAction(e -> guardarPlanGrafico());

        Button btnCargarPlan = botonNormal("Cargar plan");
        btnCargarPlan.setOnAction(e -> cargarPlanGrafico());

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

        Button btnFilas = botonNormal("Filas + / -");
        btnFilas.setOnAction(e -> abrirGestorFilasPersonalizadas());


        Button semanas = botonNormal("Vista semanas");
        Button meses = botonVerde("Vista meses");
        Button config = botonNormal("Configuración días");
        Button exportar = botonNormal("Exportar ▾");

        controles.getChildren().addAll(
                new Label("Inicio:"), dpFechaInicio,
                new Label("Fin:"), dpFechaFin,
                cbTipoPeriodizacion,
                generar,
                btnGuardarPlan,
                btnCargarPlan,
                btnEditarPeriodos,
                btnEditarMesociclos,
                btnEditarMicrociclos,
                btnFilas,
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

    private final Path carpetaPlanesGraficos = Paths.get(
            System.getProperty("user.home"),
            "SportManagerPro",
            "planes_graficos"
    );

    private void guardarPlanGrafico() {
        TextInputDialog dialog = new TextInputDialog("Plan gráfico 2026");
        dialog.setTitle("Guardar plan gráfico");
        dialog.setHeaderText("Nombre del plan");
        dialog.setContentText("Escribe un nombre para guardar este plan:");

        Optional<String> respuesta = dialog.showAndWait();

        if (respuesta.isEmpty() || respuesta.get().trim().isEmpty()) {
            return;
        }

        try {
            PlanGrafico plan = convertirVistaAPlanGrafico(respuesta.get().trim());

            planGraficoRepository.guardar(plan);

            mostrarInformacion(
                    "Plan guardado",
                    "El plan gráfico se guardó correctamente en formato JSON."
            );

        } catch (Exception ex) {
            mostrarAlerta("Error al guardar", "No se pudo guardar el plan gráfico.");
            ex.printStackTrace();
        }
    }

    private void cargarPlanGrafico() {
        try {
            List<Path> archivos = planGraficoRepository.listarPlanes();

            if (archivos.isEmpty()) {
                mostrarAlerta("Sin planes guardados", "Todavía no existe ningún plan gráfico guardado.");
                return;
            }

            ChoiceDialog<Path> dialog = new ChoiceDialog<>(archivos.get(0), archivos);
            dialog.setTitle("Cargar plan gráfico");
            dialog.setHeaderText("Selecciona el plan que deseas cargar");
            dialog.setContentText("Plan:");

            Optional<Path> respuesta = dialog.showAndWait();

            if (respuesta.isEmpty()) {
                return;
            }

            PlanGrafico plan = planGraficoRepository.cargar(respuesta.get());

            aplicarPlanGraficoEnVista(plan);

            mostrarInformacion(
                    "Plan cargado",
                    "El plan gráfico seleccionado se cargó correctamente."
            );

        } catch (Exception ex) {
            mostrarAlerta("Error al cargar", "No se pudo cargar el plan gráfico seleccionado.");
            ex.printStackTrace();
        }
    }

    /*private void guardarPlanGrafico() {
        TextInputDialog dialog = new TextInputDialog("Plan gráfico 2026");
        dialog.setTitle("Guardar plan gráfico");
        dialog.setHeaderText("Nombre del plan");
        dialog.setContentText("Escribe un nombre para guardar este plan:");

        Optional<String> respuesta = dialog.showAndWait();

        if (respuesta.isEmpty() || respuesta.get().trim().isEmpty()) {
            return;
        }

        String nombreArchivo = respuesta.get()
                .trim()
                .replaceAll("[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ _-]", "")
                .replace(" ", "_");

        Path archivoPlanGrafico = carpetaPlanesGraficos.resolve(nombreArchivo + ".properties");

        try {
            Files.createDirectories(carpetaPlanesGraficos);

            Properties props = new Properties();

            props.setProperty("fechaInicio", fechaInicioPlan.toString());
            props.setProperty("fechaFin", fechaFinPlan.toString());
            props.setProperty("tipoPeriodizacion", tipoPeriodizacionActual.name());
            props.setProperty("deporte", deporteActual);
            props.setProperty("modoPeriodosManual", String.valueOf(modoPeriodosManual));

            props.setProperty("periodos.total", String.valueOf(periodosPlanificados.size()));
            for (int i = 0; i < periodosPlanificados.size(); i++) {
                PeriodoPlanificado p = periodosPlanificados.get(i);
                String base = "periodos." + i + ".";

                props.setProperty(base + "tipo", p.getTipoPeriodo().name());
                props.setProperty(base + "semanaInicio", String.valueOf(p.getSemanaInicio()));
                props.setProperty(base + "semanaFin", String.valueOf(p.getSemanaFin()));
                props.setProperty(base + "porcentaje", String.valueOf(p.getPorcentaje()));
            }

            props.setProperty("mesociclos.total", String.valueOf(mesociclosPlanificados.size()));
            for (int i = 0; i < mesociclosPlanificados.size(); i++) {
                MesocicloPlanificado m = mesociclosPlanificados.get(i);
                String base = "mesociclos." + i + ".";

                props.setProperty(base + "tipo", m.getTipoMesociclo().name());
                props.setProperty(base + "nombre", m.getNombre());
                props.setProperty(base + "semanaInicio", String.valueOf(m.getSemanaInicio()));
                props.setProperty(base + "duracion", String.valueOf(m.getDuracionSemanas()));
                props.setProperty(base + "color", m.getColorHex());
            }

            props.setProperty("microciclos.total", String.valueOf(microciclosPlanificados.size()));
            for (int i = 0; i < microciclosPlanificados.size(); i++) {
                MicrocicloGraficoPlanificado m = microciclosPlanificados.get(i);
                String base = "microciclos." + i + ".";

                props.setProperty(base + "tipo", m.getTipoMicrociclo().name());
                props.setProperty(base + "nombre", m.getNombre());
                props.setProperty(base + "semanaInicio", String.valueOf(m.getSemanaInicio()));
                props.setProperty(base + "duracion", String.valueOf(m.getDuracionSemanas()));
                props.setProperty(base + "color", m.getColorHex());
            }

            try (OutputStream out = Files.newOutputStream(archivoPlanGrafico)) {
                props.store(out, "Plan grafico SportManagerPro");
            }

            mostrarInformacion("Plan guardado", "El plan gráfico se guardó correctamente.");

        } catch (IOException ex) {
            mostrarAlerta("Error al guardar", "No se pudo guardar el plan gráfico.");
            ex.printStackTrace();
        }
    }

    private void cargarPlanGrafico() {
        try {
            Files.createDirectories(carpetaPlanesGraficos);

            List<Path> archivos = Files.list(carpetaPlanesGraficos)
                    .filter(path -> path.toString().endsWith(".properties"))
                    .toList();

            if (archivos.isEmpty()) {
                mostrarAlerta("Sin planes guardados", "Todavía no existe ningún plan gráfico guardado.");
                return;
            }

            ChoiceDialog<Path> dialog = new ChoiceDialog<>(archivos.get(0), archivos);
            dialog.setTitle("Cargar plan gráfico");
            dialog.setHeaderText("Selecciona el plan que deseas cargar");
            dialog.setContentText("Plan:");

            Optional<Path> respuesta = dialog.showAndWait();

            if (respuesta.isEmpty()) {
                return;
            }

            cargarPlanGraficoDesdeArchivo(respuesta.get());

        } catch (IOException ex) {
            mostrarAlerta("Error al cargar", "No se pudieron leer los planes guardados.");
            ex.printStackTrace();
        }
    }
*/
    private PlanGrafico convertirVistaAPlanGrafico(String nombrePlan) {
        PlanGrafico plan = new PlanGrafico();

        plan.id = UUID.randomUUID().toString();
        plan.nombrePlan = nombrePlan;

        plan.fechaInicio = fechaInicioPlan.toString();
        plan.fechaFin = fechaFinPlan.toString();

        plan.deporte = deporteActual;
        plan.categoria = "Sub 17 Femenil";
        plan.objetivo = "Estatal CONADEMS";
        plan.tipoPeriodizacion = tipoPeriodizacionActual.name();

        plan.modoPeriodosManual = modoPeriodosManual;

        plan.fechaCreacion = LocalDateTime.now().toString();
        plan.fechaUltimaModificacion = LocalDateTime.now().toString();

        for (PeriodoPlanificado periodo : periodosPlanificados) {
            PlanGrafico.PeriodoDTO dto = new PlanGrafico.PeriodoDTO();
            dto.tipo = periodo.getTipoPeriodo().name();
            dto.semanaInicio = periodo.getSemanaInicio();
            dto.semanaFin = periodo.getSemanaFin();
            dto.porcentaje = periodo.getPorcentaje();

            plan.periodos.add(dto);
        }

        for (MesocicloPlanificado mesociclo : mesociclosPlanificados) {
            PlanGrafico.MesocicloDTO dto = new PlanGrafico.MesocicloDTO();
            dto.tipo = mesociclo.getTipoMesociclo().name();
            dto.nombre = mesociclo.getNombre();
            dto.semanaInicio = mesociclo.getSemanaInicio();
            dto.duracion = mesociclo.getDuracionSemanas();
            dto.color = mesociclo.getColorHex();

            plan.mesociclos.add(dto);
        }

        for (MicrocicloGraficoPlanificado microciclo : microciclosPlanificados) {
            PlanGrafico.MicrocicloDTO dto = new PlanGrafico.MicrocicloDTO();
            dto.tipo = microciclo.getTipoMicrociclo().name();
            dto.nombre = microciclo.getNombre();
            dto.semanaInicio = microciclo.getSemanaInicio();
            dto.duracion = microciclo.getDuracionSemanas();
            dto.color = microciclo.getColorHex();

            plan.microciclos.add(dto);
        }

        for (SesionMicrocicloPlanificada sesion : sesionesMicrociclo) {
            PlanGrafico.SesionDTO dto = new PlanGrafico.SesionDTO();
            dto.semana = sesion.getSemana();
            dto.diaSemana = sesion.getDiaSemana().name();
            dto.horaInicio = sesion.getHoraInicio().toString();
            dto.duracionMinutos = sesion.getDuracionMinutos();
            dto.extra = sesion.isExtra();
            dto.observaciones = sesion.getObservaciones();

            plan.sesiones.add(dto);
        }

        if (configuracionPlanificacion != null) {
            for (CompetenciaPlanificada competencia : configuracionPlanificacion.getCompetencias()) {
                PlanGrafico.CompetenciaDTO dto = new PlanGrafico.CompetenciaDTO();
                dto.nombre = competencia.getNombre();
                dto.tipoCompetencia = competencia.getTipoCompetencia().name();
                dto.fechaInicio = competencia.getFechaInicio().toString();
                dto.fechaFin = competencia.getFechaFin().toString();
                dto.fase = competencia.getFase();
                dto.sede = competencia.getSede();
                dto.objetivo = competencia.getObjetivo();
                dto.prioridad = competencia.getPrioridad();
                dto.competenciaClave = competencia.isCompetenciaClave();
                dto.observaciones = competencia.getObservaciones();

                plan.competencias.add(dto);
            }
        }

        for (FilaPlanGraficoPersonalizada fila : filasPersonalizadas) {
            PlanGrafico.FilaPersonalizadaDTO dto = new PlanGrafico.FilaPersonalizadaDTO();
            dto.id = fila.getId();
            dto.nombre = fila.getNombre();
            dto.colorTitulo = fila.getColorTitulo();
            dto.editable = fila.isEditable();

            plan.filasPersonalizadas.add(dto);
        }

        for (CeldaPlanGrafico celda : celdasPlan.values()) {
            PlanGrafico.CeldaDTO dto = new PlanGrafico.CeldaDTO();
            dto.fila = celda.getFila();
            dto.semana = celda.getSemana();
            dto.valor = celda.getValor();
            dto.colorHex = celda.getColorHex();
            dto.editable = celda.isEditable();

            plan.celdas.add(dto);
        }

        return plan;
    }

    private void aplicarPlanGraficoEnVista(PlanGrafico plan) {
        fechaInicioPlan = LocalDate.parse(plan.fechaInicio);
        fechaFinPlan = LocalDate.parse(plan.fechaFin);

        deporteActual = plan.deporte == null ? "Fútbol" : plan.deporte;
        tipoPeriodizacionActual = TipoPeriodizacion.valueOf(plan.tipoPeriodizacion);
        modoPeriodosManual = plan.modoPeriodosManual;

        dpFechaInicio.setValue(fechaInicioPlan);
        dpFechaFin.setValue(fechaFinPlan);
        cbTipoPeriodizacion.setValue(tipoPeriodizacionActual);

        generarSemanasPlanificacion();

        periodosPlanificados.clear();
        mesociclosPlanificados.clear();
        microciclosPlanificados.clear();
        sesionesMicrociclo.clear();
        filasPersonalizadas.clear();
        celdasPlan.clear();
        labelsPlan.clear();

        cargarPeriodosDesdePlan(plan);
        cargarMesociclosDesdePlan(plan);
        cargarMicrociclosDesdePlan(plan);
        cargarSesionesDesdePlan(plan);
        cargarCompetenciasDesdePlan(plan);
        cargarFilasPersonalizadasDesdePlan(plan);
        cargarCeldasDesdePlan(plan);

        actualizarFechasPeriodos();
        actualizarFechasMesociclos();
        actualizarFechasMicrociclos();

        mesociclosInicializados = !mesociclosPlanificados.isEmpty();
        microciclosInicializados = !microciclosPlanificados.isEmpty();
        sesionesInicializadas = !sesionesMicrociclo.isEmpty();

        etapasPlanificadas = etapasService.generarEtapas(
                tipoPeriodizacionActual,
                periodosPlanificados,
                semanasPlan
        );

        limpiarSeleccionCelda();
        construirPlanGrafico();
    }

    private void cargarFilasPersonalizadasDesdePlan(PlanGrafico plan) {
        if (plan.filasPersonalizadas == null) {
            return;
        }

        for (PlanGrafico.FilaPersonalizadaDTO dto : plan.filasPersonalizadas) {
            filasPersonalizadas.add(
                    new FilaPlanGraficoPersonalizada(
                            dto.id,
                            dto.nombre,
                            dto.colorTitulo,
                            dto.editable
                    )
            );
        }
    }

    private void cargarPeriodosDesdePlan(PlanGrafico plan) {
        for (PlanGrafico.PeriodoDTO dto : plan.periodos) {
            TipoPeriodoPlanificacion tipo = TipoPeriodoPlanificacion.valueOf(dto.tipo);

            periodosPlanificados.add(
                    crearPeriodoManual(
                            tipo,
                            dto.semanaInicio,
                            dto.semanaFin,
                            dto.porcentaje
                    )
            );
        }

        periodosPlanificados.sort(Comparator.comparingInt(PeriodoPlanificado::getSemanaInicio));
    }

    private void cargarMesociclosDesdePlan(PlanGrafico plan) {
        for (PlanGrafico.MesocicloDTO dto : plan.mesociclos) {
            TipoMesociclo tipo = TipoMesociclo.valueOf(dto.tipo);

            mesociclosPlanificados.add(
                    crearMesociclo(
                            tipo,
                            dto.nombre,
                            dto.semanaInicio,
                            dto.duracion,
                            dto.color
                    )
            );
        }

        mesociclosPlanificados.sort(Comparator.comparingInt(MesocicloPlanificado::getSemanaInicio));
    }

    private void cargarMicrociclosDesdePlan(PlanGrafico plan) {
        for (PlanGrafico.MicrocicloDTO dto : plan.microciclos) {
            TipoMicrociclo tipo = TipoMicrociclo.valueOf(dto.tipo);

            microciclosPlanificados.add(
                    crearMicrocicloGrafico(
                            tipo,
                            dto.nombre,
                            dto.semanaInicio,
                            dto.duracion,
                            dto.color
                    )
            );
        }

        microciclosPlanificados.sort(Comparator.comparingInt(MicrocicloGraficoPlanificado::getSemanaInicio));
    }

    private void cargarSesionesDesdePlan(PlanGrafico plan) {
        for (PlanGrafico.SesionDTO dto : plan.sesiones) {
            sesionesMicrociclo.add(
                    new SesionMicrocicloPlanificada(
                            dto.semana,
                            DayOfWeek.valueOf(dto.diaSemana),
                            LocalTime.parse(dto.horaInicio),
                            dto.duracionMinutos,
                            dto.extra,
                            dto.observaciones
                    )
            );
        }
    }

    private void cargarCompetenciasDesdePlan(PlanGrafico plan) {
        if (configuracionPlanificacion == null) {
            configuracionPlanificacion = ConfiguracionPlanificacionStore.getConfiguracionActiva();
        }

        configuracionPlanificacion.getCompetencias().clear();

        for (PlanGrafico.CompetenciaDTO dto : plan.competencias) {
            configuracionPlanificacion.getCompetencias().add(
                    new CompetenciaPlanificada(
                            dto.nombre,
                            TipoCompetencia.valueOf(dto.tipoCompetencia),
                            LocalDate.parse(dto.fechaInicio),
                            LocalDate.parse(dto.fechaFin),
                            dto.fase,
                            dto.sede,
                            dto.objetivo,
                            dto.prioridad,
                            dto.competenciaClave,
                            dto.observaciones
                    )
            );
        }

        ConfiguracionPlanificacionStore.setConfiguracionActiva(configuracionPlanificacion);
    }

    private void cargarCeldasDesdePlan(PlanGrafico plan) {
        for (PlanGrafico.CeldaDTO dto : plan.celdas) {
            String key = dto.fila + "-" + dto.semana;

            celdasPlan.put(
                    key,
                    new CeldaPlanGrafico(
                            dto.fila,
                            dto.semana,
                            dto.valor,
                            dto.colorHex,
                            dto.editable
                    )
            );
        }
    }

    private void cargarPlanGraficoDesdeArchivo(Path archivoPlanGrafico) {
        try (InputStream in = Files.newInputStream(archivoPlanGrafico)) {
            Properties props = new Properties();
            props.load(in);

            fechaInicioPlan = LocalDate.parse(props.getProperty("fechaInicio"));
            fechaFinPlan = LocalDate.parse(props.getProperty("fechaFin"));
            tipoPeriodizacionActual = TipoPeriodizacion.valueOf(props.getProperty("tipoPeriodizacion"));
            deporteActual = props.getProperty("deporte", "Fútbol");
            modoPeriodosManual = Boolean.parseBoolean(props.getProperty("modoPeriodosManual", "false"));

            dpFechaInicio.setValue(fechaInicioPlan);
            dpFechaFin.setValue(fechaFinPlan);
            cbTipoPeriodizacion.setValue(tipoPeriodizacionActual);

            generarSemanasPlanificacion();

            periodosPlanificados.clear();

            int totalPeriodos = Integer.parseInt(props.getProperty("periodos.total", "0"));

            for (int i = 0; i < totalPeriodos; i++) {
                String base = "periodos." + i + ".";

                TipoPeriodoPlanificacion tipo = TipoPeriodoPlanificacion.valueOf(
                        props.getProperty(base + "tipo")
                );

                int semanaInicio = Integer.parseInt(props.getProperty(base + "semanaInicio"));
                int semanaFin = Integer.parseInt(props.getProperty(base + "semanaFin"));
                double porcentaje = Double.parseDouble(props.getProperty(base + "porcentaje", "0"));

                periodosPlanificados.add(
                        crearPeriodoManual(
                                tipo,
                                semanaInicio,
                                semanaFin,
                                porcentaje
                        )
                );
            }

            mesociclosPlanificados.clear();

            int totalMesociclos = Integer.parseInt(props.getProperty("mesociclos.total", "0"));

            for (int i = 0; i < totalMesociclos; i++) {
                String base = "mesociclos." + i + ".";

                TipoMesociclo tipo = TipoMesociclo.valueOf(
                        props.getProperty(base + "tipo")
                );

                String nombre = props.getProperty(base + "nombre", tipo.name());
                int semanaInicio = Integer.parseInt(props.getProperty(base + "semanaInicio"));
                int duracion = Integer.parseInt(props.getProperty(base + "duracion"));
                String color = props.getProperty(base + "color", colorMesociclo(tipo));

                mesociclosPlanificados.add(
                        crearMesociclo(
                                tipo,
                                nombre,
                                semanaInicio,
                                duracion,
                                color
                        )
                );
            }

            microciclosPlanificados.clear();

            int totalMicrociclos = Integer.parseInt(props.getProperty("microciclos.total", "0"));

            for (int i = 0; i < totalMicrociclos; i++) {
                String base = "microciclos." + i + ".";

                TipoMicrociclo tipo = TipoMicrociclo.valueOf(
                        props.getProperty(base + "tipo")
                );

                String nombre = props.getProperty(base + "nombre", abreviaturaMicrociclo(tipo));
                int semanaInicio = Integer.parseInt(props.getProperty(base + "semanaInicio"));
                int duracion = Integer.parseInt(props.getProperty(base + "duracion"));
                String color = props.getProperty(base + "color", colorMicrociclo(tipo));

                microciclosPlanificados.add(
                        crearMicrocicloGrafico(
                                tipo,
                                nombre,
                                semanaInicio,
                                duracion,
                                color
                        )
                );
            }

            actualizarFechasPeriodos();
            actualizarFechasMesociclos();
            actualizarFechasMicrociclos();

            mesociclosInicializados = !mesociclosPlanificados.isEmpty();
            microciclosInicializados = !microciclosPlanificados.isEmpty();

            etapasPlanificadas = etapasService.generarEtapas(
                    tipoPeriodizacionActual,
                    periodosPlanificados,
                    semanasPlan
            );

            limpiarSeleccionCelda();
            construirPlanGrafico();

            mostrarInformacion(
                    "Plan cargado",
                    "El plan gráfico seleccionado se cargó correctamente."
            );

        } catch (Exception ex) {
            mostrarAlerta("Error al cargar", "No se pudo cargar el plan gráfico seleccionado.");
            ex.printStackTrace();
        }
    }

    private void mostrarInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(mensaje);
        alert.showAndWait();
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
            mostrarAlerta("Fechas inválidas", "La fecha final no puede ser anterior a la fecha inicial.");
            return;
        }

        fechaInicioPlan = dpFechaInicio.getValue();
        fechaFinPlan = dpFechaFin.getValue();

        generarSemanasPlanificacion();

        if (periodosPlanificados == null || periodosPlanificados.isEmpty()) {
            periodosPlanificados = periodizacionService.generarPeriodos(
                    semanasPlan,
                    tipoPeriodizacionActual,
                    deporteActual
            );
            modoPeriodosManual = false;
        } else {
            modoPeriodosManual = true;
            actualizarFechasPeriodos();
        }

        if (etapasPlanificadas == null || etapasPlanificadas.isEmpty()) {
            etapasPlanificadas = etapasService.generarEtapas(
                    tipoPeriodizacionActual,
                    periodosPlanificados,
                    semanasPlan
            );
        }

        if (!mesociclosPlanificados.isEmpty()) {
            actualizarFechasMesociclos();
            mesociclosInicializados = true;
        }

        if (!microciclosPlanificados.isEmpty()) {
            actualizarFechasMicrociclos();
            microciclosInicializados = true;
        }

        sesionesInicializadas = !sesionesMicrociclo.isEmpty();

        limpiarSeleccionCelda();
        construirPlanGrafico();
    }

    private void actualizarFechasPeriodos() {
        for (PeriodoPlanificado periodo : periodosPlanificados) {
            int semanaInicio = Math.max(1, periodo.getSemanaInicio());
            int semanaFin = Math.min(semanasPlan.size(), periodo.getSemanaFin());

            if (semanaInicio <= semanaFin) {
                SemanaPlanificacion primera = semanasPlan.get(semanaInicio - 1);
                SemanaPlanificacion ultima = semanasPlan.get(semanaFin - 1);

                periodo.setFechaInicio(primera.getFechaInicio());
                periodo.setFechaFin(ultima.getFechaFin());
            }
        }
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
        filaPeriodosEditables(row++);

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
        filaCompetenciasPlanificadas(row++);
        filaIconos(row++, "CONTROLES / TEST", new String[]{"", "📋", "📋", "", "", "📋", "", "📋", "", "", "📋", "📋", ""});
        filaSesionesCalculadas(row++);
        filaMinutosPlanificadosCalculados(row++);
        filaNumericaConColor(row++, "MINUTOS REAL.", new int[]{420, 450, 430, 400, 450, 410, 380, 420, 440, 430, 400, 0, 0});
        filaCumplimiento(row++, "CUMPLIMIENTO (%)", new int[]{93, 100, 96, 89, 100, 91, 84, 93, 98, 96, 89, 0, 0});
        filaNumerica(row++, "CARGA PLAN. (MIN x RPE)", new int[]{2700, 2880, 3240, 2160, 2700, 3240, 3780, 2160, 2700, 3240, 1800, 1260, 720});
        filaNumerica(row++, "CARGA REAL (MIN x RPE)", new int[]{2520, 2880, 3090, 1920, 2700, 2460, 2850, 2520, 2990, 3090, 2880, 0, 0});

        filaNumerica(row++, "PREP. FÍSICA", generarValores(totalSemanas, 0));
        filaNumerica(row++, "AERÓBICA", generarValores(totalSemanas, 0));
        filaNumerica(row++, "FUERZA", generarValores(totalSemanas, 0));
        filaNumerica(row++, "PREP. TÉCNICO-TÁCTICA", generarValores(totalSemanas, 0));
        filaNumerica(row++, "COMPLEJOS I-II", generarValores(totalSemanas, 0));

        /*filaBarras(row++, "PREP. TÉCNICO-TÁCTICA", "#1f9d46", new int[]{30, 45, 55, 60, 70, 75, 85, 65, 75, 80, 85, 55, 25});*/
        filaNumerica(row++, "PREP. TÉCNICO-TÁCTICA", generarValores(totalSemanas, 30, 45, 55, 60, 70, 75, 85));
        filaBarras(row++, "PREP. PSICOLÓGICA", "#8e44ad", new int[]{10, 20, 30, 35, 50, 55, 60, 45, 55, 60, 50, 25, 15});
        filaBarras(row++, "PREP. TEÓRICA", "#d49a00", new int[]{15, 25, 40, 45, 55, 65, 70, 55, 70, 75, 45, 25, 10});

        pintarFilasPersonalizadas(row);
    }

    private void pintarFilasPersonalizadas(int rowInicial) {
        int row = rowInicial;

        for (FilaPlanGraficoPersonalizada fila : filasPersonalizadas) {
            pintarFilaPersonalizada(row++, fila);
        }
    }

    private void pintarFilaPersonalizada(int row, FilaPlanGraficoPersonalizada fila) {
        Label titulo = celdaTitulo(fila.getNombre());
        titulo.setStyle(estiloCelda()
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #123456;"
                + "-fx-background-color: " + fila.getColorTitulo() + ";"
        );

        titulo.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                abrirEditorFilaPersonalizada(fila);
            }
        });

        Tooltip.install(titulo, new Tooltip("Doble clic para editar esta fila"));

        grid.add(titulo, 0, row);

        for (int semana = 1; semana <= semanasPlan.size(); semana++) {
            grid.add(
                    celdaEditable(
                            fila.getClaveFila(),
                            semana,
                            "",
                            "#ffffff",
                            82,
                            34
                    ),
                    semana,
                    row
            );
        }
    }

    private void abrirGestorFilasPersonalizadas() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Filas del plan gráfico");
        dialog.setHeaderText("Agrega, edita, elimina o reordena filas personalizadas");

        VBox contenido = new VBox(12);
        contenido.setPadding(new Insets(15));

        TableView<FilaPlanGraficoPersonalizada> tabla = new TableView<>();
        tabla.setEditable(true);
        tabla.setPrefHeight(360);

        TableColumn<FilaPlanGraficoPersonalizada, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colNombre.setCellFactory(TextFieldTableCell.forTableColumn());
        colNombre.setOnEditCommit(e -> e.getRowValue().setNombre(e.getNewValue()));

        TableColumn<FilaPlanGraficoPersonalizada, String> colColor = new TableColumn<>("Color título");
        colColor.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getColorTitulo()));
        colColor.setCellFactory(TextFieldTableCell.forTableColumn());
        colColor.setOnEditCommit(e -> e.getRowValue().setColorTitulo(e.getNewValue()));

        tabla.getColumns().addAll(colNombre, colColor);
        tabla.getItems().setAll(filasPersonalizadas);

        Button btnAgregar = botonNormal("Agregar fila");
        btnAgregar.setOnAction(e -> {
            FilaPlanGraficoPersonalizada nueva = new FilaPlanGraficoPersonalizada("Nueva fila");
            filasPersonalizadas.add(nueva);
            tabla.getItems().setAll(filasPersonalizadas);
            tabla.getSelectionModel().select(nueva);
        });

        Button btnEliminar = botonNormal("Eliminar fila");
        btnEliminar.setOnAction(e -> {
            FilaPlanGraficoPersonalizada seleccionada = tabla.getSelectionModel().getSelectedItem();

            if (seleccionada == null) {
                mostrarAlerta("Sin selección", "Selecciona una fila para eliminar.");
                return;
            }

            Alert confirmar = new Alert(Alert.AlertType.CONFIRMATION);
            confirmar.setTitle("Eliminar fila");
            confirmar.setHeaderText("¿Deseas eliminar esta fila?");
            confirmar.setContentText("Se eliminarán también los valores capturados en sus semanas.");

            Optional<ButtonType> respuesta = confirmar.showAndWait();

            if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
                filasPersonalizadas.remove(seleccionada);
                eliminarCeldasFilaPersonalizada(seleccionada);
                tabla.getItems().setAll(filasPersonalizadas);
            }
        });

        Button btnSubir = botonNormal("Subir");
        btnSubir.setOnAction(e -> moverFilaPersonalizada(tabla, -1));

        Button btnBajar = botonNormal("Bajar");
        btnBajar.setOnAction(e -> moverFilaPersonalizada(tabla, 1));

        HBox acciones = new HBox(10, btnAgregar, btnEliminar, btnSubir, btnBajar);

        Label nota = new Label("Puedes editar el nombre directamente en la tabla. El color debe escribirse en formato hexadecimal, por ejemplo #ffffff.");
        nota.setStyle("-fx-text-fill: #5c6b7a;");

        contenido.getChildren().addAll(tabla, acciones, nota);

        dialog.getDialogPane().setContent(contenido);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                limpiarSeleccionCelda();
                construirPlanGrafico();
            }
        });
    }

    private void abrirEditorFilaPersonalizada(FilaPlanGraficoPersonalizada fila) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar fila");
        dialog.setHeaderText("Modificar fila personalizada");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.setPadding(new Insets(20));

        TextField txtNombre = new TextField(fila.getNombre());
        ColorPicker cpColor = new ColorPicker(Color.web(fila.getColorTitulo()));

        form.add(new Label("Nombre:"), 0, 0);
        form.add(txtNombre, 1, 0);

        form.add(new Label("Color título:"), 0, 1);
        form.add(cpColor, 1, 1);

        dialog.getDialogPane().setContent(form);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                fila.setNombre(txtNombre.getText());
                fila.setColorTitulo(toHex(cpColor.getValue()));
                construirPlanGrafico();
            }
        });
    }

    private void moverFilaPersonalizada(TableView<FilaPlanGraficoPersonalizada> tabla, int direccion) {
        FilaPlanGraficoPersonalizada seleccionada = tabla.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Sin selección", "Selecciona una fila para mover.");
            return;
        }

        int indexActual = filasPersonalizadas.indexOf(seleccionada);
        int nuevoIndex = indexActual + direccion;

        if (nuevoIndex < 0 || nuevoIndex >= filasPersonalizadas.size()) {
            return;
        }

        Collections.swap(filasPersonalizadas, indexActual, nuevoIndex);
        tabla.getItems().setAll(filasPersonalizadas);
        tabla.getSelectionModel().select(seleccionada);
    }

    private void eliminarCeldasFilaPersonalizada(FilaPlanGraficoPersonalizada fila) {
        String claveFila = fila.getClaveFila();

        List<String> clavesAEliminar = celdasPlan.keySet()
                .stream()
                .filter(key -> key.startsWith(claveFila + "-"))
                .toList();

        for (String key : clavesAEliminar) {
            celdasPlan.remove(key);
            labelsPlan.remove(key);
        }
    }

    private void filaPeriodosEditables(int row) {
        grid.add(celdaTitulo("PERÍODO"), 0, row);

        int semana = 1;

        while (semana <= semanasPlan.size()) {
            PeriodoPlanificado periodo = buscarPeriodoPorSemanaInicio(semana);

            if (periodo != null) {
                Label celda = celdaEditable(
                        "PERÍODO",
                        periodo.getSemanaInicio(),
                        periodo.getTipoPeriodo().toString() + "\n" + periodo.getPorcentaje() + "%",
                        obtenerColorPeriodo(periodo.getTipoPeriodo()),
                        82 * periodo.getDuracionSemanas(),
                        34
                );

                celda.setStyle(celda.getStyle()
                        + "-fx-font-weight: bold;"
                        + "-fx-font-size: 11px;"
                        + "-fx-cursor: hand;"
                );

                celda.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {
                        abrirEditorPeriodo(periodo);
                    }
                });

                Tooltip.install(celda, new Tooltip(
                        "Doble clic para editar o eliminar\n"
                                + "Tipo: " + periodo.getTipoPeriodo()
                                + "\nSemana inicio: " + periodo.getSemanaInicio()
                                + "\nSemana fin: " + periodo.getSemanaFin()
                                + "\nDuración: " + periodo.getDuracionSemanas() + " semanas"
                                + "\nPorcentaje: " + periodo.getPorcentaje() + "%"
                ));

                grid.add(celda, periodo.getSemanaInicio(), row, periodo.getDuracionSemanas(), 1);

                semana = periodo.getSemanaFin() + 1;
            } else {
                int semanaDisponible = semana;

                Label celdaVacia = celdaPeriodoDisponible(semanaDisponible, row);
                grid.add(celdaVacia, semanaDisponible, row);

                semana++;
            }
        }
    }

    private PeriodoPlanificado buscarPeriodoPorSemanaInicio(int semanaInicio) {
        for (PeriodoPlanificado periodo : periodosPlanificados) {
            if (periodo.getSemanaInicio() == semanaInicio) {
                return periodo;
            }
        }

        return null;
    }

    private double calcularPorcentajePeriodo(int semanaInicio, int semanaFin) {
        int duracion = semanaFin - semanaInicio + 1;
        int totalSemanas = semanasPlan.size();

        if (totalSemanas <= 0) {
            return 0;
        }

        double porcentaje = (duracion * 100.0) / totalSemanas;

        return Math.round(porcentaje * 10.0) / 10.0;
    }

    private void abrirEditorPeriodo(PeriodoPlanificado periodoOriginal) {
        Dialog<PeriodoPlanificado> dialog = new Dialog<>();
        dialog.setTitle("Editar periodo");
        dialog.setHeaderText("Modificar o eliminar periodo");

        ButtonType btnGuardar = new ButtonType("Guardar cambios", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnEliminar = new ButtonType("Eliminar periodo", ButtonBar.ButtonData.LEFT);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnEliminar, btnCancelar);

        ComboBox<TipoPeriodoPlanificacion> cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll(TipoPeriodoPlanificacion.values());
        cbTipo.setValue(periodoOriginal.getTipoPeriodo());

        Spinner<Integer> spSemanaInicio = new Spinner<>(
                1,
                semanasPlan.size(),
                periodoOriginal.getSemanaInicio()
        );
        spSemanaInicio.setEditable(true);

        Spinner<Integer> spSemanaFin = new Spinner<>(
                1,
                semanasPlan.size(),
                periodoOriginal.getSemanaFin()
        );


        spSemanaFin.setEditable(true);

        Spinner<Double> spPorcentaje = new Spinner<>(
                0.0,
                100.0,
                calcularPorcentajePeriodo(periodoOriginal.getSemanaInicio(), periodoOriginal.getSemanaFin()),
                0.1
        );
        spPorcentaje.setEditable(false);
        spPorcentaje.setDisable(true);

        spSemanaInicio.valueProperty().addListener((obs, old, val) -> {
            double nuevoPorcentaje = calcularPorcentajePeriodo(val, spSemanaFin.getValue());
            spPorcentaje.getValueFactory().setValue(nuevoPorcentaje);
        });

        spSemanaFin.valueProperty().addListener((obs, old, val) -> {
            double nuevoPorcentaje = calcularPorcentajePeriodo(spSemanaInicio.getValue(), val);
            spPorcentaje.getValueFactory().setValue(nuevoPorcentaje);
        });


        GridPane gridDialog = new GridPane();
        gridDialog.setHgap(12);
        gridDialog.setVgap(12);
        gridDialog.setPadding(new Insets(20));

        gridDialog.add(new Label("Tipo de periodo:"), 0, 0);
        gridDialog.add(cbTipo, 1, 0);

        gridDialog.add(new Label("Semana inicio:"), 0, 1);
        gridDialog.add(spSemanaInicio, 1, 1);

        gridDialog.add(new Label("Semana fin:"), 0, 2);
        gridDialog.add(spSemanaFin, 1, 2);

        gridDialog.add(new Label("Porcentaje:"), 0, 3);
        gridDialog.add(spPorcentaje, 1, 3);

        dialog.getDialogPane().setContent(gridDialog);

        final boolean[] eliminar = {false};

        Button btnEliminarNode = (Button) dialog.getDialogPane().lookupButton(btnEliminar);
        btnEliminarNode.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            event.consume();

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Eliminar periodo");
            confirmacion.setHeaderText("¿Deseas eliminar este periodo?");
            confirmacion.setContentText("Las semanas ocupadas quedarán disponibles para agregar otro periodo.");

            Optional<ButtonType> respuesta = confirmacion.showAndWait();

            if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
                eliminar[0] = true;
                dialog.setResult(null);
                dialog.close();
            }
        });

        dialog.setResultConverter(button -> {
            if (button == btnGuardar) {
                int semanaInicio = spSemanaInicio.getValue();
                int semanaFin = spSemanaFin.getValue();

                if (semanaFin < semanaInicio) {
                    mostrarAlerta("Error", "La semana final no puede ser menor que la semana inicial.");
                    return null;
                }

                if (existeCrucePeriodo(periodoOriginal, semanaInicio, semanaFin)) {
                    mostrarAlerta("Cruce de periodos", "El rango seleccionado se cruza con otro periodo.");
                    return null;
                }

                return new PeriodoPlanificado(
                        cbTipo.getValue(),
                        semanaInicio,
                        semanaFin,
                        obtenerFechaInicioSemana(semanaInicio),
                        obtenerFechaFinSemana(semanaFin),
                        calcularPorcentajePeriodo(semanaInicio, semanaFin)
                );
            }

            return null;
        });

        Optional<PeriodoPlanificado> resultado = dialog.showAndWait();

        if (eliminar[0]) {
            eliminarPeriodo(periodoOriginal);
            return;
        }

        resultado.ifPresent(periodoNuevo -> {
            reemplazarPeriodo(periodoOriginal, periodoNuevo);
            modoPeriodosManual = true;
            redibujarPlanGraficoCompleto();
        });
    }

    private void eliminarPeriodo(PeriodoPlanificado periodo) {
        periodosPlanificados.remove(periodo);
        modoPeriodosManual = true;
        redibujarPlanGraficoCompleto();
    }


    private void abrirEditorNuevoPeriodo(int semanaDisponible) {
        Dialog<PeriodoPlanificado> dialog = new Dialog<>();
        dialog.setTitle("Agregar periodo");
        dialog.setHeaderText("Agregar nuevo periodo desde la semana " + semanaDisponible);

        ButtonType btnGuardar = new ButtonType("Agregar periodo", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);

        ComboBox<TipoPeriodoPlanificacion> cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll(TipoPeriodoPlanificacion.values());
        cbTipo.setValue(TipoPeriodoPlanificacion.PREPARATORIO);

        Spinner<Integer> spSemanaInicio = new Spinner<>(
                1,
                semanasPlan.size(),
                semanaDisponible
        );
        spSemanaInicio.setEditable(true);

        Spinner<Integer> spSemanaFin = new Spinner<>(
                1,
                semanasPlan.size(),
                semanaDisponible
        );
        spSemanaFin.setEditable(true);

        Spinner<Double> spPorcentaje = new Spinner<>(
                0.0,
                100.0,
                calcularPorcentajePeriodo(semanaDisponible, semanaDisponible),
                0.1
        );
        spPorcentaje.setEditable(false);
        spPorcentaje.setDisable(true);

        spSemanaInicio.valueProperty().addListener((obs, old, val) -> {
            double nuevoPorcentaje = calcularPorcentajePeriodo(val, spSemanaFin.getValue());
            spPorcentaje.getValueFactory().setValue(nuevoPorcentaje);
        });

        spSemanaFin.valueProperty().addListener((obs, old, val) -> {
            double nuevoPorcentaje = calcularPorcentajePeriodo(spSemanaInicio.getValue(), val);
            spPorcentaje.getValueFactory().setValue(nuevoPorcentaje);
        });

        GridPane gridDialog = new GridPane();
        gridDialog.setHgap(12);
        gridDialog.setVgap(12);
        gridDialog.setPadding(new Insets(20));

        gridDialog.add(new Label("Tipo de periodo:"), 0, 0);
        gridDialog.add(cbTipo, 1, 0);

        gridDialog.add(new Label("Semana inicio:"), 0, 1);
        gridDialog.add(spSemanaInicio, 1, 1);

        gridDialog.add(new Label("Semana fin:"), 0, 2);
        gridDialog.add(spSemanaFin, 1, 2);

        gridDialog.add(new Label("Porcentaje:"), 0, 3);
        gridDialog.add(spPorcentaje, 1, 3);

        dialog.getDialogPane().setContent(gridDialog);

        dialog.setResultConverter(button -> {
            if (button == btnGuardar) {
                int semanaInicio = spSemanaInicio.getValue();
                int semanaFin = spSemanaFin.getValue();

                if (semanaFin < semanaInicio) {
                    mostrarAlerta("Error", "La semana final no puede ser menor que la semana inicial.");
                    return null;
                }

                if (existeCrucePeriodo(null, semanaInicio, semanaFin)) {
                    mostrarAlerta("Cruce de periodos", "El rango seleccionado se cruza con otro periodo.");
                    return null;
                }

                return new PeriodoPlanificado(
                        cbTipo.getValue(),
                        semanaInicio,
                        semanaFin,
                        obtenerFechaInicioSemana(semanaInicio),
                        obtenerFechaFinSemana(semanaFin),
                        calcularPorcentajePeriodo(semanaInicio, semanaFin)                );
            }

            return null;
        });

        Optional<PeriodoPlanificado> resultado = dialog.showAndWait();

        resultado.ifPresent(periodoNuevo -> {
            periodosPlanificados.add(periodoNuevo);
            periodosPlanificados.sort(Comparator.comparingInt(PeriodoPlanificado::getSemanaInicio));
            modoPeriodosManual = true;
            redibujarPlanGraficoCompleto();
        });
    }


    private boolean existeCrucePeriodo(PeriodoPlanificado periodoIgnorado, int nuevaSemanaInicio, int nuevaSemanaFin) {
        for (PeriodoPlanificado periodo : periodosPlanificados) {
            if (periodo == periodoIgnorado) {
                continue;
            }

            boolean cruza = nuevaSemanaInicio <= periodo.getSemanaFin()
                    && nuevaSemanaFin >= periodo.getSemanaInicio();

            if (cruza) {
                return true;
            }
        }

        return false;
    }


    private void reemplazarPeriodo(PeriodoPlanificado periodoOriginal, PeriodoPlanificado periodoNuevo) {
        int index = periodosPlanificados.indexOf(periodoOriginal);

        if (index >= 0) {
            periodosPlanificados.set(index, periodoNuevo);
        }

        periodosPlanificados.sort(Comparator.comparingInt(PeriodoPlanificado::getSemanaInicio));
    }


    private LocalDate obtenerFechaInicioSemana(int numeroSemana) {
        return semanasPlan.stream()
                .filter(s -> s.getNumeroSemana() == numeroSemana)
                .findFirst()
                .map(SemanaPlanificacion::getFechaInicio)
                .orElse(fechaInicioPlan);
    }


    private LocalDate obtenerFechaFinSemana(int numeroSemana) {
        return semanasPlan.stream()
                .filter(s -> s.getNumeroSemana() == numeroSemana)
                .findFirst()
                .map(SemanaPlanificacion::getFechaFin)
                .orElse(fechaFinPlan);
    }

    private String obtenerColorPeriodo(TipoPeriodoPlanificacion tipo) {
        return switch (tipo) {
            case PREPARATORIO -> "#cfe3ff";
            case COMPETITIVO -> "#d8f5d0";
            case TRANSITORIO -> "#ffe5c4";
            default -> "#e5e7eb";
        };
    }

    private void redibujarPlanGraficoCompleto() {
        grid.getChildren().clear();
        celdasPlan.clear();
        labelsPlan.clear();

        construirPlanGrafico();
    }


    private void filaCompetenciasPlanificadas(int row) {
        grid.add(celdaTitulo("COMPETENCIAS"), 0, row);

        for (int i = 0; i < semanasPlan.size(); i++) {
            SemanaPlanificacion semana = semanasPlan.get(i);
            int numeroSemana = semana.getNumeroSemana();

            List<CompetenciaPlanificada> competenciasSemana = obtenerCompetenciasDeSemana(semana);

            if (competenciasSemana.isEmpty()) {
                grid.add(
                        celdaEditable("COMPETENCIAS", numeroSemana, "", "#ffffff", 82, 34),
                        i + 1,
                        row
                );
                continue;
            }

            String texto = generarTextoCompetencia(competenciasSemana);
            String color = obtenerColorCompetencia(competenciasSemana);

            Label celda = celdaEditable(
                    "COMPETENCIAS",
                    numeroSemana,
                    texto,
                    color,
                    82,
                    34
            );

            celda.setStyle(celda.getStyle()
                    + "-fx-font-size: 13px;"
                    + "-fx-font-weight: bold;"
                    + "-fx-text-fill: #08294a;"
                    + "-fx-cursor: hand;"
            );

            Tooltip tooltip = new Tooltip(generarTooltipCompetencias(competenciasSemana));
            tooltip.setWrapText(true);
            tooltip.setMaxWidth(350);
            Tooltip.install(celda, tooltip);

            celda.setOnMouseClicked(e -> mostrarDetalleCompetencias(competenciasSemana));

            grid.add(celda, i + 1, row);
        }
    }

    private Label celdaPeriodoDisponible(int semana, int row) {
        Label celda = new Label("+");
        celda.setAlignment(Pos.CENTER);
        celda.setMinSize(82, 34);
        celda.setPrefSize(82, 34);
        celda.setMaxSize(82, 34);

        String estiloNormal =
                "-fx-background-color: #f8fafc;"
                        + "-fx-border-color: #94a3b8;"
                        + "-fx-border-style: dashed;"
                        + "-fx-border-width: 1.2;"
                        + "-fx-text-fill: #08294a;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-cursor: hand;";

        String estiloHover =
                "-fx-background-color: #dbeafe;"
                        + "-fx-border-color: #0875c9;"
                        + "-fx-border-style: dashed;"
                        + "-fx-border-width: 1.6;"
                        + "-fx-text-fill: #0875c9;"
                        + "-fx-font-size: 16px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-cursor: hand;";

        celda.setStyle(estiloNormal);

        Tooltip tooltip = new Tooltip("Doble clic para agregar un periodo en la semana " + semana);
        Tooltip.install(celda, tooltip);

        celda.setOnMouseEntered(e -> celda.setStyle(estiloHover));
        celda.setOnMouseExited(e -> celda.setStyle(estiloNormal));

        celda.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                abrirEditorNuevoPeriodo(semana);
            }
        });

        return celda;
    }

    private List<CompetenciaPlanificada> obtenerCompetenciasDeSemana(SemanaPlanificacion semana) {
        if (configuracionPlanificacion == null) {
            configuracionPlanificacion = ConfiguracionPlanificacionStore.getConfiguracionActiva();
        }

        List<CompetenciaPlanificada> resultado = new ArrayList<>();

        for (CompetenciaPlanificada competencia : configuracionPlanificacion.getCompetencias()) {
            if (competenciaCruzaSemana(competencia, semana)) {
                resultado.add(competencia);
            }
        }

        resultado.sort(Comparator
                .comparing(CompetenciaPlanificada::isCompetenciaClave).reversed()
                .thenComparing(CompetenciaPlanificada::getPrioridad).reversed()
                .thenComparing(CompetenciaPlanificada::getFechaInicio)
        );

        return resultado;
    }

    private boolean competenciaCruzaSemana(CompetenciaPlanificada competencia, SemanaPlanificacion semana) {
        LocalDate inicioCompetencia = competencia.getFechaInicio();
        LocalDate finCompetencia = competencia.getFechaFin();

        LocalDate inicioSemana = semana.getFechaInicio();
        LocalDate finSemana = semana.getFechaFin();

        return !inicioCompetencia.isAfter(finSemana) && !finCompetencia.isBefore(inicioSemana);
    }

    private String generarTextoCompetencia(List<CompetenciaPlanificada> competencias) {
        if (competencias.size() == 1) {
            CompetenciaPlanificada competencia = competencias.get(0);

            if (competencia.isCompetenciaClave()) {
                return "⚽★";
            }

            return "⚽";
        }

        boolean hayClave = competencias.stream().anyMatch(CompetenciaPlanificada::isCompetenciaClave);

        if (hayClave) {
            return "⚽★" + competencias.size();
        }

        return "⚽" + competencias.size();
    }

    private String obtenerColorCompetencia(List<CompetenciaPlanificada> competencias) {
        boolean hayClave = competencias.stream().anyMatch(CompetenciaPlanificada::isCompetenciaClave);

        if (hayClave) {
            return "#ffb3b3";
        }

        boolean hayPrincipal = competencias.stream()
                .anyMatch(c -> c.getTipoCompetencia() == TipoCompetencia.PRINCIPAL);

        if (hayPrincipal) {
            return "#ffd6a5";
        }

        boolean haySecundaria = competencias.stream()
                .anyMatch(c -> c.getTipoCompetencia() == TipoCompetencia.SECUNDARIA);

        if (haySecundaria) {
            return "#fff3b0";
        }

        boolean hayPreparatoria = competencias.stream()
                .anyMatch(c -> c.getTipoCompetencia() == TipoCompetencia.PREPARATORIA);

        if (hayPreparatoria) {
            return "#d9f99d";
        }

        boolean hayAmistosa = competencias.stream()
                .anyMatch(c -> c.getTipoCompetencia() == TipoCompetencia.AMISTOSA);

        if (hayAmistosa) {
            return "#dbeafe";
        }

        return "#e5e7eb";
    }

    private String generarTooltipCompetencias(List<CompetenciaPlanificada> competencias) {
        StringBuilder sb = new StringBuilder();

        for (CompetenciaPlanificada competencia : competencias) {
            sb.append(competencia.getNombre()).append("\n");
            sb.append("Tipo: ").append(competencia.getTipoCompetencia()).append("\n");
            sb.append("Fechas: ").append(competencia.getFechaInicio()).append(" a ").append(competencia.getFechaFin()).append("\n");
            sb.append("Fase: ").append(competencia.getFase()).append("\n");
            sb.append("Sede: ").append(competencia.getSede()).append("\n");
            sb.append("Prioridad: ").append(competencia.getPrioridad()).append("\n");

            if (competencia.isCompetenciaClave()) {
                sb.append("Competencia clave: Sí\n");
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    private void mostrarDetalleCompetencias(List<CompetenciaPlanificada> competencias) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Competencias de la semana");
        alert.setHeaderText("Competencias registradas en esta semana");

        TextArea area = new TextArea(generarTooltipCompetencias(competencias));
        area.setEditable(false);
        area.setWrapText(true);
        area.setPrefWidth(500);
        area.setPrefHeight(300);

        alert.getDialogPane().setContent(area);
        alert.showAndWait();
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

        int semana = 1;

        while (semana <= semanasPlan.size()) {
            MesocicloPlanificado mesociclo = buscarMesocicloPorSemanaInicio(semana);

            if (mesociclo != null) {
                Label celda = celdaEditable(
                        "MESOCICLO",
                        mesociclo.getSemanaInicio(),
                        mesociclo.getNombre() + "\n" + mesociclo.getDuracionSemanas() + " sem.",
                        mesociclo.getColorHex(),
                        mesociclo.getDuracionSemanas() * 82,
                        38
                );

                celda.setStyle(celda.getStyle()
                        + "-fx-font-weight: bold;"
                        + "-fx-font-size: 11px;"
                        + "-fx-cursor: hand;"
                );

                Tooltip.install(celda, new Tooltip(
                        "Doble clic para editar o eliminar mesociclo\n"
                                + "Tipo: " + mesociclo.getTipoMesociclo()
                                + "\nSemana inicio: " + mesociclo.getSemanaInicio()
                                + "\nSemana fin: " + mesociclo.getSemanaFin()
                                + "\nObjetivo: " + mesociclo.getObjetivo()
                                + "\nCapacidades: " + mesociclo.getCapacidadesPrioritarias()
                ));

                celda.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {
                        abrirEditorMesocicloDesdeBloque(mesociclo);
                    }
                });

                grid.add(celda, mesociclo.getSemanaInicio(), row, mesociclo.getDuracionSemanas(), 1);

                semana = mesociclo.getSemanaFin() + 1;
            } else {
                int semanaDisponible = semana;
                grid.add(celdaMesocicloDisponible(semanaDisponible), semanaDisponible, row);
                semana++;
            }
        }
    }

    private MesocicloPlanificado buscarMesocicloPorSemanaInicio(int semanaInicio) {
        for (MesocicloPlanificado mesociclo : mesociclosPlanificados) {
            if (mesociclo.getSemanaInicio() == semanaInicio) {
                return mesociclo;
            }
        }

        return null;
    }

    private Label celdaMesocicloDisponible(int semana) {
        Label celda = new Label("+");
        celda.setAlignment(Pos.CENTER);
        celda.setMinSize(82, 38);
        celda.setPrefSize(82, 38);
        celda.setMaxSize(82, 38);

        String normal = """
            -fx-background-color: #f8fafc;
            -fx-border-color: #94a3b8;
            -fx-border-style: dashed;
            -fx-border-width: 1.2;
            -fx-text-fill: #08294a;
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-cursor: hand;
            """;

        String hover = """
            -fx-background-color: #dbeafe;
            -fx-border-color: #0875c9;
            -fx-border-style: dashed;
            -fx-border-width: 1.6;
            -fx-text-fill: #0875c9;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-cursor: hand;
            """;

        celda.setStyle(normal);

        Tooltip.install(celda, new Tooltip("Doble clic para agregar mesociclo en la semana " + semana));

        celda.setOnMouseEntered(e -> celda.setStyle(hover));
        celda.setOnMouseExited(e -> celda.setStyle(normal));

        celda.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                abrirEditorNuevoMesocicloDesdeSemana(semana);
            }
        });

        return celda;
    }

    private void abrirEditorMesocicloDesdeBloque(MesocicloPlanificado mesocicloOriginal) {
        Dialog<MesocicloPlanificado> dialog = new Dialog<>();
        dialog.setTitle("Editor de mesociclo");
        dialog.setHeaderText("Configurar mesociclo, microciclos, cargas y distribución de tiempos");

        ButtonType btnGuardar = new ButtonType("Guardar cambios", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnEliminar = new ButtonType("Eliminar mesociclo", ButtonBar.ButtonData.LEFT);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnEliminar, btnCancelar);

        ComboBox<TipoMesociclo> cbTipo = new ComboBox<>();
        cbTipo.getItems().setAll(TipoMesociclo.values());
        cbTipo.setValue(mesocicloOriginal.getTipoMesociclo());

        TextField txtNombre = new TextField(mesocicloOriginal.getNombre());

        Spinner<Integer> spSemanaInicio = new Spinner<>(1, semanasPlan.size(), mesocicloOriginal.getSemanaInicio());
        Spinner<Integer> spDuracion = new Spinner<>(1, semanasPlan.size(), mesocicloOriginal.getDuracionSemanas());

        spSemanaInicio.setEditable(true);
        spDuracion.setEditable(true);

        ColorPicker cpColor = new ColorPicker(Color.web(mesocicloOriginal.getColorHex()));

        TextArea txtObjetivo = new TextArea(mesocicloOriginal.getObjetivo());
        txtObjetivo.setPrefRowCount(2);
        txtObjetivo.setWrapText(true);

        TextField txtCapacidades = new TextField(mesocicloOriginal.getCapacidadesPrioritarias());

        Spinner<Double> spPrepFisica = new Spinner<>(0.0, 100.0, mesocicloOriginal.getPorcentajePreparacionFisica(), 1.0);
        Spinner<Double> spPrepTecTac = new Spinner<>(0.0, 100.0, mesocicloOriginal.getPorcentajePreparacionTecnicoTactica(), 1.0);
        Spinner<Double> spAerobico = new Spinner<>(0.0, 100.0, mesocicloOriginal.getPorcentajeAerobico(), 1.0);
        Spinner<Double> spFuerza = new Spinner<>(0.0, 100.0, mesocicloOriginal.getPorcentajeFuerza(), 1.0);
        Spinner<Double> spComplejos = new Spinner<>(0.0, 100.0, mesocicloOriginal.getPorcentajeComplejos(), 1.0);

        spPrepFisica.setEditable(true);
        spPrepTecTac.setEditable(true);
        spAerobico.setEditable(true);
        spFuerza.setEditable(true);
        spComplejos.setEditable(true);

        List<MicrocicloMesocicloConfig> configuracionTemporal = copiarConfiguracionMicrociclos(mesocicloOriginal);
        VBox tablaMicrociclos = new VBox(6);
        VBox resultados = new VBox(6);

        Runnable refrescarTodo = () -> {
            ajustarConfiguracionTemporal(configuracionTemporal, spDuracion.getValue());
            construirTablaMicrociclosMesociclo(tablaMicrociclos, configuracionTemporal, resultados,
                    spPrepFisica, spPrepTecTac, spAerobico, spFuerza, spComplejos);
            actualizarResultadosMesociclo(resultados, configuracionTemporal,
                    spPrepFisica.getValue(), spPrepTecTac.getValue(), spAerobico.getValue(), spFuerza.getValue(), spComplejos.getValue());
        };

        spDuracion.valueProperty().addListener((obs, oldVal, newVal) -> refrescarTodo.run());
        spPrepFisica.valueProperty().addListener((obs, oldVal, newVal) -> actualizarResultadosMesociclo(resultados, configuracionTemporal,
                spPrepFisica.getValue(), spPrepTecTac.getValue(), spAerobico.getValue(), spFuerza.getValue(), spComplejos.getValue()));
        spPrepTecTac.valueProperty().addListener((obs, oldVal, newVal) -> actualizarResultadosMesociclo(resultados, configuracionTemporal,
                spPrepFisica.getValue(), spPrepTecTac.getValue(), spAerobico.getValue(), spFuerza.getValue(), spComplejos.getValue()));
        spAerobico.valueProperty().addListener((obs, oldVal, newVal) -> actualizarResultadosMesociclo(resultados, configuracionTemporal,
                spPrepFisica.getValue(), spPrepTecTac.getValue(), spAerobico.getValue(), spFuerza.getValue(), spComplejos.getValue()));
        spFuerza.valueProperty().addListener((obs, oldVal, newVal) -> actualizarResultadosMesociclo(resultados, configuracionTemporal,
                spPrepFisica.getValue(), spPrepTecTac.getValue(), spAerobico.getValue(), spFuerza.getValue(), spComplejos.getValue()));
        spComplejos.valueProperty().addListener((obs, oldVal, newVal) -> actualizarResultadosMesociclo(resultados, configuracionTemporal,
                spPrepFisica.getValue(), spPrepTecTac.getValue(), spAerobico.getValue(), spFuerza.getValue(), spComplejos.getValue()));

        cbTipo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtNombre.setText(nombreMesociclo(newVal));
                cpColor.setValue(Color.web(colorMesociclo(newVal)));
            }
        });

        GridPane datosGenerales = new GridPane();
        datosGenerales.setHgap(12);
        datosGenerales.setVgap(12);
        datosGenerales.setPadding(new Insets(15));

        datosGenerales.add(new Label("Tipo:"), 0, 0);
        datosGenerales.add(cbTipo, 1, 0);
        datosGenerales.add(new Label("Nombre:"), 0, 1);
        datosGenerales.add(txtNombre, 1, 1);
        datosGenerales.add(new Label("Semana inicio:"), 0, 2);
        datosGenerales.add(spSemanaInicio, 1, 2);
        datosGenerales.add(new Label("Duración:"), 0, 3);
        datosGenerales.add(spDuracion, 1, 3);
        datosGenerales.add(new Label("Color:"), 0, 4);
        datosGenerales.add(cpColor, 1, 4);
        datosGenerales.add(new Label("Objetivo:"), 0, 5);
        datosGenerales.add(txtObjetivo, 1, 5);
        datosGenerales.add(new Label("Capacidades:"), 0, 6);
        datosGenerales.add(txtCapacidades, 1, 6);

        GridPane distribucion = new GridPane();
        distribucion.setHgap(12);
        distribucion.setVgap(12);
        distribucion.setPadding(new Insets(15));

        distribucion.add(new Label("Preparación física %:"), 0, 0);
        distribucion.add(spPrepFisica, 1, 0);
        distribucion.add(new Label("Preparación técnico-táctica %:"), 0, 1);
        distribucion.add(spPrepTecTac, 1, 1);
        distribucion.add(new Label("Aeróbico % dentro de P.F.:"), 0, 2);
        distribucion.add(spAerobico, 1, 2);
        distribucion.add(new Label("Fuerza % dentro de P.F.:"), 0, 3);
        distribucion.add(spFuerza, 1, 3);
        distribucion.add(new Label("Complejos I-II % dentro de Tec-Tac:"), 0, 4);
        distribucion.add(spComplejos, 1, 4);

        TabPane tabs = new TabPane();

        Tab tabDatos = new Tab("1. Datos generales", datosGenerales);
        Tab tabMicro = new Tab("2. Microciclos y % carga", new ScrollPane(tablaMicrociclos));
        Tab tabDistribucion = new Tab("3. Distribución", distribucion);
        Tab tabResultados = new Tab("4. Resultado calculado", new ScrollPane(resultados));

        tabDatos.setClosable(false);
        tabMicro.setClosable(false);
        tabDistribucion.setClosable(false);
        tabResultados.setClosable(false);

        tabs.getTabs().addAll(tabDatos, tabMicro, tabDistribucion, tabResultados);
        tabs.setPrefWidth(980);
        tabs.setPrefHeight(560);

        dialog.getDialogPane().setContent(tabs);

        refrescarTodo.run();

        final boolean[] eliminar = {false};

        Button btnEliminarNode = (Button) dialog.getDialogPane().lookupButton(btnEliminar);
        btnEliminarNode.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            event.consume();

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Eliminar mesociclo");
            confirmacion.setHeaderText("¿Deseas eliminar este mesociclo?");
            confirmacion.setContentText("También podrás eliminar los microciclos hijos si lo confirmas después.");

            Optional<ButtonType> respuesta = confirmacion.showAndWait();

            if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
                eliminar[0] = true;
                dialog.setResult(null);
                dialog.close();
            }
        });

        dialog.setResultConverter(button -> {
            if (button == btnGuardar) {
                int semanaInicio = spSemanaInicio.getValue();
                int duracion = spDuracion.getValue();
                int semanaFin = semanaInicio + duracion - 1;

                if (semanaFin > semanasPlan.size()) {
                    mostrarAlerta("Mesociclo fuera de rango", "La duración excede el total de semanas del plan.");
                    return null;
                }

                if (existeCruceMesociclo(mesocicloOriginal, semanaInicio, semanaFin)) {
                    mostrarAlerta("Cruce de mesociclos", "El mesociclo se cruza con otro mesociclo.");
                    return null;
                }

                if (Math.abs((spPrepFisica.getValue() + spPrepTecTac.getValue()) - 100.0) > 0.01) {
                    mostrarAlerta("Distribución incorrecta", "Preparación física y técnico-táctica deben sumar 100 %.");
                    return null;
                }

                if (Math.abs((spAerobico.getValue() + spFuerza.getValue()) - 100.0) > 0.01) {
                    mostrarAlerta("Distribución incorrecta", "Aeróbico y fuerza deben sumar 100 %.");
                    return null;
                }

                MesocicloPlanificado nuevo = crearMesociclo(
                        cbTipo.getValue(),
                        txtNombre.getText(),
                        semanaInicio,
                        duracion,
                        toHex(cpColor.getValue())
                );

                nuevo.setObjetivo(txtObjetivo.getText());
                nuevo.setCapacidadesPrioritarias(txtCapacidades.getText());
                nuevo.setPorcentajePreparacionFisica(spPrepFisica.getValue());
                nuevo.setPorcentajePreparacionTecnicoTactica(spPrepTecTac.getValue());
                nuevo.setPorcentajeAerobico(spAerobico.getValue());
                nuevo.setPorcentajeFuerza(spFuerza.getValue());
                nuevo.setPorcentajeComplejos(spComplejos.getValue());
                nuevo.setConfiguracionMicrociclos(configuracionTemporal);

                return nuevo;
            }

            return null;
        });

        Optional<MesocicloPlanificado> resultado = dialog.showAndWait();

        if (eliminar[0]) {
            eliminarMesociclo(mesocicloOriginal);
            return;
        }

        resultado.ifPresent(nuevo -> {
            reemplazarMesociclo(mesocicloOriginal, nuevo);
            aplicarDistribucionMesocicloEnPlan(nuevo);
            mesociclosInicializados = true;
            microciclosInicializados = true;
            actualizarFechasMesociclos();
            actualizarFechasMicrociclos();
            limpiarSeleccionCelda();
            construirPlanGrafico();
        });
    }

    private List<MicrocicloMesocicloConfig> copiarConfiguracionMicrociclos(MesocicloPlanificado mesociclo) {
        List<MicrocicloMesocicloConfig> copia = new ArrayList<>();

        for (MicrocicloMesocicloConfig config : mesociclo.getConfiguracionMicrociclos()) {
            copia.add(new MicrocicloMesocicloConfig(
                    config.getTipoMicrociclo(),
                    config.getPorcentajeCarga(),
                    config.getUnidadesEntrenamientoSemana(),
                    config.getMinutosPorUnidad()
            ));
        }

        return copia;
    }

    private void ajustarConfiguracionTemporal(List<MicrocicloMesocicloConfig> configuracion, int duracion) {
        while (configuracion.size() < duracion) {
            configuracion.add(new MicrocicloMesocicloConfig(
                    TipoMicrociclo.CARGA,
                    70,
                    5,
                    120
            ));
        }

        while (configuracion.size() > duracion) {
            configuracion.remove(configuracion.size() - 1);
        }
    }

    private void construirTablaMicrociclosMesociclo(VBox contenedor,
                                                    List<MicrocicloMesocicloConfig> configuracion,
                                                    VBox resultados,
                                                    Spinner<Double> spPrepFisica,
                                                    Spinner<Double> spPrepTecTac,
                                                    Spinner<Double> spAerobico,
                                                    Spinner<Double> spFuerza,
                                                    Spinner<Double> spComplejos) {
        contenedor.getChildren().clear();

        GridPane tabla = new GridPane();
        tabla.setHgap(8);
        tabla.setVgap(8);
        tabla.setPadding(new Insets(15));

        tabla.add(new Label("Micro"), 0, 0);
        tabla.add(new Label("Tipo"), 1, 0);
        tabla.add(new Label("% microciclo"), 2, 0);
        tabla.add(new Label("U.E / semana"), 3, 0);
        tabla.add(new Label("Minutos U.E"), 4, 0);
        tabla.add(new Label("Minutos micro"), 5, 0);

        for (int i = 0; i < configuracion.size(); i++) {
            MicrocicloMesocicloConfig config = configuracion.get(i);

            Label lblMicro = new Label("Micro " + (i + 1));

            ComboBox<TipoMicrociclo> cbTipo = new ComboBox<>();
            cbTipo.getItems().setAll(TipoMicrociclo.values());
            cbTipo.setValue(config.getTipoMicrociclo());

            Spinner<Double> spPorcentaje = new Spinner<>(0.0, 100.0, config.getPorcentajeCarga(), 1.0);
            Spinner<Integer> spUE = new Spinner<>(1, 14, config.getUnidadesEntrenamientoSemana());
            Spinner<Integer> spMin = new Spinner<>(1, 300, config.getMinutosPorUnidad());

            spPorcentaje.setEditable(true);
            spUE.setEditable(true);
            spMin.setEditable(true);

            Label lblMinutos = new Label(String.valueOf(config.getMinutosTotalesMicrociclo()));

            cbTipo.valueProperty().addListener((obs, oldVal, newVal) -> {
                config.setTipoMicrociclo(newVal);
                actualizarResultadosMesociclo(resultados, configuracion,
                        spPrepFisica.getValue(), spPrepTecTac.getValue(), spAerobico.getValue(), spFuerza.getValue(), spComplejos.getValue());
            });

            spPorcentaje.valueProperty().addListener((obs, oldVal, newVal) -> {
                config.setPorcentajeCarga(newVal);
                actualizarResultadosMesociclo(resultados, configuracion,
                        spPrepFisica.getValue(), spPrepTecTac.getValue(), spAerobico.getValue(), spFuerza.getValue(), spComplejos.getValue());
            });

            spUE.valueProperty().addListener((obs, oldVal, newVal) -> {
                config.setUnidadesEntrenamientoSemana(newVal);
                lblMinutos.setText(String.valueOf(config.getMinutosTotalesMicrociclo()));
                actualizarResultadosMesociclo(resultados, configuracion,
                        spPrepFisica.getValue(), spPrepTecTac.getValue(), spAerobico.getValue(), spFuerza.getValue(), spComplejos.getValue());
            });

            spMin.valueProperty().addListener((obs, oldVal, newVal) -> {
                config.setMinutosPorUnidad(newVal);
                lblMinutos.setText(String.valueOf(config.getMinutosTotalesMicrociclo()));
                actualizarResultadosMesociclo(resultados, configuracion,
                        spPrepFisica.getValue(), spPrepTecTac.getValue(), spAerobico.getValue(), spFuerza.getValue(), spComplejos.getValue());
            });

            int row = i + 1;

            tabla.add(lblMicro, 0, row);
            tabla.add(cbTipo, 1, row);
            tabla.add(spPorcentaje, 2, row);
            tabla.add(spUE, 3, row);
            tabla.add(spMin, 4, row);
            tabla.add(lblMinutos, 5, row);
        }

        contenedor.getChildren().add(tabla);
    }

    private void actualizarResultadosMesociclo(VBox contenedor,
                                               List<MicrocicloMesocicloConfig> configuracion,
                                               double porcentajePrepFisica,
                                               double porcentajePrepTecTac,
                                               double porcentajeAerobico,
                                               double porcentajeFuerza,
                                               double porcentajeComplejos) {
        contenedor.getChildren().clear();

        double sumaPorcentajesMicro = configuracion.stream()
                .mapToDouble(MicrocicloMesocicloConfig::getPorcentajeCarga)
                .sum();

        int minutosTotalesBloque = configuracion.stream()
                .mapToInt(MicrocicloMesocicloConfig::getMinutosTotalesMicrociclo)
                .sum();

        double tiempoPrepFisica = minutosTotalesBloque * (porcentajePrepFisica / 100.0);
        double tiempoPrepTecTac = minutosTotalesBloque * (porcentajePrepTecTac / 100.0);

        double coeficienteFisico = sumaPorcentajesMicro == 0 ? 0 : tiempoPrepFisica / sumaPorcentajesMicro;
        double coeficienteTecTac = sumaPorcentajesMicro == 0 ? 0 : tiempoPrepTecTac / sumaPorcentajesMicro;

        Label resumen = new Label(
                "Minutos totales del bloque: " + minutosTotalesBloque
                        + "\nTiempo preparación física: " + Math.round(tiempoPrepFisica)
                        + "\nTiempo técnico-táctico: " + Math.round(tiempoPrepTecTac)
                        + "\nSuma % microciclos: " + Math.round(sumaPorcentajesMicro)
                        + "\nCoeficiente físico: " + redondear(coeficienteFisico)
                        + "\nCoeficiente técnico-táctico: " + redondear(coeficienteTecTac)
        );

        resumen.setStyle("-fx-font-weight: bold; -fx-text-fill: #08294a;");

        GridPane tabla = new GridPane();
        tabla.setHgap(8);
        tabla.setVgap(8);
        tabla.setPadding(new Insets(15));

        tabla.add(new Label("Concepto"), 0, 0);

        for (int i = 0; i < configuracion.size(); i++) {
            tabla.add(new Label("Micro " + (i + 1)), i + 1, 0);
        }

        agregarFilaResultado(tabla, 1, "% Microciclo", configuracion, c -> Math.round(c.getPorcentajeCarga()));
        agregarFilaResultado(tabla, 2, "Minutos micro", configuracion, MicrocicloMesocicloConfig::getMinutosTotalesMicrociclo);

        tabla.add(new Label("Prep. Física"), 0, 3);
        tabla.add(new Label("Aeróbica"), 0, 4);
        tabla.add(new Label("Fuerza"), 0, 5);
        tabla.add(new Label("Prep. Tec-Tac"), 0, 6);
        tabla.add(new Label("Complejos I-II"), 0, 7);

        for (int i = 0; i < configuracion.size(); i++) {
            MicrocicloMesocicloConfig config = configuracion.get(i);

            double tiempoFisicoMicro = coeficienteFisico * config.getPorcentajeCarga();
            double aerobico = tiempoFisicoMicro * (porcentajeAerobico / 100.0);
            double fuerza = tiempoFisicoMicro * (porcentajeFuerza / 100.0);

            double tiempoTecTacMicro = coeficienteTecTac * config.getPorcentajeCarga();
            double complejos = tiempoTecTacMicro * (porcentajeComplejos / 100.0);

            int col = i + 1;

            tabla.add(new Label(String.valueOf(Math.round(tiempoFisicoMicro))), col, 3);
            tabla.add(new Label(String.valueOf(Math.round(aerobico))), col, 4);
            tabla.add(new Label(String.valueOf(Math.round(fuerza))), col, 5);
            tabla.add(new Label(String.valueOf(Math.round(tiempoTecTacMicro))), col, 6);
            tabla.add(new Label(String.valueOf(Math.round(complejos))), col, 7);
        }

        contenedor.getChildren().addAll(resumen, tabla);
    }

    private void agregarFilaResultado(GridPane tabla,
                                      int row,
                                      String titulo,
                                      List<MicrocicloMesocicloConfig> configuracion,
                                      java.util.function.Function<MicrocicloMesocicloConfig, Number> extractor) {
        tabla.add(new Label(titulo), 0, row);

        for (int i = 0; i < configuracion.size(); i++) {
            tabla.add(new Label(String.valueOf(extractor.apply(configuracion.get(i)))), i + 1, row);
        }
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private void aplicarDistribucionMesocicloEnPlan(MesocicloPlanificado mesociclo) {
        microciclosPlanificados.removeIf(m ->
                m.getSemanaInicio() <= mesociclo.getSemanaFin()
                        && m.getSemanaFin() >= mesociclo.getSemanaInicio()
        );

        List<MicrocicloMesocicloConfig> configuracion = mesociclo.getConfiguracionMicrociclos();

        double sumaPorcentajesMicro = configuracion.stream()
                .mapToDouble(MicrocicloMesocicloConfig::getPorcentajeCarga)
                .sum();

        int minutosTotalesBloque = configuracion.stream()
                .mapToInt(MicrocicloMesocicloConfig::getMinutosTotalesMicrociclo)
                .sum();

        double tiempoPrepFisica = minutosTotalesBloque * (mesociclo.getPorcentajePreparacionFisica() / 100.0);
        double tiempoPrepTecTac = minutosTotalesBloque * (mesociclo.getPorcentajePreparacionTecnicoTactica() / 100.0);

        double coeficienteFisico = sumaPorcentajesMicro == 0 ? 0 : tiempoPrepFisica / sumaPorcentajesMicro;
        double coeficienteTecTac = sumaPorcentajesMicro == 0 ? 0 : tiempoPrepTecTac / sumaPorcentajesMicro;

        for (int i = 0; i < configuracion.size(); i++) {
            MicrocicloMesocicloConfig config = configuracion.get(i);
            int semana = mesociclo.getSemanaInicio() + i;

            if (semana > mesociclo.getSemanaFin() || semana > semanasPlan.size()) {
                break;
            }

            MicrocicloGraficoPlanificado micro = crearMicrocicloGrafico(
                    config.getTipoMicrociclo(),
                    abreviaturaMicrociclo(config.getTipoMicrociclo()),
                    semana,
                    1,
                    colorMicrociclo(config.getTipoMicrociclo())
            );

            microciclosPlanificados.add(micro);

            double tiempoFisicoMicro = coeficienteFisico * config.getPorcentajeCarga();
            double aerobico = tiempoFisicoMicro * (mesociclo.getPorcentajeAerobico() / 100.0);
            double fuerza = tiempoFisicoMicro * (mesociclo.getPorcentajeFuerza() / 100.0);

            double tiempoTecTacMicro = coeficienteTecTac * config.getPorcentajeCarga();
            double complejos = tiempoTecTacMicro * (mesociclo.getPorcentajeComplejos() / 100.0);

            asignarValorCalculado("VOLUMEN (%)", semana, String.valueOf(Math.round(config.getPorcentajeCarga())));
            asignarValorCalculado("SESIONES", semana, String.valueOf(config.getUnidadesEntrenamientoSemana()));
            asignarValorCalculado("MINUTOS PLAN.", semana, String.valueOf(config.getMinutosTotalesMicrociclo()));
            asignarValorCalculado("PREP. FÍSICA", semana, String.valueOf(Math.round(tiempoFisicoMicro)));
            asignarValorCalculado("AERÓBICA", semana, String.valueOf(Math.round(aerobico)));
            asignarValorCalculado("FUERZA", semana, String.valueOf(Math.round(fuerza)));
            asignarValorCalculado("PREP. TÉCNICO-TÁCTICA", semana, String.valueOf(Math.round(tiempoTecTacMicro)));
            asignarValorCalculado("COMPLEJOS I-II", semana, String.valueOf(Math.round(complejos)));
        }

        microciclosPlanificados.sort(Comparator.comparingInt(MicrocicloGraficoPlanificado::getSemanaInicio));
        microciclosInicializados = true;
        actualizarFechasMicrociclos();
    }

    private void asignarValorCalculado(String fila, int semana, String valor) {
        String key = fila + "-" + semana;

        CeldaPlanGrafico celda = celdasPlan.computeIfAbsent(
                key,
                k -> new CeldaPlanGrafico(fila, semana, valor, "#ffffff", true)
        );

        celda.setValor(valor);
    }


    private void abrirEditorNuevoMesocicloDesdeSemana(int semanaDisponible) {
        MesocicloPlanificado temporal = crearMesociclo(
                TipoMesociclo.PERSONALIZADO,
                "Nuevo mesociclo",
                semanaDisponible,
                1,
                colorMesociclo(TipoMesociclo.PERSONALIZADO)
        );

        abrirEditorMesocicloNuevo(temporal);
    }

    private void abrirEditorMesocicloNuevo(MesocicloPlanificado mesocicloTemporal) {
        Dialog<MesocicloPlanificado> dialog = new Dialog<>();
        dialog.setTitle("Agregar mesociclo");
        dialog.setHeaderText("Crear nuevo mesociclo");

        ButtonType btnGuardar = new ButtonType("Agregar mesociclo", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);

        ComboBox<TipoMesociclo> cbTipo = new ComboBox<>();
        cbTipo.getItems().setAll(TipoMesociclo.values());
        cbTipo.setValue(mesocicloTemporal.getTipoMesociclo());

        TextField txtNombre = new TextField(mesocicloTemporal.getNombre());

        TextArea txtObjetivo = new TextArea();
        txtObjetivo.setPrefRowCount(2);
        txtObjetivo.setWrapText(true);

        TextField txtCapacidades = new TextField();

        Spinner<Integer> spSemanaInicio = new Spinner<>(1, semanasPlan.size(), mesocicloTemporal.getSemanaInicio());
        Spinner<Integer> spDuracion = new Spinner<>(1, semanasPlan.size(), 1);

        spSemanaInicio.setEditable(true);
        spDuracion.setEditable(true);

        ColorPicker cpColor = new ColorPicker(Color.web(mesocicloTemporal.getColorHex()));

        HBox boxCiclaje = new HBox(8);
        boxCiclaje.setAlignment(Pos.CENTER_LEFT);

        List<TipoMicrociclo> ciclajeTemporal = new ArrayList<>();
        ciclajeTemporal.add(TipoMicrociclo.CARGA);

        Runnable reconstruirCiclaje = () -> construirControlesCiclaje(boxCiclaje, ciclajeTemporal, spDuracion.getValue());
        reconstruirCiclaje.run();

        spDuracion.valueProperty().addListener((obs, oldVal, newVal) -> reconstruirCiclaje.run());

        cbTipo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtNombre.setText(nombreMesociclo(newVal));
                cpColor.setValue(Color.web(colorMesociclo(newVal)));
            }
        });

        CheckBox chkGenerarMicrociclos = new CheckBox("Generar microciclos hijos según el ciclaje");
        chkGenerarMicrociclos.setSelected(true);

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.setPadding(new Insets(20));

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

        form.add(new Label("Objetivo:"), 0, 5);
        form.add(txtObjetivo, 1, 5);

        form.add(new Label("Capacidades:"), 0, 6);
        form.add(txtCapacidades, 1, 6);

        form.add(new Label("Ciclaje:"), 0, 7);
        form.add(boxCiclaje, 1, 7);

        form.add(chkGenerarMicrociclos, 1, 8);

        dialog.getDialogPane().setContent(form);

        dialog.setResultConverter(button -> {
            if (button == btnGuardar) {
                int semanaInicio = spSemanaInicio.getValue();
                int duracion = spDuracion.getValue();
                int semanaFin = semanaInicio + duracion - 1;

                if (semanaFin > semanasPlan.size()) {
                    mostrarAlerta("Mesociclo fuera de rango", "La duración excede el total de semanas del plan.");
                    return null;
                }

                if (existeCruceMesociclo(null, semanaInicio, semanaFin)) {
                    mostrarAlerta("Cruce de mesociclos", "El mesociclo se cruza con otro mesociclo.");
                    return null;
                }

                MesocicloPlanificado nuevo = crearMesociclo(
                        cbTipo.getValue(),
                        txtNombre.getText(),
                        semanaInicio,
                        duracion,
                        toHex(cpColor.getValue())
                );

                nuevo.setObjetivo(txtObjetivo.getText());
                nuevo.setCapacidadesPrioritarias(txtCapacidades.getText());
                List<MicrocicloMesocicloConfig> configuracionMicrociclos = new ArrayList<>();

                for (TipoMicrociclo tipoMicrociclo : ciclajeTemporal) {
                    configuracionMicrociclos.add(
                            new MicrocicloMesocicloConfig(
                                    tipoMicrociclo,
                                    porcentajeCargaPorTipoMicrociclo(tipoMicrociclo),
                                    5,
                                    120
                            )
                    );
                }

                nuevo.setConfiguracionMicrociclos(configuracionMicrociclos);

                if (chkGenerarMicrociclos.isSelected()) {
                    aplicarCiclajeMesociclo(nuevo);
                }

                return nuevo;
            }

            return null;
        });

        Optional<MesocicloPlanificado> resultado = dialog.showAndWait();

        resultado.ifPresent(nuevo -> {
            mesociclosPlanificados.add(nuevo);
            mesociclosPlanificados.sort(Comparator.comparingInt(MesocicloPlanificado::getSemanaInicio));
            mesociclosInicializados = true;
            actualizarFechasMesociclos();
            construirPlanGrafico();
        });
    }

    private double porcentajeCargaPorTipoMicrociclo(TipoMicrociclo tipo) {
        return switch (tipo) {
            case AJUSTE -> 60;
            case CARGA -> 70;
            case IMPACTO -> 75;
            case RECUPERACION -> 50;
            case PRECOMPETITIVO -> 60;
            case COMPETENCIA -> 40;
        };
    }

    private void construirControlesCiclaje(HBox contenedor,
                                           List<TipoMicrociclo> ciclajeActual,
                                           int duracion) {
        contenedor.getChildren().clear();

        while (ciclajeActual.size() < duracion) {
            ciclajeActual.add(TipoMicrociclo.CARGA);
        }

        while (ciclajeActual.size() > duracion) {
            ciclajeActual.remove(ciclajeActual.size() - 1);
        }

        for (int i = 0; i < duracion; i++) {
            VBox box = new VBox(4);
            box.setAlignment(Pos.CENTER);

            Label lblSemana = new Label("Sem. " + (i + 1));
            lblSemana.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");

            ComboBox<TipoMicrociclo> cbMicro = new ComboBox<>();
            cbMicro.getItems().setAll(TipoMicrociclo.values());
            cbMicro.setValue(ciclajeActual.get(i));
            cbMicro.setPrefWidth(120);

            int index = i;
            cbMicro.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    ciclajeActual.set(index, newVal);
                }
            });

            box.getChildren().addAll(lblSemana, cbMicro);
            contenedor.getChildren().add(box);
        }
    }

    /*private List<TipoMicrociclo> obtenerCiclajeDesdeControles(HBox contenedor) {
        List<TipoMicrociclo> ciclaje = new ArrayList<>();

        for (javafx.scene.Node node : contenedor.getChildren()) {
            if (node instanceof VBox vbox) {
                for (javafx.scene.Node hijo : vbox.getChildren()) {
                    if (hijo instanceof ComboBox<?> combo) {
                        Object valor = combo.getValue();

                        if (valor instanceof TipoMicrociclo tipoMicrociclo) {
                            ciclaje.add(tipoMicrociclo);
                        }
                    }
                }
            }
        }

        return ciclaje;
    }*/

    private void aplicarCiclajeMesociclo(MesocicloPlanificado mesociclo) {
        int semanaInicioMesociclo = mesociclo.getSemanaInicio();
        int semanaFinMesociclo = mesociclo.getSemanaFin();

        microciclosPlanificados.removeIf(microciclo ->
                microciclo.getSemanaInicio() <= semanaFinMesociclo
                        && microciclo.getSemanaFin() >= semanaInicioMesociclo
        );

        int semanaActual = semanaInicioMesociclo;

        for (MicrocicloMesocicloConfig config : mesociclo.getConfiguracionMicrociclos()) {
            if (semanaActual > semanaFinMesociclo) {
                break;
            }

            TipoMicrociclo tipoMicrociclo = config.getTipoMicrociclo();

            MicrocicloGraficoPlanificado nuevoMicrociclo = crearMicrocicloGrafico(
                    tipoMicrociclo,
                    abreviaturaMicrociclo(tipoMicrociclo),
                    semanaActual,
                    1,
                    colorMicrociclo(tipoMicrociclo)
            );

            microciclosPlanificados.add(nuevoMicrociclo);

            semanaActual++;
        }

        microciclosPlanificados.sort(
                Comparator.comparingInt(MicrocicloGraficoPlanificado::getSemanaInicio)
        );

        microciclosInicializados = true;

        actualizarFechasMicrociclos();

        limpiarSeleccionCelda();
    }


    private boolean existeCruceMesociclo(MesocicloPlanificado mesocicloIgnorado,
                                         int nuevaSemanaInicio,
                                         int nuevaSemanaFin) {
        for (MesocicloPlanificado mesociclo : mesociclosPlanificados) {
            if (mesociclo == mesocicloIgnorado) {
                continue;
            }

            boolean cruza = nuevaSemanaInicio <= mesociclo.getSemanaFin()
                    && nuevaSemanaFin >= mesociclo.getSemanaInicio();

            if (cruza) {
                return true;
            }
        }

        return false;
    }

    private void reemplazarMesociclo(MesocicloPlanificado original,
                                     MesocicloPlanificado nuevo) {
        int index = mesociclosPlanificados.indexOf(original);

        if (index >= 0) {
            mesociclosPlanificados.set(index, nuevo);
        }

        mesociclosPlanificados.sort(Comparator.comparingInt(MesocicloPlanificado::getSemanaInicio));
    }

    private void eliminarMesociclo(MesocicloPlanificado mesociclo) {
        Alert confirmacionMicrociclos = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacionMicrociclos.setTitle("Eliminar microciclos hijos");
        confirmacionMicrociclos.setHeaderText("¿También deseas eliminar los microciclos dentro de este mesociclo?");
        confirmacionMicrociclos.setContentText("Si eliges aceptar, se eliminarán los microciclos hijos del rango del mesociclo.");

        Optional<ButtonType> respuesta = confirmacionMicrociclos.showAndWait();

        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            microciclosPlanificados.removeIf(m ->
                    m.getSemanaInicio() >= mesociclo.getSemanaInicio()
                            && m.getSemanaFin() <= mesociclo.getSemanaFin()
            );
        }

        mesociclosPlanificados.remove(mesociclo);
        mesociclosInicializados = true;
        actualizarFechasMicrociclos();
        limpiarSeleccionCelda();
        construirPlanGrafico();
    }

    private void filaEtapasCalculadas(int row) {
        grid.add(celdaTitulo("ETAPA"), 0, row);

        if (etapasPlanificadas == null) {
            etapasPlanificadas = new ArrayList<>();
        }

        if (etapasPlanificadas.isEmpty()) {
            etapasPlanificadas = etapasService.generarEtapas(
                    tipoPeriodizacionActual,
                    periodosPlanificados,
                    semanasPlan
            );
        }

        int semana = 1;

        while (semana <= semanasPlan.size()) {
            EtapaPlanificada etapa = buscarEtapaPorSemanaInicio(semana);

            if (etapa != null) {
                Label celda = celdaEditable(
                        "ETAPA",
                        etapa.getSemanaInicio(),
                        nombreEtapa(etapa.getTipoEtapa()) + "\n" + etapa.getPorcentajeDentroPeriodo() + "%",
                        colorEtapa(etapa.getTipoEtapa()),
                        etapa.getDuracionSemanas() * 82,
                        38
                );

                celda.setStyle(celda.getStyle()
                        + "-fx-font-weight: bold;"
                        + "-fx-font-size: 11px;"
                        + "-fx-cursor: hand;"
                );

                celda.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2) {
                        abrirEditorEtapa(etapa);
                    }
                });

                Tooltip.install(celda, new Tooltip(
                        "Doble clic para editar o eliminar\n"
                                + "Tipo: " + etapa.getTipoEtapa()
                                + "\nSemana inicio: " + etapa.getSemanaInicio()
                                + "\nSemana fin: " + etapa.getSemanaFin()
                                + "\nDuración: " + etapa.getDuracionSemanas() + " semanas"
                                + "\nPorcentaje: " + etapa.getPorcentajeDentroPeriodo() + "%"
                ));

                grid.add(celda, etapa.getSemanaInicio(), row, etapa.getDuracionSemanas(), 1);

                semana = etapa.getSemanaFin() + 1;
            } else {
                int semanaDisponible = semana;

                Label celdaVacia = celdaEtapaDisponible(semanaDisponible);
                grid.add(celdaVacia, semanaDisponible, row);

                semana++;
            }
        }
    }

    private EtapaPlanificada buscarEtapaPorSemanaInicio(int semanaInicio) {
        for (EtapaPlanificada etapa : etapasPlanificadas) {
            if (etapa.getSemanaInicio() == semanaInicio) {
                return etapa;
            }
        }

        return null;
    }

    private Label celdaEtapaDisponible(int semana) {
        Label celda = new Label("+");
        celda.setAlignment(Pos.CENTER);
        celda.setMinSize(82, 38);
        celda.setPrefSize(82, 38);
        celda.setMaxSize(82, 38);

        String estiloNormal =
                "-fx-background-color: #f8fafc;"
                        + "-fx-border-color: #94a3b8;"
                        + "-fx-border-style: dashed;"
                        + "-fx-border-width: 1.2;"
                        + "-fx-text-fill: #08294a;"
                        + "-fx-font-size: 14px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-cursor: hand;";

        String estiloHover =
                "-fx-background-color: #dbeafe;"
                        + "-fx-border-color: #0875c9;"
                        + "-fx-border-style: dashed;"
                        + "-fx-border-width: 1.6;"
                        + "-fx-text-fill: #0875c9;"
                        + "-fx-font-size: 16px;"
                        + "-fx-font-weight: bold;"
                        + "-fx-cursor: hand;";

        celda.setStyle(estiloNormal);

        Tooltip.install(celda, new Tooltip("Doble clic para agregar una etapa en la semana " + semana));

        celda.setOnMouseEntered(e -> celda.setStyle(estiloHover));
        celda.setOnMouseExited(e -> celda.setStyle(estiloNormal));

        celda.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                abrirEditorNuevaEtapa(semana);
            }
        });

        return celda;
    }

    private void abrirEditorEtapa(EtapaPlanificada etapaOriginal) {
        Dialog<EtapaPlanificada> dialog = new Dialog<>();
        dialog.setTitle("Editar etapa");
        dialog.setHeaderText("Modificar o eliminar etapa");

        ButtonType btnGuardar = new ButtonType("Guardar cambios", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnEliminar = new ButtonType("Eliminar etapa", ButtonBar.ButtonData.LEFT);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnEliminar, btnCancelar);

        ComboBox<TipoEtapaPlanificacion> cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll(TipoEtapaPlanificacion.values());
        cbTipo.setValue(etapaOriginal.getTipoEtapa());

        Spinner<Integer> spSemanaInicio = new Spinner<>(
                1,
                semanasPlan.size(),
                etapaOriginal.getSemanaInicio()
        );
        spSemanaInicio.setEditable(true);

        Spinner<Integer> spSemanaFin = new Spinner<>(
                1,
                semanasPlan.size(),
                etapaOriginal.getSemanaFin()
        );
        spSemanaFin.setEditable(true);

        Spinner<Double> spPorcentaje = new Spinner<>(
                0.0,
                100.0,
                calcularPorcentajeEtapa(spSemanaInicio.getValue(), spSemanaFin.getValue()),
                0.1
        );
        spPorcentaje.setEditable(false);
        spPorcentaje.setDisable(true);

        spSemanaInicio.valueProperty().addListener((obs, old, val) -> {
            spPorcentaje.getValueFactory().setValue(
                    calcularPorcentajeEtapa(val, spSemanaFin.getValue())
            );
        });

        spSemanaFin.valueProperty().addListener((obs, old, val) -> {
            spPorcentaje.getValueFactory().setValue(
                    calcularPorcentajeEtapa(spSemanaInicio.getValue(), val)
            );
        });

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.setPadding(new Insets(20));

        form.add(new Label("Tipo de etapa:"), 0, 0);
        form.add(cbTipo, 1, 0);

        form.add(new Label("Semana inicio:"), 0, 1);
        form.add(spSemanaInicio, 1, 1);

        form.add(new Label("Semana fin:"), 0, 2);
        form.add(spSemanaFin, 1, 2);

        form.add(new Label("Porcentaje:"), 0, 3);
        form.add(spPorcentaje, 1, 3);

        dialog.getDialogPane().setContent(form);

        final boolean[] eliminar = {false};

        Button btnEliminarNode = (Button) dialog.getDialogPane().lookupButton(btnEliminar);
        btnEliminarNode.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            event.consume();

            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Eliminar etapa");
            confirmacion.setHeaderText("¿Deseas eliminar esta etapa?");
            confirmacion.setContentText("Las semanas ocupadas quedarán disponibles para agregar otra etapa.");

            Optional<ButtonType> respuesta = confirmacion.showAndWait();

            if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
                eliminar[0] = true;
                dialog.setResult(null);
                dialog.close();
            }
        });

        dialog.setResultConverter(button -> {
            if (button == btnGuardar) {
                int semanaInicio = spSemanaInicio.getValue();
                int semanaFin = spSemanaFin.getValue();

                if (semanaFin < semanaInicio) {
                    mostrarAlerta("Error", "La semana final no puede ser menor que la semana inicial.");
                    return null;
                }

                if (existeCruceEtapa(etapaOriginal, semanaInicio, semanaFin)) {
                    mostrarAlerta("Cruce de etapas", "El rango seleccionado se cruza con otra etapa.");
                    return null;
                }

                return crearEtapaManual(
                        cbTipo.getValue(),
                        semanaInicio,
                        semanaFin,
                        calcularPorcentajeEtapa(semanaInicio, semanaFin)
                );
            }

            return null;
        });

        Optional<EtapaPlanificada> resultado = dialog.showAndWait();

        if (eliminar[0]) {
            eliminarEtapa(etapaOriginal);
            return;
        }

        resultado.ifPresent(etapaNueva -> {
            reemplazarEtapa(etapaOriginal, etapaNueva);
            redibujarPlanGraficoCompleto();
        });
    }

    private void abrirEditorNuevaEtapa(int semanaDisponible) {
        Dialog<EtapaPlanificada> dialog = new Dialog<>();
        dialog.setTitle("Agregar etapa");
        dialog.setHeaderText("Agregar nueva etapa desde la semana " + semanaDisponible);

        ButtonType btnGuardar = new ButtonType("Agregar etapa", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, btnCancelar);

        ComboBox<TipoEtapaPlanificacion> cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll(TipoEtapaPlanificacion.values());
        cbTipo.setValue(TipoEtapaPlanificacion.PERSONALIZADA);

        Spinner<Integer> spSemanaInicio = new Spinner<>(
                1,
                semanasPlan.size(),
                semanaDisponible
        );
        spSemanaInicio.setEditable(true);

        Spinner<Integer> spSemanaFin = new Spinner<>(
                1,
                semanasPlan.size(),
                semanaDisponible
        );
        spSemanaFin.setEditable(true);

        Spinner<Double> spPorcentaje = new Spinner<>(
                0.0,
                100.0,
                calcularPorcentajeEtapa(spSemanaInicio.getValue(), spSemanaFin.getValue()),
                0.1
        );
        spPorcentaje.setEditable(false);
        spPorcentaje.setDisable(true);

        spSemanaInicio.valueProperty().addListener((obs, old, val) -> {
            spPorcentaje.getValueFactory().setValue(
                    calcularPorcentajeEtapa(val, spSemanaFin.getValue())
            );
        });

        spSemanaFin.valueProperty().addListener((obs, old, val) -> {
            spPorcentaje.getValueFactory().setValue(
                    calcularPorcentajeEtapa(spSemanaInicio.getValue(), val)
            );
        });

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.setPadding(new Insets(20));

        form.add(new Label("Tipo de etapa:"), 0, 0);
        form.add(cbTipo, 1, 0);

        form.add(new Label("Semana inicio:"), 0, 1);
        form.add(spSemanaInicio, 1, 1);

        form.add(new Label("Semana fin:"), 0, 2);
        form.add(spSemanaFin, 1, 2);

        form.add(new Label("Porcentaje:"), 0, 3);
        form.add(spPorcentaje, 1, 3);

        dialog.getDialogPane().setContent(form);

        dialog.setResultConverter(button -> {
            if (button == btnGuardar) {
                int semanaInicio = spSemanaInicio.getValue();
                int semanaFin = spSemanaFin.getValue();

                if (semanaFin < semanaInicio) {
                    mostrarAlerta("Error", "La semana final no puede ser menor que la semana inicial.");
                    return null;
                }

                if (existeCruceEtapa(null, semanaInicio, semanaFin)) {
                    mostrarAlerta("Cruce de etapas", "El rango seleccionado se cruza con otra etapa.");
                    return null;
                }

                return crearEtapaManual(
                        cbTipo.getValue(),
                        semanaInicio,
                        semanaFin,
                        calcularPorcentajeEtapa(semanaInicio, semanaFin)
                );
            }

            return null;
        });

        Optional<EtapaPlanificada> resultado = dialog.showAndWait();

        resultado.ifPresent(etapaNueva -> {
            etapasPlanificadas.add(etapaNueva);
            etapasPlanificadas.sort(Comparator.comparingInt(EtapaPlanificada::getSemanaInicio));
            redibujarPlanGraficoCompleto();
        });
    }

    private EtapaPlanificada crearEtapaManual(TipoEtapaPlanificacion tipo,
                                              int semanaInicio,
                                              int semanaFin,
                                              double porcentaje) {

        SemanaPlanificacion primera = semanasPlan.get(semanaInicio - 1);
        SemanaPlanificacion ultima = semanasPlan.get(semanaFin - 1);

        return new EtapaPlanificada(
                tipo,
                semanaInicio,
                semanaFin,
                primera.getFechaInicio(),
                ultima.getFechaFin(),
                porcentaje
        );
    }

    private double calcularPorcentajeEtapa(int semanaInicio, int semanaFin) {
        int duracion = semanaFin - semanaInicio + 1;
        int totalSemanas = semanasPlan.size();

        if (totalSemanas <= 0) {
            return 0;
        }

        double porcentaje = (duracion * 100.0) / totalSemanas;

        return Math.round(porcentaje * 10.0) / 10.0;
    }

    private boolean existeCruceEtapa(EtapaPlanificada etapaIgnorada, int nuevaSemanaInicio, int nuevaSemanaFin) {
        for (EtapaPlanificada etapa : etapasPlanificadas) {
            if (etapa == etapaIgnorada) {
                continue;
            }

            boolean cruza = nuevaSemanaInicio <= etapa.getSemanaFin()
                    && nuevaSemanaFin >= etapa.getSemanaInicio();

            if (cruza) {
                return true;
            }
        }

        return false;
    }

    private void reemplazarEtapa(EtapaPlanificada etapaOriginal, EtapaPlanificada etapaNueva) {
        int index = etapasPlanificadas.indexOf(etapaOriginal);

        if (index >= 0) {
            etapasPlanificadas.set(index, etapaNueva);
        }

        etapasPlanificadas.sort(Comparator.comparingInt(EtapaPlanificada::getSemanaInicio));
    }

    private void eliminarEtapa(EtapaPlanificada etapa) {
        etapasPlanificadas.remove(etapa);
        redibujarPlanGraficoCompleto();
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
        ConfiguracionPlanificacionView.main(args);
    }
}