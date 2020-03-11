package com.lassilaiho.calculator.core.parser;

/**
 * {@link ParserException} signals an error occured during parsing.
 */
public class ParserException extends RuntimeException {
    /**
     * Constructs a new {@link ParserException}.
     * 
     * @param message the error message
     */
    public ParserException(String message) {
        super(message);
    }
}
