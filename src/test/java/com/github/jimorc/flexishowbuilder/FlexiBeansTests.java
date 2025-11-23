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
        FlexiBeans flexiBeans = new FlexiBeans(csvInputStream);
        List<FlexiBean> beans = flexiBeans.getBeans();
        assertEquals(2, beans.size());
        /*FlexiBean bean1 = beans.get(0);
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
        assertEquals("Smith", bean2.getLastName());*/
    }
}
