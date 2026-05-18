package com.example.sportmanagerpro.planificacion.controller;

import com.example.sportmanagerpro.planificacion.engine.DistribucionTiemposEngine;
import com.example.sportmanagerpro.planificacion.enums.TipoBloquePlanificacion;
import com.example.sportmanagerpro.planificacion.enums.TipoMicrociclo;
import com.example.sportmanagerpro.planificacion.enums.TipoPeriodizacion;
import com.example.sportmanagerpro.planificacion.model.BloqueMacrociclo;
import com.example.sportmanagerpro.planificacion.model.BloquePlanificacion;
import com.example.sportmanagerpro.planificacion.model.MicrocicloPlanificado;
import com.example.sportmanagerpro.planificacion.model.SemanaPlanificacion;
import com.example.sportmanagerpro.planificacion.service.BloquesPorPeriodizacionService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

/**
 * Controlador de la vista de distribución de tiempos.
 * Permite capturar microciclos desde la interfaz y calcular automáticamente
 * los tiempos de preparación física y técnico-táctica.
 */


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.List;

import static com.example.sportmanagerpro.planificacion.enums.TipoBloquePlanificacion.*;
import static com.example.sportmanagerpro.planificacion.enums.TipoBloquePlanificacion.CARGA;
import static com.example.sportmanagerpro.planificacion.enums.TipoBloquePlanificacion.IMPACTO;
import static com.example.sportmanagerpro.planificacion.enums.TipoBloquePlanificacion.PRECOMPETITIVO;
import static com.example.sportmanagerpro.planificacion.enums.TipoBloquePlanificacion.RECUPERACION;
import static com.example.sportmanagerpro.planificacion.enums.TipoMicrociclo.*;

public class DistribucionTiemposController {


    @FXML private ComboBox<TipoPeriodizacion> cmbTipoPeriodizacion;
    @FXML private TableView<BloqueMacrociclo> tablaBloques;
    @FXML private TableColumn<BloqueMacrociclo, TipoBloquePlanificacion> colTipoBloque;
    @FXML private TableColumn<BloqueMacrociclo, Integer> colDuracionBloque;
    @FXML private TableColumn<BloqueMacrociclo, Double> colPorcentajeFisicoBloque;
    @FXML private TableColumn<BloqueMacrociclo, Double> colPorcentajeTecTacBloque;

    @FXML private TextField txtNombreBloque;
    @FXML private TextField txtPorcentajeFisico;
    @FXML private TextField txtPorcentajeTecnicoTactico;
    @FXML private GridPane gridPlanificacion;

    @FXML private DatePicker dpInicioMacrociclo;
    @FXML private DatePicker dpFinMacrociclo;

    private final ObservableList<SemanaPlanificacion> semanas =
            FXCollections.observableArrayList();

    private final ObservableList<MicrocicloPlanificado> microciclos =
            FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        /*txtPorcentajeFisico.setText("70");
        txtNombreBloque.setText("Acumulación");
        txtPorcentajeTecnicoTactico.setText("30");*/

        microciclos.addAll(
                new MicrocicloPlanificado(1, LocalDate.now(),TipoMicrociclo.CARGA, 60, 5, 120),
                new MicrocicloPlanificado(2, LocalDate.now(), TipoMicrociclo.CARGA, 70, 5, 120),
                new MicrocicloPlanificado(3, LocalDate.now(), TipoMicrociclo.CARGA, 75, 5, 120),
                new MicrocicloPlanificado(4, LocalDate.now(), TipoMicrociclo.RECUPERACION, 50, 5, 120)
        );

        cmbTipoPeriodizacion.getItems().setAll(TipoPeriodizacion.values());
        cmbTipoPeriodizacion.setValue(TipoPeriodizacion.ATR);

        cmbTipoPeriodizacion.setOnAction(e -> actualizarBloquesPermitidos());

        dibujarMatriz();
    }

    @FXML
    private void generarSemanasMacrociclo() {
        semanas.clear();
        microciclos.clear();

        LocalDate inicio = dpInicioMacrociclo.getValue();
        LocalDate fin = dpFinMacrociclo.getValue();

        if (inicio == null || fin == null || fin.isBefore(inicio)) {
            mostrarAlerta("Fechas inválidas", "Selecciona una fecha de inicio y fin correcta.");
            return;
        }

        int numeroSemana = 1;
        LocalDate semanaInicio = inicio;

        while (!semanaInicio.isAfter(fin)) {
            LocalDate semanaFin = semanaInicio.plusDays(6);

            if (semanaFin.isAfter(fin)) {
                semanaFin = fin;
            }

            semanas.add(new SemanaPlanificacion(numeroSemana, semanaInicio, semanaFin));

            microciclos.add(
                    new MicrocicloPlanificado(
                            numeroSemana,
                            semanaInicio,
                            TipoMicrociclo.CARGA,
                            70,
                            5,
                            120
                    )
            );

            numeroSemana++;
            semanaInicio = semanaInicio.plusWeeks(1);
        }

        dibujarMatriz();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML private ComboBox<TipoBloquePlanificacion> cmbBloqueAsignar;
    @FXML private TextField txtSemanaInicioBloque;
    @FXML private TextField txtSemanaFinBloque;

    @FXML
    private void asignarBloqueARango() {
        TipoBloquePlanificacion bloque = cmbBloqueAsignar.getValue();

        if (bloque == null) {
            mostrarAlerta("Bloque no seleccionado", "Selecciona un tipo de bloque.");
            return;
        }

        int inicio = Integer.parseInt(txtSemanaInicioBloque.getText());
        int fin = Integer.parseInt(txtSemanaFinBloque.getText());

        for (SemanaPlanificacion semana : semanas) {
            if (semana.getNumeroSemana() >= inicio && semana.getNumeroSemana() <= fin) {
                semana.setNombreBloque(bloque.name());
            }
        }

        dibujarMatriz();
    }
    @FXML
    private void calcularDistribucion() {
        BloquePlanificacion bloque = new BloquePlanificacion(
                txtNombreBloque.getText(),
                Double.parseDouble(txtPorcentajeFisico.getText()),
                Double.parseDouble(txtPorcentajeTecnicoTactico.getText())
        );

        for (MicrocicloPlanificado micro : microciclos) {
            bloque.agregarMicrociclo(micro);
        }

        new DistribucionTiemposEngine().calcular(bloque);

        dibujarMatriz();
    }

    @FXML
    private void agregarMicrociclo() {
        int semana = microciclos.size() + 1;

        microciclos.add(
                new MicrocicloPlanificado(
                        semana,
                        LocalDate.now(),
                        TipoMicrociclo.CARGA,
                        70,
                        5,
                        120
                )
        );

        dibujarMatriz();
    }

    /**
     * Actualiza los tipos de bloque disponibles cuando cambia
     * el tipo de periodización seleccionada.
     */
    private void actualizarBloquesPermitidos() {

        TipoPeriodizacion tipoPeriodizacion =
                cmbTipoPeriodizacion.getValue();

        if (tipoPeriodizacion == null) {
            return;
        }

        BloquesPorPeriodizacionService service =
                new BloquesPorPeriodizacionService();

        List<TipoBloquePlanificacion> bloquesPermitidos =
                service.obtenerBloquesPermitidos(tipoPeriodizacion);

        tablaBloques.getItems().clear();

        if (!bloquesPermitidos.isEmpty()) {
            tablaBloques.getItems().add(
                    new BloqueMacrociclo(
                            bloquesPermitidos.get(0),
                            4,
                            70,
                            30
                    )
            );
        }
        cmbBloqueAsignar.getItems().setAll(bloquesPermitidos);
        configurarComboBloques(bloquesPermitidos);
    }

    /**
     * Configura la columna de tipo de bloque para que solo muestre
     * los bloques permitidos según la periodización seleccionada.
     */
    private void configurarComboBloques(
            List<TipoBloquePlanificacion> bloquesPermitidos) {

        colTipoBloque.setCellFactory(
                ComboBoxTableCell.forTableColumn(
                        FXCollections.observableArrayList(bloquesPermitidos)
                )
        );

        colTipoBloque.setOnEditCommit(event ->
                event.getRowValue().setTipoBloque(event.getNewValue())
        );
    }
    @FXML
    private void eliminarUltimoMicrociclo() {
        if (!microciclos.isEmpty()) {
            microciclos.remove(microciclos.size() - 1);
            dibujarMatriz();
        }
    }

    private void dibujarMatriz() {
        gridPlanificacion.getChildren().clear();

        agregarCelda(0, 0, "Mes", true);
        agregarCelda(0, 1, "Fechas", true);
        agregarCelda(0, 2, "Semanas", true);
        agregarCelda(0, 3, "Bloques", true);
        agregarCelda(0, 4, "Microciclo", true);
        agregarCelda(0, 5, "% Microciclo", true);
        agregarCelda(0, 6, "U.E / Semana", true);
        agregarCelda(0, 7, "Minutos entreno", true);
        agregarCelda(0, 8, "Prep. Física", true);
        agregarCelda(0, 9, "Aeróbica", false);
        agregarCelda(0, 10, "Fuerza", false);
        agregarCelda(0, 11, "Prep. Tec-Tac", true);
        agregarCelda(0, 12, "Complejos I-II", false);

        int totalColumnas = Math.min(microciclos.size(), semanas.size());

        for (int i = 0; i < totalColumnas; i++) {
            MicrocicloPlanificado micro = microciclos.get(i);
            SemanaPlanificacion semana = semanas.get(i);

            int col = i + 1;

            agregarCelda(
                    col,
                    0,
                    semana.getFechaInicio().getMonth().toString(),
                    true
            );

            agregarCelda(
                    col,
                    1,
                    semana.getFechaInicio().getDayOfMonth()
                            + "-"
                            + semana.getFechaFin().getDayOfMonth(),
                    false
            );

            agregarCelda(col, 2, String.valueOf(semana.getNumeroSemana()), true);
            agregarCelda(col, 3, semana.getNombreBloque(), true);
            agregarCelda(col, 4, abreviarMicrociclo(micro.getTipoMicrociclo()), false);

            agregarCampo(col, 5, micro, "porcentaje");
            agregarCampo(col, 6, micro, "ue");
            agregarCampo(col, 7, micro, "minutos");

            agregarCelda(col, 8, "", true);
            agregarCelda(col, 9, String.format("%.0f", micro.getMinutosPreparacionFisica() * 0.45), false);
            agregarCelda(col, 10, String.format("%.0f", micro.getMinutosPreparacionFisica() * 0.55), false);
            agregarCelda(col, 11, "", true);
            agregarCelda(col, 12, String.format("%.0f", micro.getMinutosPreparacionTecnicoTactica()), false);
        }
    }

    private void agregarCampo(int col, int row, MicrocicloPlanificado micro, String tipo) {
        TextField campo = new TextField();

        if (tipo.equals("porcentaje")) {
            campo.setText(String.valueOf(micro.getPorcentajeCarga()));
        } else if (tipo.equals("ue")) {
            campo.setText(String.valueOf(micro.getUnidadesEntrenamientoSemana()));
        } else {
            campo.setText(String.valueOf(micro.getMinutosPorUnidad()));
        }

        campo.setPrefWidth(90);
        campo.setStyle("-fx-border-color: black; -fx-alignment: center;");

        campo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tipo.equals("porcentaje")) {
                    micro.setPorcentajeCarga(Double.parseDouble(campo.getText()));
                } else if (tipo.equals("ue")) {
                    micro.setUnidadesEntrenamientoSemana(Integer.parseInt(campo.getText()));
                } else {
                    micro.setMinutosPorUnidad(Integer.parseInt(campo.getText()));
                }
            }
        });

        gridPlanificacion.add(campo, col, row);
    }

    private void agregarCelda(int col, int row, String texto, boolean encabezado) {
        Label label = new Label(texto);
        label.setMinWidth(col == 0 ? 150 : 90);
        label.setMinHeight(32);
        label.setStyle(
                "-fx-border-color: black;" +
                        "-fx-alignment: center;" +
                        "-fx-padding: 4;" +
                        (encabezado
                                ? "-fx-font-weight: bold; -fx-background-color: #e6e6e6;"
                                : "-fx-background-color: white;")
        );

        gridPlanificacion.add(label, col, row);
    }

    private String abreviarMicrociclo(TipoMicrociclo tipo) {
        return switch (tipo) {
            case CARGA -> "C";
            case IMPACTO -> "I";
            case RECUPERACION -> "R";
            case PRECOMPETITIVO -> "PC";
            case COMPETENCIA -> "COMP.";
            case AJUSTE -> "A";
          //  case ACTIVACION -> "ACT.";
        };
    }

    /**
     * Agrega un bloque al macrociclo según el tipo de periodización seleccionado.
     */
    @FXML
    private void agregarBloque() {
        TipoPeriodizacion tipoPeriodizacion = cmbTipoPeriodizacion.getValue();

        if (tipoPeriodizacion == null) {
            return;
        }

        List<TipoBloquePlanificacion> bloquesPermitidos =
                new BloquesPorPeriodizacionService()
                        .obtenerBloquesPermitidos(tipoPeriodizacion);

        if (bloquesPermitidos.isEmpty()) {
            return;
        }

        tablaBloques.getItems().add(
                new BloqueMacrociclo(
                        bloquesPermitidos.get(0),
                        4,
                        70,
                        30
                )
        );
    }

    /**
     * Configura la tabla superior donde se capturan los bloques del macrociclo.
     */
    private void configurarTablaBloques() {
        tablaBloques.setEditable(true);

        colTipoBloque.setCellValueFactory(cell ->
                cell.getValue().tipoBloqueProperty());

        colDuracionBloque.setCellValueFactory(cell ->
                cell.getValue().duracionSemanasProperty().asObject());

        colPorcentajeFisicoBloque.setCellValueFactory(cell ->
                cell.getValue().porcentajeFisicoProperty().asObject());

        colPorcentajeTecTacBloque.setCellValueFactory(cell ->
                cell.getValue().porcentajeTecnicoTacticoProperty().asObject());

        colDuracionBloque.setCellFactory(
                TextFieldTableCell.forTableColumn(new IntegerStringConverter())
        );

        colPorcentajeFisicoBloque.setCellFactory(
                TextFieldTableCell.forTableColumn(new DoubleStringConverter())
        );

        colPorcentajeTecTacBloque.setCellFactory(
                TextFieldTableCell.forTableColumn(new DoubleStringConverter())
        );

        colDuracionBloque.setOnEditCommit(event ->
                event.getRowValue().setDuracionSemanas(event.getNewValue())
        );

        colPorcentajeFisicoBloque.setOnEditCommit(event ->
                event.getRowValue().setPorcentajeFisico(event.getNewValue())
        );

        colPorcentajeTecTacBloque.setOnEditCommit(event ->
                event.getRowValue().setPorcentajeTecnicoTactico(event.getNewValue())
        );
    }


    /**
     * Genera automáticamente los microciclos de la matriz
     * a partir de los bloques capturados en la tabla superior.
     */
    @FXML
    private void generarMatriz() {
        microciclos.clear();

        int semanaGlobal = 1;
        LocalDate fechaInicio = LocalDate.now();

        for (BloqueMacrociclo bloque : tablaBloques.getItems()) {
            for (int i = 0; i < bloque.getDuracionSemanas(); i++) {

                TipoMicrociclo tipoMicrociclo = obtenerTipoMicrocicloSugerido(
                        bloque.getTipoBloque(),
                        i,
                        bloque.getDuracionSemanas()
                );

                double porcentajeCarga = obtenerPorcentajeSugerido(tipoMicrociclo);

                microciclos.add(
                        new MicrocicloPlanificado(
                                semanaGlobal,
                                fechaInicio.plusWeeks(semanaGlobal - 1),
                                tipoMicrociclo,
                                porcentajeCarga,
                                5,
                                120
                        )
                );

                semanaGlobal++;
            }
        }

        dibujarMatriz();
    }


    /**
     * Sugiere el tipo de microciclo según el tipo de bloque.
     */
    private TipoMicrociclo obtenerTipoMicrocicloSugerido(
            TipoBloquePlanificacion bloque,
            int indiceSemana,
            int totalSemanas) {

        return switch (bloque) {
            case ACUMULACION, PREPARACION_GENERAL, PREPARACION_ESPECIAL -> {
                if (indiceSemana == totalSemanas - 1) {
                    yield TipoMicrociclo.RECUPERACION;
                }
                yield TipoMicrociclo.CARGA;
            }

            case TRANSFORMACION, CARGA, IMPACTO -> {
                if (indiceSemana == totalSemanas - 1) {
                    yield TipoMicrociclo.RECUPERACION;
                }
                yield TipoMicrociclo.IMPACTO;
            }

            case REALIZACION, PRECOMPETITIVO -> TipoMicrociclo.PRECOMPETITIVO;

            case COMPETITIVO -> COMPETENCIA;

            case TRANSITORIO, RECUPERACION -> TipoMicrociclo.RECUPERACION;

            case PERSONALIZADO -> TipoMicrociclo.CARGA;
        };
    }

        /**
         * Asigna un porcentaje de carga inicial según el tipo de microciclo.
         */
        private double obtenerPorcentajeSugerido(TipoMicrociclo tipoMicrociclo) {
            return switch (tipoMicrociclo) {
                case CARGA -> 70;
                case IMPACTO -> 90;
                case RECUPERACION -> 50;
                case PRECOMPETITIVO -> 40;
                case COMPETENCIA -> 25;
                case AJUSTE -> 60;
             //   case ACTIVACION -> 35;
            };
        }
    }


