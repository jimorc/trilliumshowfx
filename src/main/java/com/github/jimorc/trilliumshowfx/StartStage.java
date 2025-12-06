package com.github.jimorc.trilliumshowfx;

import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import org.tinylog.Logger;

/**
 * StartStage is the first Stage that is displayed.
 */
public class StartStage extends FlexiStage {
    private final int spacing = 100;
    private InputCSV iCSV;

    /**
     * Constructor.
     */
    public StartStage() {
        Logger.trace("In StartStage constructor");
        FlexiButton loadCSV = new FlexiButton("Load CSV");
        loadCSV.setOnAction(_ -> {
            Logger.trace("Processing Load CSV button click");
            loadCSVFile();
            Logger.trace("Closing StartStage");
            this.close();
        });
        QuitButton quit = new QuitButton();
        VBox box = new VBox(spacing);
        box.getChildren().addAll(loadCSV, quit);
        box.setAlignment(Pos.CENTER);
        Scene scene = new Scene(box);
        this.setScene(scene);
        Logger.trace("Returning from StartStage constructor");
    }

    private void loadCSVFile() {
        //BuilderGUI.LOG.debug("Building and showing FileChooser");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV File");
        fileChooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV Files", "*.csv"));
        File csvFile = fileChooser.showOpenDialog(null);
        Logger.trace("Back from FileChooser");

        if (csvFile != null) {
            try {
                iCSV = new InputCSV(csvFile);
            } catch (CSVException e) {
                Logger.error("InputCSV threw CSVException: ", e);
                BuilderGUI.handleCSVException(e);
            } catch (IOException ioe) {
                Logger.error("InputCSV constructor threw IOException: ", ioe);
                BuilderGUI.handleIOException(ioe, iCSV);
            }
        } else {
            Logger.debug("FileChooser returned null (cancel clicked)");
            Platform.exit();
            System.exit(0);
        }
    }

    /**
     * Retrieve the loaded InputCSV object.
     * @return the InputCSV object.
     */
    public InputCSV getInputCSV() {
        return iCSV;
    }
}
