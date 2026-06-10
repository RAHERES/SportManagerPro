package com.example.sportmanagerpro.nutricion.ui.components;

import javafx.animation.*;
import javafx.beans.property.*;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.Locale;

/**
 * Componente visual reutilizable para métricas breves. No usa CSS externo.
 */
public class MetricTile extends StackPane {
    public enum IconType { WEIGHT, HEIGHT, TARGET, ENERGY, BODY, HEART, APPLE, CALENDAR, NONE }

    private final StringProperty title = new SimpleStringProperty(this, "title", "Peso actual");
    private final DoubleProperty value = new SimpleDoubleProperty(this, "value", 0);
    private final DoubleProperty displayedValue = new SimpleDoubleProperty(this, "displayedValue", 0);
    private final StringProperty unit = new SimpleStringProperty(this, "unit", "kg");
    private final StringProperty subtitle = new SimpleStringProperty(this, "subtitle", "IMC: 26.1");
    private final ObjectProperty<Color> accentColor = new SimpleObjectProperty<>(this, "accentColor", Color.web("#7C3AED"));
    private final ObjectProperty<IconType> iconType = new SimpleObjectProperty<>(this, "iconType", IconType.WEIGHT);
    private final BooleanProperty animated = new SimpleBooleanProperty(this, "animated", true);
    private final BooleanProperty selected = new SimpleBooleanProperty(this, "selected", false);
    private final IntegerProperty decimals = new SimpleIntegerProperty(this, "decimals", 1);

    private final VBox root = new VBox(7);
    private final StackPane iconBox = new StackPane();
    private final SVGPath icon = new SVGPath();
    private final Label titleLabel = new Label();
    private final Label valueLabel = new Label();
    private final Label unitLabel = new Label();
    private final Label subtitleLabel = new Label();
    private Timeline timeline;

    public MetricTile() {
        build();
        registerListeners();
        refresh();
    }

    public MetricTile(String title, double value, String unit, String subtitle, IconType iconType, Color accentColor) {
        this();
        setTitle(title);
        setUnit(unit);
        setSubtitle(subtitle);
        setIconType(iconType);
        setAccentColor(accentColor);
        this.value.set(value);
        this.displayedValue.set(value);
        refresh();
    }

    private void build() {
        setPrefSize(145, 112);
        setMinSize(125, 104);
        setMaxSize(170, 124);
        setCursor(Cursor.HAND);
        root.setPadding(new Insets(14));
        root.setAlignment(Pos.TOP_LEFT);
        root.setFillWidth(true);

        iconBox.setMinSize(40, 40);
        iconBox.setPrefSize(40, 40);
        iconBox.setMaxSize(40, 40);
        icon.setFill(Color.TRANSPARENT);
        icon.setStrokeWidth(2.2);
        iconBox.getChildren().add(icon);

        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(80);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.MEDIUM, 11));
        titleLabel.setTextFill(Color.web("#334155"));

        HBox top = new HBox(10, iconBox, titleLabel);
        top.setAlignment(Pos.CENTER_LEFT);

        valueLabel.setFont(Font.font("Segoe UI", FontWeight.EXTRA_BOLD, 25));
        valueLabel.setTextFill(Ui.TEXT);
        valueLabel.setMinWidth(Region.USE_PREF_SIZE);

        unitLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));
        unitLabel.setTextFill(Ui.TEXT);
        unitLabel.setMinWidth(Region.USE_PREF_SIZE);

        HBox valueBox = new HBox(3, valueLabel, unitLabel);
        valueBox.setAlignment(Pos.BASELINE_LEFT);
        valueBox.setMinWidth(Region.USE_PREF_SIZE);

        subtitleLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 11));
        subtitleLabel.setTextFill(Ui.MUTED);

        root.getChildren().setAll(top, valueBox, subtitleLabel);
        getChildren().setAll(root);
    }

    private void registerListeners() {
        title.addListener((o, a, b) -> refresh());
        unit.addListener((o, a, b) -> refresh());
        subtitle.addListener((o, a, b) -> refresh());
        accentColor.addListener((o, a, b) -> refresh());
        iconType.addListener((o, a, b) -> refresh());
        selected.addListener((o, a, b) -> refresh());
        decimals.addListener((o, a, b) -> refresh());
        displayedValue.addListener((o, a, b) -> refresh());
        hoverProperty().addListener((o, a, b) -> refresh());
        value.addListener((o, oldValue, newValue) -> animateValue(oldValue.doubleValue(), newValue.doubleValue()));
    }

    private void animateValue(double start, double end) {
        if (timeline != null) timeline.stop();
        if (!isAnimated()) {
            displayedValue.set(end);
            return;
        }
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(displayedValue, start)),
                new KeyFrame(Duration.millis(550), new KeyValue(displayedValue, end, Interpolator.EASE_BOTH))
        );
        timeline.play();
    }

    private void refresh() {
        Color accent = getAccentColor();
        Color bg = isSelected() ? mix(accent, Color.WHITE, 0.90) : Color.WHITE;
        if (isHover()) bg = mix(accent, Color.WHITE, 0.94);
        setBackground(new Background(new BackgroundFill(bg, new CornerRadii(15), Insets.EMPTY)));
        setBorder(new Border(new BorderStroke(isSelected() ? accent : Ui.BORDER, BorderStrokeStyle.SOLID, new CornerRadii(15), new BorderWidths(isSelected() ? 1.6 : 1))));
        setEffect(new DropShadow(isHover() ? 20 : 14, Color.rgb(15, 23, 42, isHover() ? 0.14 : 0.08)));

        iconBox.setBackground(new Background(new BackgroundFill(Color.rgb((int)(accent.getRed()*255), (int)(accent.getGreen()*255), (int)(accent.getBlue()*255), .12), new CornerRadii(10), Insets.EMPTY)));
        icon.setContent(svgFor(getIconType()));
        icon.setStroke(accent);

        titleLabel.setText(getTitle());
        valueLabel.setText(String.format(Locale.US, "% .".replace(" ", "") + getDecimals() + "f", getDisplayedValue()));
        unitLabel.setText(getUnit());
        subtitleLabel.setText(getSubtitle() == null ? "" : getSubtitle());
    }

    private String svgFor(IconType type) {
        return switch (type) {
            case WEIGHT -> "M5 8 L19 8 L19 22 L5 22 Z M9 8 C9 4 15 4 15 8 M9 13 L15 13";
            case HEIGHT -> "M7 3 L7 23 M17 3 L17 23 M5 5 L9 5 M15 5 L19 5 M5 21 L9 21 M15 21 L19 21";
            case TARGET -> "M12 3 A9 9 0 1 1 11.9 3 M12 7 A5 5 0 1 1 11.9 7 M12 11 A1 1 0 1 1 11.9 11";
            case ENERGY -> "M14 2 L6 14 L12 14 L10 24 L19 10 L13 10 Z";
            case BODY -> "M12 4 A3 3 0 1 1 11.9 4 M7 12 C9 10 15 10 17 12 M9 13 L8 23 M15 13 L16 23";
            case HEART -> "M12 22 C5 16 3 11 6 7 C9 4 12 7 12 7 C12 7 15 4 18 7 C21 11 19 16 12 22";
            case APPLE -> "M12 7 C8 5 5 8 6 14 C7 20 11 22 12 20 C13 22 17 20 18 14 C19 8 16 5 12 7 M12 7 C12 4 14 3 16 3";
            case CALENDAR -> "M6 5 L18 5 L18 21 L6 21 Z M6 9 L18 9 M9 3 L9 7 M15 3 L15 7";
            case NONE -> "";
        };
    }

    private Color mix(Color base, Color target, double targetRatio) {
        return new Color(base.getRed()*(1-targetRatio)+target.getRed()*targetRatio, base.getGreen()*(1-targetRatio)+target.getGreen()*targetRatio, base.getBlue()*(1-targetRatio)+target.getBlue()*targetRatio, 1);
    }

    public String getTitle() { return title.get(); }
    public void setTitle(String value) { title.set(value); }
    public StringProperty titleProperty() { return title; }
    public double getValue() { return value.get(); }
    public void setValue(double value) { this.value.set(value); }
    public DoubleProperty valueProperty() { return value; }
    public double getDisplayedValue() { return displayedValue.get(); }
    public String getUnit() { return unit.get(); }
    public void setUnit(String value) { unit.set(value); }
    public StringProperty unitProperty() { return unit; }
    public String getSubtitle() { return subtitle.get(); }
    public void setSubtitle(String value) { subtitle.set(value); }
    public StringProperty subtitleProperty() { return subtitle; }
    public Color getAccentColor() { return accentColor.get(); }
    public void setAccentColor(Color value) { accentColor.set(value); }
    public ObjectProperty<Color> accentColorProperty() { return accentColor; }
    public IconType getIconType() { return iconType.get(); }
    public void setIconType(IconType value) { iconType.set(value); }
    public ObjectProperty<IconType> iconTypeProperty() { return iconType; }
    public boolean isAnimated() { return animated.get(); }
    public void setAnimated(boolean value) { animated.set(value); }
    public BooleanProperty animatedProperty() { return animated; }
    public boolean isSelected() { return selected.get(); }
    public void setSelected(boolean value) { selected.set(value); }
    public BooleanProperty selectedProperty() { return selected; }
    public int getDecimals() { return decimals.get(); }
    public void setDecimals(int value) { decimals.set(value); }
    public IntegerProperty decimalsProperty() { return decimals; }
}
