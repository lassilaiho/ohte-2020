package com.lassilaiho.calculator.core.parser;

/**
 * {@link Number} is a number node.
 */
public class Number implements Expression {
    public final double value;

    /**
     * Constructs a number node.
     * 
     * @param value the value of the node
     */
    public Number(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof Number)) {
            return false;
        }
        return value == ((Number) other).value;
    }
}
