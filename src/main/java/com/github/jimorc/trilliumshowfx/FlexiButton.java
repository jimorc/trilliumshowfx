package com.github.jimorc.trilliumshowfx;

import javafx.scene.control.Button;

/**
 * FlexiButton contains common functionality for all Button objects in the program.
 */
public class FlexiButton extends Button {
    private final int minWidth = 100;

    /**
     * Constructor.
     * @param text the text to display in the button.
     */
    public FlexiButton(String text) {
        super(text);
        setMinWidth(minWidth);
    }

}
