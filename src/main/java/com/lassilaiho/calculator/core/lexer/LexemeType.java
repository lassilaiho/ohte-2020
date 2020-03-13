package com.lassilaiho.calculator.core.lexer;

/**
 * Different lexeme types.
 *
 * Unless otherwise specified, {@link Lexeme#value} is null for the lexeme type.
 */
public enum LexemeType {
    EOF,
    /**
     * Value is of type double.
     */
    NUMBER,
    PLUS,
    MINUS,
    ASTERISK,
    SLASH,
    LEFT_PAREN,
    RIGHT_PAREN,
}
