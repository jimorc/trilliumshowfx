package com.github.jimorc.trilliumshowfx;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
// Many of these tests read from a file named "testing/data/test.csv"
// with the following content:
// Filename,Title,Full Name,First Name,Last Name
// image1.jpg,Image One,John Doe,John,Doe
// image2.jpg,"Image, Two",Jane Smith,Jane,Smith
//
// Other tests read from "testing/data/empty.csv" which is an empty file.
// Sort tests read from "testing/data/sort.csv" with the following content:
// Filename,Title,Full Name,First Name,Last Name
// image1.jpg,Image One,John Doe,John,Doe
// image2.jpg,"Image, Two",Jane Smith,Jane,Smith
// image3.jpg,Image Three,Bob Brown,Bob,Brown
// image4.jpg,Image Four,Wilma Flintstone,Wilma,Flintstone
// image5.jpg,Image Five,Fred Flintstone,Fred,Flintstone
// image6.jpg,Image Six,Fred Flintstone,Fred,Flintstone
// image7.jpg,Image Seven,Barney Rubble,Barney,Rubble
/*  *
 * InputCSVTests contains tests of methods in InputCSV class.
 */
public class InputCSVTests {
    @Test
    void testConstructor() {
        File f = new File("testing/data/sort.csv");
        try {
            InputCSV iCSV = new InputCSV(f);
            assertNotNull(iCSV);
        } catch (CSVException ce) {
            fail(ce.getMessage());
        } catch (IOException ioe) {
            fail(ioe.getMessage());
        }
    }

    @Test
    void testConstructorNonexistentFile() {
        File f = new File("testing/data/notafile.csv");
        assertThrows(CSVException.class, () -> new InputCSV(f));
    }

    @Test
    void testConstructorNoHeaderInFile() {
        File f = new File("testing/data/zeroheaderlength.csv");
        try {
            new InputCSV(f);
        } catch (IOException ioe) {
            fail("IOException thrown: " + ioe.getMessage());
        } catch (CSVException csve) {
            fail("CSVException thrown: " + csve.getMessage());
        } catch (IllegalStateException ise) {
            // we need to ignore this
            return;
        }
    }

    @Test
    void testConstructorInvalidLineInFile() {
        File f = new File("testing/data/invalidline.csv");
        assertThrows(CSVException.class, () -> new InputCSV(f));
    }

    @Test
    void testConstructorNotAFile() {
        File f = new File("testing/data");
        assertThrows(CSVException.class, () -> new InputCSV(f));
    }

    // Test disabled on Windows because it is not possible to set a file to not be readable.
    @Test
    @DisabledOnOs(OS.WINDOWS)
    void testConstructorIOError() {
        Path path = Path.of("testing/data/temp.csv");
        File f = new File("testing/data/temp.csv");
        try {
            Files.writeString(path, "A bunch of text");
        } catch (IOException ioe) {
            fail(ioe.getMessage());
        }
        try {
            // make it so an IOException will be thrown when trying to read the file.
            f.setReadable(false);
            new InputCSV(f);
        } catch (CSVException ce) {
            f.delete();
            fail("Threw CSVException, not IOException");
        } catch (IOException ioe) {
            f.delete();
            return;
        }
        fail("Did not throw IOException");
    }

    @Test
    void testInsertAt() {
        final int bean1 = 1;
        final int bean2 = 2;
        final int bean0 = 0;
        InputCSV csv = null;
        try {
            File f = new File("testing/data/test.csv");
            csv = new InputCSV(f);
        } catch (IOException ioe) {
            fail("IOException thrown: " + ioe.getMessage());
        } catch (CSVException csve) {
            fail("CSVException thrown: " + csve.getMessage());
        }
        final int numBeans = csv.getNumberOfBeans();
        FlexiBean newBean = new FlexiBean();
        newBean.setFilename("image4.jpg");
        newBean.setTitle("\"Image, Two\"");
        newBean.setFullName("Bob Brown");
        newBean.setFirstName("Bob");
        newBean.setLastName("Brown");
        csv.insertAt(1, newBean);
        assertEquals(numBeans + 1, csv.getNumberOfBeans());
        assertEquals("image4.jpg", csv.getBeans().getBeans().get(bean1).getFilename());
        assertEquals("image1.jpg", csv.getBeans().getBeans().get(bean0).getFilename());
        assertEquals("image2.jpg", csv.getBeans().getBeans().get(bean2).getFilename());
    }

    @Test
    void testInsertAtBeginning() {
        final int numBeans = 3;
        final int bean0 = 0;
        final int bean1 = 1;
        final int bean2 = 2;
        InputCSV csv = null;
        try {
            File f = new File("testing/data/test.csv");
            csv = new InputCSV(f);
        } catch (IOException ioe) {
            fail("IOException thrown: " + ioe.getMessage());
        } catch (CSVException csve) {
            fail("CSVException thrown: " + csve.getMessage());
        }

        FlexiBean newBean = new FlexiBean();
        newBean.setFilename("image4.jpg");
        newBean.setTitle("Image Four");
        newBean.setFullName("Bob Brown");
        newBean.setFirstName("Bob");
        newBean.setLastName("Brown");
        csv.insertAt(0, newBean);
        assertEquals(numBeans, csv.getNumberOfBeans());
        assertEquals("image4.jpg", csv.getBeans().getBeans().get(bean0).getFilename());
        assertEquals("image1.jpg", csv.getBeans().getBeans().get(bean1).getFilename());
        assertEquals("image2.jpg", csv.getBeans().getBeans().get(bean2).getFilename());
    }

    @Test
    void testInsertAtEnd() {
        final int numBeans = 3;
        final int bean0 = 0;
        final int bean1 = 1;
        final int bean2 = 2;
        InputCSV csv = null;
        try {
            File f = new File("testing/data/test.csv");
            csv = new InputCSV(f);
        } catch (IOException ioe) {
            fail("IOException thrown: " + ioe.getMessage());
        } catch (CSVException csve) {
            fail("CSVException thrown: " + csve.getMessage());
        }
        FlexiBean newBean = new FlexiBean();
        newBean.setFilename("image4.jpg");
        newBean.setTitle("Image Three");
        newBean.setFullName("Bob Brown");
        newBean.setFirstName("Bob");
        newBean.setLastName("Brown");
        csv.insertAt(bean2, newBean);
        assertEquals(numBeans, csv.getNumberOfBeans());
        assertEquals("image1.jpg", csv.getBeans().getBeans().get(bean0).getFilename());
        assertEquals("image2.jpg", csv.getBeans().getBeans().get(bean1).getFilename());
        assertEquals("image4.jpg", csv.getBeans().getBeans().get(bean2).getFilename());
    }

    @Test
    void testInsertAtInvalidIndex() {
        final int minus1 = -1;
        InputCSV csv = null;
        try {
            File f = new File("testing/data/test.csv");
            csv = new InputCSV(f);
        } catch (IOException ioe) {
            fail("IOException thrown: " + ioe.getMessage());
        } catch (CSVException csve) {
            fail("CSVException thrown: " + csve.getMessage());
        }
        FlexiBean newBean = new FlexiBean();
        newBean.setFilename("image3.jpg");
        newBean.setTitle("Image Three");
        newBean.setFullName("Bob Brown");
        newBean.setFirstName("Bob");
        newBean.setLastName("Brown");
        try {
            csv.insertAt(minus1, newBean);
            fail("ArrayIndexOutOfBoundsException not thrown for negative index");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // expected
        }
        try {
            csv.insertAt(csv.getNumberOfBeans() + 1, newBean);
            fail("ArrayIndexOutOfBoundsException not thrown for index greater than number of lines");
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            // expected
        }
    }

    @Test
    void testAppend() {
        final int numBeans = 3;
        final int bean0 = 0;
        final int bean1 = 1;
        final int bean2 = 2;
        InputCSV csv = null;
        try {
            File f = new File("testing/data/test.csv");
            csv = new InputCSV(f);
        } catch (IOException ioe) {
            fail("IOException thrown: " + ioe.getMessage());
        } catch (CSVException csve) {
            fail("CSVException thrown: " + csve.getMessage());
        }
        FlexiBean newBean = new FlexiBean();
        newBean.setFilename("image4.jpg");
        newBean.setTitle("Image Four");
        newBean.setFullName("Bob Brown");
        newBean.setFirstName("Bob");
        newBean.setLastName("Brown");
        csv.append(newBean);
        assertEquals(numBeans, csv.getNumberOfBeans());
        assertEquals("image1.jpg", csv.getBeans().getBeans().get(bean0).getFilename());
        assertEquals("image2.jpg", csv.getBeans().getBeans().get(bean1).getFilename());
        assertEquals("image4.jpg", csv.getBeans().getBeans().get(bean2).getFilename());
    }

    @Test
    void testAppendMultiple() {
        final int numBeans = 5;
        final int firstBean = 2;
        final int secondBean = 3;
        final int thirdBean = 4;
        InputCSV csv = null;
        try {
            File f = new File("testing/data/test.csv");
            csv = new InputCSV(f);
        } catch (IOException ioe) {
            fail("IOException thrown: " + ioe.getMessage());
        } catch (CSVException csve) {
            fail("CSVException thrown: " + csve.getMessage());
        }
        FlexiBean newBean1 = new FlexiBean();
        newBean1.setFilename("image4.jpg");
        newBean1.setTitle("Image One");
        newBean1.setFullName("John Doe");
        newBean1.setFirstName("John");
        newBean1.setLastName("Doe");
        FlexiBean newBean2 = new FlexiBean();
        newBean2.setFilename("image5.jpg");
        newBean2.setTitle("Image Two");
        newBean2.setFullName("Jane Smith");
        newBean2.setFirstName("Jane");
        newBean2.setLastName("Smith");
        FlexiBean newBean3 = new FlexiBean();
        newBean3.setFilename("image6.jpg");
        newBean3.setTitle("Image Three");
        newBean3.setFullName("Bob Brown");
        newBean3.setFirstName("Bob");
        newBean3.setLastName("Brown");
        csv.append(newBean1);
        csv.append(newBean2);
        csv.append(newBean3);
        assertEquals(numBeans, csv.getNumberOfBeans());
        assertEquals("image4.jpg", csv.getBeans().getBeans().get(firstBean).getFilename());
        assertEquals("image5.jpg", csv.getBeans().getBeans().get(secondBean).getFilename());
        assertEquals("image6.jpg", csv.getBeans().getBeans().get(thirdBean).getFilename());
    }

    @Test
    void testGetPersonValid() {
        InputCSV csv = null;
        try {
            File f = new File("testing/data/test.csv");
            csv = new InputCSV(f);
        } catch (IOException ioe) {
            fail("IOException thrown: " + ioe.getMessage());
        } catch (CSVException csve) {
            fail("CSVException thrown: " + csve.getMessage());
        }

        try {
            Person john = csv.getPerson("John Doe");
            assertNotNull(john);
            assertEquals("John Doe", john.getFullName());
        } catch (CSVException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testGetPersonInvalid() {
        InputCSV csv = null;
        try {
            File f = new File("testing/data/test.csv");
            csv = new InputCSV(f);
        } catch (IOException ioe) {
            fail("IOException thrown: " + ioe.getMessage());
        } catch (CSVException csve) {
            fail("CSVException thrown: " + csve.getMessage());
        }

        try {
            csv.getPerson("Fred Smith");
            fail("No exception thrown for invalid name");
        } catch (CSVException e) {
            String expectedMessage = "Programming error: Trying to retrieve info for Fred Smith but it does not exist.";
            String actualMessage = e.getMessage();
            assertEquals(expectedMessage, actualMessage);

        }
    }

    @Test
    void testGetBeansValidName() {
        InputCSV csv = null;
        try {
            File f = new File("testing/data/sort.csv");
            csv = new InputCSV(f);
            FlexiBean[] beans = csv.getBeans().getBeans().stream()
                .filter(b -> b.getFullName().equals("John Doe"))
                .toArray(FlexiBean[]::new);
            assertEquals(1, beans.length);
            assertEquals("image1.jpg", beans[0].getFilename());
            beans = csv.getBeans().getBeans().stream()
                .filter(b -> b.getFullName().equals("Fred Flintstone"))
                .toArray(FlexiBean[]::new);
            assertEquals(2, beans.length);
            assertEquals("DSC-0424.jpg", beans[0].getFilename());
            assertEquals("IMG-276.jpg", beans[1].getFilename());
        } catch (IOException ioe) {
            fail("IOException thrown: " + ioe.getMessage());
        } catch (CSVException csve) {
            fail("CSVException thrown: " + csve.getMessage());
        }
    }

    @Test
    void testGetImageLinesInvalidName() {
        InputCSV csv = null;
        try {
            File f = new File("testing/data/sort.csv");
            csv = new InputCSV(f);
            FlexiBean[] beans = csv.getBeans().getBeans().stream()
                .filter(b -> b.getFullName().equals("Bob Brown"))
                .toArray(FlexiBean[]::new);
            assertEquals(0, beans.length);
        } catch (CSVException csve) {
            assertEquals("The CSV file does not contain lines for Bob Brown", csve.getMessage());
        } catch (IOException ioe) {
            fail(ioe.getMessage());
        }
    }
}
