package com.github.jimorc.trilliumshowfx;

/**
 * Class to represent the size of a slide.
 */
public class SlideSize {
    public static final int DEFAULT_WIDTH = 1400;
    public static final int DEFAULT_HEIGHT = 1050;
    private static final int MIN_SIZE = 100;
    private static final int MAX_SIZE = 99999;
    private int width;
    private int height;

    /**
     * Default constructor. Sets width and height to default values.
     */
    public SlideSize() {
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
    }

    /**
     * Constructor with specified width and height.
     * @param width The width of the slide.
     * @param height The height of the slide.
     */
    public SlideSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    /**
     * Get the width of the slide.
     * @return The width of the slide.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the slide.
     * @return The height of the slide.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set the width of the slide.
     * @param width The width to set.
     */
    public void setWidth(int width) {
        this.width = width;
        if (width < MIN_SIZE) {
            this.width = MIN_SIZE;
        } else if (width > MAX_SIZE) {
            this.width = MAX_SIZE;
        }
    }

    /**
     * Set the height of the slide.
     * @param height The height to set.
     */
    public void setHeight(int height) {
        this.height = height;
        if (height < MIN_SIZE) {
            this.height = MIN_SIZE;
        } else if (height > MAX_SIZE) {
            this.height = MAX_SIZE;
        }
    }
}
