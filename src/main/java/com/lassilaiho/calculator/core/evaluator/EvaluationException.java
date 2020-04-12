package com.lassilaiho.calculator.core.evaluator;

/**
 * {@link EvaluationException} signals an error occured during the evaluation of an expression.
 */
public final class EvaluationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@link EvaluationException} with the specified message.
     * 
     * @param message error message
     */
    public EvaluationException(String message) {
        super(message);
    }
}
