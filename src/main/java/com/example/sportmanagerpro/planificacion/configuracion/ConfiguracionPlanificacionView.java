package com.example.sportmanagerpro.planificacion.configuracion;

import com.example.sportmanagerpro.planificacion.model.PlanGraficoView;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Vista previa al plan gráfico.
 * Permite establecer los parámetros generales del ciclo y las competencias
 * antes de construir la matriz de planificación.
 */
public class ConfiguracionPlanificacionView extends Application {

    private final ConfiguracionPlanificacion configuracion = ConfiguracionPlanificacionStore.getConfiguracionActiva();

    private TextField txtNombreTemporada;
    private TextField txtDeporte;
    private TextField txtCategoria;
    private TextField txtRama;
    private TextField txtNivel;
    private TextArea txtObjetivo;

    private DatePicker dpInicio;
    private DatePicker dpFin;

    private ComboBox<String> cbModelo;
    private ComboBox<String> cbProgresion;

    private Spinner<Double> spPrep;
    private Spinner<Double> spComp;
    private Spinner<Double> spTrans;

    private Spinner<Double> spFisica;
    private Spinner<Double> spTecTac;
    private Spinner<Double> spTeorica;
    private Spinner<Double> spPsicologica;

    private CheckBox chkReducirCarga;
    private CheckBox chkAjustesManuales;

    private GridPane gridDias;

    private TableView<CompetenciaPlanificada> tablaCompetencias;
    private ObservableList<CompetenciaPlanificada> competenciasObservable;

    private TextField txtNombreCompetencia;
    private ComboBox<TipoCompetencia> cbTipoCompetencia;
    private DatePicker dpCompetenciaInicio;
    private DatePicker dpCompetenciaFin;
    private TextField txtFaseCompetencia;
    private TextField txtSedeCompetencia;
    private TextField txtObjetivoCompetencia;
    private Spinner<Integer> spPrioridadCompetencia;
    private CheckBox chkCompetenciaClave;
    private TextArea txtObservacionesCompetencia;

    private Label lblResumen;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f7fb;");

        root.setTop(crearEncabezado());
        root.setCenter(crearContenido());
        root.setBottom(crearBarraInferior(stage));

        Scene scene = new Scene(root, 1350, 850);
        stage.setTitle("Configuración previa de planificación");
        stage.setScene(scene);
        stage.show();

        actualizarResumen();
    }

    private VBox crearEncabezado() {
        Label titulo = new Label("CONFIGURACIÓN PREVIA DEL PLAN GRÁFICO");
        titulo.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #08294a;");

        Label subtitulo = new Label("Define fechas, modelo, competencias, cargas y semana tipo antes de construir la matriz.");
        subtitulo.setStyle("-fx-font-size: 13px; -fx-text-fill: #5c6b7a;");

        VBox box = new VBox(4, titulo, subtitulo);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: white;");

        return box;
    }

    private ScrollPane crearContenido() {
        VBox contenido = new VBox(18);
        contenido.setPadding(new Insets(20));

        contenido.getChildren().addAll(
                crearTarjetaDatosGenerales(),
                crearTarjetaFechasModelo(),
                crearTarjetaCompetencias(),
                crearTarjetaDistribucionPeriodos(),
                crearTarjetaDistribucionContenidos(),
                crearTarjetaSemanaEntrenamiento(),
                crearTarjetaReglas()
        );

        ScrollPane scroll = new ScrollPane(contenido);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #f4f7fb; -fx-background-color: #f4f7fb;");

        return scroll;
    }

    private VBox crearTarjetaDatosGenerales() {
        txtNombreTemporada = new TextField(configuracion.getNombreTemporada());
        txtDeporte = new TextField(configuracion.getDeporte());
        txtCategoria = new TextField(configuracion.getCategoria());
        txtRama = new TextField(configuracion.getRama());
        txtNivel = new TextField(configuracion.getNivelCompetitivo());

        txtObjetivo = new TextArea(configuracion.getObjetivoGeneral());
        txtObjetivo.setPrefRowCount(3);
        txtObjetivo.setWrapText(true);

        GridPane grid = crearGridFormulario();
        grid.add(campo("Temporada", txtNombreTemporada), 0, 0);
        grid.add(campo("Deporte", txtDeporte), 1, 0);
        grid.add(campo("Categoría", txtCategoria), 2, 0);
        grid.add(campo("Rama", txtRama), 0, 1);
        grid.add(campo("Nivel competitivo", txtNivel), 1, 1);
        grid.add(campo("Objetivo general", txtObjetivo), 0, 2, 3, 1);

        return tarjeta("1. Datos generales del ciclo", grid);
    }

    private VBox crearTarjetaFechasModelo() {
        dpInicio = new DatePicker(configuracion.getFechaInicio());
        dpFin = new DatePicker(configuracion.getFechaFin());

        cbModelo = new ComboBox<>();
        cbModelo.getItems().addAll(
                "TRADICIONAL",
                "ATR",
                "BLOQUES",
                "ONDULANTE",
                "CONCURRENTE",
                "PERSONALIZADO"
        );
        cbModelo.setValue(configuracion.getModeloPeriodizacion());

        cbProgresion = new ComboBox<>();
        cbProgresion.getItems().addAll(
                "LINEAL",
                "ONDULANTE",
                "ESCALONADA",
                "ASCENDENTE",
                "DESCENDENTE",
                "PERSONALIZADA"
        );
        cbProgresion.setValue(configuracion.getTipoProgresionCarga());

        GridPane grid = crearGridFormulario();
        grid.add(campo("Fecha inicio", dpInicio), 0, 0);
        grid.add(campo("Fecha final", dpFin), 1, 0);
        grid.add(campo("Modelo de periodización", cbModelo), 2, 0);
        grid.add(campo("Progresión de carga", cbProgresion), 0, 1);

        return tarjeta("2. Fechas, calendario y modelo", grid);
    }

    private VBox crearTarjetaCompetencias() {
        competenciasObservable = FXCollections.observableArrayList(configuracion.getCompetencias());

        txtNombreCompetencia = new TextField();
        cbTipoCompetencia = new ComboBox<>();
        cbTipoCompetencia.getItems().addAll(TipoCompetencia.values());
        cbTipoCompetencia.setValue(TipoCompetencia.PRINCIPAL);

        dpCompetenciaInicio = new DatePicker();
        dpCompetenciaFin = new DatePicker();

        txtFaseCompetencia = new TextField();
        txtSedeCompetencia = new TextField();
        txtObjetivoCompetencia = new TextField();

        spPrioridadCompetencia = new Spinner<>(1, 5, 3);
        spPrioridadCompetencia.setEditable(true);

        chkCompetenciaClave = new CheckBox("Competencia clave");

        txtObservacionesCompetencia = new TextArea();
        txtObservacionesCompetencia.setPrefRowCount(2);
        txtObservacionesCompetencia.setWrapText(true);

        GridPane formulario = crearGridFormulario();
        formulario.add(campo("Nombre competencia", txtNombreCompetencia), 0, 0);
        formulario.add(campo("Tipo", cbTipoCompetencia), 1, 0);
        formulario.add(campo("Fecha inicio", dpCompetenciaInicio), 2, 0);
        formulario.add(campo("Fecha fin", dpCompetenciaFin), 3, 0);
        formulario.add(campo("Fase", txtFaseCompetencia), 0, 1);
        formulario.add(campo("Sede", txtSedeCompetencia), 1, 1);
        formulario.add(campo("Objetivo", txtObjetivoCompetencia), 2, 1);
        formulario.add(campo("Prioridad 1 a 5", spPrioridadCompetencia), 3, 1);
        formulario.add(chkCompetenciaClave, 0, 2);
        formulario.add(campo("Observaciones", txtObservacionesCompetencia), 1, 2, 3, 1);

        Button btnAgregar = new Button("Agregar competencia");
        Button btnEliminar = new Button("Eliminar seleccionada");
        Button btnLimpiar = new Button("Limpiar campos");

        btnAgregar.setStyle("-fx-background-color: #0875c9; -fx-text-fill: white; -fx-font-weight: bold;");
        btnEliminar.setStyle("-fx-background-color: #fbe4e6; -fx-text-fill: #8b1e2d; -fx-font-weight: bold;");
        btnLimpiar.setStyle("-fx-background-color: #dce8f5; -fx-text-fill: #08294a; -fx-font-weight: bold;");

        btnAgregar.setOnAction(e -> agregarCompetencia());
        btnEliminar.setOnAction(e -> eliminarCompetenciaSeleccionada());
        btnLimpiar.setOnAction(e -> limpiarFormularioCompetencia());

        HBox acciones = new HBox(10, btnAgregar, btnEliminar, btnLimpiar);
        acciones.setAlignment(Pos.CENTER_LEFT);

        tablaCompetencias = new TableView<>();
        tablaCompetencias.setItems(competenciasObservable);
        tablaCompetencias.setPrefHeight(230);
        tablaCompetencias.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<CompetenciaPlanificada, String> colNombre = new TableColumn<>("Competencia");
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));

        TableColumn<CompetenciaPlanificada, TipoCompetencia> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTipoCompetencia()));

        TableColumn<CompetenciaPlanificada, String> colInicio = new TableColumn<>("Inicio");
        colInicio.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getFechaInicio())));

        TableColumn<CompetenciaPlanificada, String> colFin = new TableColumn<>("Fin");
        colFin.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getFechaFin())));

        TableColumn<CompetenciaPlanificada, String> colFase = new TableColumn<>("Fase");
        colFase.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFase()));

        TableColumn<CompetenciaPlanificada, String> colSede = new TableColumn<>("Sede");
        colSede.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSede()));

        TableColumn<CompetenciaPlanificada, Integer> colPrioridad = new TableColumn<>("Prioridad");
        colPrioridad.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getPrioridad()).asObject());

        TableColumn<CompetenciaPlanificada, Boolean> colClave = new TableColumn<>("Clave");
        colClave.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isCompetenciaClave()));

        tablaCompetencias.getColumns().addAll(
                colNombre,
                colTipo,
                colInicio,
                colFin,
                colFase,
                colSede,
                colPrioridad,
                colClave
        );

        tablaCompetencias.setRowFactory(tv -> {
            TableRow<CompetenciaPlanificada> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    cargarCompetenciaEnFormulario(row.getItem());
                }
            });
            return row;
        });

        Label nota = new Label("Doble clic sobre una competencia para cargarla en el formulario. Si quieres modificarla, elimínala y vuelve a agregarla corregida.");
        nota.setStyle("-fx-text-fill: #5c6b7a;");

        VBox box = new VBox(12, formulario, acciones, tablaCompetencias, nota);

        return tarjeta("3. Calendario competitivo", box);
    }

    private VBox crearTarjetaDistribucionPeriodos() {
        spPrep = spinner(configuracion.getPorcentajePreparatorio());
        spComp = spinner(configuracion.getPorcentajeCompetitivo());
        spTrans = spinner(configuracion.getPorcentajeTransitorio());

        GridPane grid = crearGridFormulario();
        grid.add(campo("% Periodo preparatorio", spPrep), 0, 0);
        grid.add(campo("% Periodo competitivo", spComp), 1, 0);
        grid.add(campo("% Periodo transitorio", spTrans), 2, 0);

        Label nota = new Label("La suma debe ser 100 %. Esta distribución permitirá calcular semanas y periodos automáticamente.");
        nota.setStyle("-fx-text-fill: #5c6b7a;");

        VBox box = new VBox(10, grid, nota);

        return tarjeta("4. Distribución porcentual de periodos", box);
    }

    private VBox crearTarjetaDistribucionContenidos() {
        spFisica = spinner(configuracion.getPorcentajePreparacionFisica());
        spTecTac = spinner(configuracion.getPorcentajePreparacionTecnicoTactica());
        spTeorica = spinner(configuracion.getPorcentajePreparacionTeorica());
        spPsicologica = spinner(configuracion.getPorcentajePreparacionPsicologica());

        GridPane grid = crearGridFormulario();
        grid.add(campo("% Preparación física", spFisica), 0, 0);
        grid.add(campo("% Técnico-táctica", spTecTac), 1, 0);
        grid.add(campo("% Teórica", spTeorica), 2, 0);
        grid.add(campo("% Psicológica", spPsicologica), 0, 1);

        Label nota = new Label("Estos porcentajes servirán para repartir minutos y contenidos dentro de cada microciclo.");
        nota.setStyle("-fx-text-fill: #5c6b7a;");

        VBox box = new VBox(10, grid, nota);

        return tarjeta("5. Distribución de contenidos de entrenamiento", box);
    }

    private VBox crearTarjetaSemanaEntrenamiento() {
        gridDias = new GridPane();
        gridDias.setHgap(10);
        gridDias.setVgap(10);

        int col = 0;

        for (DayOfWeek dia : DayOfWeek.values()) {
            DiaEntrenamientoConfig diaConfig = configuracion.getDiasEntrenamiento().get(dia);

            CheckBox chkEntrena = new CheckBox("Entrena");
            chkEntrena.setSelected(diaConfig.isEntrena());

            Spinner<Integer> spHora = new Spinner<>(0, 23, diaConfig.getHoraInicio().getHour());
            Spinner<Integer> spMin = new Spinner<>(0, 59, diaConfig.getHoraInicio().getMinute());
            Spinner<Integer> spDuracion = new Spinner<>(30, 240, diaConfig.getDuracionMinutos(), 15);
            Spinner<Integer> spUnidades = new Spinner<>(1, 3, diaConfig.getUnidadesEntrenamiento());

            spHora.setEditable(true);
            spMin.setEditable(true);
            spDuracion.setEditable(true);
            spUnidades.setEditable(true);

            chkEntrena.selectedProperty().addListener((obs, old, val) -> {
                diaConfig.setEntrena(val);
                actualizarResumen();
            });

            spHora.valueProperty().addListener((obs, old, val) -> {
                diaConfig.setHoraInicio(LocalTime.of(val, spMin.getValue()));
                actualizarResumen();
            });

            spMin.valueProperty().addListener((obs, old, val) -> {
                diaConfig.setHoraInicio(LocalTime.of(spHora.getValue(), val));
                actualizarResumen();
            });

            spDuracion.valueProperty().addListener((obs, old, val) -> {
                diaConfig.setDuracionMinutos(val);
                actualizarResumen();
            });

            spUnidades.valueProperty().addListener((obs, old, val) -> {
                diaConfig.setUnidadesEntrenamiento(val);
                actualizarResumen();
            });

            VBox cardDia = new VBox(8);
            cardDia.setPadding(new Insets(12));
            cardDia.setPrefWidth(150);
            cardDia.setStyle("-fx-background-color: white; -fx-border-color: #d9e2ec; -fx-background-radius: 10; -fx-border-radius: 10;");

            Label lblDia = new Label(nombreDia(dia));
            lblDia.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #08294a;");

            HBox horaBox = new HBox(5, spHora, new Label(":"), spMin);
            horaBox.setAlignment(Pos.CENTER_LEFT);

            cardDia.getChildren().addAll(
                    lblDia,
                    chkEntrena,
                    new Label("Hora inicio"),
                    horaBox,
                    new Label("Duración min"),
                    spDuracion,
                    new Label("U.E"),
                    spUnidades
            );

            gridDias.add(cardDia, col, 0);
            col++;
        }

        return tarjeta("6. Semana tipo de entrenamiento", gridDias);
    }

    private VBox crearTarjetaReglas() {
        chkReducirCarga = new CheckBox("Reducir carga antes de competencias importantes");
        chkReducirCarga.setSelected(configuracion.isReducirCargaAntesCompetencia());

        chkAjustesManuales = new CheckBox("Permitir ajustes manuales después de generar el plan gráfico");
        chkAjustesManuales.setSelected(configuracion.isPermitirAjustesManuales());

        lblResumen = new Label();
        lblResumen.setStyle("-fx-font-size: 13px; -fx-text-fill: #08294a; -fx-font-weight: bold;");

        VBox box = new VBox(10, chkReducirCarga, chkAjustesManuales, lblResumen);

        return tarjeta("7. Reglas automáticas y resumen", box);
    }

    private HBox crearBarraInferior(Stage stage) {
        Button btnCancelar = new Button("Cancelar");
        Button btnGuardar = new Button("Guardar configuración");
        Button btnGenerar = new Button("Guardar y abrir plan gráfico");

        btnCancelar.setOnAction(e -> stage.close());

        btnGuardar.setOnAction(e -> guardarConfiguracion());

        btnGenerar.setOnAction(e -> {
            if (guardarConfiguracion()) {
                abrirPlanGrafico(stage);
            }
        });

        btnGuardar.setStyle("-fx-background-color: #dce8f5; -fx-text-fill: #08294a; -fx-font-weight: bold;");
        btnGenerar.setStyle("-fx-background-color: #0875c9; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox box = new HBox(12);
        box.setPadding(new Insets(14, 20, 14, 20));
        box.setAlignment(Pos.CENTER_RIGHT);
        box.setStyle("-fx-background-color: white;");
        box.getChildren().addAll(btnCancelar, btnGuardar, btnGenerar);

        return box;
    }

    private void agregarCompetencia() {
        if (!validarFormularioCompetencia()) {
            return;
        }

        CompetenciaPlanificada competencia = new CompetenciaPlanificada(
                txtNombreCompetencia.getText().trim(),
                cbTipoCompetencia.getValue(),
                dpCompetenciaInicio.getValue(),
                dpCompetenciaFin.getValue(),
                txtFaseCompetencia.getText().trim(),
                txtSedeCompetencia.getText().trim(),
                txtObjetivoCompetencia.getText().trim(),
                spPrioridadCompetencia.getValue(),
                chkCompetenciaClave.isSelected(),
                txtObservacionesCompetencia.getText().trim()
        );

        competenciasObservable.add(competencia);
        actualizarResumen();
        limpiarFormularioCompetencia();
    }

    private boolean validarFormularioCompetencia() {
        if (txtNombreCompetencia.getText() == null || txtNombreCompetencia.getText().trim().isEmpty()) {
            alerta("Competencia incompleta", "Debes escribir el nombre de la competencia.");
            return false;
        }

        if (cbTipoCompetencia.getValue() == null) {
            alerta("Competencia incompleta", "Debes seleccionar el tipo de competencia.");
            return false;
        }

        if (dpCompetenciaInicio.getValue() == null || dpCompetenciaFin.getValue() == null) {
            alerta("Fechas incompletas", "Debes seleccionar fecha de inicio y fecha final de la competencia.");
            return false;
        }

        if (dpCompetenciaFin.getValue().isBefore(dpCompetenciaInicio.getValue())) {
            alerta("Fechas inválidas", "La fecha final de la competencia no puede ser anterior a la fecha inicial.");
            return false;
        }

        if (dpInicio.getValue() != null && dpCompetenciaInicio.getValue().isBefore(dpInicio.getValue())) {
            alerta("Competencia fuera del ciclo", "La competencia inicia antes de la fecha inicial del ciclo.");
            return false;
        }

        if (dpFin.getValue() != null && dpCompetenciaFin.getValue().isAfter(dpFin.getValue())) {
            alerta("Competencia fuera del ciclo", "La competencia termina después de la fecha final del ciclo.");
            return false;
        }

        return true;
    }

    private void eliminarCompetenciaSeleccionada() {
        CompetenciaPlanificada seleccionada = tablaCompetencias.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            alerta("Sin selección", "Selecciona una competencia para eliminar.");
            return;
        }

        competenciasObservable.remove(seleccionada);
        actualizarResumen();
    }

    private void cargarCompetenciaEnFormulario(CompetenciaPlanificada competencia) {
        txtNombreCompetencia.setText(competencia.getNombre());
        cbTipoCompetencia.setValue(competencia.getTipoCompetencia());
        dpCompetenciaInicio.setValue(competencia.getFechaInicio());
        dpCompetenciaFin.setValue(competencia.getFechaFin());
        txtFaseCompetencia.setText(competencia.getFase());
        txtSedeCompetencia.setText(competencia.getSede());
        txtObjetivoCompetencia.setText(competencia.getObjetivo());
        spPrioridadCompetencia.getValueFactory().setValue(competencia.getPrioridad());
        chkCompetenciaClave.setSelected(competencia.isCompetenciaClave());
        txtObservacionesCompetencia.setText(competencia.getObservaciones());
    }

    private void limpiarFormularioCompetencia() {
        txtNombreCompetencia.clear();
        cbTipoCompetencia.setValue(TipoCompetencia.PRINCIPAL);
        dpCompetenciaInicio.setValue(null);
        dpCompetenciaFin.setValue(null);
        txtFaseCompetencia.clear();
        txtSedeCompetencia.clear();
        txtObjetivoCompetencia.clear();
        spPrioridadCompetencia.getValueFactory().setValue(3);
        chkCompetenciaClave.setSelected(false);
        txtObservacionesCompetencia.clear();
    }

    private boolean guardarConfiguracion() {
        if (!validar()) {
            return false;
        }

        configuracion.setNombreTemporada(txtNombreTemporada.getText());
        configuracion.setDeporte(txtDeporte.getText());
        configuracion.setCategoria(txtCategoria.getText());
        configuracion.setRama(txtRama.getText());
        configuracion.setNivelCompetitivo(txtNivel.getText());
        configuracion.setObjetivoGeneral(txtObjetivo.getText());

        configuracion.setFechaInicio(dpInicio.getValue());
        configuracion.setFechaFin(dpFin.getValue());

        configuracion.setModeloPeriodizacion(cbModelo.getValue());
        configuracion.setTipoProgresionCarga(cbProgresion.getValue());

        configuracion.setPorcentajePreparatorio(spPrep.getValue());
        configuracion.setPorcentajeCompetitivo(spComp.getValue());
        configuracion.setPorcentajeTransitorio(spTrans.getValue());

        configuracion.setPorcentajePreparacionFisica(spFisica.getValue());
        configuracion.setPorcentajePreparacionTecnicoTactica(spTecTac.getValue());
        configuracion.setPorcentajePreparacionTeorica(spTeorica.getValue());
        configuracion.setPorcentajePreparacionPsicologica(spPsicologica.getValue());

        configuracion.setReducirCargaAntesCompetencia(chkReducirCarga.isSelected());
        configuracion.setPermitirAjustesManuales(chkAjustesManuales.isSelected());

        configuracion.getCompetencias().clear();
        configuracion.getCompetencias().addAll(competenciasObservable);

        ConfiguracionPlanificacionStore.setConfiguracionActiva(configuracion);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Configuración guardada");
        alert.setHeaderText("La configuración previa fue guardada correctamente.");
        alert.showAndWait();

        return true;
    }

    private boolean validar() {
        if (dpInicio.getValue() == null || dpFin.getValue() == null) {
            alerta("Fechas incompletas", "Debes seleccionar fecha de inicio y fecha final.");
            return false;
        }

        if (dpFin.getValue().isBefore(dpInicio.getValue())) {
            alerta("Fechas inválidas", "La fecha final no puede ser anterior a la fecha inicial.");
            return false;
        }

        double sumaPeriodos = spPrep.getValue() + spComp.getValue() + spTrans.getValue();

        if (Math.abs(sumaPeriodos - 100) > 0.01) {
            alerta("Distribución incorrecta", "La suma de los periodos debe ser 100 %.");
            return false;
        }

        double sumaContenidos = spFisica.getValue() + spTecTac.getValue() + spTeorica.getValue() + spPsicologica.getValue();

        if (Math.abs(sumaContenidos - 100) > 0.01) {
            alerta("Distribución incorrecta", "La suma de los contenidos debe ser 100 %.");
            return false;
        }

        boolean existeDiaEntrenamiento = configuracion.getDiasEntrenamiento()
                .values()
                .stream()
                .anyMatch(DiaEntrenamientoConfig::isEntrena);

        if (!existeDiaEntrenamiento) {
            alerta("Semana sin entrenamiento", "Debes seleccionar al menos un día de entrenamiento.");
            return false;
        }

        for (CompetenciaPlanificada competencia : competenciasObservable) {
            if (competencia.getFechaInicio().isBefore(dpInicio.getValue())
                    || competencia.getFechaFin().isAfter(dpFin.getValue())) {
                alerta(
                        "Competencia fuera del ciclo",
                        "La competencia " + competencia.getNombre() + " está fuera de las fechas del ciclo."
                );
                return false;
            }
        }

        return true;
    }

    private void abrirPlanGrafico(Stage stageActual) {
        try {
            PlanGraficoView view = new PlanGraficoView();
            Stage nuevoStage = new Stage();
            view.start(nuevoStage);
            stageActual.close();
        } catch (Exception ex) {
            alerta("Error", "No se pudo abrir el plan gráfico: " + ex.getMessage());
        }
    }

    private GridPane crearGridFormulario() {
        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(14);
        return grid;
    }

    private VBox campo(String titulo, Control control) {
        Label label = new Label(titulo);
        label.setStyle("-fx-font-size: 12px; -fx-text-fill: #5c6b7a; -fx-font-weight: bold;");
        control.setPrefWidth(260);
        return new VBox(5, label, control);
    }

    private VBox tarjeta(String titulo, javafx.scene.Node contenido) {
        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #08294a;");

        VBox box = new VBox(14, lblTitulo, contenido);
        box.setPadding(new Insets(18));
        box.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-border-radius: 14; -fx-border-color: #e1e7ef;");

        return box;
    }

    private Spinner<Double> spinner(double valor) {
        Spinner<Double> spinner = new Spinner<>(0.0, 100.0, valor, 1.0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, old, val) -> actualizarResumen());
        return spinner;
    }

    private void actualizarResumen() {
        if (lblResumen == null) {
            return;
        }

        int competenciasTotales = competenciasObservable == null ? configuracion.getCompetencias().size() : competenciasObservable.size();

        long competenciasClave = competenciasObservable == null
                ? configuracion.getCompetencias().stream().filter(CompetenciaPlanificada::isCompetenciaClave).count()
                : competenciasObservable.stream().filter(CompetenciaPlanificada::isCompetenciaClave).count();

        lblResumen.setText(
                "Sesiones por semana: " + configuracion.getSesionesSemanales()
                        + "   |   Minutos por semana: " + configuracion.getMinutosSemanales()
                        + "   |   Competencias: " + competenciasTotales
                        + "   |   Competencias clave: " + competenciasClave
        );
    }

    private String nombreDia(DayOfWeek dia) {
        return dia.getDisplayName(TextStyle.FULL, new Locale("es", "MX")).toUpperCase();
    }

    private void alerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(mensaje);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}