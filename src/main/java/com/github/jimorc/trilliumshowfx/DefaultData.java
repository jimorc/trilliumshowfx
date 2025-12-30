package com.github.jimorc.trilliumshowfx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

/**
 * Class to manage default data from a JSON file.
 */
public class DefaultData {
    private String fileName;
    private JSONObject jsonObject;

    /**
     * Constructor to load defaults from a JSON file.
     * @param jsonFile The JSON file containing default data.
     */
    public DefaultData(File jsonFile) {
        fileName = jsonFile.getName();
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
        jsonObject = new JSONObject(jsonString);
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
            jsonObject.put("slide_size", slideSize);
            if (fileName != null) {
                saveDefaults();
            }
        }
        return slideSize;
    }

    /**
     * Save the current defaults back to the JSON file.
     */
    public void saveDefaults() {
        final int indentFactor = 4;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            fos.write(jsonObject.toString(indentFactor).getBytes());
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
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
