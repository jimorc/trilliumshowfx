package com.github.jimorc.flexishowbuilder;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.tinylog.Logger;

/**
 * FlexiBeans handles parsing the CSV input into FlexiBean objects.
 */
public class FlexiBeans {
    private List<FlexiBean> beans;

    /**
     * Constructor that takes an InputStream for the CSV data.
     * @param csvInputStream InputStream of CSV data
     * @throws CSVException if an error occurs during CSV parsing
     */
    public FlexiBeans(InputStream csvInputStream) throws CSVException {
        Logger.trace("In FlexiBeans constructor(InputStream)");
        try (InputStreamReader reader = new InputStreamReader(csvInputStream)) {
            CsvToBean<FlexiBean> csvToBean = new CsvToBeanBuilder<FlexiBean>(reader)
                    .withType(FlexiBean.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            beans = csvToBean.parse();
            Logger.debug("Parsed {} beans from CSV input", beans.size());
            for (FlexiBean bean : beans) {
                Logger.trace("{}\n", bean.toString());
            }
        } catch (IOException e) {
            throw new CSVException(BuilderGUI.buildLogMessage(
                "Error reading CSV input stream:\n", e.toString()));
        }
    }

    /**
     * Constructor that takes a File for the CSV data.
     * @param csvF File object for CSV data
     * @throws CSVException if an error occurs during CSV parsing
     * @throws CSVException if the File is null or not a file
     * @throws CSVException if an IOException occurs during parsing
     */
    public FlexiBeans(File csvF) throws CSVException {
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
        try (InputStreamReader reader = new InputStreamReader(new java.io.FileInputStream(csvF))) {
            CsvToBean<FlexiBean> csvToBean = new CsvToBeanBuilder<FlexiBean>(reader)
                    .withType(FlexiBean.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            beans = csvToBean.parse();
            Logger.debug("Parsed {} beans from CSV file {}", beans.size(), csvF.getAbsolutePath());
            for (FlexiBean bean : beans) {
                Logger.trace("{}\n", bean.toString());
            }
        } catch (IOException e) {
            throw new CSVException(BuilderGUI.buildLogMessage(
                "Error parsing CSV file " + csvF.getAbsolutePath() + ":\n", e.toString()));
        }
    }

    /**
     * Get the list of FlexiBean objects.
     * @return list of FlexiBean objects
     */
    public List<FlexiBean> getBeans() {
        return beans;
    }
}
