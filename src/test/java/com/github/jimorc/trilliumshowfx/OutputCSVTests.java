package com.github.jimorc.trilliumshowfx;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * OutputCSVTests contains tests for the OutputCSV class.
 */
public class OutputCSVTests {
    @Test
    void testAppendLine() {
        FlexiBean headerBean = new FlexiBean();
        headerBean.setFilename("Filename");
        headerBean.setTitle("Title");
        headerBean.setFullName("Full Name");
        headerBean.setFirstName("First Name");
        headerBean.setLastName("Last Name");
        String header = headerBean.toString();

        OutputCSV csv = new OutputCSV();
        String c = csv.toString();
        assertEquals(header, c);

        csv.appendBean(headerBean);
        c = csv.toString();
        assertEquals(header + "\n" + header, c);
    }
}
