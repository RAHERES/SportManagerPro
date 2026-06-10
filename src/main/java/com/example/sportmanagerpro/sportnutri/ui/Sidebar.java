package com.example.sportmanagerpro.sportnutri.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Sidebar extends VBox {

    private final Consumer<String> router;
    private final Map<String, Label> menuItems = new HashMap<>();

    public Sidebar(Consumer<String> router) {
        this.router = router;

        getStyleClass().add("sidebar");
        setPrefWidth(245);
        setMinWidth(245);
        setMaxWidth(245);

        VBox content = new VBox(10);
        content.setPadding(new Insets(22, 16, 16, 16));

        Label logo = new Label("⚽  SPORTNUTRI\n     MANAGER PRO");
        logo.getStyleClass().add("sidebar-logo");

        VBox menu = new VBox(6);
        menu.getChildren().addAll(
                section("MENÚ PRINCIPAL"),
                item("⌂", "Inicio", "inicio"),
                item("●", "Personas", "personas"),
                item("▣", "Expedientes", "expedientes"),
                item("👥", "Equipos", "equipos"),
                item("✚", "Entrenamiento", "entrenamiento"),
                item("●", "Nutrición", "nutricion"),
                item("☑", "Evaluaciones", "evaluaciones"),
                item("♛", "Competencias", "competencias"),
                item("▮", "Estadísticas", "estadisticas"),
                item("▣", "Calendario", "calendario"),
                item("◫", "Reportes", "reportes"),
                item("▰", "Catálogos", "catalogos"),
                item("⚙", "Configuración", "configuracion"),
                section("ACCESOS RÁPIDOS"),
                quickItem("●", "Nueva Persona"),
                quickItem("▣", "Nueva Cita Nutricional"),
                quickItem("♕", "Nueva Evaluación"),
                quickItem("♛", "Nueva Sesión"),
                quickItem("▣", "Nuevo Plan Alimentario")
        );

        content.getChildren().addAll(logo, menu);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("sidebar-scroll");

        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        Label user = new Label("◉  Entrenadora\n     Administrador");
        user.getStyleClass().add("sidebar-user");
        user.setPadding(new Insets(14, 16, 18, 16));

        getChildren().addAll(scrollPane, user);

        setActiveItem("inicio");
    }

    private Label section(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("sidebar-section");
        return label;
    }

    private Label item(String icon, String text, String route) {
        Label label = new Label(icon + "  " + text);
        label.getStyleClass().add("sidebar-item");

        menuItems.put(route, label);

        label.setOnMouseClicked(e -> {
            setActiveItem(route);
            router.accept(route);
        });

        return label;
    }

    private void setActiveItem(String activeRoute) {
        for (Map.Entry<String, Label> entry : menuItems.entrySet()) {
            Label label = entry.getValue();
            label.getStyleClass().removeAll("sidebar-item", "sidebar-item-active");

            if (entry.getKey().equals(activeRoute)) {
                label.getStyleClass().add("sidebar-item-active");
            } else {
                label.getStyleClass().add("sidebar-item");
            }
        }
    }

    private Label quickItem(String icon, String text) {
        Label label = new Label(icon + "  " + text);
        label.getStyleClass().add("sidebar-quick-item");
        return label;
    }
}