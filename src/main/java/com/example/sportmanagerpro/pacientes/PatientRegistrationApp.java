package com.example.sportmanagerpro.pacientes;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Aplicación JavaFX completa para registrar, almacenar, buscar y recuperar pacientes.
 *
 * <p>La interfaz está construida sin hojas CSS externas. El almacenamiento se realiza
 * con SQLite mediante {@link PatientDAO}. Al seleccionar un paciente guardado en la lista
 * derecha, el expediente se recupera y se carga nuevamente en el formulario.</p>
 */
public class PatientRegistrationApp extends Application {

    private final PatientDAO dao = new PatientDAO();
    private final ListView<Patient> patientList = new ListView<>();

    private Long currentPatientId;
    private String selectedPhotoPath;

    private TextField nombresField;
    private TextField apellidoPaternoField;
    private TextField apellidoMaternoField;
    private DatePicker fechaNacimientoPicker;
    private TextField edadField;
    private ComboBox<String> generoCombo;
    private TextField telefonoField;
    private TextField correoField;
    private ComboBox<String> estadoCivilCombo;
    private ComboBox<String> objetivoCombo;
    private ComboBox<String> actividadCombo;
    private TextField ocupacionField;
    private ComboBox<String> comoConocioCombo;
    private TextArea notasArea;
    private TextField pesoField;
    private TextField estaturaField;
    private TextField grasaField;
    private TextField imcField;
    private TextField cinturaField;
    private TextField caderaField;
    private TextField searchField;

    private Label expedienteLabel;
    private Label fechaRegistroLabel;
    private Label nutriologoLabel;
    private Label adviceLabel;
    private StackPane photoPreview;

    /**
     * Punto de entrada gráfico de JavaFX.
     *
     * @param stage ventana principal.
     */
    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(UiKit.BACKGROUND, CornerRadii.EMPTY, Insets.EMPTY)));
        root.setLeft(createSidebar());
        root.setTop(createTopBar());
        root.setCenter(createMainContent(stage));

        Scene scene = new Scene(root, 1500, 900);
        stage.setTitle("SportNutri Pro - Registrar paciente");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        cargarPacientes("");
    }

    /**
     * Crea el menú lateral oscuro de la aplicación.
     *
     * @return panel lateral.
     */
    private VBox createSidebar() {
        VBox sidebar = new VBox(22);
        sidebar.setPadding(new Insets(28, 18, 22, 18));
        sidebar.setPrefWidth(230);
        sidebar.setBackground(new Background(new BackgroundFill(UiKit.NAVY, CornerRadii.EMPTY, Insets.EMPTY)));

        Label logo = new Label("SPORTNUTRI\nPRO");
        logo.setTextFill(Color.WHITE);
        logo.setStyle("-fx-font-size: 22px; -fx-font-weight: 900;");

        VBox menu = new VBox(11);
        menu.getChildren().addAll(
                menuButton("Inicio", false),
                menuButton("Pacientes", true),
                menuButton("Evaluación", false),
                menuButton("Diagnóstico", false),
                menuButton("Cálculo dietético", false),
                menuButton("Menú", false),
                menuButton("Evolución", false),
                menuButton("Reportes", false),
                menuButton("Agenda", false),
                menuButton("Recordatorios", false)
        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Button config = menuButton("Configuración", false);

        sidebar.getChildren().addAll(logo, menu, spacer, config);
        return sidebar;
    }

    private Button menuButton(String text, boolean active) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPrefHeight(44);
        button.setTextFill(Color.WHITE);
        button.setStyle(active
                ? "-fx-background-color: linear-gradient(to right, #22C55E, #16A34A); -fx-background-radius: 10; -fx-font-weight: 800; -fx-font-size: 14;"
                : "-fx-background-color: transparent; -fx-background-radius: 10; -fx-font-weight: 700; -fx-font-size: 14;");
        return button;
    }

    /**
     * Crea la barra superior con ruta, búsqueda y usuario.
     *
     * @return barra superior.
     */
    private HBox createTopBar() {
        HBox top = new HBox(18);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(18, 28, 18, 28));
        top.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        top.setBorder(new Border(new BorderStroke(Color.web("#E2E8F0"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 1, 0))));

        Label menuIcon = UiKit.title("☰", 21);
        Label breadcrumb = UiKit.muted("Pacientes   ›   Registrar nuevo paciente", 14);
        breadcrumb.setTextFill(Color.web("#0F8A3C"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        searchField = UiKit.input("Buscar (Ctrl + K)");
        searchField.setPrefWidth(310);
        searchField.textProperty().addListener((obs, old, val) -> cargarPacientes(val));

        Label bell = UiKit.title("🔔", 14);
        Label user = UiKit.title("Lic. Valeria M.\nNutrióloga", 13);

        top.getChildren().addAll(menuIcon, breadcrumb, spacer, searchField, bell, user);
        return top;
    }

    /**
     * Crea el contenido principal con formulario y panel derecho.
     *
     * @param stage ventana usada para abrir selector de imágenes.
     * @return contenido central.
     */
    private ScrollPane createMainContent(Stage stage) {
        HBox content = new HBox(18);
        content.setPadding(new Insets(24));
        content.setAlignment(Pos.TOP_LEFT);

        VBox mainColumn = new VBox(16);
        mainColumn.setMinWidth(820);
        mainColumn.setPrefWidth(920);
        HBox.setHgrow(mainColumn, Priority.ALWAYS);

        Label title = UiKit.title("Registrar nuevo paciente", 24);
        Label subtitle = UiKit.muted("Completa la información para crear un nuevo expediente", 14);

        mainColumn.getChildren().addAll(title, subtitle, createPersonalDataCard(), createAdditionalInfoCard(), createPhysicalDataCard(), createActionBar());

        VBox right = new VBox(14);
        right.setPrefWidth(330);
        right.getChildren().addAll(createPhotoCard(stage), createQuickSummaryCard(), createSavedPatientsCard());

        content.getChildren().addAll(mainColumn, right);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #F4F7FB; -fx-background-color: #F4F7FB;");
        return scroll;
    }

    private VBox createPersonalDataCard() {
        VBox card = UiKit.card(18, 22);
        card.getChildren().add(UiKit.sectionTitle("Datos personales", UiKit.ICON_USER));

        nombresField = UiKit.input("Ej. Ana María");
        apellidoPaternoField = UiKit.input("Ej. López");
        apellidoMaternoField = UiKit.input("Ej. Martínez");
        fechaNacimientoPicker = UiKit.datePicker();
        edadField = UiKit.input("Ej. 23");
        generoCombo = UiKit.combo("Seleccionar", "Femenino", "Masculino", "Otro", "Prefiere no decir");
        telefonoField = UiKit.input("Ej. 55 1234 5678");
        correoField = UiKit.input("Ej. ana.lopez@email.com");
        estadoCivilCombo = UiKit.combo("Seleccionar", "Soltera/o", "Casada/o", "Unión libre", "Separada/o", "Viuda/o");

        GridPane grid = grid();
        addField(grid, "Nombre(s)", nombresField, 0, 0);
        addField(grid, "Apellido paterno", apellidoPaternoField, 1, 0);
        addField(grid, "Apellido materno", apellidoMaternoField, 2, 0);
        addField(grid, "Fecha de nacimiento", fechaNacimientoPicker, 0, 1);
        addField(grid, "Edad", edadField, 1, 1);
        addField(grid, "Género", generoCombo, 2, 1);
        addField(grid, "Teléfono", telefonoField, 0, 2);
        addField(grid, "Correo electrónico", correoField, 1, 2);
        addField(grid, "Estado civil", estadoCivilCombo, 2, 2);

        card.getChildren().add(grid);
        return card;
    }

    private VBox createAdditionalInfoCard() {
        VBox card = UiKit.card(18, 22);
        card.getChildren().add(UiKit.sectionTitle("Información adicional", UiKit.ICON_CLIPBOARD));

        objetivoCombo = UiKit.combo("Seleccionar objetivo", "Pérdida de grasa", "Aumento de masa muscular", "Recomposición corporal", "Control clínico", "Rendimiento deportivo", "Embarazo", "Otro");
        actividadCombo = UiKit.combo("Seleccionar nivel", "Sedentario", "Ligero", "Moderado", "Activo", "Muy activo", "Atleta");
        ocupacionField = UiKit.input("Ej. Estudiante");
        comoConocioCombo = UiKit.combo("Seleccionar opción", "Recomendación", "Redes sociales", "Escuela / equipo", "Consulta familiar", "Otro");
        notasArea = UiKit.textArea("Observaciones generales del paciente...");

        GridPane grid = grid();
        addField(grid, "Objetivo principal", objetivoCombo, 0, 0);
        addField(grid, "Nivel de actividad física", actividadCombo, 1, 0);
        addField(grid, "Ocupación", ocupacionField, 2, 0);
        addField(grid, "¿Cómo nos conoció?", comoConocioCombo, 0, 1);
        addField(grid, "Notas iniciales (opcional)", notasArea, 1, 1, 2, 1);

        card.getChildren().add(grid);
        return card;
    }

    private VBox createPhysicalDataCard() {
        VBox card = UiKit.card(18, 22);
        card.getChildren().add(UiKit.sectionTitle("Datos físicos iniciales", UiKit.ICON_WEIGHT));

        pesoField = UiKit.input("Ej. 68.5");
        estaturaField = UiKit.input("Ej. 165");
        grasaField = UiKit.input("Ej. 22");
        imcField = UiKit.input("--");
        imcField.setEditable(false);
        cinturaField = UiKit.input("Ej. 78");
        caderaField = UiKit.input("Ej. 98");

        pesoField.textProperty().addListener((o, a, b) -> calcularImc());
        estaturaField.textProperty().addListener((o, a, b) -> calcularImc());

        GridPane grid = grid();
        addField(grid, "Peso actual (kg)", pesoField, 0, 0);
        addField(grid, "Estatura (cm)", estaturaField, 1, 0);
        addField(grid, "% Grasa corporal (opcional)", grasaField, 2, 0);
        addField(grid, "IMC (se calculará automáticamente)", imcField, 0, 1);
        addField(grid, "Circunferencia cintura (cm)", cinturaField, 1, 1);
        addField(grid, "Circunferencia cadera (cm)", caderaField, 2, 1);

        card.getChildren().add(grid);
        return card;
    }

    private HBox createActionBar() {
        HBox bar = new HBox(12);
        bar.setAlignment(Pos.CENTER_RIGHT);

        Button limpiar = UiKit.outlineButton("Limpiar", Color.web("#64748B"), UiKit.ICON_REFRESH);
        Button borrador = UiKit.outlineButton("Guardar borrador", UiKit.GREEN, UiKit.ICON_SAVE);
        Button guardar = UiKit.primaryButton("Guardar paciente", UiKit.ICON_PLUS_USER);
        Button eliminar = UiKit.outlineButton("Eliminar", Color.web("#DC2626"), UiKit.ICON_TRASH);

        limpiar.setOnAction(e -> limpiarFormulario());
        borrador.setOnAction(e -> guardarPaciente("BORRADOR"));
        guardar.setOnAction(e -> guardarPaciente("ACTIVO"));
        eliminar.setOnAction(e -> eliminarPacienteActual());

        bar.getChildren().addAll(limpiar, borrador, guardar, eliminar);
        return bar;
    }

    private VBox createPhotoCard(Stage stage) {
        VBox card = UiKit.card(16, 22);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(330);
        Label title = UiKit.title("Foto del paciente", 15);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER_LEFT);

        photoPreview = new StackPane();
        photoPreview.setMinSize(150, 150);
        photoPreview.setPrefSize(150, 150);
        Circle circle = UiKit.avatar(70);
        photoPreview.getChildren().addAll(circle, UiKit.icon(UiKit.ICON_CAMERA, Color.web("#60A5FA"), 42));

        Button select = UiKit.outlineButton("Seleccionar foto", Color.web("#334155"), UiKit.ICON_CAMERA);
        select.setOnAction(e -> seleccionarFoto(stage));

        Label helper = UiKit.muted("JPG, PNG o WEBP. Máx. 5MB", 12);
        card.getChildren().addAll(title, photoPreview, select, helper);
        return card;
    }

    private VBox createQuickSummaryCard() {
        VBox card = UiKit.card(18, 22);
        card.setPrefWidth(330);
        card.getChildren().add(UiKit.title("Resumen rápido", 15));
        expedienteLabel = summaryItem(card, "Expediente", "Se generará automáticamente", Color.web("#3B82F6"));
        fechaRegistroLabel = summaryItem(card, "Fecha de registro", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")), UiKit.GREEN);
        nutriologoLabel = summaryItem(card, "Nutriólogo asignado", "Lic. Valeria M.", Color.web("#F97316"));
        return card;
    }

    private Label summaryItem(VBox card, String title, String value, Color color) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        StackPane icon = UiKit.coloredIcon(UiKit.ICON_CLIPBOARD, color);
        VBox text = new VBox(3);
        Label t = UiKit.title(title, 13);
        Label v = UiKit.muted(value, 13);
        text.getChildren().addAll(t, v);
        row.getChildren().addAll(icon, text);
        card.getChildren().add(row);
        return v;
    }

    private VBox createSavedPatientsCard() {
        VBox card = UiKit.card(12, 18);
        card.setPrefWidth(330);
        Label title = UiKit.title("Pacientes guardados", 15);
        Label db = UiKit.muted("BD: " + dao.getDatabasePath(), 10);
        db.setWrapText(true);

        patientList.setPrefHeight(230);
        patientList.setItems(FXCollections.observableArrayList());
        patientList.setCellFactory(list -> new ListCell<>() {
            @Override protected void updateItem(Patient p, boolean empty) {
                super.updateItem(p, empty);
                if (empty || p == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                VBox box = new VBox(2);
                box.getChildren().addAll(
                        UiKit.title((p.getId() == null ? "" : "#" + p.getId() + "  ") + p.getNombreCompleto(), 13),
                        UiKit.muted((p.getTelefono() == null ? "" : p.getTelefono()) + "  ·  " + (p.getEstadoRegistro() == null ? "" : p.getEstadoRegistro()), 12)
                );
                setGraphic(box);
                setText(null);
            }
        });
        patientList.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) cargarPacienteEnFormulario(selected);
        });

        adviceLabel = UiKit.muted("Selecciona un paciente para recuperar su información.", 12);
        adviceLabel.setWrapText(true);
        card.getChildren().addAll(title, db, patientList, adviceLabel);
        return card;
    }

    private GridPane grid() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(16);
        for (int i = 0; i < 3; i++) {
            ColumnConstraints c = new ColumnConstraints();
            c.setPercentWidth(33.33);
            c.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(c);
        }
        return grid;
    }

    private void addField(GridPane grid, String label, Control field, int col, int row) {
        addField(grid, label, field, col, row, 1, 1);
    }

    private void addField(GridPane grid, String label, Control field, int col, int row, int colspan, int rowspan) {
        VBox wrapper = new VBox(8);
        Label l = UiKit.muted(label, 13);
        l.setTextFill(Color.web("#243247"));
        field.setMaxWidth(Double.MAX_VALUE);
        wrapper.getChildren().addAll(l, field);
        grid.add(wrapper, col, row, colspan, rowspan);
    }

    private void seleccionarFoto(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Seleccionar foto del paciente");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.webp"));
        File file = chooser.showOpenDialog(stage);
        if (file == null) return;
        selectedPhotoPath = file.getAbsolutePath();
        Image image = new Image(file.toURI().toString(), 140, 140, true, true);
        ImageView view = new ImageView(image);
        view.setFitWidth(140);
        view.setFitHeight(140);
        Circle clip = new Circle(70, 70, 70);
        view.setClip(clip);
        photoPreview.getChildren().setAll(view);
    }

    private void guardarPaciente(String estado) {
        try {
            Patient p = leerFormulario();
            p.setEstadoRegistro(estado);
            Patient saved = dao.guardar(p);
            currentPatientId = saved.getId();
            expedienteLabel.setText("#" + saved.getId());
            cargarPacientes(searchField.getText());
            mostrarInfo("Paciente guardado", "El expediente se almacenó correctamente.");
        } catch (Exception ex) {
            mostrarError("No se pudo guardar", ex.getMessage());
        }
    }

    private Patient leerFormulario() {
        if (nombresField.getText().isBlank()) {
            throw new IllegalArgumentException("El nombre del paciente es obligatorio.");
        }
        Patient p = new Patient();
        p.setId(currentPatientId);
        p.setNombres(nombresField.getText().trim());
        p.setApellidoPaterno(apellidoPaternoField.getText().trim());
        p.setApellidoMaterno(apellidoMaternoField.getText().trim());
        p.setFechaNacimiento(fechaNacimientoPicker.getValue());
        p.setEdad(parseInt(edadField.getText()));
        p.setGenero(generoCombo.getValue());
        p.setTelefono(telefonoField.getText().trim());
        p.setCorreo(correoField.getText().trim());
        p.setEstadoCivil(estadoCivilCombo.getValue());
        p.setObjetivoPrincipal(objetivoCombo.getValue());
        p.setNivelActividad(actividadCombo.getValue());
        p.setOcupacion(ocupacionField.getText().trim());
        p.setComoNosConocio(comoConocioCombo.getValue());
        p.setNotasIniciales(notasArea.getText().trim());
        p.setPesoActualKg(parseDouble(pesoField.getText()));
        p.setEstaturaCm(parseDouble(estaturaField.getText()));
        p.setPorcentajeGrasa(parseDouble(grasaField.getText()));
        p.setImc(parseDouble(imcField.getText()));
        p.setCinturaCm(parseDouble(cinturaField.getText()));
        p.setCaderaCm(parseDouble(caderaField.getText()));
        p.setFotoPath(selectedPhotoPath);
        p.setCreadoEn(LocalDateTime.now().toString());
        return p;
    }

    private void cargarPacienteEnFormulario(Patient p) {
        currentPatientId = p.getId();
        selectedPhotoPath = p.getFotoPath();
        nombresField.setText(value(p.getNombres()));
        apellidoPaternoField.setText(value(p.getApellidoPaterno()));
        apellidoMaternoField.setText(value(p.getApellidoMaterno()));
        fechaNacimientoPicker.setValue(p.getFechaNacimiento());
        edadField.setText(p.getEdad() == null ? "" : String.valueOf(p.getEdad()));
        generoCombo.setValue(p.getGenero());
        telefonoField.setText(value(p.getTelefono()));
        correoField.setText(value(p.getCorreo()));
        estadoCivilCombo.setValue(p.getEstadoCivil());
        objetivoCombo.setValue(p.getObjetivoPrincipal());
        actividadCombo.setValue(p.getNivelActividad());
        ocupacionField.setText(value(p.getOcupacion()));
        comoConocioCombo.setValue(p.getComoNosConocio());
        notasArea.setText(value(p.getNotasIniciales()));
        pesoField.setText(format(p.getPesoActualKg()));
        estaturaField.setText(format(p.getEstaturaCm()));
        grasaField.setText(format(p.getPorcentajeGrasa()));
        imcField.setText(format(p.getImc()));
        cinturaField.setText(format(p.getCinturaCm()));
        caderaField.setText(format(p.getCaderaCm()));
        expedienteLabel.setText("#" + p.getId());
        adviceLabel.setText("Expediente recuperado: " + p.getNombreCompleto());
        if (selectedPhotoPath != null && !selectedPhotoPath.isBlank()) {
            File file = new File(selectedPhotoPath);
            if (file.exists()) {
                ImageView view = new ImageView(new Image(file.toURI().toString(), 140, 140, true, true));
                view.setFitWidth(140);
                view.setFitHeight(140);
                view.setClip(new Circle(70, 70, 70));
                photoPreview.getChildren().setAll(view);
            }
        }
    }

    private void cargarPacientes(String query) {
        Platform.runLater(() -> {
            List<Patient> pacientes = dao.buscar(query);
            patientList.setItems(FXCollections.observableArrayList(pacientes));
        });
    }

    private void eliminarPacienteActual() {
        if (currentPatientId == null) {
            mostrarInfo("Sin expediente", "Primero selecciona o guarda un paciente.");
            return;
        }
        dao.eliminar(currentPatientId);
        limpiarFormulario();
        cargarPacientes(searchField.getText());
        mostrarInfo("Paciente eliminado", "El expediente fue eliminado de la base de datos.");
    }

    private void limpiarFormulario() {
        currentPatientId = null;
        selectedPhotoPath = null;
        nombresField.clear(); apellidoPaternoField.clear(); apellidoMaternoField.clear();
        fechaNacimientoPicker.setValue(null); edadField.clear(); generoCombo.setValue(null);
        telefonoField.clear(); correoField.clear(); estadoCivilCombo.setValue(null);
        objetivoCombo.setValue(null); actividadCombo.setValue(null); ocupacionField.clear();
        comoConocioCombo.setValue(null); notasArea.clear(); pesoField.clear(); estaturaField.clear();
        grasaField.clear(); imcField.clear(); cinturaField.clear(); caderaField.clear();
        expedienteLabel.setText("Se generará automáticamente");
        adviceLabel.setText("Selecciona un paciente para recuperar su información.");
        photoPreview.getChildren().setAll(UiKit.avatar(70), UiKit.icon(UiKit.ICON_CAMERA, Color.web("#60A5FA"), 42));
    }

    private void calcularImc() {
        Double peso = parseDouble(pesoField.getText());
        Double estatura = parseDouble(estaturaField.getText());
        if (peso == null || estatura == null || estatura <= 0) {
            imcField.setText("");
            return;
        }
        double metros = estatura / 100.0;
        imcField.setText(String.format("%.2f", peso / (metros * metros)));
    }

    private Integer parseInt(String text) {
        try { return text == null || text.isBlank() ? null : Integer.parseInt(text.trim()); }
        catch (Exception e) { throw new IllegalArgumentException("Revisa los campos numéricos enteros, por ejemplo edad."); }
    }

    private Double parseDouble(String text) {
        try {
            if (text == null || text.isBlank() || text.equals("--")) return null;
            return Double.parseDouble(text.trim().replace(",", "."));
        } catch (Exception e) {
            throw new IllegalArgumentException("Revisa los campos numéricos. Usa números como 68.5, 165 o 22.");
        }
    }

    private String value(String text) { return text == null ? "" : text; }
    private String format(Double value) { return value == null ? "" : String.format("%.2f", value); }

    private void mostrarInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.showAndWait();
    }

    private void mostrarError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
