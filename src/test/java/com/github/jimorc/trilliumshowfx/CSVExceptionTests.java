package com.github.jimorc.trilliumshowfx;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * CSVExceptionTests contains tests for the CSVException class.
 */
public class CSVExceptionTests {
    @Test
    public void testConstructor() {
        CSVException ex = new CSVException("Exception message");
        assertEquals("Exception message", ex.getMessage());
    }
}
