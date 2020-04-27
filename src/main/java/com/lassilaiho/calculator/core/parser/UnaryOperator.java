package com.lassilaiho.calculator.core.parser;

/**
 * {@link UnaryOperator} is a unary operator.
 */
public enum UnaryOperator {
    PLUS,
    NEGATE;

    @Override
    public String toString() {
        switch (this) {
            case PLUS:
                return "+";
            case NEGATE:
                return "-";
            default:
                throw new IllegalArgumentException("invalid unary operator: " + this);
        }
    }
}
