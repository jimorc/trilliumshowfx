package com.github.jimorc.trilliumshowfx;

import java.io.File;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javax.swing.filechooser.FileSystemView;
import org.tinylog.Logger;

/**
 * TitleAndSortStage contains inputs for title image text and the image sort order.
 */
public class TitleAndSortStage extends FlexiStage {
    private final int spacing = 10;
    private SortOrder sortOrder = SortOrder.AsIs;
    private SizeTextField widthField;
    private SizeTextField heightField;
    private Button saveSizesButton;
    private CheckBox createStartEndCheckBox;
    private Button saveStartEndSliderButton;
    private TextArea startTitleArea;
    private TextArea endTitleArea;
    private ToggleGroup sortGroup;
    private RadioButton noneButton;
    private RadioButton alphaFullButton;
    private RadioButton alphaLastFirstButton;
    private RadioButton alphaFullRevButton;
    private RadioButton alphaLastFirstRevButton;
    private DefaultData defaultData;

    /**
     * Constructor.
     */
    public TitleAndSortStage() {
        String defFileName = FileSystemView.getFileSystemView().getHomeDirectory()
            + System.getProperty("file.separator")
            + ".config" + System.getProperty("file.separator")
            + "trilliumshowfx" + System.getProperty("file.separator")
            + "defaults.json";
        File defFile = new File(defFileName);
        defaultData = new DefaultData(defFile);

        Logger.debug("In TitleAndSortStage constructor");
        VBox vbox = createBox();
        Scene scene = new Scene(vbox);
        this.setScene(scene);
    }

    /**
     * getData retrieves the values set in the TitleAndSortStage object.
     * @return data set in stage object.
     */
    public TitleAndSortData getData() {
        int slideWidth = Integer.parseInt(widthField.getText());
        int slideHeight = Integer.parseInt(heightField.getText());
        SlideSize slideSize = new SlideSize(slideWidth, slideHeight);
        TitleAndSortData data = new TitleAndSortData(slideSize,
            createStartEndCheckBox.isSelected(),
            startTitleArea.getText(),
            endTitleArea.getText(), sortOrder);
        return data;
    }

    private VBox createBox() {
        final int fontSize = 14;
        final int tLabelMarginTop = 5;
        final int sizeLabelMarginTop = 5;
        final int sizeLabelMarginBottom = 5;
        final int topMargin = 0;
        final int rightMargin = 10;
        final int bottomMargin = 0;
        final int leftMargin = 10;
        final int buttonTopMargin = 5;
        final int buttonRightMargin = 20;
        final int buttonBottomMargin = 5;
        final int buttonLeftMargin = 20;
        final Font labelFont = Font.font("Arial", FontWeight.BOLD, fontSize);
        Insets vBoxInsets = new Insets(topMargin, rightMargin, bottomMargin, leftMargin);
        Insets tLabelInsets = new Insets(tLabelMarginTop, rightMargin, bottomMargin, leftMargin);
        Insets sizeInsets = new Insets(sizeLabelMarginTop, rightMargin, sizeLabelMarginBottom,
            leftMargin);
        VBox sizeBox = createSizeBox(labelFont, sizeInsets);
        TitledPane startEndPane = createStartEndSlidesPane(labelFont, tLabelInsets, vBoxInsets);
        Label sortLabel = createSortLabel(labelFont, vBoxInsets);
        sortGroup = new ToggleGroup();
        createNoneButton(vBoxInsets);
        createAlphaFullButton(vBoxInsets);
        createAlphaLastFirstButton(vBoxInsets);
        createAlphaFullRevButton(vBoxInsets);
        createAlphaLastFirstRevButton(vBoxInsets);

        HBox buttonBox = createButtonBox(buttonTopMargin, buttonRightMargin, buttonBottomMargin, buttonLeftMargin);

        VBox vbox = new VBox(spacing);
        vbox.getChildren().addAll(sizeBox, startEndPane, sortLabel,
            noneButton, alphaFullButton, alphaLastFirstButton, alphaFullRevButton,
            alphaLastFirstRevButton, buttonBox);
        return vbox;
    }

    private TitledPane createStartEndSlidesPane(final Font labelFont,
            final Insets labelInsets, final Insets boxInsets) {
        HBox startEndCheck = createCreateStartEndSlidesBox();
        Label startLabel = createStartEndLabel("Start Image Text", labelFont, labelInsets);
        String defaultStartTitle = defaultData.getStartTitle();
        startTitleArea = createTextArea("Start", defaultStartTitle, boxInsets);
        HBox startTitleBox = createTextBox(startTitleArea, boxInsets);
        Label endLabel = createStartEndLabel("End Image Text", labelFont, labelInsets);
        String defaultEndTitle = defaultData.getEndTitle();
        endTitleArea = createTextArea("End", defaultEndTitle, boxInsets);
        HBox endTitleBox = createTextBox(endTitleArea, boxInsets);
        VBox box = new VBox(startEndCheck, startLabel, startTitleBox, endLabel, endTitleBox);
        VBox.setMargin(startEndCheck, labelInsets);
        TitledPane startEndPane = new TitledPane();
        Label paneLabel = new Label("Start and End Slides");
        paneLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px");
        startEndPane.setGraphic(paneLabel);
        startEndPane.setContent(box);
        startEndPane.setCollapsible(false);
        // must call here to enable/disable widgets correctly when first displayed.
        saveStartEndSliderButtonAction(null);
        return startEndPane;
    }

    private HBox createCreateStartEndSlidesBox() {
        createStartEndCheckBox = new CheckBox("Create Start and End Slides");
        createStartEndCheckBox.setSelected(defaultData.getCreateStartEndSlides());
        createStartEndCheckBox.setOnAction(e -> {
            saveStartEndSliderButtonAction(e);
        });
        Tooltip tooltip = new Tooltip("Check this to create start and end slides");
        createStartEndCheckBox.setTooltip(tooltip);
        saveStartEndSliderButton = createSaveStartEndSliderButton();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox box = new HBox(createStartEndCheckBox, spacer, saveStartEndSliderButton);
        return box;
    }

    private void saveStartEndSliderButtonAction(Event e) {
        boolean checked = createStartEndCheckBox.isSelected();
        startTitleArea.setDisable(!checked);
        endTitleArea.setDisable(!checked);
        boolean sameAsDefault = createStartEndCheckBox.isSelected()
            == defaultData.getCreateStartEndSlides();
        saveStartEndSliderButton.setDisable(sameAsDefault);
    }

    private Button createSaveStartEndSliderButton() {
        Button button = new Button("Save Create Slides as Default");
        button.setDisable(true);
        button.setOnAction(_ -> {
            defaultData.setCreateStartEndSlides(createStartEndCheckBox.isSelected());
            defaultData.saveDefaults();
            button.setDisable(true);
        });
        return button;
    }

    private VBox createSizeBox(final Font labelFont, final Insets insets) {
        Label sizeLabel = new Label("Slide Size");
        sizeLabel.setFont(labelFont);
        sizeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px");
        VBox.setMargin(sizeLabel, insets);
        ChangeListener<String> createS = createSizeFieldChangeListener();
        SlideSize slideSize = defaultData.getSlideSize();
        widthField = new SizeTextField(slideSize.getWidth(), createS);
        heightField = new SizeTextField(slideSize.getHeight(), createS);
        Label x = new Label(" x ");
        Label pixels = new Label(" pixels");
        saveSizesButton = createSaveSizesButton();
        saveSizesButton.setDisable(true);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox sizePane = new HBox(spacing);
        sizePane.getChildren().addAll(widthField, x, heightField, pixels, spacer, saveSizesButton);
        VBox.setMargin(sizePane, insets);
        VBox sizeBox = new VBox();
        sizeBox.getChildren().addAll(sizeLabel, sizePane);
        return sizeBox;
    }

    private Button createSaveSizesButton() {
        Button button = new Button("Save Slide Size as Default");
        button.setOnAction(_ -> {
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());
            defaultData.setSlideSize(new SlideSize(width, height));
            defaultData.saveDefaults();
            button.setDisable(true);
        });
        return button;
    }

    private HBox createButtonBox(final int buttonTopMargin, final int buttonRightMargin, final int buttonBottomMargin,
            final int buttonLeftMargin) {
        QuitButton quit = new QuitButton();
        Button gen = createGenButton();
        HBox buttonBox = new HBox(spacing);
        buttonBox.getChildren().addAll(quit, gen);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        Insets buttonInsets = new Insets(buttonTopMargin, buttonRightMargin,
            buttonBottomMargin, buttonLeftMargin);
        HBox.setMargin(quit, buttonInsets);
        HBox.setMargin(gen, buttonInsets);
        return buttonBox;
    }

    private FlexiButton createGenButton() {
        FlexiButton gen = new FlexiButton("Create title and person slides");
        gen.setDefaultButton(true);
        gen.setOnAction(_ -> {
            this.close();
        });
        return gen;
    }

    private Label createStartEndLabel(String text, final Font labelFont, final Insets insets) {
        Label startLabel = new Label(text);
        startLabel.setFont(labelFont);
        VBox.setMargin(startLabel, insets);
        return startLabel;
    }

    private Label createSortLabel(final Font labelFont, final Insets insets) {
        Label sortLabel = new Label("Sort Order");
        sortLabel.setFont(labelFont);
        VBox.setMargin(sortLabel, insets);
        return sortLabel;
    }

    private void createNoneButton(Insets insets) {
        noneButton = createRadioButton("As Is", sortGroup, SortOrder.AsIs);
        Tooltip noneTooltip = new Tooltip("No sorting - use the order in the CSV file.\n"
            + "All images for each person are grouped together.");
        noneButton.setTooltip(noneTooltip);
        noneButton.setOnAction(_ -> {
            sortOrder = SortOrder.AsIs;
        });
        noneButton.setSelected(true);
        VBox.setMargin(noneButton, insets);
    }

    private void createAlphaFullButton(Insets insets) {
        alphaFullButton = createRadioButton("Alphabetical by Full Name", sortGroup, SortOrder.AlphabeticalByFullName);
        alphaFullButton.setOnAction(_ -> {
            sortOrder = SortOrder.AlphabeticalByFullName;
        });
        Tooltip alphaFullTooltip = new Tooltip("Sort by person's full name (first name then last "
            + "name).\nAll images for each person are grouped together.");
        alphaFullButton.setTooltip(alphaFullTooltip);
        VBox.setMargin(alphaFullButton, insets);
    }

    private void createAlphaLastFirstRevButton(Insets insets) {
        alphaLastFirstRevButton = createRadioButton("Alphabetical by Last Name then First Name Reverse",
            sortGroup, SortOrder.AlphabeticalByLastNameThenFirstNameReverse);
        alphaLastFirstRevButton.setOnAction(_ -> {
            sortOrder = SortOrder.AlphabeticalByLastNameThenFirstNameReverse;
        });
        Tooltip alphaLastFirstRevTooltip = new Tooltip("Sort by person's last name then first name "
            + "in reverse order.\nAll images for each person are grouped together.");
        alphaLastFirstRevButton.setTooltip(alphaLastFirstRevTooltip);
        VBox.setMargin(alphaLastFirstRevButton, insets);
    }

    private void createAlphaLastFirstButton(Insets insets) {
        alphaLastFirstButton = createRadioButton("Alphabetical by Last Name then First Name",
            sortGroup, SortOrder.AlphabeticalByLastNameThenFirstName);
        alphaLastFirstButton.setOnAction(_ -> {
            sortOrder = SortOrder.AlphabeticalByLastNameThenFirstName;
        });
        Tooltip alphaLastFirstTooltip = new Tooltip("Sort by person's last name then first name.\n"
            + "All images for each person are grouped together.");
        alphaLastFirstButton.setTooltip(alphaLastFirstTooltip);
        VBox.setMargin(alphaLastFirstButton, insets);
    }

    private void createAlphaFullRevButton(Insets insets) {
        alphaFullRevButton = createRadioButton("Alphabetical by Full Name Reverse",
            sortGroup, SortOrder.AlphabeticalByFullNameReverse);
        alphaFullRevButton.setOnAction(_ -> {
            sortOrder = SortOrder.AlphabeticalByFullNameReverse;
        });
        Tooltip alphaFullRevTooltip = new Tooltip("Sort by person's full name (first name then last "
            + "name) in reverse order.\nAll images for each person are grouped together.");
        alphaFullRevButton.setTooltip(alphaFullRevTooltip);
        VBox.setMargin(alphaFullRevButton, insets);
    }

    private TextArea createTextArea(String startEnd, String defaultText, Insets insets) {
        final int prefColumnCount = 50;
        final int prefRowCount = 2;
        TextArea textArea = new TextArea();
        textArea.setText(defaultText);
        textArea.setPromptText("Enter title text here");
        textArea.setPrefColumnCount(prefColumnCount);
        textArea.setPrefRowCount(prefRowCount);
        StringBuffer sb = new StringBuffer("Enter the title text to appear on the ");
        sb.append(startEnd);
        sb.append(" slide. Two or three lines of text is recommended.");
        Tooltip tTooltip = new Tooltip(sb.toString());
        textArea.setTooltip(tTooltip);
        VBox.setMargin(textArea, insets);
        return textArea;
    }

    private HBox createTextBox(TextArea tArea, Insets insets) {
        Button saveButton = new Button("Save as Default");
        saveButton.setOnAction(_ -> {
            String text = tArea.getText();
            if (startTitleArea == tArea) {
                defaultData.setStartTitle(text);
            } else {
                defaultData.setEndTitle(text);
            }
            defaultData.saveDefaults();
            saveButton.setDisable(true);
        });
        HBox box = new HBox(spacing);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        box.getChildren().addAll(tArea, spacer, saveButton);
        VBox.setMargin(box, insets);
        return box;
    }

    private RadioButton createRadioButton(String text, ToggleGroup group, SortOrder order) {
        RadioButton button = new RadioButton(text);
        button.setToggleGroup(group);
        button.setUserData(order);
        return button;
    }

    private ChangeListener<String> createSizeFieldChangeListener() {
        ChangeListener<String> listener = (observable, oldValue, newValue) -> {
            String widthText = widthField.getText();
            String heightText = heightField.getText();
            int width = Integer.parseInt(widthText);
            int height = Integer.parseInt(heightText);
            SlideSize slideSize = defaultData.getSlideSize();
            boolean disableButton = false;
            if (width == slideSize.getWidth()
                && height == slideSize.getHeight()) {
                disableButton = true;
            }
            if (width < SlideSize.MIN_SIZE) {
                widthField.setStyle("-fx-border-color: red; -fx-border-width: 3px;");
                disableButton = true;
            } else {
                widthField.setStyle("-fx-border-color: transparent; -fx-border-width: 3px;");
            }
            if (height < SlideSize.MIN_SIZE) {
                heightField.setStyle("-fx-border-color: red; -fx-border-width: 3px;");
                disableButton = true;
            } else {
                heightField.setStyle("-fx-border-color: transparent; -fx-border-width: 3px;");
            }
            saveSizesButton.setDisable(disableButton);
        };
        return listener;
    }
}
