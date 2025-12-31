package com.github.jimorc.trilliumshowfx;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderWidths;

/**
 * A TextField that only accepts numeric input for size values.
 */
public class SizeTextField extends TextField {
    private final int maxDigits = 5;
    private final double textFieldMaxWidth = 60;
    private final double borderWidth = 3.0;

    /**
     * Constructs a SizeTextField with the specified value as text.
     * @param size the size to set the text field to
     */
    public SizeTextField(int size, ChangeListener<String> sizeFieldChangeListener) {
        super(Integer.toString(size));
        this.setPrefColumnCount(maxDigits);
        this.setPrefWidth(textFieldMaxWidth);
        this.positionCaret(this.getText().length());
        this.setAlignment(Pos.CENTER_RIGHT);
        BorderWidths widths = new BorderWidths(borderWidth);
        BorderStroke stroke = new BorderStroke(
            null, null, null, widths);
        this.setBorder(new Border(stroke));
        Tooltip sizeTooltip = new Tooltip("Enter the width or height in pixels for the slides.");
        this.setTooltip(sizeTooltip);
        this.addEventFilter(KeyEvent.KEY_TYPED,
            e -> {
                filterSizeFieldKeyInput(e);
            });
        this.focusedProperty().addListener(sizeFieldListener());
        this.textProperty().addListener(sizeFieldChangeListener);
    }

    private void filterSizeFieldKeyInput(KeyEvent e) {
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

    private InvalidationListener sizeFieldListener() {
        InvalidationListener listener = observable -> {
            String text = this.getText();
            int value = Integer.parseInt(text);
            if (value < SlideSize.MIN_SIZE) {
                this.setText(Integer.toString(SlideSize.MIN_SIZE));
            }
            // don't need to check for max size because of maxDigits limit
            this.positionCaret(this.getText().length());
        };
        return listener;
    }
}
