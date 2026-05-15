package com.example.sportmanagerpro.controllers;




import com.example.sportmanagerpro.models.Categoria;
import com.example.sportmanagerpro.services.CategoriaService;
import com.example.sportmanagerpro.utils.AlertUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CategoriaController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtRangoEdad;
    @FXML private TextArea txtDescripcion;
    @FXML private CheckBox chkActiva;

    @FXML private TableView<Categoria> tablaCategorias;
    @FXML private TableColumn<Categoria, Integer> colId;
    @FXML private TableColumn<Categoria, String> colNombre;
    @FXML private TableColumn<Categoria, String> colRangoEdad;
    @FXML private TableColumn<Categoria, String> colDescripcion;
    @FXML private TableColumn<Categoria, Boolean> colActiva;

    private final CategoriaService categoriaService = new CategoriaService();
    private Categoria categoriaSeleccionada;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdCategoria()).asObject());
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colRangoEdad.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRangoEdad()));
        colDescripcion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescripcion()));
        colActiva.setCellValueFactory(data -> new javafx.beans.property.SimpleBooleanProperty(data.getValue().isActiva()).asObject());

        tablaCategorias.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> cargarCategoriaSeleccionada(newSelection)
        );

        cargarCategorias();
    }

    @FXML
    private void guardarCategoria() {
        try {
            Categoria categoria = leerFormulario();
            categoriaService.guardar(categoria);
            AlertUtils.mostrarInformacion("Categoría guardada correctamente.");
            limpiarFormulario();
            cargarCategorias();
        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }

    @FXML
    private void actualizarCategoria() {
        if (categoriaSeleccionada == null) {
            AlertUtils.mostrarError("Debe seleccionar una categoría.");
            return;
        }

        try {
            Categoria categoria = leerFormulario();
            categoria.setIdCategoria(categoriaSeleccionada.getIdCategoria());
            categoriaService.actualizar(categoria);
            AlertUtils.mostrarInformacion("Categoría actualizada correctamente.");
            limpiarFormulario();
            cargarCategorias();
        } catch (Exception e) {
            AlertUtils.mostrarError(e.getMessage());
        }
    }

    @FXML
    private void desactivarCategoria() {
        if (categoriaSeleccionada == null) {
            AlertUtils.mostrarError("Debe seleccionar una categoría.");
            return;
        }

        categoriaService.desactivar(categoriaSeleccionada.getIdCategoria());
        AlertUtils.mostrarInformacion("Categoría desactivada correctamente.");
        limpiarFormulario();
        cargarCategorias();
    }

    @FXML
    private void limpiarFormulario() {
        categoriaSeleccionada = null;
        txtNombre.clear();
        txtRangoEdad.clear();
        txtDescripcion.clear();
        chkActiva.setSelected(true);
        tablaCategorias.getSelectionModel().clearSelection();
    }

    private Categoria leerFormulario() {
        Categoria categoria = new Categoria();
        categoria.setNombre(txtNombre.getText());
        categoria.setRangoEdad(txtRangoEdad.getText());
        categoria.setDescripcion(txtDescripcion.getText());
        categoria.setActiva(chkActiva.isSelected());
        return categoria;
    }

    private void cargarCategoriaSeleccionada(Categoria categoria) {
        if (categoria == null) {
            return;
        }

        categoriaSeleccionada = categoria;
        txtNombre.setText(categoria.getNombre());
        txtRangoEdad.setText(categoria.getRangoEdad());
        txtDescripcion.setText(categoria.getDescripcion());
        chkActiva.setSelected(categoria.isActiva());
    }

    private void cargarCategorias() {
        tablaCategorias.setItems(FXCollections.observableArrayList(categoriaService.listarTodas()));
    }
}