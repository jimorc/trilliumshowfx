package com.github.jimorc.flexishowbuilder;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
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
     * Get the list of FlexiBean objects.
     * @return list of FlexiBean objects
     */
    public List<FlexiBean> getBeans() {
        return beans;
    }
}
