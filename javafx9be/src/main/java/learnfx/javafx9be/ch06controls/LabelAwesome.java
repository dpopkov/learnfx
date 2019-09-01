package learnfx.javafx9be.ch06controls;

import de.jensd.fx.glyphs.GlyphIcons;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import de.jensd.fx.glyphs.octicons.OctIcon;
import de.jensd.fx.glyphs.octicons.OctIconView;
import de.jensd.fx.glyphs.weathericons.WeatherIcon;
import de.jensd.fx.glyphs.weathericons.WeatherIconView;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class LabelAwesome extends Application {
    /** Maps names of the icon packs to lists of GlyphIcons objects. */
    private static final Map<String, List<GlyphIcons>> ICON_PACKS_MAP = new HashMap<>();

    private BorderPane root;
    private VBox labelDisplayPanel;
    private VBox controlsPanel;
    private ToggleGroup positionGroup;
    private TextField inputField;
    private ComboBox<String> iconPacks;

    @Override
    public void init() throws Exception {
        loadIcons();
        prepareIcons();
    }

    private void loadIcons() throws IOException {
        String[] ttfPaths = {
                FontAwesomeIconView.TTF_PATH, MaterialDesignIconView.TTF_PATH, MaterialIconView.TTF_PATH,
                OctIconView.TTF_PATH, WeatherIconView.TTF_PATH
        };
        for (String path : ttfPaths) {
            Font.loadFont(GlyphsDude.class.getResource(path).openStream(), 10.0);
        }
    }

    private void prepareIcons() {
        ICON_PACKS_MAP.put("FontAwesomeIcon", Arrays.asList(FontAwesomeIcon.values()));
        ICON_PACKS_MAP.put("MaterialDesignIcon", Arrays.asList(MaterialDesignIcon.values()));
        ICON_PACKS_MAP.put("MaterialIcon", Arrays.asList(MaterialIcon.values()));
        ICON_PACKS_MAP.put("OctIcon", Arrays.asList(OctIcon.values()));
        ICON_PACKS_MAP.put("WeatherIcon", Arrays.asList(WeatherIcon.values()));
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("LabelAwesome");
        root = new BorderPane();
        Scene scene = new Scene(root, 600, 450);

        createTitleBanner();
        initLabelDisplayPanel();
        initSelectIconPacksComboBox();
        initInputField();
        initIconPositionRadioButtons();

        ChangeListener<String> searchTextListener = (observable, oldValue, newValue) ->
                showIconList(newValue, labelDisplayPanel, iconPacks.getValue(),
                        positionGroup.getSelectedToggle().getUserData());
        ChangeListener<Toggle> selectIconPositionListener = (observable, oldValue, newValue) ->
                showIconList(inputField.getText(), labelDisplayPanel, iconPacks.getValue(),
                        positionGroup.getSelectedToggle().getUserData());
        EventHandler<ActionEvent> selectIconPackListener = actionEvent ->
                showIconList(inputField.getText(), labelDisplayPanel, iconPacks.getValue(),
                        positionGroup.getSelectedToggle().getUserData());

        inputField.textProperty().addListener(searchTextListener);
        positionGroup.selectedToggleProperty().addListener(selectIconPositionListener);
        iconPacks.setOnAction(selectIconPackListener);

        stage.setScene(scene);
        stage.show();
    }

    private void createTitleBanner() {
        Text labelText = new Text("Label ");
        labelText.setFont(Font.font("Helvetica", FontWeight.EXTRA_LIGHT, 60));
        Text awesomeText = new Text("Awesome");
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setOffsetX(3.0F);
        innerShadow.setOffsetY(3.0F);
        awesomeText.setEffect(innerShadow);
        awesomeText.setFill(Color.WHITE);
        awesomeText.setFont(Font.font("Helvetica", FontWeight.BOLD, 60));
        TextFlow title = new TextFlow(labelText, awesomeText);
        HBox banner = new HBox(title);
        banner.setPadding(new Insets(10, 0, 10, 10));
        root.setTop(banner);
    }

    private void initLabelDisplayPanel() {
        labelDisplayPanel = new VBox(5);
        labelDisplayPanel.setAlignment(Pos.CENTER);
        ScrollPane scrollPane = new ScrollPane(labelDisplayPanel);
        scrollPane.setPadding(new Insets(10));
        root.setCenter(scrollPane);
    }

    private void initSelectIconPacksComboBox() {
        controlsPanel = new VBox(10);
        controlsPanel.setPadding(new Insets(10));
        List<String> iconPackList = new ArrayList<>();
        iconPackList.add("FontAwesomeIcon");
        iconPackList.add("MaterialDesignIcon");
        iconPackList.add("MaterialIcon");
        iconPackList.add("OctIcon");
        iconPackList.add("WeatherIcon");

        ObservableList<String> obsIconPackList = FXCollections.observableList(iconPackList);
        iconPacks = new ComboBox<>(obsIconPackList);
        iconPacks.setValue(iconPackList.get(0));
        controlsPanel.getChildren().add(iconPacks);
        root.setLeft(controlsPanel);
    }

    private void initInputField() {
        inputField = new TextField();
        inputField.setPrefWidth(200);
        inputField.setPromptText("Search Icon Name");
        controlsPanel.getChildren().add(inputField);
    }

    private void initIconPositionRadioButtons() {
        VBox imagePositionPanel = new VBox(5);
        positionGroup = new ToggleGroup();
        RadioButton topPosition = new RadioButton("Top");
        topPosition.setSelected(true);
        topPosition.setUserData(ContentDisplay.TOP);
        topPosition.requestFocus();
        topPosition.setToggleGroup(positionGroup);

        RadioButton bottomPosition = new RadioButton("Bottom");
        bottomPosition.setUserData(ContentDisplay.BOTTOM);
        bottomPosition.setToggleGroup(positionGroup);

        RadioButton leftPosition = new RadioButton("Left");
        leftPosition.setUserData(ContentDisplay.LEFT);
        leftPosition.setToggleGroup(positionGroup);

        RadioButton rightPosition = new RadioButton("Right");
        rightPosition.setUserData(ContentDisplay.RIGHT);
        rightPosition.setToggleGroup(positionGroup);

        imagePositionPanel.getChildren().addAll(topPosition, bottomPosition, leftPosition, rightPosition);
        controlsPanel.getChildren().add(imagePositionPanel);
    }

    private void showIconList(String textInput, VBox labelDisplayPanel, String iconPack, Object position) {
        labelDisplayPanel.getChildren().clear();
        List<GlyphIcons> iconPackIcons = ICON_PACKS_MAP.get(iconPack);
        final String textInputUpper = textInput.toUpperCase();
        Predicate<GlyphIcons> predicate;
        if (textInputUpper.equals("*")) {
            predicate = iconEnum -> true;
        } else {
            predicate = iconEnum -> iconEnum.toString().toUpperCase().contains(textInputUpper);
        }
        iconPackIcons.stream()
                .filter(predicate)
                .forEach(iconEnum -> {
                    Label label = createLabelForGlyphIcon(iconEnum, (ContentDisplay) position);
                    labelDisplayPanel.getChildren().add(label);
                });
    }

    private Label createLabelForGlyphIcon(GlyphIcons iconEnum, ContentDisplay position) {
        Text iconShape = new Text(iconEnum.characterToString());
        iconShape.getStyleClass().add("glyph-icon");
        iconShape.setStyle(String.format("-fx-font-family: %s; -fx-font-size: %d;", iconEnum.getFontFamily(), 20));
        Label label = new Label(iconEnum.toString(), iconShape);
        label.setContentDisplay(position);
        return label;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
