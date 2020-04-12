package com.lassilaiho.calculator.core.evaluator;

/**
 * {@link Constant} is an immutable container for a single value. Evaluating a {@link Constant}
 * requires no arguments and returns the value of the constant.
 */
public final class Constant implements Evaluatable {
    private final double value;

    /**
     * Constructs an new variable with value.
     * 
     * @param value the value of the variable
     */
    public Constant(double value) {
        this.value = value;
    }

    @Override
    public int getArgumentCount() {
        return 0;
    }

    @Override
    public double evaluate(double... arguments) {
        return value;
    }
}
