package com.example.sportmanagerpro.sportnutri.ui;

import com.example.sportmanagerpro.sportnutri.model.Paciente;
import com.example.sportmanagerpro.sportnutri.repository.PacienteRepository;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Vista moderna para registrar y recuperar expedientes de pacientes conforme a campos base NOM-004.
 */
public class RegistroPacientesModernView extends BorderPane {
    private final PacienteRepository repository = new PacienteRepository();
    private final ListView<Paciente> listaPacientes = new ListView<>();
    private Paciente seleccionado;

    private final TextField buscar = Ui.input("Buscar paciente...");
    private final TextField nombres = Ui.input("Ej. Ana María");
    private final TextField apellidoPaterno = Ui.input("Ej. López");
    private final TextField apellidoMaterno = Ui.input("Ej. Martínez");
    private final DatePicker fechaNacimiento = Ui.datePicker("dd/mm/aaaa");
    private final TextField edad = Ui.input("Ej. 23");
    private final ComboBox<String> sexo = Ui.combo("Seleccionar", "Femenino", "Masculino", "Otro");
    private final TextField curp = Ui.input("Ej. LOPES950101MDFPRS08");
    private final TextField telefono = Ui.input("Ej. 55 1234 5678");
    private final TextField correo = Ui.input("Ej. correo@ejemplo.com");
    private final ComboBox<String> estadoCivil = Ui.combo("Seleccionar", "Soltera/o", "Casada/o", "Unión libre", "Divorciada/o", "Viuda/o");
    private final TextField ocupacion = Ui.input("Ej. Estudiante");
    private final ComboBox<String> escolaridad = Ui.combo("Seleccionar", "Primaria", "Secundaria", "Bachillerato", "Licenciatura", "Posgrado");
    private final TextArea domicilio = Ui.area("Calle, número, colonia, municipio, estado, código postal", 62);

    private final TextField establecimiento = Ui.input("Ej. SportNutri Pro");
    private final TextField razonSocial = Ui.input("Ej. Clínica de Nutrición y Rendimiento");
    private final ComboBox<String> tipoServicio = Ui.combo("Seleccionar", "Consulta nutricional", "Nutrición deportiva", "Entrenamiento", "Consulta clínica ambulatoria");
    private final TextField responsable = Ui.input("Ej. Lic. Valeria Martínez");
    private final TextField cedula = Ui.input("Ej. 12345678");
    private final DatePicker fechaApertura = Ui.datePicker("Fecha de apertura");

    private final TextArea antecedentesHF = Ui.area("Antecedentes heredo-familiares", 95);
    private final TextArea antecedentesPP = Ui.area("Patológicos, medicamentos, alergias, tabaco, alcohol y sustancias", 95);
    private final TextArea antecedentesPNP = Ui.area("Hábitos, actividad física, sueño, alimentación, agua, etc.", 95);
    private final TextArea padecimientoActual = Ui.area("Motivo de consulta y padecimiento actual", 95);
    private final TextArea interrogatorio = Ui.area("Interrogatorio por aparatos y sistemas", 95);
    private final TextArea exploracion = Ui.area("Habitus exterior, signos vitales, peso, talla y datos pertinentes a nutrición", 95);
    private final TextArea estudios = Ui.area("Resultados previos y actuales de laboratorio, gabinete y otros", 95);
    private final TextArea diagnosticos = Ui.area("Diagnósticos o problemas clínicos / nutricionales", 95);
    private final TextArea pronostico = Ui.area("Pronóstico", 95);
    private final TextArea indicacion = Ui.area("Indicación terapéutica, plan alimentario, suplementación o recomendaciones", 110);
    private final TextArea notas = Ui.area("Notas adicionales, consentimiento informado, observaciones o documentos pendientes", 110);

    private final Label resumenExpediente = Ui.label("Se generará automáticamente", 13, false, Ui.MUTED);
    private final Label resumenNombre = Ui.label("Nuevo registro", 13, false, Ui.MUTED);

    public RegistroPacientesModernView() {
        setBackground(new Background(new BackgroundFill(Ui.BG, CornerRadii.EMPTY, Insets.EMPTY)));
        setLeft(crearSidebar());
        setTop(crearTopbar());
        setCenter(crearContenido());
        cargarLista();
        fechaApertura.setValue(LocalDate.now());
    }

    public Scene crearEscena() {
        return new Scene(this, 1500, 900);
    }

    private VBox crearSidebar() {
        VBox side = new VBox(22);
        side.setPadding(new Insets(28, 20, 24, 20));
        side.setPrefWidth(240);
        side.setBackground(new Background(new BackgroundFill(Ui.NAVY, CornerRadii.EMPTY, Insets.EMPTY)));

        Label logo = Ui.label("SPORTNUTRI\nPRO", 24, true, Color.WHITE);
        side.getChildren().add(logo);
        side.getChildren().addAll(
                menu("Dashboard", false), menu("Pacientes", true), menu("Evaluación", false), menu("Planes", false),
                menu("Nutrición", false), menu("Entrenamiento", false), menu("Calendario", false), menu("Reportes", false),
                menu("Mensajes", false), menu("Configuración", false)
        );
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        VBox user = new VBox(3, Ui.label("Lic. Valeria M.", 15, true, Color.WHITE), Ui.label("Nutrióloga", 13, false, Color.web("#D6E4F3")));
        user.setPadding(new Insets(16));
        user.setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255,0.08), new CornerRadii(14), Insets.EMPTY)));
        side.getChildren().addAll(spacer, user, menu("Cerrar sesión", false));
        return side;
    }

    private HBox menu(String text, boolean active) {
        HBox item = new HBox(12, new Circle(4, active ? Color.WHITE : Color.web("#C8D7E8")), Ui.label(text, 14, true, active ? Color.WHITE : Color.web("#D8E6F5")));
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(13, 16, 13, 16));
        item.setBackground(new Background(new BackgroundFill(active ? Ui.GREEN : Color.TRANSPARENT, new CornerRadii(12), Insets.EMPTY)));
        return item;
    }

    private HBox crearTopbar() {
        HBox top = new HBox(18);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(16, 28, 16, 28));
        top.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        top.setBorder(new Border(new BorderStroke(Color.web("#E2E8F0"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,0,1,0))));

        Label crumb = Ui.label("Pacientes  ›  Registro de nuevo paciente", 15, true, Ui.TEXT);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        buscar.setPrefWidth(320);
        buscar.textProperty().addListener((o,a,b) -> cargarLista());
        Button consultar = Ui.button("Consultar expedientes", "blue");
        consultar.setOnAction(e -> abrirConsultaExpedientes());

        Button guardar = Ui.button("Guardar paciente", "primary");
        guardar.setOnAction(e -> guardarPaciente());
        top.getChildren().addAll(crumb, spacer, buscar, Ui.label("🔔", 18, true, Ui.TEXT), consultar, guardar);
        return top;
    }

    /**
     * Abre la vista de consulta de expedientes usando el mismo repositorio SQLite.
     * De esta forma la información guardada en el registro se recupera en la nueva interfaz.
     */
    private void abrirConsultaExpedientes() {
        ConsultaExpedientesModernView consulta = new ConsultaExpedientesModernView();
        Stage stage = new Stage();
        stage.setTitle("NutriSport Pro - Consulta de expedientes");
        stage.setScene(new Scene(consulta, 1500, 900));
        stage.setMaximized(true);
        stage.show();
    }

    private HBox crearContenido() {
        HBox root = new HBox(18);
        root.setPadding(new Insets(28));

        VBox principal = new VBox(18);
        HBox.setHgrow(principal, Priority.ALWAYS);

        principal.getChildren().addAll(crearTitulo(), crearSteps(), crearDatosIdentificacion(), crearDatosEstablecimiento(), crearHistoriaClinica(), crearBotones());

        ScrollPane scroll = new ScrollPane(principal);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color:transparent;-fx-background:transparent;");
        HBox.setHgrow(scroll, Priority.ALWAYS);

        root.getChildren().addAll(scroll, crearPanelDerecho());
        return root;
    }

    private VBox crearTitulo() {
        Label title = Ui.label("Registro de nuevo paciente", 27, true, Ui.TEXT);
        Label sub = Ui.label("Completa la información para crear o actualizar el expediente clínico ambulatorio y nutricional.", 14, false, Ui.GREEN);
        return new VBox(6, title, sub);
    }

    private HBox crearSteps() {
        HBox steps = new HBox(12);
        steps.getChildren().addAll(
                Ui.step("1. Identificación", "👤", Ui.GREEN, true),
                Ui.step("2. Antecedentes", "♡", Ui.PURPLE, false),
                Ui.step("3. Exploración", "📋", Ui.BLUE, false),
                Ui.step("4. Diagnóstico", "🩺", Color.web("#F97316"), false),
                Ui.step("5. Plan terapéutico", "🍏", Ui.GREEN, false),
                Ui.step("6. Notas", "▣", Ui.MUTED, false)
        );
        return steps;
    }

    private VBox crearDatosIdentificacion() {
        GridPane g = Ui.grid(3);
        g.add(Ui.field("* Nombre(s)", nombres), 0, 0);
        g.add(Ui.field("* Apellido paterno", apellidoPaterno), 1, 0);
        g.add(Ui.field("Apellido materno", apellidoMaterno), 2, 0);
        g.add(Ui.field("* Fecha de nacimiento", fechaNacimiento), 0, 1);
        g.add(Ui.field("* Edad", edad), 1, 1);
        g.add(Ui.field("* Sexo", sexo), 2, 1);
        g.add(Ui.field("CURP", curp), 0, 2);
        g.add(Ui.field("* Teléfono", telefono), 1, 2);
        g.add(Ui.field("Correo electrónico", correo), 2, 2);
        g.add(Ui.field("Estado civil", estadoCivil), 0, 3);
        g.add(Ui.field("Ocupación", ocupacion), 1, 3);
        g.add(Ui.field("Escolaridad", escolaridad), 2, 3);
        g.add(Ui.field("* Domicilio completo", domicilio), 0, 4, 3, 1);
        return Ui.card(new HBox(10, Ui.iconCircle(Ui.SVG_USER, Ui.GREEN, 32), Ui.label("Datos de identificación", 18, true, Ui.TEXT)), g);
    }

    private VBox crearDatosEstablecimiento() {
        GridPane g = Ui.grid(3);
        g.add(Ui.field("* Nombre del establecimiento", establecimiento), 0, 0);
        g.add(Ui.field("* Institución o razón social", razonSocial), 1, 0);
        g.add(Ui.field("* Tipo de servicio", tipoServicio), 2, 0);
        g.add(Ui.field("* Médico o nutriólogo responsable", responsable), 0, 1);
        g.add(Ui.field("* Cédula profesional", cedula), 1, 1);
        g.add(Ui.field("Fecha de apertura del expediente", fechaApertura), 2, 1);
        VBox c = Ui.card(new HBox(10, Ui.iconCircle(Ui.SVG_CLIP, Ui.GREEN, 32), Ui.label("Datos del establecimiento", 18, true, Ui.TEXT)), g);
        c.setBackground(new Background(new BackgroundFill(Color.web("#F0FDF4"), new CornerRadii(18), Insets.EMPTY)));
        return c;
    }

    private VBox crearHistoriaClinica() {
        GridPane g = Ui.grid(3);
        g.add(Ui.field("Antecedentes heredo-familiares", antecedentesHF), 0, 0);
        g.add(Ui.field("Antecedentes personales patológicos", antecedentesPP), 1, 0);
        g.add(Ui.field("Antecedentes personales no patológicos", antecedentesPNP), 2, 0);
        g.add(Ui.field("Padecimiento actual", padecimientoActual), 0, 1);
        g.add(Ui.field("Interrogatorio por aparatos y sistemas", interrogatorio), 1, 1);
        g.add(Ui.field("Exploración física", exploracion), 2, 1);
        g.add(Ui.field("Estudios de laboratorio, gabinete y otros", estudios), 0, 2);
        g.add(Ui.field("Diagnósticos o problemas clínicos", diagnosticos), 1, 2);
        g.add(Ui.field("Pronóstico", pronostico), 2, 2);
        g.add(Ui.field("Indicación terapéutica", indicacion), 0, 3, 2, 1);
        g.add(Ui.field("Notas adicionales", notas), 2, 3);
        return Ui.card(new HBox(10, Ui.iconCircle(Ui.SVG_SHIELD, Ui.BLUE, 32), Ui.label("Historia clínica y nota nutricional", 18, true, Ui.TEXT)), g);
    }

    private HBox crearBotones() {
        HBox buttons = new HBox(14);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        Button nuevo = Ui.button("Nuevo", "default");
        Button eliminar = Ui.button("Eliminar", "danger");
        Button borrador = Ui.button("Guardar borrador", "blue");
        Button guardar = Ui.button("Guardar y continuar", "primary");
        nuevo.setOnAction(e -> limpiar());
        eliminar.setOnAction(e -> eliminarPaciente());
        borrador.setOnAction(e -> guardarPaciente());
        guardar.setOnAction(e -> guardarPaciente());
        buttons.getChildren().addAll(nuevo, eliminar, borrador, guardar);
        return buttons;
    }

    private VBox crearPanelDerecho() {
        VBox right = new VBox(18);
        right.setPrefWidth(300);

        VBox foto = Ui.card(
                Ui.label("Foto del paciente", 16, true, Ui.TEXT),
                Ui.iconCircle(Ui.SVG_CAMERA, Ui.GREEN, 132),
                Ui.button("Subir foto", "default"),
                Ui.label("JPG, PNG o WEBP. Máx. 5MB", 12, false, Ui.MUTED)
        );
        foto.setAlignment(Pos.CENTER);

        VBox resumen = Ui.card(
                Ui.label("Resumen rápido", 16, true, Ui.TEXT),
                resumenItem("📋", "Expediente", resumenExpediente),
                resumenItem("📅", "Fecha de registro", Ui.label(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 13, false, Ui.MUTED)),
                resumenItem("👤", "Paciente", resumenNombre)
        );

        VBox consejo = Ui.card(Ui.label("💡  Recordatorio NOM-004-SSA3-2012", 14, true, Ui.TEXT),
                Ui.label("El expediente debe ser claro, completo, legible, confidencial y oportuno. Verifica identificación, antecedentes, exploración, diagnóstico, pronóstico e indicación terapéutica.", 13, false, Color.web("#3B556E")));
        consejo.setBackground(new Background(new BackgroundFill(Color.web("#ECFDF5"), new CornerRadii(18), Insets.EMPTY)));

        VBox registrados = Ui.card(Ui.label("Pacientes registrados", 16, true, Ui.TEXT), listaPacientes);
        listaPacientes.setPrefHeight(220);
        listaPacientes.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Paciente item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getExpediente() + " · " + item.nombreCompleto());
            }
        });
        listaPacientes.getSelectionModel().selectedItemProperty().addListener((o, a, p) -> cargarPaciente(p));

        right.getChildren().addAll(foto, resumen, consejo, registrados);
        return right;
    }

    private HBox resumenItem(String icon, String title, Label value) {
        VBox text = new VBox(3, Ui.label(title, 13, true, Ui.TEXT), value);
        HBox row = new HBox(12, Ui.label(icon, 20, true, Ui.BLUE), text);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private Paciente leerFormulario() {
        Paciente p = seleccionado == null ? new Paciente() : seleccionado;
        p.setNombres(nombres.getText()); p.setApellidoPaterno(apellidoPaterno.getText()); p.setApellidoMaterno(apellidoMaterno.getText());
        p.setFechaNacimiento(fechaNacimiento.getValue()); p.setEdad(parseInt(edad.getText())); p.setSexo(sexo.getValue());
        p.setCurp(curp.getText()); p.setTelefono(telefono.getText()); p.setCorreo(correo.getText()); p.setEstadoCivil(estadoCivil.getValue());
        p.setOcupacion(ocupacion.getText()); p.setEscolaridad(escolaridad.getValue()); p.setDomicilio(domicilio.getText());
        p.setNombreEstablecimiento(establecimiento.getText()); p.setRazonSocial(razonSocial.getText()); p.setTipoServicio(tipoServicio.getValue());
        p.setResponsable(responsable.getText()); p.setCedulaProfesional(cedula.getText()); p.setFechaApertura(fechaApertura.getValue());
        p.setAntecedentesHeredoFamiliares(antecedentesHF.getText()); p.setAntecedentesPersonalesPatologicos(antecedentesPP.getText());
        p.setAntecedentesPersonalesNoPatologicos(antecedentesPNP.getText()); p.setPadecimientoActual(padecimientoActual.getText());
        p.setInterrogatorioSistemas(interrogatorio.getText()); p.setExploracionFisica(exploracion.getText()); p.setEstudiosPrevios(estudios.getText());
        p.setDiagnosticos(diagnosticos.getText()); p.setPronostico(pronostico.getText()); p.setIndicacionTerapeutica(indicacion.getText()); p.setNotasAdicionales(notas.getText());
        return p;
    }

    private void guardarPaciente() {
        if (nombres.getText().isBlank()) { alerta("Falta el nombre del paciente."); return; }
        Paciente p = leerFormulario();
        if (p.getId() == 0) repository.guardar(p); else repository.actualizar(p);
        seleccionado = p;
        resumenExpediente.setText(p.getExpediente()); resumenNombre.setText(p.nombreCompleto());
        cargarLista();
        alerta("Paciente guardado correctamente.");
    }

    private void cargarPaciente(Paciente p) {
        if (p == null) return;
        seleccionado = p;
        nombres.setText(p.getNombres()); apellidoPaterno.setText(p.getApellidoPaterno()); apellidoMaterno.setText(p.getApellidoMaterno());
        fechaNacimiento.setValue(p.getFechaNacimiento()); edad.setText(p.getEdad() == null ? "" : String.valueOf(p.getEdad())); sexo.setValue(p.getSexo());
        curp.setText(p.getCurp()); telefono.setText(p.getTelefono()); correo.setText(p.getCorreo()); estadoCivil.setValue(p.getEstadoCivil());
        ocupacion.setText(p.getOcupacion()); escolaridad.setValue(p.getEscolaridad()); domicilio.setText(p.getDomicilio());
        establecimiento.setText(p.getNombreEstablecimiento()); razonSocial.setText(p.getRazonSocial()); tipoServicio.setValue(p.getTipoServicio());
        responsable.setText(p.getResponsable()); cedula.setText(p.getCedulaProfesional()); fechaApertura.setValue(p.getFechaApertura());
        antecedentesHF.setText(p.getAntecedentesHeredoFamiliares()); antecedentesPP.setText(p.getAntecedentesPersonalesPatologicos()); antecedentesPNP.setText(p.getAntecedentesPersonalesNoPatologicos());
        padecimientoActual.setText(p.getPadecimientoActual()); interrogatorio.setText(p.getInterrogatorioSistemas()); exploracion.setText(p.getExploracionFisica());
        estudios.setText(p.getEstudiosPrevios()); diagnosticos.setText(p.getDiagnosticos()); pronostico.setText(p.getPronostico()); indicacion.setText(p.getIndicacionTerapeutica()); notas.setText(p.getNotasAdicionales());
        resumenExpediente.setText(p.getExpediente()); resumenNombre.setText(p.nombreCompleto());
    }

    private void eliminarPaciente() {
        if (seleccionado == null || seleccionado.getId() == 0) return;
        repository.eliminar(seleccionado.getId());
        limpiar(); cargarLista();
    }

    private void limpiar() {
        seleccionado = null;
        List<Control> controls = List.of(nombres, apellidoPaterno, apellidoMaterno, edad, curp, telefono, correo, ocupacion, establecimiento, razonSocial, responsable, cedula);
        controls.forEach(c -> ((TextField)c).clear());
        List<TextArea> areas = List.of(domicilio, antecedentesHF, antecedentesPP, antecedentesPNP, padecimientoActual, interrogatorio, exploracion, estudios, diagnosticos, pronostico, indicacion, notas);
        areas.forEach(TextArea::clear);
        sexo.setValue(null); estadoCivil.setValue(null); escolaridad.setValue(null); tipoServicio.setValue(null);
        fechaNacimiento.setValue(null); fechaApertura.setValue(LocalDate.now());
        resumenExpediente.setText("Se generará automáticamente"); resumenNombre.setText("Nuevo registro");
        listaPacientes.getSelectionModel().clearSelection();
    }

    private void cargarLista() {
        List<Paciente> pacientes = repository.buscar(buscar.getText());
        listaPacientes.setItems(FXCollections.observableArrayList(pacientes));
    }

    private Integer parseInt(String value) {
        try { return value == null || value.isBlank() ? null : Integer.parseInt(value.trim()); }
        catch (NumberFormatException ex) { return null; }
    }

    private void alerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
