package com.lassilaiho.calculator.core;

import java.io.IOException;
import java.io.Reader;
import com.lassilaiho.calculator.core.lexer.*;
import com.lassilaiho.calculator.core.parser.*;

/**
 * Calculator evaluates mathematical expressions.
 */
public class Calculator {
    /**
     * Calculates the value of expression.
     * 
     * @param  input               the expression to calculate
     * @return                     the value of the expression
     * @throws IOException         thrown if an error occurs when reading input
     * @throws LexerException      thrown if an error occurs during lexing
     * @throws ParserException     thrown if an error occurs during parsing
     * @throws EvaluationException thrown if an error occurs during evaluation
     */
    public double calculate(Reader input)
        throws IOException, LexerException, ParserException, EvaluationException {
        var lexer = new Lexer(input);
        var parser = new Parser(lexer.lex());
        var evaluator = new Evaluator(0);
        parser.parseExpression().accept(evaluator);
        return evaluator.getValue();
    }
}
