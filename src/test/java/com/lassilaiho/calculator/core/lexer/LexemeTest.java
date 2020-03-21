package com.lassilaiho.calculator.core.lexer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class LexemeTest {
    @Test
    public void equalWhenSameLexemeTypeAndValue() {
        var left = new Lexeme(LexemeType.ASTERISK);
        var right = new Lexeme(LexemeType.ASTERISK);
        assertTrue(left.equals(right));

        left = new Lexeme(LexemeType.NUMBER, 2.0);
        right = new Lexeme(LexemeType.NUMBER, 2.0);
        assertTrue(left.equals(right));
    }

    @Test
    public void notEqualWithNull() {
        assertFalse(new Lexeme(LexemeType.PLUS).equals(null));
    }

    @Test
    public void notEqualWhenNullValue() {
        var left = new Lexeme(LexemeType.NUMBER, 5.0);
        var right = new Lexeme(LexemeType.NUMBER, null);
        assertFalse(left.equals(right));

        left = new Lexeme(LexemeType.NUMBER, null);
        right = new Lexeme(LexemeType.NUMBER, 5.0);
        assertFalse(left.equals(right));
    }

    @Test
    public void notEqualWhenDifferentType() {
        var left = new Lexeme(LexemeType.NUMBER, 5.0);
        assertFalse(left.equals((Object) 5.0));
    }

    @Test
    public void notEqualWhenDifferentLexemeType() {
        var left = new Lexeme(LexemeType.ASTERISK);
        var right = new Lexeme(LexemeType.LEFT_PAREN);
        assertFalse(left.equals(right));
    }
}
