package com.github.jimorc.flexishowbuilder;

import java.io.IOException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.tinylog.Logger;

/**
 * OutputCSVStage is the panel that displays the OutputCSV object.
 */
public class OutputCSVStage extends FlexiStage {
    /**
     * OutputCSVStage constructor.
     * @param csv the OutputCSV object to display.
     * @param dir the folder to save the XLS file to when the "Save" button is clicked.
     */
    public OutputCSVStage(OutputCSV csv, String dir) {
        ScrollPane sPane = new ScrollPane();
        sPane.setContent(new CsvGrid(csv));
        HBox buttonBox = createButtonBox(csv, dir);
        VBox box = new VBox(sPane, buttonBox);
        Scene scene = new Scene(box);
        this.setScene(scene);
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
