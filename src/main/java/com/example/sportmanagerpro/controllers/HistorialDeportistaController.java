package com.example.sportmanagerpro.controllers;

import com.example.sportmanagerpro.models.Deportista;
import com.example.sportmanagerpro.models.HistorialDeportistaResumen;
import com.example.sportmanagerpro.services.DeportistaService;
import com.example.sportmanagerpro.services.PdfHistorialDeportistaService;
import com.example.sportmanagerpro.services.ReporteService;
import com.example.sportmanagerpro.utils.AlertUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.stage.FileChooser;

import java.io.File;

public class HistorialDeportistaController {

    @FXML private ComboBox<Deportista> cmbDeportista;

    @FXML private Label lblNombre;
    @FXML private Label lblEdad;
    @FXML private Label lblPosicion;
    @FXML private Label lblPierna;

    @FXML private Label lblTotalEntrenamientos;
    @FXML private Label lblAsistencias;
    @FXML private Label lblFaltas;
    @FXML private Label lblRetardos;
    @FXML private Label lblPartidos;
    @FXML private Label lblMinutos;
    @FXML private Label lblGoles;
    @FXML private Label lblAsistenciasPartido;

    private final DeportistaService deportistaService = new DeportistaService();
    private final ReporteService reporteService = new ReporteService();

    @FXML
    public void initialize() {
        cargarDeportistas();
        limpiarDatos();
    }

    @FXML
    private void cargarHistorial() {
        Deportista deportista = cmbDeportista.getValue();

        if (deportista == null) {
            limpiarDatos();
            return;
        }

        lblNombre.setText(deportista.getNombreCompleto());
        lblEdad.setText(String.valueOf(deportistaService.calcularEdad(deportista.getFechaNacimiento())));
        lblPosicion.setText(deportista.getPosicionPrincipal());
        lblPierna.setText(deportista.getPiernaDominante());

        HistorialDeportistaResumen resumen =
                reporteService.obtenerResumenDeportista(deportista.getIdDeportista());

        lblTotalEntrenamientos.setText(String.valueOf(resumen.getTotalEntrenamientos()));
        lblAsistencias.setText(String.valueOf(resumen.getAsistencias()));
        lblFaltas.setText(String.valueOf(resumen.getFaltas()));
        lblRetardos.setText(String.valueOf(resumen.getRetardos()));
        lblPartidos.setText(String.valueOf(resumen.getPartidosJugados()));
        lblMinutos.setText(String.valueOf(resumen.getMinutosJugados()));
        lblGoles.setText(String.valueOf(resumen.getGoles()));
        lblAsistenciasPartido.setText(String.valueOf(resumen.getAsistenciasPartido()));
    }

    private void cargarDeportistas() {
        cmbDeportista.setItems(
                FXCollections.observableArrayList(deportistaService.listarTodas())
        );

        cmbDeportista.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Deportista deportista, boolean empty) {
                super.updateItem(deportista, empty);
                setText(empty || deportista == null ? null : deportista.getNombreCompleto());
            }
        });

        cmbDeportista.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Deportista deportista, boolean empty) {
                super.updateItem(deportista, empty);
                setText(empty || deportista == null ? null : deportista.getNombreCompleto());
            }
        });
    }

    private void limpiarDatos() {
        lblNombre.setText("-");
        lblEdad.setText("-");
        lblPosicion.setText("-");
        lblPierna.setText("-");

        lblTotalEntrenamientos.setText("0");
        lblAsistencias.setText("0");
        lblFaltas.setText("0");
        lblRetardos.setText("0");
        lblPartidos.setText("0");
        lblMinutos.setText("0");
        lblGoles.setText("0");
        lblAsistenciasPartido.setText("0");
    }

    private final PdfHistorialDeportistaService pdfService =
            new PdfHistorialDeportistaService();

    @FXML
    private void exportarPdf() {

        Deportista deportista = cmbDeportista.getValue();

        if (deportista == null) {

            AlertUtils.mostrarError(
                    "Debe seleccionar una deportista."
            );

            return;
        }

        try {

            HistorialDeportistaResumen resumen =
                    reporteService.obtenerResumenDeportista(
                            deportista.getIdDeportista()
                    );

            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle(
                    "Guardar historial PDF"
            );

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "PDF",
                            "*.pdf"
                    )
            );

            fileChooser.setInitialFileName(
                    "historial_"
                            + deportista.getNombreCompleto()
                            .replace(" ", "_")
                            + ".pdf"
            );

            File archivo = fileChooser.showSaveDialog(
                    cmbDeportista.getScene().getWindow()
            );

            if (archivo == null) {
                return;
            }

            pdfService.generarPdf(
                    deportista,
                    resumen,
                    archivo
            );

            AlertUtils.mostrarInformacion(
                    "PDF generado correctamente."
            );

        } catch (Exception e) {

            AlertUtils.mostrarError(
                    e.getMessage()
            );
        }
    }
}