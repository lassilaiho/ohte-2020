package com.lassilaiho.calculator.core.parser;

/**
 * {@link BinaryOperator} is a binary operator.
 */
public enum BinaryOperator {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE;

    /**
     * Returns the precedence of the operator.
     * 
     * @return The precedence value or -1 if the operator is not one of the constants defined above.
     */
    public int precedence() {
        switch (this) {
            case ADD:
            case SUBTRACT:
                return 1;
            case MULTIPLY:
            case DIVIDE:
                return 2;
            default:
                throw new IllegalArgumentException("invalid binary operator: " + this);
        }
    }

    /**
     * The lowest precedence of any operator.
     */
    public static final int MIN_PRECEDENCE = 1;
    /**
     * The highest precedence of any operator.
     */
    public static final int MAX_PRECEDENCE = 3;

    @Override
    public String toString() {
        switch (this) {
            case ADD:
                return "+";
            case SUBTRACT:
                return "-";
            case MULTIPLY:
                return "*";
            case DIVIDE:
                return "/";
            default:
                throw new IllegalArgumentException("invalid binary operator: " + this);
        }
    }
}
