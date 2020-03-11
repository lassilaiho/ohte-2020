package com.lassilaiho.calculator.core.lexer;

import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.lassilaiho.calculator.core.Operator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class LexerTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
            new Object[][] {
                {
                    "5 * (3+ 4) / 2",
                    Arrays.asList(
                        new Lexeme(LexemeType.NUMBER, 5.0),
                        new Lexeme(LexemeType.OPERATOR, Operator.MULTIPLY),
                        new Lexeme(LexemeType.LEFT_PAREN),
                        new Lexeme(LexemeType.NUMBER, 3.0),
                        new Lexeme(LexemeType.OPERATOR, Operator.ADD),
                        new Lexeme(LexemeType.NUMBER, 4.0),
                        new Lexeme(LexemeType.RIGHT_PAREN),
                        new Lexeme(LexemeType.OPERATOR, Operator.DIVIDE),
                        new Lexeme(LexemeType.NUMBER, 2.0),
                        new Lexeme(LexemeType.EOF))},
                {"", Arrays.asList(new Lexeme(LexemeType.EOF))}});
    }

    private String input;
    private List<Lexeme> expected;

    public LexerTest(String input, List<Lexeme> expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void testLexer() throws IOException {
        var lexer = new Lexer(new StringReader(input));
        var output = lexer.lex();
        assertEquals(expected, output);
    }
}
