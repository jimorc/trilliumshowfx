package com.github.jimorc.flexishowbuilder;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.tinylog.Logger;

/**
 * FlexiBeans handles parsing the CSV input into FlexiBean objects.
 */
public class FlexiBeans {
    private List<FlexiBean> beans;

    /**
     * Default constructor.
     */
    public FlexiBeans() {
        Logger.trace("In FlexiBeans default constructor");
        beans = new java.util.ArrayList<FlexiBean>();
    }

    /**
     * Constructor that takes an InputStream for the CSV data.
     * @param csvInputStream InputStream of CSV data
     */
    public FlexiBeans(InputStream csvInputStream) throws BadHeaderException, CSVException {
        this();
        Logger.trace("In FlexiBeans constructor(InputStream)");
        parseInputStreamReader(new InputStreamReader(csvInputStream));
    }

    /**
     * Constructor that takes a File for the CSV data.
     * @param csvF File object for CSV data
     * @throws CSVException if an error occurs during CSV parsing
     * @throws CSVException if the File is null or not a file
     * @throws CSVException if the file is not found. If this happens, there is likely
     *                      a programming error somewhere else in the code.
     */
    public FlexiBeans(File csvF) throws BadHeaderException, CSVException, FileNotFoundException {
        this();
        Logger.trace("In FlexiBeans constructor(File)");
        if (csvF == null) {
            Logger.error("FlexiBeans constructor was passed a null CSV file object");
            throw new CSVException("Trying to read a null CSVFile.\n"
                + "This may be a programming error.\n"
                + "Please report this.");
        }
        if (!csvF.isFile()) {
            Logger.error(csvF.getAbsolutePath(), " passed to FlexiBeans constructor, is not a file");
            throw new CSVException("Trying to read " + csvF.getAbsolutePath()
                + " which is not a file.\nThis may be a programming error.\n"
                + "Please report this.");
        }
        InputStreamReader reader = new InputStreamReader(new java.io.FileInputStream(csvF));
        parseInputStreamReader(reader);
    }

    /**
     * Get the list of FlexiBean objects.
     * @return list of FlexiBean objects
     */
    public List<FlexiBean> getBeans() {
        return beans;
    }

    /**
     * Get the list of Person objects represented by the FlexiBean objects.
     * @return list of Person objects
     */
    public List<Person> getPersons() {
        List<Person> fullNames = new ArrayList<Person>();
        for (FlexiBean bean : beans) {
            Person person = new Person(bean.getFirstName(), bean.getLastName());
            if (!fullNames.contains(person)) {
                fullNames.add(new Person(bean.getFirstName(), bean.getLastName()));
            }
        }
        return fullNames;
    }

    /**
     * Append a FlexiBean to the list.
     * @param bean FlexiBean to append
     */
    public void append(FlexiBean bean) {
        beans.add(bean);
    }

    /**
     * Sort the FlexiBean objects according to the specified SortOrder.
     * @param order SortOrder to use for sorting
     */
    public void sort(SortOrder order) {
        Map<String, FlexiBeans> beanMap = generateBeanMap();
        List<String> fullNames = generateFullNamesList();
        switch (order) {
            case AsIs:
                sortBeans(beanMap, fullNames);
                break;
            case AlphabeticalByFullName:
                fullNames.sort(String::compareToIgnoreCase);
                sortBeans(beanMap, fullNames);
                break;
            case AlphabeticalByFullNameReverse:
                fullNames.sort((a, b) -> b.compareToIgnoreCase(a));
                sortBeans(beanMap, fullNames);
                break;
            case AlphabeticalByLastNameThenFirstName:
                fullNames.sort((a, b) -> {
                    String[] aParts = a.split(" ");
                    String[] bParts = b.split(" ");
                    String aLastName = aParts[aParts.length - 1];
                    String bLastName = bParts[bParts.length - 1];
                    int lastNameCompare = aLastName.compareToIgnoreCase(bLastName);
                    if (lastNameCompare != 0) {
                        return lastNameCompare;
                    } else {
                        return a.compareToIgnoreCase(b);
                    }
                });
                sortBeans(beanMap, fullNames);
                break;
            case AlphabeticalByLastNameThenFirstNameReverse:
                fullNames.sort((a, b) -> {
                    String[] aParts = a.split(" ");
                    String[] bParts = b.split(" ");
                    String aLastName = aParts[aParts.length - 1];
                    String bLastName = bParts[bParts.length - 1];
                    int lastNameCompare = bLastName.compareToIgnoreCase(aLastName);
                    if (lastNameCompare != 0) {
                        return lastNameCompare;
                    } else {
                        return b.compareToIgnoreCase(a);
                    }
                });
                sortBeans(beanMap, fullNames);
                break;
            default:
                Logger.error("Sort order ", order.toString(), " not yet implemented.");
                throw new UnsupportedOperationException("Sort order " + order.toString() + " not yet implemented.");
        }
    }

    private void sortBeans(Map<String, FlexiBeans> beanMap, List<String> fullNames) {
        List<FlexiBean> sortedBeans = new ArrayList<FlexiBean>();
        for (String fullName : fullNames) {
            FlexiBeans flexiBeans = beanMap.get(fullName);
            sortedBeans.addAll(flexiBeans.getBeans());
        }
        beans = sortedBeans;
    }

    // Need to suppress checkstyle IllegalCatch here because
    // openCSV CsvToBean.parse() may throw RuntimeException.
    // We need to catch that and re-throw as CSVException or BadHeaderException.
    @SuppressWarnings("checkstyle:IllegalCatch")
    private void parseInputStreamReader(InputStreamReader reader)
            throws BadHeaderException, CSVException {
        CsvToBean<FlexiBean> csvToBean = new CsvToBeanBuilder<FlexiBean>(reader)
                .withType(FlexiBean.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        // openCSV parse() may throw RuntimeException. We need to catch it
        // and re-throw as our own exception.
        try {
            beans = csvToBean.parse();
        } catch (RuntimeException e) {
            if (e instanceof RuntimeException) {
                Logger.error(e, "RuntimeException caught during CSV parsing");
                if (e.getMessage().contains("Error capturing CSV header!")) {
                    throw new BadHeaderException(e.getMessage());
                }
                throw new CSVException("Error parsing CSV input: " + e.getMessage());
            }
        }
        Logger.debug("Parsed {} beans from CSV input", beans.size());
        for (FlexiBean bean : beans) {
            Logger.trace("{}\n", bean.toString());
        }
    }

    private Map<String, FlexiBeans> generateBeanMap() {
        Map<String, FlexiBeans> beanMap = new HashMap<>();
        for (FlexiBean bean : beans) {
            beanMap.putIfAbsent(bean.getFullName(), new FlexiBeans());
            beanMap.get(bean.getFullName()).append(bean);
        }
        return beanMap;
    }

    private List<String> generateFullNamesList() {
        List<String> fullNames = new ArrayList<String>();
        for (FlexiBean bean : beans) {
            if (!fullNames.contains(bean.getFullName())) {
                fullNames.add(bean.getFullName());
            }
        }
        return fullNames;
    }
}
