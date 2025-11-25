package com.github.jimorc.flexishowbuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private CSVLine[] lines = new CSVLine[0];
    private Map<String, ImageAndPersonLine[]> fullNameMap;
    private Set<String> fullNameKeys;
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
        loadCSVFile();
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
    protected Map<String, ImageAndPersonLine[]> getHashMap() {
        return fullNameMap;
    }

    /**
     * Retrieve the Set of name keys. This method is provided only for test purposes.
     * @return the Set of name keys
     */
    protected Set<String> getFullNameKeys() {
        return fullNameKeys;
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
            ImageAndPersonLine[] personLines = fullNameMap.get(name);
            Person p = new Person(personLines[0].getPersonFirstName(), personLines[0].getPersonLastName());
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
    protected void loadCSVFile() throws IOException, CSVException {
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
     * @return a string representation of the CSV object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(FlexiBean.CSV_HEADER);
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
     * Retrieve the input CSV line corresponding to the specified index.
     * @param index The number of the line to retrieve
     * @return the line specified by index
     */
    public CSVLine getLine(int index) {
        if (index < 0 || index >= lines.length) {
            Logger.error(BuilderGUI.buildLogMessage(
                "Invalid index in InputCSV.getLine: ", Integer.toString(index)));
            throw new ArrayIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return lines[index];
    }

    /**
     * Retrieve the input CSV lines corresponding to the name provided by the argument.
     * @param fullName the full name of the person to retrieve the lines for.
     * @return All input CSV lines for the named person
     * @throws CSVException when there are no lines for the specified person.
     */
    public ImageAndPersonLine[] getImageLines(String fullName) throws CSVException {
        if (fullNameMap.containsKey(fullName)) {
            return fullNameMap.get(fullName);
        } else {
            Logger.error(BuilderGUI.buildLogMessage(
                "There are no CSVLines for: ", fullName));
            throw new CSVException("The CSV file does not contain lines for " + fullName);
        }
    }

    private void buildFullNameHashMap() {
        Logger.trace("In InputCSV.buildFullNameHashMap");
        fullNameMap = new HashMap<>();
        boolean firstLine = true;
        for (CSVLine line : lines) {
            if (firstLine) {
                firstLine = false;
                continue; // skip header line
            }
            ImageAndPersonLine ipLine = (ImageAndPersonLine) line;
            String fullName = ipLine.getPersonFullName();
            if (!fullNameMap.containsKey(fullName)) {
                Logger.debug(BuilderGUI.buildLogMessage(
                    "Adding ", fullName, " to hash map"));
                fullNameMap.put(fullName, new ImageAndPersonLine[] {ipLine});
            } else {
                ImageAndPersonLine[] existingLines = fullNameMap.get(fullName);
                ImageAndPersonLine[] newLines = new ImageAndPersonLine[existingLines.length + 1];
                System.arraycopy(existingLines, 0, newLines, 0, existingLines.length);
                newLines[existingLines.length] = ipLine;
                fullNameMap.put(fullName, newLines);
            }
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
        for (int i = 1; i < lines.length; i++) { // skip header line
            ImageAndPersonLine ipLine = (ImageAndPersonLine) lines[i];
            String imageFileName = ipLine.getImageFileName();
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
     * @return
     * @throws CSVException
     */
    protected Exception validateCSVFile() throws CSVException {
        final int headerLineSize = 5;
        final int fileNameLine = 0;
        final int titleLine = 1;
        final int fullNameLine = 2;
        final int firstNameLine = 3;
        final int lastNameLine = 4;
        if (lines.length == 0) {
            throw new CSVException("No data found in CSV file " + getFileName());
        }
        // validate header line
        CSVLine headerLine = lines[0];
        if (headerLine.length() != headerLineSize) {
            throw new CSVException("Invalid header found in CSV file " + getFileName());
        }
        if (!headerLine.field(fileNameLine).equalsIgnoreCase("Filename")
            || !headerLine.field(titleLine).equalsIgnoreCase("Title")
            || !headerLine.field(fullNameLine).equalsIgnoreCase("Full Name")
            || !headerLine.field(firstNameLine).equalsIgnoreCase("First Name")
            || !headerLine.field(lastNameLine).equalsIgnoreCase("Last Name")) {
            throw new CSVException("Invalid header: "
                + headerLine.toString() + " found in CSV file " + getFileName());
        }
        // check for missing images
        List<String> missingImages = getListOfMissingImages();
        if (missingImages.size() > 0) {
            String msg = "Some images listed in CSV file " + getFileName() + " are missing:\n";
            for (String img : missingImages) {
                msg += img + "\n";
            }
            throw new CSVException(msg);
        }
        return null; // no errors found
    }
}
