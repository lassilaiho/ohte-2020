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
    /**
     * Value is of type {@link Operator}.
     */
    OPERATOR,
    LEFT_PAREN,
    RIGHT_PAREN,
}
