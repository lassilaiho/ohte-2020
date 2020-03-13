package com.lassilaiho.calculator.core;

/**
 * An expression that was evaluated and the corresponding value.
 */
public final class HistoryEntry {
    private String expression;

    public String getExpression() {
        return expression;
    }

    private Number value;

    public Number getValue() {
        return value;
    }

    /**
     * Constructs a new {@link HistoryEntry} with the specified values.
     * 
     * @param expression the math expression as a {@link String}
     * @param value      the evaluated value of expression
     */
    public HistoryEntry(String expression, Number value) {
        this.expression = expression;
        this.value = value;
    }
}
