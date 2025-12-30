package com.github.jimorc.trilliumshowfx;

import java.io.File;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for DefaultData class.
 */
public class DefaultDataTests {
    @Test
    public void testConstructorValidString() {
        final int width = 1920;
        final int height = 1080;
        String jsonContent = "{ \"slide_size\": { \"width\": 1920, \"height\": 1080 } }";
        DefaultData data = new DefaultData(jsonContent);
        SlideSize slideSize = data.getSlideSize();
        assertEquals(width, slideSize.getWidth());
        assertEquals(height, slideSize.getHeight());
    }

    @Test
    public void testConstructorDefaultFile() {
        final int width = 1024;
        final int height = 768;
        File jsonFile = new File("testing/data/defaults.json");
        DefaultData data = new DefaultData(jsonFile);
        SlideSize slideSize = data.getSlideSize();
        assertEquals(width, slideSize.getWidth());
        assertEquals(height, slideSize.getHeight());
    }

    @Test
    public void testConstructorMissingFile() {
        final int width = 1400;
        final int height = 1050;
        File jsonFile = new File("testing/data/nonexistent.json");
        DefaultData data = new DefaultData(jsonFile);
        SlideSize slideSize = data.getSlideSize();
        assertEquals(width, slideSize.getWidth());
        assertEquals(height, slideSize.getHeight());
        jsonFile = new File("nonexistent.json");
        if (jsonFile.exists()) {
            jsonFile.delete();
        }
    }
}
