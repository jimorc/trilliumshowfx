package com.github.jimorc.trilliumshowfx;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.tinylog.Logger;

/**
 * The InputCSV class reads the CSV file and stores multiple CSVLine objects.
 *
 *
 * ```java
 * File f = new File("<CSV-file-name>")
 * InputCSV csv = new InputCSV(f);
 * ```
 */
public final class InputCSV {
    private File csvFile;
    private FlexiBeans flexiBeans;
    private Map<String, FlexiBeans> fullNameMap;
    private ArrayList<String> sortedFullNames;

    /** This constructor parses the specified CSV file and builds an InputCSV
     * object from the file's contents.
     * @param csvF is the File containing the CSV data to parse.
     * @throws CSVException if csvF is null.
     * @throws CSVException if csvF is not a file (i.e directory, link, etc.)
     * @throws CSVException if csvF contains an invalid header line.
     * @throws CSVException if csvF contains an invalid line.
     * @throws IOException if the file cannot be read.
     */
    public InputCSV(File csvF) throws CSVException, IOException {
        Logger.trace("In InputCSV constructor");
        flexiBeans = new FlexiBeans();
        csvFile = csvF;
        if (csvF == null) {
            Logger.error("InputCSV constructor was passed a null CSV file object");
            throw new CSVException("Trying to read a null CSVFile");
        }
        if (!csvF.isFile()) {
            Logger.error(csvF.getAbsolutePath(), " passed to InputCSV constructor, is not a file");
            throw new CSVException("Trying to read " + csvF.getAbsolutePath()
                + " which is not a file.");
        }
        try {
            loadCSVFile();
        } catch (BadHeaderException bhe) {
            Logger.error("BadHeaderException caught trying to read CSV file ", csvFile.getAbsolutePath());
            try {
                BuilderGUI.handleBadHeaderException(bhe);
            } catch (ExceptionInInitializerError e) {
                // Ignore this. It is thrown in handleBadHeaderException during testing.
            }
        }
        buildFullNameHashMap();
    }

    /**
     * Returns the name of the CSV file.
     * @return the CSV file name
     */
    public String getFileName() {
        if (csvFile != null) {
            return csvFile.getName();
        } else {
            return null;
        }
    }

    /**
     * Returns the directory containing the CSV file.
     * @return the CSV file directory, or null if no file is loaded.
     */
    public String getFileDir() {
        if (csvFile != null) {
            return csvFile.getParent();
        } else {
            return null;
        }
    }

    /**
     * Retrieve the HashMap for the lines in the CSV file. This method is only provided for test
     * purposes.
     * @return the HashMap containing the CSV file lines
     */
    protected Map<String, FlexiBeans> getHashMap() {
        return fullNameMap;
    }

    /**
     * Retrieve the FlexiBeans in this object.
     * @return the FlexiBeans in this object
     */
    protected FlexiBeans getBeans() {
        return flexiBeans;
    }

    /**
     * Retrieve the number of FlexiBean's in the CSV.
     * @return the number of FlexiBean's in the CSV.
     */
    public int getNumberOfBeans() {
        return flexiBeans.getBeans().size();
    }

    /**
     * Return a Person object if there is one or more lines containing that name in the CSV lines.
     * @param name - the full name of the person to retrieve a Person object for.
     * @return a Person object for the named person or null if not in CSV object.
     */
    public Person getPerson(String name) throws CSVException {
        Logger.debug(BuilderGUI.buildLogMessage(
            "Retrieving Person info for ", name));
        for (FlexiBean bean : flexiBeans.getBeans()) {
            if (bean.getFullName().equals(name)) {
                Person p = new Person(bean.getFirstName(), bean.getLastName());
                Logger.debug(BuilderGUI.buildLogMessage(
                    "getPerson returning: ", p.toString()));
                return p;
            }
        }
        if (fullNameMap.containsKey(name)) {
            FlexiBeans beans = fullNameMap.get(name);
            Person p = new Person(beans.getBeans().get(0).getFirstName(),
                beans.getBeans().get(0).getFirstName());
            Logger.debug(BuilderGUI.buildLogMessage(
                "getPerson returning: ", p.toString()));
            return p;
        }
        throw new CSVException("Programming error: Trying to retrieve info for " + name + " but it does not exist.");
    }

    /**
     * Returns a list of sorted full names that appear in the CSV file.
     * @return sorted list of full names.
     */
    protected ArrayList<String> getSortedFullNames() {
        return sortedFullNames;
    }

    /**
     * Loads the CSV file specified by the fileName field.
     * The lines field is populated with CSVLine objects.
     * If there is an error reading the file, an error message is
     * displayed and the program exits.
     *
     * This file is protected rather than private so that
     * it can called for testing purposes.
     */
    protected void loadCSVFile() throws IOException, CSVException, BadHeaderException {
        Logger.trace("In InputCSV.loadCSVFile");
        String dir = getFileDir();
        java.nio.file.Path path = java.nio.file.Paths.get(dir, getFileName());
        csvFile = path.toFile();
        try {
            flexiBeans = new FlexiBeans(csvFile);
        } catch (BadHeaderException bhe) {
            Logger.error("BadHeaderException caught trying to read CSV file ", csvFile.getAbsolutePath());
            try {
                BuilderGUI.handleBadHeaderException(bhe);
            } catch (ExceptionInInitializerError e) {
                // Ignore this. It is thrown in handleBadHeaderException during testing.
            }
        }
    }

    /**
     * Returns a string representation of the CSV object.
     * @return a string representation of the CSV object
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(FlexiBean.HEADER_BEAN.toString());
        for (FlexiBean bean : flexiBeans.getBeans()) {
            sb.append("\n");
            sb.append(bean.toString());
        }
        return sb.toString();
    }

    /**
     * Inserts a CSVLine at the specified index.
     * @param index - the index at which to insert the line
     * @param bean  - the FlexiBean to insert
     * @throws ArrayIndexOutOfBoundsException if the index is negative, or
     * greater than the number of lines.
     */
    public void insertAt(int index, FlexiBean bean) throws ArrayIndexOutOfBoundsException {
        if (index < 0 || index > flexiBeans.getBeans().size()) {
            Logger.error(BuilderGUI.buildLogMessage(
                "Index out of bounds in InputCSV.insertAt: ", Integer.toString(index)));
            throw new ArrayIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        FlexiBeans beans = new FlexiBeans();
        for (int i = 0; i < index; i++) {
            beans.append(flexiBeans.getBeans().get(i));
        }
        beans.append(bean);
        for (int i = index; i < flexiBeans.getBeans().size(); i++) {
            beans.append(flexiBeans.getBeans().get(i));
        }
        flexiBeans = beans;
    }

    /**
     * Appends a CSVLine to the end of the CSV object.
     * @param bean - the FlexiBean to append
     */
    public void append(FlexiBean bean) {
        insertAt(flexiBeans.getBeans().size(), bean);
    }

    /**
     * Retrieve the input FlexiBean corresponding to the specified index.
     * @param index The number of the FlexiBean to retrieve
     * @return the FlexiBean specified by index
     */
    public FlexiBean getBean(int index) {
        if (index < 0 || index >= flexiBeans.getBeans().size()) {
            Logger.error(BuilderGUI.buildLogMessage(
                "Invalid index in InputCSV.getLine: ", Integer.toString(index)));
            throw new ArrayIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return flexiBeans.getBeans().get(index);
    }

    /**
     * Retrieve the input FlexiBeans corresponding to the name provided by the argument.
     * @param fullName the full name of the person to retrieve the lines for.
     * @return All input FlexiBeans for the named person
     * @throws CSVException when there are no FlexiBeans for the specified person.
     */
    public FlexiBeans getPersonBeans(String fullName) throws CSVException {
        if (fullNameMap.containsKey(fullName)) {
            FlexiBeans beans =  fullNameMap.get(fullName);
            Logger.debug(BuilderGUI.buildLogMessage(
                "Retrieved FlexiBeans for ", fullName, ": ", beans.toString()));
            for (FlexiBean bean : beans.getBeans()) {
                Logger.debug(BuilderGUI.buildLogMessage(
                    "Bean: ", bean.toString()));
            }
            return beans;
        } else {
            Logger.error(BuilderGUI.buildLogMessage(
                "There are no CSVLines for: ", fullName));
            throw new CSVException("The CSV file does not contain lines for " + fullName);
        }
    }

    private void buildFullNameHashMap() {
        Logger.trace("In InputCSV.buildFullNameHashMap");
        fullNameMap = new HashMap<>();
        for (FlexiBean bean: flexiBeans.getBeans()) {
            String fullName = bean.getFullName();
            if (!fullNameMap.containsKey(fullName)) {
                fullNameMap.put(fullName, new FlexiBeans());
                Logger.debug(BuilderGUI.buildLogMessage(
                    "Adding ", fullName, " to hash map"));
            }
            FlexiBeans flexiBeansForName = fullNameMap.get(fullName);
            flexiBeansForName.append(bean);
        }
        Logger.debug(BuilderGUI.buildLogMessage(
            "fullNameHashMap: ", fullNameMap.toString()));
    }

    /**
     * Returns a list of image file names that are referenced in the CSV file
     * but do not exist in the same directory as the CSV file.
     * @return a list of missing image file names.
     */
    private List<String> getListOfMissingImages() {
        ArrayList<String> missingImages = new ArrayList<>();
        String dir = getFileDir();
        for (FlexiBean bean : flexiBeans.getBeans()) {
            String imageFileName = bean.getFilename();
            java.nio.file.Path imagePath = java.nio.file.Paths.get(dir, imageFileName);
            File imageFile = imagePath.toFile();
            if (!imageFile.isFile()) {
                missingImages.add(imageFileName);
            }
        }
        Logger.debug(BuilderGUI.buildLogMessage(
            "Image files not found in CSV file folder: ", missingImages.toString()));
        return missingImages;
    }

    /**
     * validateCSVFile validates the contents of the InputCSV file.
     * @return Exception if there is an error found, null otherwise.
     * @throws CSVException
     */
    public Exception validateCSVFile() throws CSVException {
        Logger.trace("In InputCSV.validateCSVFile");
        if (flexiBeans.getBeans().isEmpty()) {
            throw new CSVException("No data found in CSV file " + getFileName());
        }
        // check for missing images
        List<String> missingImages = getListOfMissingImages();
        if (missingImages.size() > 0) {
            String msg = "Some images listed in CSV file " + getFileName() + " are missing:\n";
            for (String img : missingImages) {
                msg += img + "\n";
            }
            msg += "\nProgram cannot continue.\nFix this and try again.";
            Logger.error(msg);
            throw new CSVException(msg);
        }
        return null; // no errors found
    }
}
