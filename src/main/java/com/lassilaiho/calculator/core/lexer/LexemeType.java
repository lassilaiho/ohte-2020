package com.lassilaiho.calculator.core.lexer;

/**
 * An enumeration of lexeme types.
 *
 * Unless otherwise specified, {@link Lexeme#value} is null for a lexeme type.
 */
public enum LexemeType {
    EOF,
    /*** Value is of type {@link Double}. */
    NUMBER,
    PLUS,
    MINUS,
    ASTERISK,
    SLASH,
    LEFT_PAREN,
    RIGHT_PAREN,
    /*** Value is of type {@link String}. */
    IDENTIFIER,
    COMMA,
    ASSIGN,
    DELETE,
}
