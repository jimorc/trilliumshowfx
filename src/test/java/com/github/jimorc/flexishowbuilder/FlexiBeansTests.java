package com.github.jimorc.flexishowbuilder;

import java.io.ByteArrayInputStream;
import java.io.File;
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
    public void testDefaultConstructor() {
        FlexiBeans flexiBeans = new FlexiBeans();
        assertEquals(0, flexiBeans.getBeans().size());
        FlexiBean bean1 = new FlexiBean();
        bean1.setFilename("test.jpg");
        flexiBeans.append(bean1);
        List<FlexiBean> beans = flexiBeans.getBeans();
        assertEquals(1, beans.size());
        assertEquals("test.jpg", beans.get(0).getFilename());
        FlexiBean bean2 = new FlexiBean();
        bean2.setFilename("example.png");
        flexiBeans.append(bean2);
        beans = flexiBeans.getBeans();
        assertEquals(2, beans.size());
        assertEquals("example.png", beans.get(1).getFilename());
    }

    @Test
    public void test5ColumnsInputStreamConstructor() {
        String csvData = "Filename,Title,Full Name,First Name,Last Name\n"
                + "image1.jpg,An image,John Doe,John,Doe\n"
                + "image2.jpg,Another image,Jane Smith,Jane,Smith\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        FlexiBeans flexiBeans = new FlexiBeans(csvInputStream);
        List<FlexiBean> beans = flexiBeans.getBeans();
        assertEquals(2, beans.size());
        FlexiBean bean1 = beans.get(0);
        assertEquals("image1.jpg", bean1.getFilename());
        assertEquals("An image", bean1.getTitle());
        assertEquals("John Doe", bean1.getFullName());
        assertEquals("John", bean1.getFirstName());
        assertEquals("Doe", bean1.getLastName());
        FlexiBean bean2 = beans.get(1);
        assertEquals("image2.jpg", bean2.getFilename());
        assertEquals("Another image", bean2.getTitle());
        assertEquals("Jane Smith", bean2.getFullName());
        assertEquals("Jane", bean2.getFirstName());
        assertEquals("Smith", bean2.getLastName());
    }

    @Test
    public void test8ColumnsInputStreamConstructor() {
        String csvData = "Filename,Column2,Title,Full Name,First Name,Last Name,Extra1,Extra2\n"
                + "image1.jpg,,An image,John Doe,John,Doe,Data1,Data2\n"
                + "image2.jpg,Dummy,Another image,Jane Smith,Jane,Smith,Data3,Data4\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        FlexiBeans flexiBeans = new FlexiBeans(csvInputStream);
        List<FlexiBean> beans = flexiBeans.getBeans();
        assertEquals(2, beans.size());
        FlexiBean bean1 = beans.get(0);
        assertEquals("image1.jpg", bean1.getFilename());
        assertEquals("An image", bean1.getTitle());
        assertEquals("John Doe", bean1.getFullName());
        assertEquals("John", bean1.getFirstName());
        assertEquals("Doe", bean1.getLastName());
        FlexiBean bean2 = beans.get(1);
        assertEquals("image2.jpg", bean2.getFilename());
        assertEquals("Another image", bean2.getTitle());
        assertEquals("Jane Smith", bean2.getFullName());
        assertEquals("Jane", bean2.getFirstName());
        assertEquals("Smith", bean2.getLastName());
    }

    @Test
    public void testMissingOptionalColumnsInputStreamConstructor() {
        String csvData = "Filename,Title\n"
                + "image1.jpg,An image\n"
                + "image2.jpg,Another image\n";
        InputStream csvInputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        FlexiBeans flexiBeans = new FlexiBeans(csvInputStream);
        List<FlexiBean> beans = flexiBeans.getBeans();
        assertEquals(2, beans.size());
        FlexiBean bean1 = beans.get(0);
        assertEquals("image1.jpg", bean1.getFilename());
        assertEquals("An image", bean1.getTitle());
        assertEquals(null, bean1.getFullName());
        assertEquals(null, bean1.getFirstName());
        assertEquals(null, bean1.getLastName());
        FlexiBean bean2 = beans.get(1);
        assertEquals("image2.jpg", bean2.getFilename());
        assertEquals("Another image", bean2.getTitle());
        assertEquals(null, bean2.getFullName());
        assertEquals(null, bean2.getFirstName());
        assertEquals(null, bean2.getLastName());
    }

    @Test
    public void testFileConstructor() {
        File file = new File("testing/data/test.csv");
        FlexiBeans flexiBeans = null;
        try {
            flexiBeans = new FlexiBeans(file);
        } catch (CSVException e) {
            throw new RuntimeException(e);
        }
        List<FlexiBean> beans = flexiBeans.getBeans();
        assertEquals(2, beans.size());
        FlexiBean bean1 = beans.get(0);
        assertEquals("image1.jpg", bean1.getFilename());
        assertEquals("Image One", bean1.getTitle());
        assertEquals("John Doe", bean1.getFullName());
        assertEquals("John", bean1.getFirstName());
        assertEquals("Doe", bean1.getLastName());
        FlexiBean bean2 = beans.get(1);
        assertEquals("image2.jpg", bean2.getFilename());
        assertEquals("Image, Two", bean2.getTitle());
        assertEquals("Jane Smith", bean2.getFullName());
        assertEquals("Jane", bean2.getFirstName());
        assertEquals("Smith", bean2.getLastName());
    }

    @Test
    public void testNullFileConstructor() {
        File file = null;
        try {
            new FlexiBeans(file);
        } catch (CSVException e) {
            assertEquals("Trying to read a null CSVFile.\n"
                + "This may be a programming error.\n"
                + "Please report this.", e.getMessage());
        }
    }

    @Test
    public void testNonExistentFileConstructor() {
        File file = new File("non_existent_file.csv");
        try {
            new FlexiBeans(file);
        } catch (CSVException e) {
            assertEquals("Trying to read " + file.getAbsolutePath()
                + " which is not a file.\nThis may be a programming error.\n"
                + "Please report this.", e.getMessage());
        }
    }
}
