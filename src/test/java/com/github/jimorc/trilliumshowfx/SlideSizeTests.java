package com.github.jimorc.trilliumshowfx;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for SlideSize class.
 */
public class SlideSizeTests {
    @Test
    public void testSlideSizeDefaults() {
        final int defaultWidth = 1400;
        final int defaultHeight = 1050;
        SlideSize slideSize = new SlideSize();
        assertEquals(defaultWidth, slideSize.getWidth());
        assertEquals(defaultHeight, slideSize.getHeight());
    }

    @Test
    public void testSlideSizeSetters() {
        final int w2000 = 2000;
        final int h1500 = 1500;
        final int belowMin = 50;
        final int aboveMax = 100000;
        final int minWidth = 100;
        final int maxHeight = 99999;
        SlideSize slideSize = new SlideSize();
        slideSize.setWidth(w2000);
        slideSize.setHeight(h1500);
        assertEquals(w2000, slideSize.getWidth());
        assertEquals(h1500, slideSize.getHeight());

        slideSize.setWidth(belowMin); // Below minimum
        slideSize.setHeight(aboveMax); // Above maximum
        assertEquals(minWidth, slideSize.getWidth()); // Minimum enforced
        assertEquals(maxHeight, slideSize.getHeight()); // Maximum enforced
    }
}
