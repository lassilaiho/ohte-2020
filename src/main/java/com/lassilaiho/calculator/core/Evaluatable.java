package com.lassilaiho.calculator.core;

/**
 * An object implementing {@link Evaluatable} can be evaluated to produce a result. For example,
 * functions are {@link Evaluatable}.
 */
public interface Evaluatable {
    /**
     * Returns the number of arguments required by {@link #evaluate(double...)}.
     * 
     * @return non-negative argument count
     */
    int getArgumentCount();

    /**
     * Evaluates the value using arguments and returns the result.
     * 
     * @param  arguments {@link #getArgumentCount()} arguments
     * @return           the result of the evaluation
     */
    double evaluate(double... arguments);
}
