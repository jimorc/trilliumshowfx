package com.github.jimorc.trilliumshowfx;

/**
 * CSVException is a specialization of the Exception class specifically to
 * report problems with the input CSV file contents.
 */
public final class CSVException extends Exception {
    /**
     * Constructor.
     * @param msg error message.
     */
    public CSVException(final String msg) {
        super(msg);
    }
}
