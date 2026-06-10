package com.example.sportmanagerpro.sportnutri.ui;

import com.example.sportmanagerpro.sportnutri.ui.components.DashboardCard;
import com.example.sportmanagerpro.sportnutri.ui.components.EventRow;
import com.example.sportmanagerpro.sportnutri.ui.components.QuickActionCard;
import com.example.sportmanagerpro.sportnutri.ui.util.UiFactory;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class DashboardView extends BorderPane {

    public DashboardView() {
        getStyleClass().add("dashboard-root");

        VBox main = new VBox(22);
        main.setPadding(new Insets(22, 26, 18, 26));

        main.getChildren().addAll(
                createHeader(),
                createSummaryCards(),
                createMiddlePanels(),
                createQuickActions(),
                createFooter()
        );

        setCenter(main);
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.getStyleClass().add("top-header");

        VBox titleBox = new VBox(4);

        Label title = new Label("¡Bienvenida, Entrenadora!");
        title.getStyleClass().add("main-title");

        Label subtitle = new Label("Jueves, 23 de mayo de 2024   |   10:45 AM");
        subtitle.getStyleClass().add("muted-text");

        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label search = new Label("🔍  Buscar personas, expedientes, evaluaciones...");
        search.getStyleClass().add("search-box");

        Label icons = new Label("🔔  📅  ?  ●");
        icons.getStyleClass().add("top-icons");

        header.getChildren().addAll(titleBox, spacer, search, icons);
        return header;
    }

    private HBox createSummaryCards() {
        HBox cards = new HBox(12);
        cards.getChildren().addAll(
                new DashboardCard("👥", "128", "Personas registradas", "Ver todas", "blue"),
                new DashboardCard("📁", "84", "Expedientes activos", "Ver todas", "green"),
                new DashboardCard("●", "47", "Pacientes en seguimiento", "Ver pacientes", "purple"),
                new DashboardCard("🏃", "66", "Deportistas activos", "Ver deportistas", "orange"),
                new DashboardCard("☑", "15", "Evaluaciones pendientes", "Ver pendientes", "cyan"),
                new DashboardCard("♥", "4", "Alertas de salud", "Ver alertas", "red")
        );
        return cards;
    }

    private HBox createMiddlePanels() {
        HBox panels = new HBox(20);

        VBox activities = UiFactory.panel("📅  PRÓXIMAS ACTIVIDADES");
        activities.getChildren().addAll(
                new EventRow("🟣", "Cita nutricional - Ana Pérez", "23 Mayo 2024 - 11:00 AM", "Nutrición"),
                new EventRow("🟢", "Evaluación física - Sub 17", "23 Mayo 2024 - 03:00 PM", "Evaluación"),
                new EventRow("🔵", "Entrenamiento - Sub 15", "23 Mayo 2024 - 05:00 PM", "Entrenamiento"),
                new EventRow("🟣", "Seguimiento nutricional - Juan Ruiz", "24 Mayo 2024 - 09:00 AM", "Nutrición"),
                new EventRow("🟠", "Entrega de reportes", "24 Mayo 2024 - 04:00 PM", "Reportes"),
                UiFactory.link("Ver calendario completo →")
        );

        VBox followUps = UiFactory.panel("〽  SEGUIMIENTOS RECIENTES");
        followUps.getChildren().addAll(
                new EventRow("ML", "Evaluación antropométrica\nMaría López", "22 Mayo 2024", "Completada"),
                new EventRow("AP", "Plan alimentario actualizado\nAna Pérez", "22 Mayo 2024", "Nutrición"),
                new EventRow("SR", "Sesión de fuerza registrada\nSub 17 Femenil", "21 Mayo 2024", "Entrenamiento"),
                new EventRow("FR", "Expediente deportivo actualizado\nFernanda Ruiz", "21 Mayo 2024", "Expediente"),
                new EventRow("JG", "Seguimiento nutricional\nJosé García", "20 Mayo 2024", "Nutrición"),
                UiFactory.link("Ver todos los seguimientos →")
        );

        VBox alerts = UiFactory.panel("⚠  ALERTAS IMPORTANTES");
        alerts.getChildren().addAll(
                new EventRow("♥", "2 lesiones activas requieren seguimiento", "Ver lesiones", "›"),
                new EventRow("📋", "5 evaluaciones vencidas", "Ver evaluaciones", "›"),
                new EventRow("👥", "3 pacientes sin seguimiento esta semana", "Ver pacientes", "›"),
                new EventRow("☘", "4 planes alimentarios por renovar", "Ver planes", "›"),
                new EventRow("📁", "6 expedientes con datos incompletos", "Ver expedientes", "›"),
                UiFactory.link("Ver todas las alertas →")
        );

        HBox.setHgrow(activities, Priority.ALWAYS);
        HBox.setHgrow(followUps, Priority.ALWAYS);
        HBox.setHgrow(alerts, Priority.ALWAYS);

        panels.getChildren().addAll(activities, followUps, alerts);
        return panels;
    }

    private VBox createQuickActions() {
        VBox wrapper = UiFactory.panel("📅  ACCESOS RÁPIDOS");

        HBox actions = new HBox(18);
        actions.getChildren().addAll(
                new QuickActionCard("●", "Registrar\nNueva Persona", "green-soft"),
                new QuickActionCard("📁", "Abrir\nExpediente", "purple-soft"),
                new QuickActionCard("📋", "Nueva\nEvaluación", "blue-soft"),
                new QuickActionCard("✚", "Nueva\nSesión", "orange-soft"),
                new QuickActionCard("🍎", "Nuevo Plan\nAlimentario", "pink-soft"),
                new QuickActionCard("📅", "Ver\nCalendario", "cyan-soft"),
                new QuickActionCard("📄", "Generar\nReporte", "yellow-soft")
        );

        wrapper.getChildren().add(actions);
        return wrapper;
    }

    private HBox createFooter() {
        HBox footer = new HBox();

        Label left = new Label("© 2024 SportNutri Manager Pro - Sistema Integral de Entrenamiento y Nutrición");
        left.getStyleClass().add("footer-text");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label right = new Label("Versión 1.0.0");
        right.getStyleClass().add("footer-text");

        footer.getChildren().addAll(left, spacer, right);
        return footer;
    }
}