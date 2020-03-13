package com.lassilaiho.calculator.core.parser;

/**
 * {@link ParserException} signals an error occured during parsing.
 */
public class ParserException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@link ParserException}.
     * 
     * @param message the error message
     */
    public ParserException(String message) {
        super(message);
    }
}
