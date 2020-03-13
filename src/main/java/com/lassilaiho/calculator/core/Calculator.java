package com.lassilaiho.calculator.core;

import java.io.IOException;
import java.io.Reader;
import com.lassilaiho.calculator.core.lexer.*;
import com.lassilaiho.calculator.core.parser.*;
import java.lang.Number;

/**
 * Calculator evaluates mathematical expressions.
 */
public class Calculator {
    /**
     * Calculates the value of expression.
     * 
     * @param  input               the expression to calculate
     * @return                     the value of the expression or null if input is empty
     * @throws IOException         thrown if an error occurs when reading input
     * @throws CalculatorException thrown if an error occurs during calculation
     */
    public Number calculate(Reader input) throws IOException, CalculatorException {
        try {
            var lexer = new Lexer(input);
            var parser = new Parser(lexer.lex());
            var expression = parser.parseExpression();
            if (expression == null) {
                return null;
            }
            var evaluator = new Evaluator(0);
            expression.accept(evaluator);
            return evaluator.getValue();
        } catch (LexerException | ParserException | EvaluationException exception) {
            throw new CalculatorException(exception.getMessage(), exception);
        }
    }
}
