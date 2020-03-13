package com.lassilaiho.calculator.core;

/**
 * Signals an error occurred during calculation.
 */
public class CalculatorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@link CalculatorException} with message.
     * 
     * @param message error message
     * @param cause   the cause of this exception
     */
    public CalculatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
