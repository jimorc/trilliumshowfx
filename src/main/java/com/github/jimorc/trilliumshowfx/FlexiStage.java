package com.github.jimorc.trilliumshowfx;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.tinylog.Logger;

/**
 * FlexiStage encapsulates the common functionality for all Stage objects in this program.
 */
public class FlexiStage extends Stage {
    private final String title = "Flexishowbuilder";
    private final int width = 1080;
    private final int height = 800;

    /**
     * Flexistage constructor.
     */
    public FlexiStage() {
        setTitle(title);
        setWidth(width);
        setHeight(height);
        // make sure program closes when window close button is clicked.
        setOnCloseRequest(_ -> {
            Logger.trace("Handling FlexiStage close request");
            Platform.exit();
            System.exit(0);
        });
    }
}
