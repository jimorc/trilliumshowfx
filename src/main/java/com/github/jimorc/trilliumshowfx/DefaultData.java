package com.github.jimorc.trilliumshowfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javafx.scene.control.Alert;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class to manage default data from a JSON file.
 */
public class DefaultData {
    private String filePath;
    private JSONObject jsonObject;

    /**
     * Constructor to load defaults from a JSON file.
     * @param jsonFile The JSON file containing default data.
     */
    public DefaultData(File jsonFile) {
        filePath = jsonFile.getAbsolutePath();
        InputStream fis = null;
        try {
            fis = new FileInputStream(jsonFile);
        } catch (FileNotFoundException e) {
            this.jsonObject = new JSONObject();
        }
        String json = "{}";
        if (fis != null) {
            try {
                json = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                // Ignore exception. Uses default empty JSON.
            }
        }
        this(json);
    }

    /**
     * Constructor to load defaults from a JSON string. This constructor is called
     * by the File constructor after reading the file contents.
     * @param jsonString The JSON string containing default data.
     */
    public DefaultData(String jsonString) {
        String js = jsonString;
        try {
            // Try to parse the JSON to ensure it's valid.
            new JSONObject(js);
        } catch (JSONException e) {
            // If parsing fails, use empty JSON object.
            js = "{}";
        }
        jsonObject = new JSONObject(js);
    }

    /**
     * Get the SlideSize from the defaults.
     * @return The SlideSize object.
     */
    public SlideSize getSlideSize() {
        JSONObject ssObj = jsonObject.optJSONObject("slide_size");
        SlideSize slideSize = null;
        if (ssObj != null) {
            int width = ssObj.getInt("width");
            int height = ssObj.getInt("height");
            slideSize = new SlideSize(width, height);
        } else {
            slideSize = new SlideSize();
            jsonObject.put("slide_size", slideSize.toJson());
            if (filePath != null) {
                saveDefaults();
            }
        }
        return slideSize;
    }

    /**
     * Save the current defaults back to the JSON file.
     */
    public void saveDefaults() {
        File defFile = new File(filePath);
        if (!defFile.exists()) {
            try {
                Files.createDirectories(defFile.getParentFile().toPath());
                defFile.createNewFile();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Creating Defaults File");
                alert.setHeaderText("Could not create file: " + filePath);
                alert.setContentText(e.getMessage()
                    + "\nClick OK to proceed without saving.");
                alert.showAndWait();
                return;
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            fos.write(jsonObject.toString().getBytes());
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Saving Defaults");
            alert.setHeaderText("Could not save defaults to file: " + filePath);
            alert.setContentText(e.getMessage()
                + "\nClick OK to proceed without saving.");
            alert.showAndWait();
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    // ignore exception on close
                }
            }
        }
    }
}
