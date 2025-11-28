package com.github.jimorc.flexishowbuilder;

import java.io.IOException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.tinylog.Logger;

/**
 * OutputCSVStage is the panel that displays the OutputCSV object.
 */
public class OutputCSVStage extends FlexiStage {
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

    /**
     * OutputCSVStage constructor.
     * @param csv the OutputCSV object to display.
     * @param dir the folder to save the XLS file to when the "Save" button is clicked.
     */
    public OutputCSVStage(OutputCSV csv, String dir) {
        GridPane grid = createGrid(csv);
        ScrollPane sPane = new ScrollPane();
        sPane.setContent(grid);
        HBox buttonBox = createButtonBox(csv, dir);
        VBox box = new VBox(sPane, buttonBox);
        Scene scene = new Scene(box);
        this.setScene(scene);
    }

    private GridPane createGrid(OutputCSV csv) {
        final int gridGap = 5;
        final int padding = 10;

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(padding));
        grid.setVgap(gridGap);
        grid.setHgap(gridGap);

        FlexiBeans beans = csv.getBeans();
        int row = 0;
        for (FlexiBean bean : beans.getBeans()) {
            Logger.debug(BuilderGUI.buildLogMessage(
                "OutputCSVStage creating grid line for bean: ", bean.toString()));
            grid.add(new Text(bean.getFilename()), Columns.IMAGE_COL.ordinal(), row);
            grid.add(new Text(bean.getTitle()), Columns.TITLE_COL.ordinal(), row);
            grid.add(new Text(bean.getFullName()), Columns.FULL_NAME_COL.ordinal(), row);
            grid.add(new Text(bean.getFirstName()), Columns.FIRST_NAME_COL.ordinal(), row);
            grid.add(new Text(bean.getLastName()), Columns.LAST_NAME_COL.ordinal(), row++);
        }
        return grid;
    }

    private HBox createButtonBox(OutputCSV csv, String dir) {
        final int buttonTopMargin = 5;
        final int buttonRightMargin = 20;
        final int buttonBottomMargin = 5;
        final int buttonLeftMargin = 20;
        final String saveFileName = dir + "/slideshow.xls";

        Insets insets = new Insets(buttonTopMargin, buttonRightMargin,
            buttonBottomMargin, buttonLeftMargin);
        QuitButton quit = new QuitButton();
        HBox.setMargin(quit, insets);

        FlexiButton save = new FlexiButton("Save to slideshow.xls");
        save.setDefaultButton(true);
        save.setOnAction(_ -> {
            XLSWorkbook workbook = new XLSWorkbook(csv);
            try {
                workbook.writeToFile(saveFileName);
            } catch (IOException ioe) {
                Logger.error("IOException thrown writing XLS file: ", ioe);
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("IO Error");
                alert.setHeaderText("Error encountered while writing the XLS file.");
                alert.setContentText(ioe.getMessage()
                    + "\nProgram will now exit.");
                alert.showAndWait();
                System.exit(1);
            }
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("File Saved");
            alert.setHeaderText(saveFileName + " has been saved.");
            alert.setContentText("Click OK to terminate program.");
            alert.showAndWait();
            this.close();
        });
        HBox.setMargin(save, insets);
        HBox box = new HBox(quit, save);
        box.setAlignment(Pos.CENTER_RIGHT);

        return box;
    }

}
