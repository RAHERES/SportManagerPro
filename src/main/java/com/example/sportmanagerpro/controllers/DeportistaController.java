package com.example.sportmanagerpro.controllers;


import com.example.sportmanagerpro.models.Categoria;
import com.example.sportmanagerpro.models.Deportista;
import com.example.sportmanagerpro.services.CategoriaService;
import com.example.sportmanagerpro.services.DeportistaService;
import com.example.sportmanagerpro.utils.AlertUtils;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DeportistaController {

    @FXML private TextField txtNombreCompleto;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private ComboBox<String> cmbSexo;
    @FXML private TextField txtGradoGrupo;
    @FXML private ComboBox<Categoria> cmbCategoria;
    @FXML private TextField txtPosicionPrincipal;
    @FXML private TextField txtPosicionSecundaria;
    @FXML private ComboBox<String> cmbPiernaDominante;
    @FXML private TextField txtTelefonoTutor;
    @FXML private CheckBox chkActivo;
    @FXML private TextArea txtObservaciones;

    @FXML private TableView<Deportista> tablaDeportistas;
    @FXML private TableColumn<Deportista, Integer> colId;
    @FXML private TableColumn<Deportista, String> colNombre;
    @FXML private TableColumn<Deportista, Integer> colEdad;
    @FXML private TableColumn<Deportista, String> colSexo;
    @FXML private TableColumn<Deportista, Integer> colCategoria;
    @FXML private TableColumn<Deportista, String> colPosicion;
    @FXML private TableColumn<Deportista, String> colPierna;
    @FXML private TableColumn<Deportista, Boolean> colActivo;

    private final DeportistaService deportistaService = new DeportistaService();
    private final CategoriaService categoriaService = new CategoriaService();

    private Deportista deportistaSeleccionada;

    @FXML
    public void initialize() {
        configurarCombos();
        configurarTabla();
        cargarCategorias();
        cargarDeportistas();

        tablaDeportistas.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionada) -> cargarDeportistaSeleccionada(seleccionada)
        );
    }

    @FXML
    private void guardarDeportista() {
        try {
            Deportista deportista = leerFormulario();
            deportistaService.guardar(deportista);
            AlertUtils.mostrarInformacion("Deportista guardada correctamente.");
            limpiarFormulario();
            cargarDeportistas();
        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }

    @FXML
    private void actualizarDeportista() {
        if (deportistaSeleccionada == null) {
            AlertUtils.mostrarError("Debe seleccionar una deportista.");
            return;
        }

        try {
            Deportista deportista = leerFormulario();
            deportista.setIdDeportista(deportistaSeleccionada.getIdDeportista());
            deportistaService.actualizar(deportista);
            AlertUtils.mostrarInformacion("Deportista actualizada correctamente.");
            limpiarFormulario();
            cargarDeportistas();
        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }

    @FXML
    private void desactivarDeportista() {
        if (deportistaSeleccionada == null) {
            AlertUtils.mostrarError("Debe seleccionar una deportista.");
            return;
        }

        deportistaService.desactivar(deportistaSeleccionada.getIdDeportista());
        AlertUtils.mostrarInformacion("Deportista desactivada correctamente.");
        limpiarFormulario();
        cargarDeportistas();
    }

    @FXML
    private void limpiarFormulario() {
        deportistaSeleccionada = null;
        txtNombreCompleto.clear();
        dpFechaNacimiento.setValue(null);
        cmbSexo.getSelectionModel().clearSelection();
        txtGradoGrupo.clear();
        cmbCategoria.getSelectionModel().clearSelection();
        txtPosicionPrincipal.clear();
        txtPosicionSecundaria.clear();
        cmbPiernaDominante.getSelectionModel().clearSelection();
        txtTelefonoTutor.clear();
        chkActivo.setSelected(true);
        txtObservaciones.clear();
        tablaDeportistas.getSelectionModel().clearSelection();
    }

    private void configurarCombos() {
        cmbSexo.setItems(FXCollections.observableArrayList("Femenino", "Masculino"));
        cmbPiernaDominante.setItems(FXCollections.observableArrayList("Derecha", "Izquierda", "Ambas"));
    }

    private void configurarTabla() {
        colId.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getIdDeportista()).asObject());

        colNombre.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombreCompleto()));

        colEdad.setCellValueFactory(data ->
                new SimpleIntegerProperty(
                        deportistaService.calcularEdad(data.getValue().getFechaNacimiento())
                ).asObject());

        colSexo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSexo()));

        colCategoria.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getIdCategoria()).asObject());

        colPosicion.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPosicionPrincipal()));

        colPierna.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPiernaDominante()));

        colActivo.setCellValueFactory(data ->
                new SimpleBooleanProperty(data.getValue().isActivo()).asObject());
    }

    private void cargarCategorias() {
        cmbCategoria.setItems(FXCollections.observableArrayList(categoriaService.listarTodas()));

        cmbCategoria.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Categoria categoria, boolean empty) {
                super.updateItem(categoria, empty);
                setText(empty || categoria == null ? null : categoria.getNombre());
            }
        });

        cmbCategoria.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Categoria categoria, boolean empty) {
                super.updateItem(categoria, empty);
                setText(empty || categoria == null ? null : categoria.getNombre());
            }
        });
    }

    private void cargarDeportistas() {
        tablaDeportistas.setItems(FXCollections.observableArrayList(deportistaService.listarTodas()));
    }

    private Deportista leerFormulario() {
        Categoria categoria = cmbCategoria.getValue();

        Deportista d = new Deportista();
        d.setNombreCompleto(txtNombreCompleto.getText());
        d.setFechaNacimiento(dpFechaNacimiento.getValue());
        d.setSexo(cmbSexo.getValue());
        d.setGradoGrupo(txtGradoGrupo.getText());
        d.setIdCategoria(categoria != null ? categoria.getIdCategoria() : 0);
        d.setPosicionPrincipal(txtPosicionPrincipal.getText());
        d.setPosicionSecundaria(txtPosicionSecundaria.getText());
        d.setPiernaDominante(cmbPiernaDominante.getValue());
        d.setTelefonoTutor(txtTelefonoTutor.getText());
        d.setActivo(chkActivo.isSelected());
        d.setObservaciones(txtObservaciones.getText());

        return d;
    }

    private void cargarDeportistaSeleccionada(Deportista d) {
        if (d == null) {
            return;
        }

        deportistaSeleccionada = d;

        txtNombreCompleto.setText(d.getNombreCompleto());
        dpFechaNacimiento.setValue(d.getFechaNacimiento());
        cmbSexo.setValue(d.getSexo());
        txtGradoGrupo.setText(d.getGradoGrupo());
        txtPosicionPrincipal.setText(d.getPosicionPrincipal());
        txtPosicionSecundaria.setText(d.getPosicionSecundaria());
        cmbPiernaDominante.setValue(d.getPiernaDominante());
        txtTelefonoTutor.setText(d.getTelefonoTutor());
        chkActivo.setSelected(d.isActivo());
        txtObservaciones.setText(d.getObservaciones());

        for (Categoria categoria : cmbCategoria.getItems()) {
            if (categoria.getIdCategoria() == d.getIdCategoria()) {
                cmbCategoria.setValue(categoria);
                break;
            }
        }
    }
}