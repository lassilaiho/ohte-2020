package com.lassilaiho.calculator.core.lexer;

/**
 * {@link Lexeme} is a math expression lexeme.
 */
public final class Lexeme {
    /**
     * The type of the lexeme.
     */
    public final LexemeType type;
    /**
     * The value of the lexeme. The type of the value is determined by {@link #type}. See
     * {@link LexemeType} for details.
     */
    public final Object value;

    /**
     * Constructs a new {@link Lexeme}.
     * 
     * @param type  the type of the lexeme
     * @param value the value of the lexeme, or null for no value
     */
    public Lexeme(LexemeType type, Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Constructs a new Lexeme with a null value.
     * 
     * @param type the type of the lexeme
     */
    public Lexeme(LexemeType type) {
        this(type, null);
    }

    @Override
    public String toString() {
        if (value == null) {
            return type.toString();
        }
        return type.toString() + "(" + value.toString() + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof Lexeme)) {
            return false;
        }
        var o = (Lexeme) other;
        if (type != o.type) {
            return false;
        }
        if (value == null) {
            return o.value == null;
        }
        return value.equals(o.value);
    }
}
