package com.lassilaiho.calculator.core;

import java.io.IOException;
import java.io.StringReader;
import com.lassilaiho.calculator.core.lexer.*;
import com.lassilaiho.calculator.core.parser.*;
import java.lang.Number;
import java.util.ArrayList;
import java.util.List;

/**
 * Calculator evaluates mathematical expressions.
 */
public class Calculator {
    private ArrayList<HistoryEntry> history = new ArrayList<>();

    /**
     * Returns a list of past calculations. The entries are ordered from oldest to newest. The returned
     * list stays up to date when the history is modified, such as calling {@link #clearHistory}.
     * Modifying the returned list directly is undefined behavior.
     * 
     * @return A list of history entries.
     */
    public List<HistoryEntry> getHistory() {
        return history;
    }

    /**
     * Removes all history entries.
     */
    public void clearHistory() {
        history.clear();
        history.trimToSize();
    }

    /**
     * Returns the newest history entry or null if history is empty.
     * 
     * @return the newest history entry or null
     */
    public HistoryEntry newestHistoryEntry() {
        if (history.size() == 0) {
            return null;
        }
        return history.get(history.size() - 1);
    }

    /**
     * Calculates the value of expression. If the calculation succeeds, it is added to the history.
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
            history.add(new HistoryEntry(input, evaluator.getValue()));
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
