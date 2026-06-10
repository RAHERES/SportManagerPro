package com.example.sportmanagerpro.sportnutri.ui;


import com.example.sportmanagerpro.sportnutri.ui.expedientes.ExpedienteDetalleView;
import com.example.sportmanagerpro.sportnutri.ui.expedientes.ExpedientesView;
import com.example.sportmanagerpro.sportnutri.ui.personas.PersonasView;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class AppShell extends BorderPane {

    private final StackPane contentArea = new StackPane();

    public AppShell() {
        getStyleClass().add("app-root");

        Sidebar sidebar = new Sidebar(this::navigateTo);

        setLeft(sidebar);
        setCenter(contentArea);

        navigateTo("inicio");
    }

    private void navigateTo(String viewName) {
        contentArea.getChildren().setAll(resolveView(viewName));
    }

    private Node resolveView(String viewName) {
        return switch (viewName) {
            case "personas" -> new PersonasView();
            case "expedientes" -> new ExpedientesView(this::navigateTo);
            case "detalle-expediente" -> new ExpedienteDetalleView();
            case "inicio" -> new DashboardView();
            default -> new DashboardView();
        };
    }
}