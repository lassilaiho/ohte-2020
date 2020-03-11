package com.lassilaiho.calculator.core;

import static org.junit.Assert.assertEquals;
import java.io.*;
import java.util.*;
import com.lassilaiho.calculator.core.lexer.*;
import com.lassilaiho.calculator.core.parser.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class EvaluatorTest {
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
            new Object[][] {
                {"5 * (3+ 4) / 2", 17.5},
                {"3", 3.0},
                {"4+9/2*2+-23", -10.0}});
    }

    private String input;
    private double expected;
    private Expression expression;

    public EvaluatorTest(String input, double expected) {
        this.input = input;
        this.expected = expected;
    }

    @Before
    public void initialize() throws IOException {
        var lexemes = new Lexer(new StringReader(input)).lex();
        expression = new Parser(lexemes).parseExpression();
    }

    @Test
    public void testEvaluator() {
        var evaluator = new Evaluator(0);
        expression.accept(evaluator);
        assertEquals(expected, evaluator.getValue(), 0);
    }
}
