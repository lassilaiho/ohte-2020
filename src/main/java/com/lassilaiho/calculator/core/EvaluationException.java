package com.lassilaiho.calculator.core;

/**
 * EvaluationException signals an error occured during the evaluation of an expression.
 */
public class EvaluationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new EvaluationException with the specified message.
     * 
     * @param message error message
     */
    public EvaluationException(String message) {
        super(message);
    }
}
