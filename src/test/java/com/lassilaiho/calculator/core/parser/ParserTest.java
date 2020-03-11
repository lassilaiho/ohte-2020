package com.lassilaiho.calculator.core.parser;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.lassilaiho.calculator.core.lexer.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ParserTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
            new Object[][] {
                {"5 * (3+ 4) / 2", "((5.0*(3.0+4.0))/2.0)"},
                {"3", "3.0"},
                {"4+9/2*2+-23", "((4.0+((9.0/2.0)*2.0))+-23.0)"}});
    }

    private String input, expected;
    private List<Lexeme> lexemes;

    public ParserTest(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Before
    public void initialize() throws IOException {
        lexemes = new Lexer(new StringReader(input)).lex();
    }

    @Test
    public void testParser() {
        var output = new Parser(lexemes).parseExpression();
        assertEquals(expected, output.toString());
    }
}
