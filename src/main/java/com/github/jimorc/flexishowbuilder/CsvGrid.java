package com.github.jimorc.flexishowbuilder;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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

    /**
     * Constructor.
     * @param csv the OutputCSV object to display
     */
    public CsvGrid(OutputCSV csv) {
        super();
        final int gridGap = 10;
        final int padding = 10;

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
                highlightRowNodesNotSelected(sourceIndex);
            }
        });
        box.setOnMouseExited(e -> {
            Logger.trace("In setOnMouseExited, selected = {}->{}", selStart, selEnd);
            Node sourceNode = (Node) e.getSource();
            Integer sourceIndex = GridPane.getRowIndex(sourceNode);
            if (sourceIndex < selStart || sourceIndex > selEnd) {
            clearRowNodes(sourceIndex);
            }
        });
        box.setOnMousePressed(e -> {
            Logger.trace("In setOnMousePressed, selected = {}->{}", selStart, selEnd);
            Node sourceNode = (Node) e.getSource();
            Integer sourceIndex = GridPane.getRowIndex(sourceNode);
            if (e.isPrimaryButtonDown()) {
                if (e.isShiftDown()) {
                    oldSelStart = selStart;
                    oldSelEnd = selEnd;
                    selEnd = sourceIndex;
                    if (selStart > selEnd) {
                        Integer temp = selStart;
                        selStart = selEnd;
                        selEnd = temp;
                    }
                } else {
                    selStart = sourceIndex;
                    selEnd = sourceIndex;
                }
            }
        });
        box.setOnMouseReleased(e -> {
            Logger.debug("In setOnMouseReleased, selected = {}->{}", selStart, selEnd);
            for (Integer i = oldSelStart; i <= oldSelEnd; i++) {
                clearRowNodes(i);
            }
            for (Integer i = selStart; i <= selEnd; i++) {
                highlightRowNodesSelected(i);
            }
             Logger.debug("On leaving setOnMouseReleased, selected = {}->{}", selStart, selEnd);
        });
        return box;
    }

    private void highlightRowNodesSelected(Integer row) {
        for (Node node: getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            if (rowIndex == row) {
                node.setStyle("-fx-background-color: lightBlue");
            }
        }
    }

    private void highlightRowNodesNotSelected(Integer row) {
        for (Node node: getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            if (rowIndex == row) {
                node.setStyle("-fx-background-color: lightgreen");
            }
        }
    }

    private void clearRowNodes(Integer row) {
        for (Node node: getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            if (rowIndex == row) {
                node.setStyle("-fx-background-color: transparent");
            }
        }
    }
}
