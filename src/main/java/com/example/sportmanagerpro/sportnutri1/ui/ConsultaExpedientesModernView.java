package com.example.sportmanagerpro.sportnutri1.ui;

import com.example.sportmanagerpro.sportnutri1.model.Paciente;
import com.example.sportmanagerpro.sportnutri1.repository.PacienteRepository;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Vista de consulta y recuperación de expedientes guardados en SQLite.
 * Se integra con el PacienteRepository usado por RegistroPacientesModernView.
 */
public class ConsultaExpedientesModernView extends BorderPane {

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final PacienteRepository repository = new PacienteRepository();
    private final TextField txtBuscar = Ui.input("Buscar por nombre, teléfono, CURP o expediente...");
    private final ComboBox<String> filtroTipo = Ui.combo("Tipo", "Todos", "Consulta nutricional", "Nutrición deportiva", "Entrenamiento", "Consulta clínica ambulatoria");
    private final ComboBox<String> filtroSexo = Ui.combo("Sexo", "Todos", "Femenino", "Masculino", "Otro");
    private final ListView<Paciente> lista = new ListView<>();

    private final VBox detalle = new VBox(16);
    private Paciente seleccionado;

    public ConsultaExpedientesModernView() {
        setBackground(new Background(new BackgroundFill(Ui.BG, CornerRadii.EMPTY, Insets.EMPTY)));
        setLeft(crearSidebar());
        setTop(crearTopbar());
        setCenter(crearContenido());
        configurarEventos();
        cargarPacientes();
    }

    private void configurarEventos() {
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> cargarPacientes());
        filtroTipo.valueProperty().addListener((obs, oldValue, newValue) -> cargarPacientes());
        filtroSexo.valueProperty().addListener((obs, oldValue, newValue) -> cargarPacientes());
        lista.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, nuevo) -> mostrarPaciente(nuevo));
    }

    private VBox crearSidebar() {
        VBox side = new VBox(22);
        side.setPadding(new Insets(28, 20, 24, 20));
        side.setPrefWidth(240);
        side.setBackground(new Background(new BackgroundFill(Ui.NAVY, CornerRadii.EMPTY, Insets.EMPTY)));

        Label logo = Ui.label("NutriSport Pro", 24, true, Color.WHITE);
        Label sub = Ui.label("Nutrición y Entrenamiento", 12, false, Color.web("#D6E4F3"));
        VBox marca = new VBox(4, logo, sub);

        VBox menu = new VBox(8,
                menuItem("⌂", "Dashboard", false),
                menuItem("▣", "Pacientes / Alumnos", false),
                menuItem("▤", "Expedientes", true),
                menuItem("◷", "Agenda", false),
                menuItem("▦", "Evaluaciones", false),
                menuItem("⚒", "Entrenamientos", false),
                menuItem("♨", "Nutrición", false),
                menuItem("◎", "Competencias", false),
                menuItem("▧", "Reportes", false),
                menuItem("⚙", "Configuración", false)
        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox quick = new VBox(9,
                Ui.label("Accesos rápidos", 14, true, Color.WHITE),
                Ui.label("+ Nuevo paciente", 12, false, Color.web("#D8E6F5")),
                Ui.label("+ Nueva evaluación", 12, false, Color.web("#D8E6F5")),
                Ui.label("+ Nueva consulta", 12, false, Color.web("#D8E6F5")),
                Ui.label("+ Exportar reporte", 12, false, Color.web("#D8E6F5"))
        );
        quick.setPadding(new Insets(16));
        quick.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255, 0.08), new CornerRadii(14), Insets.EMPTY)));

        side.getChildren().addAll(marca, new Separator(), menu, spacer, quick);
        return side;
    }

    private HBox menuItem(String icono, String texto, boolean activo) {
        Label label = Ui.label(icono + "   " + texto, 14, true, activo ? Color.WHITE : Color.web("#D8E6F5"));
        HBox item = new HBox(label);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(13, 16, 13, 16));
        item.setBackground(new Background(new BackgroundFill(activo ? Ui.BLUE : Color.TRANSPARENT, new CornerRadii(12), Insets.EMPTY)));
        return item;
    }

    private HBox crearTopbar() {
        HBox top = new HBox(18);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(16, 28, 16, 28));
        top.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        top.setBorder(new Border(new BorderStroke(Color.web("#E2E8F0"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 1, 0))));

        VBox titles = new VBox(3,
                Ui.label("Expedientes / Consulta de pacientes", 22, true, Ui.TEXT),
                Ui.label("Busca, visualiza y administra la información guardada del paciente.", 13, false, Ui.MUTED)
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        TextField buscarGlobal = Ui.input("Buscar en toda la aplicación...");
        buscarGlobal.setPrefWidth(320);

        Label usuario = Ui.label("Raúl Hernández\nNutriólogo", 13, true, Ui.TEXT);
        top.getChildren().addAll(titles, spacer, buscarGlobal, Ui.label("🔔", 18, true, Ui.TEXT), usuario);
        return top;
    }

    private HBox crearContenido() {
        HBox root = new HBox(18);
        root.setPadding(new Insets(18));

        VBox panelLista = crearPanelLista();
        panelLista.setPrefWidth(390);

        ScrollPane scrollDetalle = new ScrollPane(detalle);
        scrollDetalle.setFitToWidth(true);
        scrollDetalle.setStyle("-fx-background-color:transparent;-fx-background:transparent;");
        HBox.setHgrow(scrollDetalle, Priority.ALWAYS);

        root.getChildren().addAll(panelLista, scrollDetalle);
        return root;
    }

    private VBox crearPanelLista() {
        VBox panel = Ui.card();
        panel.setSpacing(16);

        Label title = Ui.label("Buscar paciente", 16, true, Ui.TEXT);
        txtBuscar.setPrefHeight(44);

        HBox filtros = new HBox(10, filtroTipo, filtroSexo);
        filtroTipo.setPrefWidth(170);
        filtroSexo.setPrefWidth(140);

        HBox resumen = new HBox();
        resumen.setAlignment(Pos.CENTER_LEFT);
        Label total = Ui.label("Total de pacientes", 13, true, Ui.BLUE);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label orden = Ui.label("Más recientes", 12, false, Ui.MUTED);
        resumen.getChildren().addAll(total, spacer, orden);

        lista.setPrefHeight(620);
        lista.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Paciente paciente, boolean empty) {
                super.updateItem(paciente, empty);
                if (empty || paciente == null) {
                    setGraphic(null);
                } else {
                    setGraphic(crearItemPaciente(paciente));
                }
            }
        });

        Button cargarMas = Ui.button("Actualizar lista", "blue");
        cargarMas.setMaxWidth(Double.MAX_VALUE);
        cargarMas.setOnAction(e -> cargarPacientes());

        panel.getChildren().addAll(title, txtBuscar, Ui.label("Filtros rápidos", 14, true, Ui.TEXT), filtros, resumen, lista, cargarMas);
        return panel;
    }

    private Node crearItemPaciente(Paciente p) {
        Circle foto = new Circle(24, Color.web("#DCEAFE"));
        Label iniciales = Ui.label(iniciales(p), 13, true, Ui.BLUE);
        StackPane avatar = new StackPane(foto, iniciales);

        Label nombre = Ui.label(valor(p.nombreCompleto(), "Sin nombre"), 13, true, Ui.TEXT);
        Label meta = Ui.label(valor(p.getEdad(), "--") + " años  ·  " + valor(p.getExpediente(), "Sin expediente"), 12, false, Ui.MUTED);
        Label tel = Ui.label("☎ " + valor(p.getTelefono(), "Sin teléfono"), 12, false, Ui.MUTED);
        VBox datos = new VBox(4, nombre, meta, tel);

        Label tipo = Ui.label(valor(p.getTipoServicio(), "Paciente"), 11, true, Ui.BLUE);
        tipo.setPadding(new Insets(4, 8, 4, 8));
        tipo.setBackground(new Background(new BackgroundFill(Color.web("#EFF6FF"), new CornerRadii(8), Insets.EMPTY)));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox row = new HBox(12, avatar, datos, spacer, tipo);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10));
        row.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));
        row.setBorder(new Border(new BorderStroke(Color.web("#E2E8F0"), BorderStrokeStyle.SOLID, new CornerRadii(12), new BorderWidths(1))));
        return row;
    }

    private void mostrarPaciente(Paciente p) {
        seleccionado = p;
        detalle.getChildren().clear();

        if (p == null) {
            detalle.getChildren().add(crearEstadoVacio());
            return;
        }

        detalle.getChildren().addAll(
                crearEncabezadoPaciente(p),
                crearPestanas(p),
                crearCuerpoExpediente(p)
        );
    }

    private VBox crearEstadoVacio() {
        VBox box = Ui.card(
                Ui.label("Selecciona un expediente", 22, true, Ui.TEXT),
                Ui.label("Elige un paciente de la lista para visualizar su resumen, historia clínica, contacto, módulos y línea de tiempo.", 14, false, Ui.MUTED)
        );
        box.setAlignment(Pos.CENTER);
        box.setMinHeight(420);
        return box;
    }

    private VBox crearEncabezadoPaciente(Paciente p) {
        Circle foto = new Circle(40, Color.web("#DCEAFE"));
        Label iniciales = Ui.label(iniciales(p), 28, true, Ui.BLUE);
        StackPane avatar = new StackPane(foto, iniciales);

        Label nombre = Ui.label(valor(p.nombreCompleto(), "Sin nombre capturado"), 18, true, Ui.TEXT);
        Label tipo = badge(valor(p.getTipoServicio(), "Paciente"), Ui.BLUE, "#EFF6FF");
        VBox nombreBox = new VBox(8, nombre, tipo);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button editar = Ui.button("Editar", "blue");
        editar.setOnAction(e -> informar("Editar expediente", "Para editar, vuelve a la pantalla Registro y selecciona este paciente en la lista."));
        Button nuevaConsulta = Ui.button("Nueva consulta", "primary");
        nuevaConsulta.setOnAction(e -> informar("Nueva consulta", "Aquí se conectará el módulo de notas de evolución o consulta subsecuente."));
        Button mas = Ui.button("Exportar", "default");
        mas.setOnAction(e -> informar("Exportar", "Aquí se conectará el exportador PDF del expediente."));

        HBox top = new HBox(16, avatar, nombreBox, spacer, editar, nuevaConsulta, mas);
        top.setAlignment(Pos.CENTER_LEFT);

        GridPane info = Ui.grid(4);
        info.add(infoHeader("Edad", valor(p.getEdad(), "--") + " años\n" + fecha(p.getFechaNacimiento())), 0, 0);
        info.add(infoHeader("Sexo", valor(p.getSexo(), "--")), 1, 0);
        info.add(infoHeader("Teléfono", valor(p.getTelefono(), "--")), 2, 0);
        info.add(infoHeader("Correo", valor(p.getCorreo(), "--")), 3, 0);
        info.add(infoHeader("No. expediente", valor(p.getExpediente(), "--")), 0, 1);
        info.add(infoHeader("Fecha de alta", fecha(p.getFechaApertura())), 1, 1);
        info.add(infoHeader("Estado expediente", "Activo"), 2, 1);
        info.add(infoHeader("Profesional", valor(p.getResponsable(), "No asignado")), 3, 1);

        return Ui.card(top, info);
    }

    private VBox infoHeader(String titulo, String valor) {
        return new VBox(4, Ui.label(titulo, 12, false, Ui.MUTED), Ui.label(valor, 14, true, Ui.TEXT));
    }

    private HBox crearPestanas(Paciente p) {
        HBox tabs = new HBox(8);
        tabs.setPadding(new Insets(12));
        tabs.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(18), Insets.EMPTY)));
        tabs.setBorder(new Border(new BorderStroke(Ui.BORDER, BorderStrokeStyle.SOLID, new CornerRadii(18), new BorderWidths(1))));
        tabs.getChildren().addAll(
                tab("Resumen", true), tab("Historia clínica", false), tab("Antropometría", false), tab("Bioquímicos", false),
                tab("Nutrición", false), tab("Entrenamiento", false), tab("Lesiones", false), tab("Documentos", false), tab("Seguimiento", false)
        );
        return tabs;
    }

    private Label tab(String texto, boolean activo) {
        Label label = Ui.label(texto, 12, true, activo ? Ui.BLUE : Ui.MUTED);
        label.setPadding(new Insets(9, 12, 9, 12));
        label.setBackground(new Background(new BackgroundFill(activo ? Color.web("#EFF6FF") : Color.TRANSPARENT, new CornerRadii(10), Insets.EMPTY)));
        return label;
    }

    private VBox crearCuerpoExpediente(Paciente p) {

        VBox body = new VBox(16);

        body.getChildren().addAll(
                crearIndicadores(p),
                crearResumenGeneral(p),
                crearContacto(p),
                crearModulos(p)
        );

        return body;
    }

    private GridPane crearIndicadores(Paciente p) {

        GridPane grid = Ui.grid(4);

        grid.add(kpi("Edad",
                valor(p.getEdad(), "--") + " años"), 0, 0);

        grid.add(kpi("Sexo",
                valor(p.getSexo(), "--")), 1, 0);

        grid.add(kpi("IMC",
                "Pendiente"), 2, 0);

        grid.add(kpi("Consultas",
                "1"), 3, 0);

        return grid;
    }

    private VBox kpi(String titulo, String valor) {

        VBox box = new VBox(8);

        box.getChildren().addAll(
                Ui.label(titulo, 12, false, Ui.MUTED),
                Ui.label(valor, 18, true, Ui.TEXT)
        );

        box.setPadding(new Insets(16));

        box.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.WHITE,
                                new CornerRadii(12),
                                Insets.EMPTY)));

        box.setBorder(
                new Border(
                        new BorderStroke(
                                Color.web("#E2E8F0"),
                                BorderStrokeStyle.SOLID,
                                new CornerRadii(12),
                                new BorderWidths(1))));

        return box;
    }

    private VBox crearResumenGeneral(Paciente p) {
        GridPane grid = Ui.grid(4);
        grid.add(resumenDato("Tipo de servicio", valor(p.getTipoServicio(), "No especificado")), 0, 0);
        grid.add(resumenDato("Ocupación", valor(p.getOcupacion(), "--")), 1, 0);
        grid.add(resumenDato("Escolaridad", valor(p.getEscolaridad(), "--")), 2, 0);
        grid.add(resumenDato("Estado civil", valor(p.getEstadoCivil(), "--")), 3, 0);
        grid.add(resumenDato("Diagnóstico", valor(p.getDiagnosticos(), "Pendiente")), 0, 1);
        grid.add(resumenDato("Pronóstico", valor(p.getPronostico(), "Pendiente")), 1, 1);
        grid.add(resumenDato("Estudios", esVacio(p.getEstudiosPrevios()) ? "Pendientes" : "Registrados"), 2, 1);
        grid.add(resumenDato("Último IMC", "No registrado"), 3, 1);
        return Ui.card(Ui.label("Resumen general", 16, true, Ui.TEXT), grid);
    }

    private VBox resumenDato(String titulo, String valor) {
        VBox box = new VBox(8, Ui.label(titulo, 12, false, Ui.MUTED), Ui.label(valor, 13, true, Ui.TEXT));
        box.setPadding(new Insets(16));
        box.setMinHeight(94);
        box.setBackground(new Background(new BackgroundFill(Color.web("#F8FAFC"), new CornerRadii(12), Insets.EMPTY)));
        box.setBorder(new Border(new BorderStroke(Color.web("#E2E8F0"), BorderStrokeStyle.SOLID, new CornerRadii(12), new BorderWidths(1))));
        return box;
    }

    private VBox crearContacto(Paciente p) {
        GridPane grid = Ui.grid(3);
        grid.add(resumenDato("Dirección", valor(p.getDomicilio(), "Sin dirección")), 0, 0);
        grid.add(resumenDato("Contacto de emergencia", "Pendiente"), 1, 0);
        grid.add(resumenDato("CURP", valor(p.getCurp(), "No capturada")), 2, 0);
        return Ui.card(Ui.label("Información de contacto", 16, true, Ui.TEXT), grid);
    }

    private VBox crearModulos(Paciente p) {
        FlowPane chips = new FlowPane(8, 8);
        String[] modulos = {"Historia clínica", "Antropometría", "Bioquímicos", "Evaluación nutricional", "Dietoterapia", "Documentos", "Seguimiento"};
        for (String modulo : modulos) {
            chips.getChildren().add(badge("✓ " + modulo, Ui.GREEN, "#ECFDF5"));
        }
        return Ui.card(Ui.label("Módulos activos en el expediente", 16, true, Ui.TEXT), chips);
    }

    private VBox crearLineaTiempo(Paciente p) {
        VBox timeline = new VBox(12);
        timeline.getChildren().add(Ui.label("Línea de tiempo", 16, true, Ui.TEXT));
        timeline.getChildren().add(evento(fecha(p.getFechaApertura()), "Expediente creado", valor(p.getResponsable(), "Sistema")));
        if (!esVacio(p.getAntecedentesHeredoFamiliares()) || !esVacio(p.getPadecimientoActual())) {
            timeline.getChildren().add(evento(fecha(LocalDate.now()), "Historia clínica actualizada", valor(p.getResponsable(), "Sistema")));
        }
        if (!esVacio(p.getEstudiosPrevios())) {
            timeline.getChildren().add(evento(fecha(LocalDate.now()), "Estudios registrados", valor(p.getResponsable(), "Sistema")));
        }
        if (!esVacio(p.getIndicacionTerapeutica())) {
            timeline.getChildren().add(evento(fecha(LocalDate.now()), "Indicación terapéutica registrada", valor(p.getResponsable(), "Sistema")));
        }
        Button verTodo = Ui.button("Ver toda la línea de tiempo", "default");
        verTodo.setMaxWidth(Double.MAX_VALUE);
        timeline.getChildren().add(verTodo);
        return Ui.card(timeline);
    }

    private HBox evento(String fecha, String titulo, String responsable) {
        Circle dot = new Circle(7, Ui.BLUE);
        VBox texto = new VBox(3, Ui.label(fecha, 12, false, Ui.MUTED), Ui.label(titulo, 13, true, Ui.TEXT), Ui.label(responsable, 11, false, Ui.MUTED));
        HBox row = new HBox(10, dot, texto);
        row.setAlignment(Pos.TOP_LEFT);
        return row;
    }

    private VBox crearProximaCita() {
        Button nueva = Ui.button("+ Nueva cita", "blue");
        nueva.setMaxWidth(Double.MAX_VALUE);
        return Ui.card(
                Ui.label("Próxima cita", 16, true, Ui.TEXT),
                Ui.label("Sin cita programada", 14, true, Ui.TEXT),
                Ui.label("Programa una nueva cita para este paciente.", 12, false, Ui.MUTED),
                nueva
        );
    }

    private Label badge(String texto, Color color, String fondo) {
        Label label = Ui.label(texto, 11, true, color);
        label.setPadding(new Insets(5, 9, 5, 9));
        label.setBackground(new Background(new BackgroundFill(Color.web(fondo), new CornerRadii(9), Insets.EMPTY)));
        return label;
    }

    private void cargarPacientes() {
        List<Paciente> pacientes = repository.buscar(txtBuscar.getText()).stream()
                .filter(this::cumpleFiltros)
                .toList();
        lista.setItems(FXCollections.observableArrayList(pacientes));
        if (!pacientes.isEmpty()) {
            lista.getSelectionModel().select(0);
        } else {
            mostrarPaciente(null);
        }
    }

    private boolean cumpleFiltros(Paciente p) {
        String tipo = filtroTipo.getValue();
        if (tipo != null && !tipo.equals("Todos") && !tipo.equals(p.getTipoServicio())) {
            return false;
        }
        String sexo = filtroSexo.getValue();
        return sexo == null || sexo.equals("Todos") || sexo.equals(p.getSexo());
    }

    private String iniciales(Paciente p) {
        String n = valor(p.getNombres(), "");
        String a = valor(p.getApellidoPaterno(), "");
        String i1 = n.isBlank() ? "" : n.substring(0, 1);
        String i2 = a.isBlank() ? "" : a.substring(0, 1);
        return (i1 + i2).isBlank() ? "?" : (i1 + i2).toUpperCase();
    }

    private String valor(Object valor, String fallback) {
        if (valor == null) {
            return fallback;
        }
        String texto = String.valueOf(valor);
        return texto.isBlank() ? fallback : texto;
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.isBlank();
    }

    private String fecha(LocalDate fecha) {
        return fecha == null ? "--" : fecha.format(FECHA);
    }

    private String fecha(String fechaIso) {
        if (fechaIso == null || fechaIso.isBlank()) return "--";
        try {
            return LocalDate.parse(fechaIso).format(FECHA);
        } catch (Exception ex) {
            return fechaIso;
        }
    }

    private void informar(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje, ButtonType.OK);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.showAndWait();
    }
}
