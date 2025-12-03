package com.github.jimorc.flexishowbuilder;

import java.io.File;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.tinylog.Logger;

/**
 * CsvGrid is a GridPane for display and interaction with the contents of the OutputCSV object.
 */
public class CsvGrid extends GridPane {
    /**
     * Columns enum represents the columns in the output CSV display.
     */
    private enum Columns {
        /**
         * IMAGE_COL represents the image column.
         */
        IMAGE_COL,
        /**
         * TITLE_COL represents the title column.
         */
        TITLE_COL,
        /**
         * FULL_NAME_COL represents the full name column.
         */
        FULL_NAME_COL,
        /**
         * FIRST_NAME_COL represents the first name column.
         */
        FIRST_NAME_COL,
        /**
         * LAST_NAME_COL represents the last name column.
         */
        LAST_NAME_COL;
    }

    private static final Font HEADER_FONT = Font.font("System", FontWeight.BOLD, 12);
    private static final Font NORMAL_FONT = Font.font("System", FontWeight.NORMAL, 12);
    private static final Integer NO_SELECTION = -1;

    private Integer selStart = NO_SELECTION;
    private Integer selEnd = NO_SELECTION;
    private Integer oldSelStart = NO_SELECTION;
    private Integer oldSelEnd = NO_SELECTION;
    private ContextMenu displayedMenu;
    private OutputCSV csv;
    private String dir;

    /**
     * Constructor.
     * @param csv the OutputCSV object to display
     */
    public CsvGrid(OutputCSV csv, String dir) {
        super();
        final int gridGap = 10;
        final int padding = 10;

        this.csv = csv;
        this.dir = dir;
        this.setPadding(new Insets(padding));
        this.setVgap(2);
        this.setHgap(gridGap);

        FlexiBeans beans = csv.getBeans();
        createHeaderCellRow(beans.getBeans().get(0));
        createCellRows(beans);

    }

    private void createHeaderCellRow(FlexiBean b) {
        HBox headerCell = createGridCellBox(b.getFilename(),
            HEADER_FONT, Color.BLUE);
        this.add(headerCell, Columns.IMAGE_COL.ordinal(), 0);
        headerCell = createGridCellBox(b.getTitle(),
            HEADER_FONT, Color.BLUE);
        this.add(headerCell, Columns.TITLE_COL.ordinal(), 0);
        headerCell = createGridCellBox(b.getFullName(),
            HEADER_FONT, Color.BLUE);
        this.add(headerCell, Columns.FULL_NAME_COL.ordinal(), 0);
        headerCell = createGridCellBox(b.getFirstName(),
            HEADER_FONT, Color.BLUE);
        this.add(headerCell, Columns.FIRST_NAME_COL.ordinal(), 0);
        headerCell = createGridCellBox(b.getLastName(),
            HEADER_FONT, Color.BLUE);
        this.add(headerCell, Columns.LAST_NAME_COL.ordinal(), 0);
    }

    private void createCellRows(FlexiBeans beans) {
        for (int row = 1; row < beans.getBeans().size(); row++) {
            FlexiBean bean = beans.getBeans().get(row);
            Logger.debug(BuilderGUI.buildLogMessage(
                "OutputCSVStage creating grid line for bean: ", bean.toString()));
            // default color is black.
            Color color = Color.BLACK;
            // color is dark orange if bean contains only file name field.
            if (bean.getTitle() == null
                    && bean.getFullName() == null
                    && bean.getFirstName() == null
                    && bean.getLastName() == null) {
                color = Color.DARKORANGE;
            }

            HBox box = createGridCellBox(beans.getBeans().get(row).getFilename(),
                NORMAL_FONT, color);
            this.add(box, Columns.IMAGE_COL.ordinal(), row);
            box = createGridCellBox(beans.getBeans().get(row).getTitle(),
                NORMAL_FONT, color);
            this.add(box, Columns.TITLE_COL.ordinal(), row);
            box = createGridCellBox(beans.getBeans().get(row).getFullName(),
                NORMAL_FONT, color);
            this.add(box, Columns.FULL_NAME_COL.ordinal(), row);
            box = createGridCellBox(beans.getBeans().get(row).getFirstName(),
                NORMAL_FONT, color);
            this.add(box, Columns.FIRST_NAME_COL.ordinal(), row);
            box = createGridCellBox(beans.getBeans().get(row).getLastName(),
                NORMAL_FONT, color);
            this.add(box, Columns.LAST_NAME_COL.ordinal(), row);
        }
    }

    private HBox createGridCellBox(String text, Font font, Color textColor) {
        Text t = new Text(text);
        t.setFont(font);
        t.setFill(textColor);
        HBox box = new HBox(t);
        box.setOnMouseEntered(e -> {
            Logger.trace("In setOnMouseEntered, selectede = {}->{}", selStart, selEnd);
            Node sourceNode = (Node) e.getSource();
            Integer sourceIndex = GridPane.getRowIndex(sourceNode);
            if (sourceIndex < selStart || sourceIndex > selEnd) {
                setRowBackground(sourceIndex, "lightgreen");
            }
        });
        box.setOnMouseExited(e -> {
            Logger.trace("In setOnMouseExited, selected = {}->{}", selStart, selEnd);
            Node sourceNode = (Node) e.getSource();
            Integer sourceIndex = GridPane.getRowIndex(sourceNode);
            if (sourceIndex < selStart || sourceIndex > selEnd) {
                setRowBackground(sourceIndex, "transparent");
            }
        });
        box.setOnMousePressed(e -> {
            Logger.trace("In setOnMousePressed, selected = {}->{}", selStart, selEnd);
            Node sourceNode = (Node) e.getSource();
            Integer sourceIndex = GridPane.getRowIndex(sourceNode);
            if (e.isPrimaryButtonDown()) {
                processPrimaryButtonDown(e, sourceIndex);
            } else if (e.isSecondaryButtonDown()) {
                if (displayedMenu != null) {
                    displayedMenu.hide();
                }
                displayedMenu = createContextMenu(e);
                displayedMenu.show(this, Side.LEFT, e.getSceneX(), e.getSceneY());
            }
        });
        box.setOnMouseReleased(e -> {
            Logger.debug("In setOnMouseReleased, selected = {}->{}", selStart, selEnd);
            Logger.debug("oldSelStart = {}, oldSelEnd = {}", oldSelStart, oldSelEnd);
            for (Integer i = oldSelStart; i <= oldSelEnd; i++) {
                setRowBackground(i, "transparent");
            }
            for (Integer i = selStart; i <= selEnd; i++) {
                setRowBackground(i, "lightblue");
            }
            Logger.debug("On leaving setOnMouseReleased, selected = {}->{}", selStart, selEnd);
        });
        return box;
    }

    private void processPrimaryButtonDown(MouseEvent e, Integer sourceIndex) {
        oldSelStart = selStart;
        oldSelEnd = selEnd;
        if (e.isShiftDown()) {
            processShiftDown(sourceIndex);
        } else {
            selStart = sourceIndex;
            selEnd = sourceIndex;
        }
        Logger.debug("processPrimaryButtonDown: {}->{} changed to {}->{}",
            oldSelStart, oldSelEnd, selStart, selEnd);
    }

    private void processShiftDown(Integer sourceIndex) {
        if (selStart == NO_SELECTION) {
            selStart = sourceIndex;
        }
        selEnd = sourceIndex;
        sortStartEnd();
    }

    private void sortStartEnd() {
        if (selStart > selEnd) {
            Integer temp = selStart;
            selStart = selEnd;
            selEnd = temp;
        }
    }

    private void setRowBackground(Integer row, String color) {
        for (Node node: getChildren()) {
            String col = "-fx-background-color: " + color;
            Integer rowIndex = GridPane.getRowIndex(node);
            if (rowIndex == row) {
                node.setStyle(col);
            }
        }
    }

    private ContextMenu createContextMenu(MouseEvent e) {
        ContextMenu cm = new ContextMenu();
        Node sourceNode = (Node) e.getSource();
        Integer rowIndex = GridPane.getRowIndex(sourceNode);

        MenuItem select = createSelectItem(rowIndex);
        cm.getItems().add(select);

        if (selStart != NO_SELECTION) {
            MenuItem selectRange = createSelectRangeItem(rowIndex);
            cm.getItems().add(selectRange);
        }
        if (rowIndex >= selStart && rowIndex <= selEnd) {
            MenuItem deselect = createDeselectItem();
            cm.getItems().addAll(deselect);
        }
        cm.getItems().add(new SeparatorMenuItem());
        MenuItem insert = new MenuItem("Insert Image After");
        insert.setOnAction(ev -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image File to Insert");
            fileChooser.setInitialDirectory(new File(dir));
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Jpeg Files", "*.jpg", "*.jpeg"));
            File imageFile = fileChooser.showOpenDialog(null);
            if (imageFile != null) {
                String imageDir = imageFile.getParent();
                Logger.debug("In insert.setOnAction, imageDir = \'{}\'\n, dir = \'{}\'",
                    imageDir, dir);
                if (!imageDir.equals(dir)) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Invalid Image File");
                    alert.setHeaderText("The image file must be located in the\n"
                        + "same directory as all other files.");
                    alert.setContentText("Click OK to try again.");
                    alert.showAndWait();
                    return;
                }
                FlexiBean iBean = new FlexiBean();
                iBean.setFilename(imageFile.getName());
                int index = rowIndex.intValue() + 1;
                csv.getBeans().insert(index, iBean);
                resetGridRows();
            }
        });
        cm.getItems().add(insert);
        if (selStart != NO_SELECTION) {
            if (rowIndex < selStart || rowIndex > selEnd) {
                MenuItem move = new MenuItem("Insert Selected Rows After");
                move.setOnAction(ev -> {
                    FlexiBeans beans = new FlexiBeans();
                    for (int i = selStart.intValue(); i <= selEnd.intValue(); i++) {
                        beans.append(csv.getBeans().getBeans().get(i));
                    }
                    deleteBeans(selStart, selEnd);
                    int index = getIndex(rowIndex);
                    for (FlexiBean b: beans.getBeans()) {
                        csv.getBeans().insert(index++, b);
                    }
                    resetGridRows();
                });
                cm.getItems().add(move);
            }
            MenuItem delete = new MenuItem("Delete Selected Rows");
            delete.setOnAction(ev -> {
                deleteBeans(selStart, selEnd);
                resetGridRows();
            });
            cm.getItems().add(delete);
        }

        return cm;
    }

    private MenuItem createDeselectItem() {
        MenuItem deselect = new MenuItem("Deselect All Rows");
        deselect.setOnAction(ev -> {
            oldSelStart = selStart;
            oldSelEnd = selEnd;
            selStart = NO_SELECTION;
            selEnd = NO_SELECTION;
            Logger.debug("In deselect.setOnAction");
            Logger.debug("oldSelStart = {}, oldSelEnd = {}", oldSelStart, oldSelEnd);
            Logger.debug("selStart = {}, selEnd = {}", selStart, selEnd);
            for (Integer row = oldSelStart; row <= oldSelEnd; row++) {
                setRowBackground(row, "transparent");
            }
        });
        return deselect;
    }

    private MenuItem createSelectRangeItem(Integer rowIndex) {
        MenuItem selectRange = new MenuItem("Select Range");
        selectRange.setOnAction(ev -> {
            oldSelStart = selStart;
            oldSelEnd = selEnd;
            selEnd = rowIndex;
            sortStartEnd();
            Logger.debug("In selectRange.setOnAction runLater:");
            Logger.debug("oldSelStart = {}, oldSelEnd = {}", oldSelStart, oldSelEnd);
            Logger.debug("selStart = {}, selEnd = {}", selStart, selEnd);
            for (Integer row = oldSelStart; row <= oldSelEnd; row++) {
                setRowBackground(row, "transparent");
            }
            for (Integer row = selStart; row <= selEnd; row++) {
                setRowBackground(row, "lightblue");
            }
        });
        return selectRange;
    }

    private MenuItem createSelectItem(Integer rowIndex) {
        MenuItem select = new MenuItem("Select Single Row");
        select.setOnAction(ev -> {
            Logger.debug("In select.setOnAction");
            oldSelStart = selStart;
            oldSelEnd = selEnd;
            selStart = rowIndex;
            selEnd = rowIndex;
            for (Integer row = oldSelStart; row <= oldSelEnd; row++) {
                setRowBackground(row, "transparent");
            }
            setRowBackground(rowIndex, "lightblue");
        });
        return select;
    }

    private void resetGridRows() {
        oldSelStart = NO_SELECTION;
        oldSelEnd = NO_SELECTION;
        selStart = NO_SELECTION;
        selEnd = NO_SELECTION;
        this.getChildren().clear();
        createHeaderCellRow(csv.getBeans().getBeans().get(0));
        createCellRows(csv.getBeans());
    }

    private int getIndex(Integer sourceIndex) {
        int index = sourceIndex.intValue() + 1;
        if (selEnd != NO_SELECTION && sourceIndex > selEnd) {
            index = sourceIndex.intValue() - selEnd.intValue()
                    + selStart.intValue();
        }
        return index;
    }

    private void deleteBeans(Integer first, Integer last) {
        List<FlexiBean> allBeans = csv.getBeans().getBeans();
        csv.deleteAllBeans();
        for (int i = 0; i < first.intValue(); i++) {
            csv.getBeans().append(allBeans.get(i));
        }
        for (int i = last.intValue() + 1; i < allBeans.size(); i++) {
            csv.getBeans().append(allBeans.get(i));
        }
    }
}
