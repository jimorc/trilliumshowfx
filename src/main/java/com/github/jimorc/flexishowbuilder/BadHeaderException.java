package com.github.jimorc.flexishowbuilder;

/**
 * BadHeaderException is a specialization of the Exception class specifically to
 * report problems with the header line in input CSV file contents. This can occur
 * when the header line is missing or malformed.
 */
public final class BadHeaderException extends Exception {
    /**
     * Constructor.
     * @param msg error message.
     */
    public BadHeaderException(final String msg) {
        super(msg);
    }
}
