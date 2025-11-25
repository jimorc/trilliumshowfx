package com.github.jimorc.flexishowbuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.tinylog.Logger;

/**
 * XLSWorkbook generates an Apache POI workbook that can then be saved as an XLS file.
 */
public final class XLSWorkbook {
    private Workbook workbook;

    /**
     * Constructor builds a POI workbook from the input CSV object.
     * @param csv the OutputCSV object used to create the workbook content.
     * */
    public XLSWorkbook(final OutputCSV csv) {
        workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("CSVToXLS");
        FlexiBeans beans = csv.getBeans();
        int rowIndex = 0;
        for (FlexiBean bean : beans.getBeans()) {
            int colNum = 0;
            Logger.debug(BuilderGUI.buildLogMessage(
                "XLSWorkbook creating sheet line for bean: ", bean.toString()));
            Row row = sheet.createRow(rowIndex++);
            Cell cell0 = row.createCell(colNum++);
            cell0.setCellValue(bean.getFilename());
            Cell cell1 = row.createCell(colNum++);
            cell1.setCellValue(bean.getTitle());
            Cell cell2 = row.createCell(colNum++);
            cell2.setCellValue(bean.getFullName());
            Cell cell3 = row.createCell(colNum++);
            cell3.setCellValue(bean.getFirstName());
            Cell cell4 = row.createCell(colNum);
            cell4.setCellValue(bean.getLastName());
        }
    }

    /**
     * Write the workbook to the specified XLS file.
     * @param fileName the file to write the XLS data to.
     * @throws IOException thrown when the file write fails.
     */
    public void writeToFile(String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        workbook.write(fos);
        fos.close();
        workbook.close();
        Logger.debug("output.xls written successfully");
    }
}
