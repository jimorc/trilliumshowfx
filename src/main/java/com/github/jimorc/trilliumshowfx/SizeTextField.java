package com.github.jimorc.trilliumshowfx;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;

/**
 * A TextField that only accepts numeric input for size values.
 */
public class SizeTextField extends TextField {
    private final int maxDigits = 5;
    private final double textFieldMaxWidth = 55;

    /**
     * Constructs a SizeTextField with the specified value as text.
     * @param size the size to set the text field to
     */
    public SizeTextField(int size) {
        super(Integer.toString(size));
        this.setPrefColumnCount(maxDigits);
        this.setPrefWidth(textFieldMaxWidth);
        this.positionCaret(this.getText().length());
        this.setAlignment(Pos.CENTER_RIGHT);
        Tooltip sizeTooltip = new Tooltip("Enter the width or height in pixels for the slides.");
        this.setTooltip(sizeTooltip);
        this.addEventFilter(KeyEvent.KEY_TYPED,
            e -> {
                handleSizeFieldKeyInput(e);
            });
    }

    private void handleSizeFieldKeyInput(KeyEvent e) {
        String character = e.getCharacter();
        String numbers = "0123456789";
        if (numbers.contains(character)) {
            if (this.getText().length() >= maxDigits) {
                e.consume(); // Ignore input if max digits reached
            }
            if (this.getCaretPosition() == 0 && "0".equals(e.getCharacter())) {
                e.consume(); // Prevent leading zero
            }
        } else if (!("\b".equals(character)) && !("\u007F".equals(character))) {
            e.consume(); // Ignore all but Backspace and Delete
        }
    }
}
