package com.github.jimorc.trilliumshowfx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit tests for DefaultData class.
 */
public class DefaultDataTests {
    @Test
    public void testConstructorValidString() {
        final int width = 1920;
        final int height = 1080;
        String jsonContent = "{ \"slide_size\": { \"width\": 1920, \"height\": 1080 },"
            + "\"createStartEndSlides\": \"true\" }";
        DefaultData data = new DefaultData(jsonContent);
        SlideSize slideSize = data.getSlideSize();
        assertEquals(width, slideSize.getWidth());
        assertEquals(height, slideSize.getHeight());
        assertFalse(data.getCreateStartEndSlides());
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
        assertTrue(data.getCreateStartEndSlides());
    }

    @Test
    public void testConstructorMissingFile() {
        final int width = 1400;
        final int height = 1050;
        File jsonFile = new File("testing/data/nonexistent.json");
        DefaultData data = new DefaultData(jsonFile);
        SlideSize slideSize = data.getSlideSize();
        boolean ses = data.getCreateStartEndSlides();
        assertEquals(width, slideSize.getWidth());
        assertEquals(height, slideSize.getHeight());
        assertTrue(ses);
        if (jsonFile.exists()) {
            jsonFile.delete();
        }
    }

    @Test
    public void testConstructorInvalidJson() {
        final int width = 1400;
        final int height = 1050;
        String invalidJson = "{ invalid json ";
        DefaultData data = new DefaultData(invalidJson);
        SlideSize slideSize = data.getSlideSize();
        assertEquals(width, slideSize.getWidth());
        assertEquals(height, slideSize.getHeight());
        assertTrue(data.getCreateStartEndSlides());
    }

    @Test
    public void testSaveDefaults() {
        File jsonFile = new File("temp_defaults.json");
        DefaultData data = new DefaultData(jsonFile);
        // force changes to trigger save
        data.getSlideSize();
        try (BufferedReader jsonFileReader = new BufferedReader(
                new FileReader(jsonFile.getAbsolutePath()))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = jsonFileReader.readLine()) != null) {
                sb.append(line);
            }
            String fileContent = sb.toString();
            assertTrue(fileContent.contains("createStartEndSlides\":\"true\""));
            assertTrue(fileContent.contains("\"slide_size\":{\"width\":1400,\"height\":1050}"));
        } catch (IOException e) {
            fail(e.getCause().toString());
        }
        if (jsonFile.exists()) {
            jsonFile.delete();
        }
    }
}
