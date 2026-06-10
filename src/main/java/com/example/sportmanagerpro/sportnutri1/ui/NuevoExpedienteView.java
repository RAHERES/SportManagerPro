package com.example.sportmanagerpro.sportnutri1.ui;

import com.example.sportmanagerpro.sportnutri1.model.*;
import com.example.sportmanagerpro.sportnutri1.service.ExpedienteService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.LinkedHashMap;
import java.util.Map;

public class NuevoExpedienteView extends BorderPane {

    private final ExpedienteService expedienteService = new ExpedienteService();

    private int pasoActual = 1;

    private final Label tituloHeader = new Label("Nuevo expediente / Paso 1 de 5");
    private final Label subtituloHeader = new Label("Datos generales del cliente / paciente / alumno");

    private final HBox pasosBox = new HBox(52);
    private final VBox contenidoCentral = new VBox(14);

    private final Button btnAnterior = new Button("Anterior");
    private final Button btnSiguiente = new Button("Siguiente →");
    private final Button btnGuardar = new Button("Guardar expediente");

    private TipoPersona tipoPersona = TipoPersona.PACIENTE_NUTRICION;
    private EstadoFisiologico estadoFisiologico = EstadoFisiologico.ADOLESCENTE;
    private NivelActividad nivelActividad = NivelActividad.AMATEUR;

    private final TextField nombres = UiFactory.campoTexto("Ana Sofía");
    private final TextField apellidoPaterno = UiFactory.campoTexto("García");
    private final TextField apellidoMaterno = UiFactory.campoTexto("Hernández");
    private final TextField fechaNacimiento = UiFactory.campoTexto("15/03/2007");
    private final TextField edad = UiFactory.campoTexto("17 años");
    private final ComboBox<String> sexo = UiFactory.combo("Femenino", "Femenino", "Masculino", "Otro");
    private final ComboBox<String> estadoCivil = UiFactory.combo("Soltera", "Soltera", "Soltero", "Casada", "Casado", "Otro");
    private final TextField curp = UiFactory.campoTexto("GAHA070315MDFRRNA9");
    private final TextField telefono = UiFactory.campoTexto("55 1234 5678");
    private final TextField correo = UiFactory.campoTexto("ana.garcia@email.com");
    private final TextField direccion = UiFactory.campoTexto("Av. Universidad 123, Col. Centro, Querétaro, Qro.");
    private final TextField escolaridad = UiFactory.campoTexto("Preparatoria");
    private final TextField gradoSemestre = UiFactory.campoTexto("5° Semestre");
    private final TextField ocupacion = UiFactory.campoTexto("Estudiante");
    private final TextField contactoEmergencia = UiFactory.campoTexto("María Hernández");
    private final TextField telefonoEmergencia = UiFactory.campoTexto("55 8765 4321");
    private final TextField parentesco = UiFactory.campoTexto("Madre");

    private final ComboBox<String> deportePrincipal = UiFactory.combo("Fútbol", "Fútbol", "Atletismo", "Basquetbol", "Natación", "Otro");
    private final ComboBox<String> posicion = UiFactory.combo("Delantera", "Portera", "Defensa", "Mediocampista", "Delantera");
    private final TextField anosPracticando = UiFactory.campoTexto("6 años");
    private final TextField entrenamientosSemana = UiFactory.campoTexto("4 - 5 días");

    private final TextField peso = UiFactory.campoTexto("");
    private final TextField talla = UiFactory.campoTexto("");
    private final TextField imc = UiFactory.campoTexto("");
    private final TextField cintura = UiFactory.campoTexto("");
    private final TextField cadera = UiFactory.campoTexto("");

    private final TextArea motivoConsulta = UiFactory.areaTexto("");
    private final TextArea antecedentes = UiFactory.areaTexto("");
    private final TextArea patologias = UiFactory.areaTexto("");
    private final TextArea medicamentos = UiFactory.areaTexto("");
    private final TextArea alergias = UiFactory.areaTexto("");

    private final TextArea alimentosHabituales = UiFactory.areaTexto("");
    private final TextArea alimentosRechazados = UiFactory.areaTexto("");
    private final TextField consumoAgua = UiFactory.campoTexto("");
    private final TextArea objetivoNutricional = UiFactory.areaTexto("");

    private final TextArea objetivoEntrenamiento = UiFactory.areaTexto("");
    private final TextField diasEntrenamiento = UiFactory.campoTexto("");
    private final TextArea restricciones = UiFactory.areaTexto("");
    private final TextArea lesionesPrevias = UiFactory.areaTexto("");

    private final Map<String, CheckBox> modulos = new LinkedHashMap<>();

    public NuevoExpedienteView() {
        getStyleClass().add("app-root");

        setLeft(crearMenuLateral());
        setTop(crearEncabezado());

        contenidoCentral.setPadding(new Insets(22));
        actualizarContenido();

        ScrollPane scrollPane = new ScrollPane(contenidoCentral);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll");

        setCenter(scrollPane);
    }

    private VBox crearMenuLateral() {
        VBox menu = new VBox(18);
        menu.setPadding(new Insets(28, 18, 24, 18));
        menu.setPrefWidth(240);
        menu.getStyleClass().add("sidebar");

        Label logo = new Label("◉ NutriSport Pro");
        logo.getStyleClass().add("logo");

        Label subtitulo = new Label("Nutrición y Entrenamiento");
        subtitulo.getStyleClass().add("menu-subtitle");

        VBox opciones = new VBox(8);
        opciones.getChildren().addAll(
                itemMenu("⌂", "Dashboard", false),
                itemMenu("▣", "Pacientes / Alumnos", true),
                itemMenu("▤", "Expedientes", false),
                itemMenu("◷", "Agenda", false),
                itemMenu("▦", "Evaluaciones", false),
                itemMenu("⚒", "Entrenamientos", false),
                itemMenu("♨", "Nutrición", false),
                itemMenu("◎", "Competencias", false),
                itemMenu("▧", "Reportes", false),
                itemMenu("⚙", "Configuración", false)
        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox accesos = new VBox(10);
        accesos.getStyleClass().add("quick-box");
        Label titulo = new Label("Accesos rápidos");
        titulo.getStyleClass().add("quick-title");
        accesos.getChildren().addAll(
                titulo,
                quick("+ Nuevo paciente"),
                quick("+ Nueva evaluación"),
                quick("+ Nuevo plan alimentario"),
                quick("+ Nuevo entrenamiento")
        );

        menu.getChildren().addAll(logo, subtitulo, new Separator(), opciones, spacer, accesos);
        return menu;
    }

    private Label itemMenu(String icono, String texto, boolean activo) {
        Label label = new Label(icono + "   " + texto);
        label.getStyleClass().add(activo ? "menu-item-active" : "menu-item");
        return label;
    }

    private Label quick(String texto) {
        Label label = new Label(texto);
        label.getStyleClass().add("quick-item");
        return label;
    }

    private HBox crearEncabezado() {
        HBox header = new HBox(20);
        header.setPadding(new Insets(18, 28, 18, 28));
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("header");

        Label volver = new Label("←");
        volver.setFont(Font.font("Segoe UI", FontWeight.BOLD, 30));
        volver.getStyleClass().add("back-arrow");

        VBox titulos = new VBox(3);
        tituloHeader.getStyleClass().add("header-title");
        subtituloHeader.getStyleClass().add("header-subtitle");
        titulos.getChildren().addAll(tituloHeader, subtituloHeader);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button cancelar = new Button("Cancelar");
        cancelar.getStyleClass().add("btn-secondary");
        cancelar.setOnAction(e -> limpiarFormulario());

        btnAnterior.getStyleClass().add("btn-secondary");
        btnAnterior.setOnAction(e -> irAnterior());

        btnSiguiente.getStyleClass().add("btn-primary");
        btnSiguiente.setOnAction(e -> irSiguiente());

        btnGuardar.getStyleClass().add("btn-primary");
        btnGuardar.setOnAction(e -> guardarExpediente());

        header.getChildren().addAll(volver, titulos, spacer, cancelar, btnAnterior, btnSiguiente, btnGuardar);
        return header;
    }

    private void actualizarContenido() {
        contenidoCentral.getChildren().clear();

        actualizarBotones();
        actualizarEncabezado();

        contenidoCentral.getChildren().add(crearPasos());

        switch (pasoActual) {
            case 1 -> contenidoCentral.getChildren().add(crearPasoDatosGenerales());
            case 2 -> contenidoCentral.getChildren().add(crearPasoAntropometria());
            case 3 -> contenidoCentral.getChildren().add(crearPasoClinico());
            case 4 -> contenidoCentral.getChildren().add(crearPasoNutricion());
            case 5 -> contenidoCentral.getChildren().add(crearPasoActividadFisica());
            default -> contenidoCentral.getChildren().add(crearPasoDatosGenerales());
        }
    }

    private HBox crearPasos() {
        pasosBox.getChildren().clear();
        pasosBox.setAlignment(Pos.CENTER_LEFT);
        pasosBox.setPadding(new Insets(6, 12, 16, 12));

        pasosBox.getChildren().addAll(
                paso(1, "Datos generales"),
                paso(2, "Antropometría"),
                paso(3, "Datos clínicos"),
                paso(4, "Nutrición"),
                paso(5, "Actividad física")
        );

        return pasosBox;
    }

    private HBox paso(int numero, String texto) {
        Label circulo = new Label(String.valueOf(numero));
        circulo.setAlignment(Pos.CENTER);
        circulo.setPrefSize(30, 30);
        circulo.getStyleClass().add(numero == pasoActual ? "step-active" : "step");

        Label label = new Label(texto);
        label.getStyleClass().add(numero == pasoActual ? "step-text-active" : "step-text");

        HBox box = new HBox(10, circulo, label);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setOnMouseClicked(e -> {
            if (numero > pasoActual && !validarPasoActual()) {
                return;
            }
            pasoActual = numero;
            actualizarContenido();
        });

        return box;
    }

    private Pane crearPasoDatosGenerales() {
        GridPane layout = new GridPane();
        layout.setHgap(14);
        layout.setVgap(14);
        layout.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints columnaPrincipal = new ColumnConstraints();
        columnaPrincipal.setPercentWidth(68);
        columnaPrincipal.setHgrow(Priority.ALWAYS);

        ColumnConstraints columnaDerecha = new ColumnConstraints();
        columnaDerecha.setPercentWidth(32);
        columnaDerecha.setHgrow(Priority.ALWAYS);

        layout.getColumnConstraints().addAll(columnaPrincipal, columnaDerecha);

        VBox izquierda = new VBox(14);
        izquierda.setMaxWidth(Double.MAX_VALUE);
        izquierda.getChildren().addAll(
                crearBloqueTipoPersonaCompleto(),
                crearFormularioPersonal(),
                crearModulos()
        );

        VBox derecha = new VBox(14);
        derecha.setMaxWidth(Double.MAX_VALUE);
        derecha.getChildren().addAll(
                crearInformacionAdministrativa(),
                crearEstadoFisiologico(),
                crearNivelActividad(),
                crearResumenExpediente(),
                crearChecklistCaptura(),
                crearNotasGenerales()
        );

        layout.add(izquierda, 0, 0);
        layout.add(derecha, 1, 0);

        GridPane.setHgrow(izquierda, Priority.ALWAYS);
        GridPane.setHgrow(derecha, Priority.ALWAYS);

        return layout;
    }

    private VBox crearBloqueTipoPersonaCompleto() {
        VBox tarjeta = UiFactory.card();
        tarjeta.setMaxWidth(Double.MAX_VALUE);

        Label titulo = UiFactory.tituloSeccion("Tipo de persona");
        Label ayuda = UiFactory.ayuda("Selecciona el perfil principal del expediente");

        ToggleGroup grupo = new ToggleGroup();

        FlowPane opciones = new FlowPane(14, 14);
        opciones.getChildren().addAll(
                tipoPersonaButton("👤", "Paciente de\nnutrición", TipoPersona.PACIENTE_NUTRICION, grupo),
                tipoPersonaButton("🏃", "Entrenamiento", TipoPersona.DEPORTISTA, grupo),
                tipoPersonaButton("👥", "Nutrición y\nentrenamiento", TipoPersona.PACIENTE_Y_DEPORTISTA, grupo),
                tipoPersonaButton("🎓", "Alumno escolar", TipoPersona.ALUMNO_ESCOLAR, grupo),
                tipoPersonaButton("○", "Otro", TipoPersona.OTRO, grupo)
        );

        tarjeta.getChildren().addAll(titulo, ayuda, opciones);
        return tarjeta;
    }

    private VBox crearInformacionAdministrativa() {
        VBox box = UiFactory.card();

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);

        TextField expediente = UiFactory.campoTexto("EXP-2026-0001");
        TextField fechaAlta = UiFactory.campoTexto("08/06/2026");
        TextField profesional = UiFactory.campoTexto("Raúl Hernández");
        TextField referido = UiFactory.campoTexto("");
        ComboBox<String> consentimiento = UiFactory.combo("Pendiente", "Pendiente", "Firmado", "No aplica");

        grid.add(UiFactory.campo("No. expediente", expediente), 0, 0);
        grid.add(UiFactory.campo("Fecha de alta", fechaAlta), 1, 0);
        grid.add(UiFactory.campo("Profesional responsable", profesional), 0, 1, 2, 1);
        grid.add(UiFactory.campo("Referido por", referido), 0, 2);
        grid.add(campoCombo("Consentimiento", consentimiento), 1, 2);

        box.getChildren().addAll(
                UiFactory.tituloSeccion("Información administrativa"),
                UiFactory.ayuda("Datos generales del expediente antes de iniciar la valoración."),
                grid
        );

        return box;
    }

    private VBox crearNotasGenerales() {
        VBox box = UiFactory.card();

        TextArea notas = UiFactory.areaTexto("");
        notas.setPromptText("Observaciones iniciales, situación especial, motivo administrativo o notas rápidas.");

        box.getChildren().addAll(
                UiFactory.tituloSeccion("Notas generales"),
                UiFactory.ayuda("Notas internas antes de continuar con historia clínica, nutrición o entrenamiento."),
                notas
        );

        return box;
    }

    private HBox crearBloqueTipoPersona() {
        HBox bloque = new HBox(18);

        VBox tarjeta = UiFactory.card();
        tarjeta.setPrefWidth(870);

        Label titulo = UiFactory.tituloSeccion("Tipo de persona");
        Label ayuda = UiFactory.ayuda("Selecciona el perfil principal del expediente");

        ToggleGroup grupo = new ToggleGroup();

        HBox opciones = new HBox(18);
        opciones.getChildren().addAll(
                tipoPersonaButton("👤", "Paciente de\nnutrición", TipoPersona.PACIENTE_NUTRICION, grupo),
                tipoPersonaButton("🏃", "Deportista", TipoPersona.DEPORTISTA, grupo),
                tipoPersonaButton("👥", "Paciente y\ndeportista", TipoPersona.PACIENTE_Y_DEPORTISTA, grupo),
                tipoPersonaButton("🎓", "Alumno escolar", TipoPersona.ALUMNO_ESCOLAR, grupo),
                tipoPersonaButton("○", "Otro", TipoPersona.OTRO, grupo)
        );

        tarjeta.getChildren().addAll(titulo, ayuda, opciones);

        VBox info = UiFactory.card();
        info.setPrefWidth(370);
        info.getStyleClass().add("info-card");

        Label infoTitulo = UiFactory.tituloSeccion("Información");
        Label texto = UiFactory.ayuda("Puedes activar o desactivar módulos más adelante desde la configuración del expediente.");

        info.getChildren().addAll(infoTitulo, texto);

        bloque.getChildren().addAll(tarjeta, info);
        return bloque;
    }

    private ToggleButton tipoPersonaButton(String icono, String texto, TipoPersona tipo, ToggleGroup grupo) {
        ToggleButton button = new ToggleButton(icono + "\n\n" + texto);
        button.setToggleGroup(grupo);
        button.setSelected(tipoPersona == tipo);
        button.setPrefSize(150, 105);
        button.getStyleClass().add("type-card");
        button.setOnAction(e -> tipoPersona = tipo);
        return button;
    }

    private VBox crearFormularioPersonal() {
        VBox box = UiFactory.card();
        box.setMaxWidth(Double.MAX_VALUE);

        Label titulo = UiFactory.tituloSeccion("Información personal");

        VBox fotoBox = new VBox(10);
        fotoBox.setAlignment(Pos.TOP_CENTER);

        StackPane foto = new StackPane();
        foto.setPrefSize(135, 185);
        foto.getStyleClass().add("photo-box");

        Circle avatar = new Circle(45);
        avatar.setFill(Color.web("#DCEAFE"));

        Label iniciales = new Label("AS");
        iniciales.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        iniciales.setTextFill(Color.web("#0F62FE"));

        foto.getChildren().addAll(avatar, iniciales);

        Button cambiarFoto = new Button("Cambiar foto");
        cambiarFoto.getStyleClass().add("btn-photo");
        cambiarFoto.setOnAction(e -> mostrarInfo("Foto", "Aquí después se puede abrir un FileChooser para seleccionar imagen."));

        Button eliminar = new Button("Eliminar");
        eliminar.getStyleClass().add("btn-link");

        fotoBox.getChildren().addAll(foto, cambiarFoto, eliminar);

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(14);

        grid.add(UiFactory.campo("Nombre(s) *", nombres), 0, 0);
        grid.add(UiFactory.campo("Apellido paterno *", apellidoPaterno), 1, 0);
        grid.add(UiFactory.campo("Apellido materno", apellidoMaterno), 2, 0);

        grid.add(UiFactory.campo("Fecha nacimiento *", fechaNacimiento), 0, 1);
        grid.add(UiFactory.campo("Edad", edad), 1, 1);
        grid.add(campoCombo("Sexo *", sexo), 2, 1);

        grid.add(campoCombo("Estado civil", estadoCivil), 0, 2);
        grid.add(UiFactory.campo("CURP", curp), 1, 2);
        grid.add(UiFactory.campo("Teléfono", telefono), 2, 2);

        grid.add(UiFactory.campo("Correo", correo), 0, 3);
        grid.add(UiFactory.campo("Dirección *", direccion), 1, 3, 2, 1);

        grid.add(UiFactory.campo("Escolaridad", escolaridad), 0, 4);
        grid.add(UiFactory.campo("Grado / Semestre", gradoSemestre), 1, 4);
        grid.add(UiFactory.campo("Ocupación", ocupacion), 2, 4);

        grid.add(UiFactory.campo("Contacto de emergencia", contactoEmergencia), 0, 5);
        grid.add(UiFactory.campo("Teléfono emergencia", telefonoEmergencia), 1, 5);
        grid.add(UiFactory.campo("Parentesco", parentesco), 2, 5);

        HBox cuerpo = new HBox(18, fotoBox, grid);

        Label nota = new Label("* Campos obligatorios");
        nota.getStyleClass().add("required-note");

        box.getChildren().addAll(titulo, cuerpo, nota);
        return box;
    }

    private VBox crearClasificacion() {
        VBox box = new VBox(12);
        box.setPrefWidth(430);

        box.getChildren().addAll(
                crearEstadoFisiologico(),
                crearNivelActividad(),
                crearResumenExpediente(),
                crearChecklistCaptura()
        );

        return box;
    }

    private VBox crearResumenExpediente() {
        VBox box = UiFactory.card();

        Label titulo = UiFactory.tituloSeccion("Resumen del expediente");

        Label expediente = new Label("EXP-2026-0001");
        expediente.getStyleClass().add("summary-code");

        Label nombre = new Label(obtenerNombrePreview());
        nombre.getStyleClass().add("summary-name");

        Label descripcion = new Label(obtenerDescripcionPreview());
        descripcion.getStyleClass().add("muted");

        box.getChildren().addAll(
                titulo,
                expediente,
                nombre,
                descripcion,
                resumenLinea("Estado", "En captura"),
                resumenLinea("Profesional", "Raúl Hernández"),
                resumenLinea("Consentimiento", "Pendiente")
        );

        return box;
    }

    private String obtenerNombrePreview() {
        String nombre = (nombres.getText() + " " + apellidoPaterno.getText() + " " + apellidoMaterno.getText()).trim();
        return nombre.isBlank() ? "Sin nombre capturado" : nombre;
    }

    private String obtenerDescripcionPreview() {
        return edad.getText() + " | " + sexo.getValue() + " | " + estadoFisiologico.name();
    }

    private HBox resumenLinea(String etiqueta, String valor) {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER_LEFT);

        Label left = new Label(etiqueta);
        left.getStyleClass().add("summary-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label right = new Label(valor);
        right.getStyleClass().add("summary-value");

        box.getChildren().addAll(left, spacer, right);
        return box;
    }

    private VBox crearChecklistCaptura() {
        VBox box = UiFactory.card();

        box.getChildren().addAll(
                UiFactory.tituloSeccion("Estado de captura"),
                checkCaptura("Datos generales", true),
                checkCaptura("Contacto", !telefono.getText().isBlank() || !correo.getText().isBlank()),
                checkCaptura("Antropometría", !peso.getText().isBlank() && !talla.getText().isBlank()),
                checkCaptura("Datos clínicos", !motivoConsulta.getText().isBlank()),
                checkCaptura("Nutrición", !alimentosHabituales.getText().isBlank()),
                checkCaptura("Actividad física", !objetivoEntrenamiento.getText().isBlank())
        );

        return box;
    }

    private HBox checkCaptura(String texto, boolean completo) {
        Label icono = new Label(completo ? "✓" : "○");
        icono.getStyleClass().add(completo ? "check-ok" : "check-pending");

        Label label = new Label(texto);
        label.getStyleClass().add("check-text");

        HBox box = new HBox(10, icono, label);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private VBox crearEstadoFisiologico() {
        VBox box = UiFactory.card();
        box.getChildren().addAll(
                UiFactory.tituloSeccion("Estado fisiológico"),
                UiFactory.ayuda("Selecciona el estado fisiológico actual"),
                chipGroupEstado()
        );
        return box;
    }

    private FlowPane chipGroupEstado() {
        FlowPane pane = new FlowPane(10, 10);
        ToggleGroup group = new ToggleGroup();

        pane.getChildren().addAll(
                chip("Niño", EstadoFisiologico.NINO, group),
                chip("Adolescente", EstadoFisiologico.ADOLESCENTE, group),
                chip("Adulto", EstadoFisiologico.ADULTO, group),
                chip("Adulto mayor", EstadoFisiologico.ADULTO_MAYOR, group),
                chip("Embarazada", EstadoFisiologico.EMBARAZADA, group),
                chip("Lactancia", EstadoFisiologico.LACTANCIA, group)
        );

        return pane;
    }

    private ToggleButton chip(String texto, EstadoFisiologico estado, ToggleGroup group) {
        ToggleButton chip = new ToggleButton(texto);
        chip.setToggleGroup(group);
        chip.setSelected(estadoFisiologico == estado);
        chip.getStyleClass().add("chip");
        chip.setOnAction(e -> estadoFisiologico = estado);
        return chip;
    }

    private VBox crearNivelActividad() {
        VBox box = UiFactory.card();
        box.getChildren().addAll(
                UiFactory.tituloSeccion("Nivel de actividad / deportivo"),
                UiFactory.ayuda("Selecciona el nivel actual"),
                chipGroupNivel()
        );
        return box;
    }

    private FlowPane chipGroupNivel() {
        FlowPane pane = new FlowPane(10, 10);
        ToggleGroup group = new ToggleGroup();

        pane.getChildren().addAll(
                chipNivel("Sedentario", NivelActividad.SEDENTARIO, group),
                chipNivel("Recreativo", NivelActividad.RECREATIVO, group),
                chipNivel("Amateur", NivelActividad.AMATEUR, group),
                chipNivel("Competitivo", NivelActividad.COMPETITIVO, group),
                chipNivel("Alto rendimiento", NivelActividad.ALTO_RENDIMIENTO, group)
        );

        return pane;
    }

    private ToggleButton chipNivel(String texto, NivelActividad nivel, ToggleGroup group) {
        ToggleButton chip = new ToggleButton(texto);
        chip.setToggleGroup(group);
        chip.setSelected(nivelActividad == nivel);
        chip.getStyleClass().add("chip");
        chip.setOnAction(e -> nivelActividad = nivel);
        return chip;
    }

    private VBox crearDisciplina() {
        VBox box = UiFactory.card();

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);

        grid.add(campoCombo("Deporte principal", deportePrincipal), 0, 0);
        grid.add(campoCombo("Posición", posicion), 1, 0);
        grid.add(UiFactory.campo("Años practicando", anosPracticando), 0, 1);
        grid.add(UiFactory.campo("Entrenamientos por semana", entrenamientosSemana), 1, 1);

        box.getChildren().addAll(
                UiFactory.tituloSeccion("Disciplina deportiva"),
                UiFactory.ayuda("Si aplica, selecciona el deporte que practica"),
                grid
        );

        return box;
    }

    private VBox crearModulos() {
        VBox box = UiFactory.card();

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getChildren().addAll(
                UiFactory.tituloSeccion("Módulos que incluirá el expediente"),
                UiFactory.ayuda("Activa los módulos que usarás con este paciente/alumno")
        );

        FlowPane pane = new FlowPane(12, 12);
        pane.getChildren().addAll(
                modulo("Historia clínica", true),
                modulo("Antropometría", true),
                modulo("Bioquímicos", true),
                modulo("Evaluación nutricional", true),
                modulo("Dietoterapia", true),
                modulo("Entrenamiento", true),
                modulo("Lesiones", true),
                modulo("Competencias", true),
                modulo("Psicología deportiva", false),
                modulo("Documentos", true),
                modulo("Seguimiento", true)
        );

        box.getChildren().addAll(header, pane);
        return box;
    }

    private VBox modulo(String texto, boolean activo) {
        CheckBox check = new CheckBox();
        check.setSelected(activo);
        modulos.put(texto, check);

        Label label = new Label(texto);
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.getStyleClass().add("module-text");

        VBox content = new VBox(8, check, label);
        content.setAlignment(Pos.CENTER);
        content.setPrefSize(120, 82);
        content.getStyleClass().add("module-card");

        return content;
    }

    private Pane crearPasoAntropometria() {
        VBox box = UiFactory.card();
        box.getChildren().add(UiFactory.tituloSeccion("Antropometría"));

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(14);

        grid.add(UiFactory.campo("Peso actual kg", peso), 0, 0);
        grid.add(UiFactory.campo("Talla cm", talla), 1, 0);
        grid.add(UiFactory.campo("IMC", imc), 2, 0);
        grid.add(UiFactory.campo("Cintura cm", cintura), 0, 1);
        grid.add(UiFactory.campo("Cadera cm", cadera), 1, 1);

        Button calcular = new Button("Calcular IMC");
        calcular.getStyleClass().add("btn-primary");
        calcular.setOnAction(e -> calcularImc());

        box.getChildren().addAll(grid, calcular);
        return box;
    }

    private Pane crearPasoClinico() {
        VBox box = UiFactory.card();
        box.getChildren().add(UiFactory.tituloSeccion("Datos clínicos"));

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(14);

        grid.add(UiFactory.area("Motivo de consulta", motivoConsulta), 0, 0);
        grid.add(UiFactory.area("Antecedentes", antecedentes), 1, 0);
        grid.add(UiFactory.area("Patologías", patologias), 0, 1);
        grid.add(UiFactory.area("Medicamentos", medicamentos), 1, 1);
        grid.add(UiFactory.area("Alergias", alergias), 0, 2);

        box.getChildren().add(grid);
        return box;
    }

    private Pane crearPasoNutricion() {
        VBox box = UiFactory.card();
        box.getChildren().add(UiFactory.tituloSeccion("Nutrición"));

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(14);

        grid.add(UiFactory.area("Alimentos habituales", alimentosHabituales), 0, 0);
        grid.add(UiFactory.area("Alimentos rechazados", alimentosRechazados), 1, 0);
        grid.add(UiFactory.campo("Consumo de agua", consumoAgua), 0, 1);
        grid.add(UiFactory.area("Objetivo nutricional", objetivoNutricional), 1, 1);

        box.getChildren().add(grid);
        return box;
    }

    private Pane crearPasoActividadFisica() {
        VBox box = UiFactory.card();
        box.getChildren().add(UiFactory.tituloSeccion("Actividad física y entrenamiento"));

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(14);

        ComboBox<String> practicaDeporte = UiFactory.combo("No", "Sí", "No");

        VBox bloquePractica = campoCombo("¿Practica deporte?", practicaDeporte);

        grid.add(bloquePractica, 0, 0);
        grid.add(UiFactory.campo("Días de actividad física", diasEntrenamiento), 1, 0);
        grid.add(UiFactory.area("Objetivo de entrenamiento", objetivoEntrenamiento), 0, 1);
        grid.add(UiFactory.area("Restricciones", restricciones), 1, 1);
        grid.add(UiFactory.area("Lesiones previas", lesionesPrevias), 0, 2);

        VBox bloqueDeporte = crearDisciplinaCondicional();
        bloqueDeporte.setVisible(false);
        bloqueDeporte.setManaged(false);

        practicaDeporte.valueProperty().addListener((obs, oldValue, newValue) -> {
            boolean mostrar = "Sí".equals(newValue);
            bloqueDeporte.setVisible(mostrar);
            bloqueDeporte.setManaged(mostrar);
        });

        box.getChildren().addAll(grid, bloqueDeporte);

        return box;
    }

    private VBox crearDisciplinaCondicional() {
        VBox box = UiFactory.card();
        box.getStyleClass().add("sport-card");

        Label titulo = UiFactory.tituloSeccion("Información deportiva");
        Label ayuda = UiFactory.ayuda("Este bloque sólo aparece si la persona practica deporte.");

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(14);

        TextField categoria = UiFactory.campoTexto("Sub-17");

        grid.add(campoCombo("Deporte principal", deportePrincipal), 0, 0);
        grid.add(campoCombo("Posición", posicion), 1, 0);
        grid.add(UiFactory.campo("Categoría", categoria), 2, 0);
        grid.add(UiFactory.campo("Años practicando", anosPracticando), 0, 1);
        grid.add(UiFactory.campo("Entrenamientos por semana", entrenamientosSemana), 1, 1);

        box.getChildren().addAll(titulo, ayuda, grid);

        return box;
    }

    private VBox campoCombo(String etiqueta, ComboBox<String> combo) {
        VBox box = new VBox(5);
        Label label = new Label(etiqueta);
        label.getStyleClass().add("field-label");
        box.getChildren().addAll(label, combo);
        return box;
    }

    private void calcularImc() {
        try {
            double pesoValor = Double.parseDouble(peso.getText().replace(",", "."));
            double tallaCm = Double.parseDouble(talla.getText().replace(",", "."));
            double tallaM = tallaCm / 100.0;
            double resultado = pesoValor / (tallaM * tallaM);
            imc.setText(String.format("%.2f", resultado));
        } catch (NumberFormatException ex) {
            mostrarError("Datos inválidos", "Ingresa peso y talla con números válidos.");
        }
    }

    private void irSiguiente() {
        if (!validarPasoActual()) {
            return;
        }

        if (pasoActual < 5) {
            pasoActual++;
            actualizarContenido();
        }
    }

    private void irAnterior() {
        if (pasoActual > 1) {
            pasoActual--;
            actualizarContenido();
        }
    }

    private void actualizarBotones() {
        btnAnterior.setDisable(pasoActual == 1);
        btnSiguiente.setVisible(pasoActual < 5);
        btnGuardar.setVisible(pasoActual == 5);
    }

    private void actualizarEncabezado() {
        tituloHeader.setText("Nuevo expediente / Paso " + pasoActual + " de 5");

        switch (pasoActual) {
            case 1 -> subtituloHeader.setText("Datos generales del cliente / paciente / alumno");
            case 2 -> subtituloHeader.setText("Registro antropométrico inicial");
            case 3 -> subtituloHeader.setText("Historia clínica y antecedentes");
            case 4 -> subtituloHeader.setText("Datos alimentarios y objetivo nutricional");
            case 5 -> subtituloHeader.setText("Actividad física, deporte, lesiones y restricciones");
            default -> subtituloHeader.setText("");
        }
    }

    private boolean validarPasoActual() {
        if (pasoActual == 1) {
            if (nombres.getText().isBlank() || apellidoPaterno.getText().isBlank() || direccion.getText().isBlank()) {
                mostrarError("Faltan datos obligatorios", "Captura nombre, apellido paterno y dirección.");
                return false;
            }
        }

        return true;
    }

    private void guardarExpediente() {
        if (!validarPasoActual()) {
            return;
        }

        ExpedientePersona expediente = construirExpediente();
        expedienteService.guardar(expediente);

        mostrarInfo(
                "Expediente guardado",
                "Se guardó correctamente el expediente:\n" +
                        expediente.getIdExpediente() + "\n" +
                        expediente.getNombreCompleto()
        );
    }

    private ExpedientePersona construirExpediente() {
        ExpedientePersona e = new ExpedientePersona();

        e.setTipoPersona(tipoPersona);
        e.setEstadoFisiologico(estadoFisiologico);
        e.setNivelActividad(nivelActividad);

        e.setNombres(nombres.getText());
        e.setApellidoPaterno(apellidoPaterno.getText());
        e.setApellidoMaterno(apellidoMaterno.getText());
        e.setFechaNacimiento(fechaNacimiento.getText());
        e.setEdad(edad.getText());
        e.setSexo(sexo.getValue());
        e.setEstadoCivil(estadoCivil.getValue());
        e.setCurp(curp.getText());
        e.setTelefono(telefono.getText());
        e.setCorreo(correo.getText());
        e.setDireccion(direccion.getText());
        e.setEscolaridad(escolaridad.getText());
        e.setGradoSemestre(gradoSemestre.getText());
        e.setOcupacion(ocupacion.getText());
        e.setContactoEmergencia(contactoEmergencia.getText());
        e.setTelefonoEmergencia(telefonoEmergencia.getText());
        e.setParentesco(parentesco.getText());

        e.setDeportePrincipal(deportePrincipal.getValue());
        e.setPosicion(posicion.getValue());
        e.setAnosPracticando(anosPracticando.getText());
        e.setEntrenamientosSemana(entrenamientosSemana.getText());

        e.setPeso(peso.getText());
        e.setTalla(talla.getText());
        e.setImc(imc.getText());
        e.setCintura(cintura.getText());
        e.setCadera(cadera.getText());

        e.setMotivoConsulta(motivoConsulta.getText());
        e.setAntecedentes(antecedentes.getText());
        e.setPatologias(patologias.getText());
        e.setMedicamentos(medicamentos.getText());
        e.setAlergias(alergias.getText());

        e.setAlimentosHabituales(alimentosHabituales.getText());
        e.setAlimentosRechazados(alimentosRechazados.getText());
        e.setConsumoAgua(consumoAgua.getText());
        e.setObjetivoNutricional(objetivoNutricional.getText());

        e.setObjetivoEntrenamiento(objetivoEntrenamiento.getText());
        e.setDiasEntrenamiento(diasEntrenamiento.getText());
        e.setRestricciones(restricciones.getText());
        e.setLesionesPrevias(lesionesPrevias.getText());

        modulos.forEach((nombre, check) -> {
            if (check.isSelected()) {
                e.getModulosActivos().add(nombre);
            }
        });

        return e;
    }

    private void limpiarFormulario() {
        nombres.clear();
        apellidoPaterno.clear();
        apellidoMaterno.clear();
        fechaNacimiento.clear();
        edad.clear();
        curp.clear();
        telefono.clear();
        correo.clear();
        direccion.clear();
        escolaridad.clear();
        gradoSemestre.clear();
        ocupacion.clear();
        contactoEmergencia.clear();
        telefonoEmergencia.clear();
        parentesco.clear();

        peso.clear();
        talla.clear();
        imc.clear();
        cintura.clear();
        cadera.clear();

        motivoConsulta.clear();
        antecedentes.clear();
        patologias.clear();
        medicamentos.clear();
        alergias.clear();

        alimentosHabituales.clear();
        alimentosRechazados.clear();
        consumoAgua.clear();
        objetivoNutricional.clear();

        objetivoEntrenamiento.clear();
        diasEntrenamiento.clear();
        restricciones.clear();
        lesionesPrevias.clear();

        pasoActual = 1;
        actualizarContenido();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}