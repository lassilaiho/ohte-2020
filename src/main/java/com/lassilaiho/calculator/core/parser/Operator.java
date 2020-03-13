package com.lassilaiho.calculator.core.parser;

/**
 * Operator is a math operator.
 */
public enum Operator {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    NEGATE;

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
            case NEGATE:
                return 3;
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
    public static final int MAX_PRECEDENCE = 3;

    @Override
    public String toString() {
        switch (this) {
            case ADD:
                return "+";
            case SUBTRACT:
            case NEGATE:
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
