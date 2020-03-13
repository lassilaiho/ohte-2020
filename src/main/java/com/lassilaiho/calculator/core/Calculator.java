package com.lassilaiho.calculator.core;

import java.io.IOException;
import java.io.StringReader;
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
     * @throws CalculatorException thrown if an error occurs during calculation
     */
    public Number calculate(String input) throws CalculatorException {
        try {
            var lexer = new Lexer(new StringReader(input));
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
        } catch (IOException exception) {
            // The only source of IOExceptions in the method is StringReader,
            // which throws if it is read from after closing it. The reader is
            // never closed, so this should never happen.
            throw new RuntimeException(
                "An IOException occurred. This is a bug in this library.", exception);
        }
    }
}
