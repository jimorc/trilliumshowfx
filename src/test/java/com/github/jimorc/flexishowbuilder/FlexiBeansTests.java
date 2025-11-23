package com.github.jimorc.flexishowbuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * FlexiBeansTests contains tests for the FlexiBeans class.
 */
public class FlexiBeansTests {
    @Test
    public void test5ColumnsInputStreamConstructor() {
        String csvData = "Filename,Caption,Full Name,First Name,Last Name\n"
                + "image1.jpg,An image,John Doe,John,Doe\n"
                + "image2.jpg,Another image,Jane Smith,Jane,Smith\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        FlexiBeans flexiBeans = null;
        try {
            flexiBeans = new FlexiBeans(csvInputStream);
        } catch (CSVException e) {
            throw new RuntimeException(e);
        }
        List<FlexiBean> beans = flexiBeans.getBeans();
        assertEquals(2, beans.size());
        FlexiBean bean1 = beans.get(0);
        assertEquals("image1.jpg", bean1.getFilename());
        assertEquals("An image", bean1.getCaption());
        assertEquals("John Doe", bean1.getFullName());
        assertEquals("John", bean1.getFirstName());
        assertEquals("Doe", bean1.getLastName());
        FlexiBean bean2 = beans.get(1);
        assertEquals("image2.jpg", bean2.getFilename());
        assertEquals("Another image", bean2.getCaption());
        assertEquals("Jane Smith", bean2.getFullName());
        assertEquals("Jane", bean2.getFirstName());
        assertEquals("Smith", bean2.getLastName());
    }

    @Test
    public void test8ColumnsInputStreamConstructor() {
        String csvData = "Filename,Column2,Caption,Full Name,First Name,Last Name,Extra1,Extra2\n"
                + "image1.jpg,,An image,John Doe,John,Doe,Data1,Data2\n"
                + "image2.jpg,Dummy,Another image,Jane Smith,Jane,Smith,Data3,Data4\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        FlexiBeans flexiBeans = null;
        try {
            flexiBeans = new FlexiBeans(csvInputStream);
        } catch (CSVException e) {
            throw new RuntimeException(e);
        }
        List<FlexiBean> beans = flexiBeans.getBeans();
        assertEquals(2, beans.size());
        FlexiBean bean1 = beans.get(0);
        assertEquals("image1.jpg", bean1.getFilename());
        assertEquals("An image", bean1.getCaption());
        assertEquals("John Doe", bean1.getFullName());
        assertEquals("John", bean1.getFirstName());
        assertEquals("Doe", bean1.getLastName());
        FlexiBean bean2 = beans.get(1);
        assertEquals("image2.jpg", bean2.getFilename());
        assertEquals("Another image", bean2.getCaption());
        assertEquals("Jane Smith", bean2.getFullName());
        assertEquals("Jane", bean2.getFirstName());
        assertEquals("Smith", bean2.getLastName());
    }

    @Test
    public void testMissingOptionalColumnsInputStreamConstructor() {
        String csvData = "Filename,Caption\n"
                + "image1.jpg,An image\n"
                + "image2.jpg,Another image\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        FlexiBeans flexiBeans = null;
        try {
            flexiBeans = new FlexiBeans(csvInputStream);
        } catch (CSVException e) {
            throw new RuntimeException(e);
        }
        List<FlexiBean> beans = flexiBeans.getBeans();
        assertEquals(2, beans.size());
        FlexiBean bean1 = beans.get(0);
        assertEquals("image1.jpg", bean1.getFilename());
        assertEquals("An image", bean1.getCaption());
        assertEquals(null, bean1.getFullName());
        assertEquals(null, bean1.getFirstName());
        assertEquals(null, bean1.getLastName());
        FlexiBean bean2 = beans.get(1);
        assertEquals("image2.jpg", bean2.getFilename());
        assertEquals("Another image", bean2.getCaption());
        assertEquals(null, bean2.getFullName());
        assertEquals(null, bean2.getFirstName());
        assertEquals(null, bean2.getLastName());
    }
}
