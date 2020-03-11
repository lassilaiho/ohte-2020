package com.lassilaiho.calculator.core;

/**
 * Operator is a math operator.
 */
public enum Operator {
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
                return -1;
        }
    }

    /**
     * The lowest precedence of any operator.
     */
    public static final int MIN_PRECEDENCE = 1;
    /**
     * The highest precedence of any operator.
     */
    public static final int MAX_PRECEDENCE = 2;

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
                return "invalid";
        }
    }
}
