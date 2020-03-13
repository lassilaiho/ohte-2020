package com.lassilaiho.calculator.core.lexer;

/**
 * {@link LexerException} signals an error occurred during lexical analysis.
 */
public class LexerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@link LexerException}.
     * 
     * @param message the error message
     */
    public LexerException(String message) {
        super(message);
    }
}
