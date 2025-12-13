package com.github.jimorc.trilliumshowfx;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * TitleImageTests contains tests for the TitleImageClass.
 */
@ExtendWith(ApplicationExtension.class)
public class TitleImageTests {
    @Start
    private void start(Stage stage) {
        try {
            TitleImage.generateTitleImage("Test Caption Line1\nLine2", "test.jpg");
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        stage.setScene(new Scene(new HBox()));
        stage.show();
    }

    @Test
    void testGenerateTitleImage(FxRobot robot) {
        Path path1;
        String os = System.getProperty("os.name");

        if (os.contains("Windows")) {
            path1 = Path.of("testing\\data\\test_windows_image.jpg");
        } else if (os.contains("Mac OS")) {
            path1 = Path.of("testing/data/test_arial_image.jpg");
        } else {
            path1 = Path.of("testing/data/test_image.jpg");
        }
        Path path2 = Path.of("test.jpg");
        try (RandomAccessFile randomAccessFile1 = new RandomAccessFile(path1.toFile(), "r");
                RandomAccessFile randomAccessFile2 = new RandomAccessFile(path2.toFile(), "r")) {

            FileChannel ch1 = randomAccessFile1.getChannel();
            FileChannel ch2 = randomAccessFile2.getChannel();
            if (ch1.size() != ch2.size()) {
                fail("Files are not identical size");
            }
            long size = ch1.size();
            MappedByteBuffer m1 = ch1.map(FileChannel.MapMode.READ_ONLY, 0L, size);
            MappedByteBuffer m2 = ch2.map(FileChannel.MapMode.READ_ONLY, 0L, size);

            if (!m1.equals(m2)) {
                fail("Files are not identical content");
            }
        } catch (FileNotFoundException e) {
            fail("File not found: " + e.getMessage());
        } catch (IOException e) {
            fail("IO Error: " + e.getMessage());
        } finally {
            // cleanup
            // file is not deleted when running tests in VSCode on Windows.
            path2.toFile().delete();
        }
    }
}
