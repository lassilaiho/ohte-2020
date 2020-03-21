package com.lassilaiho.calculator.core;

import java.io.IOException;
import java.io.StringReader;
import com.lassilaiho.calculator.core.lexer.*;
import com.lassilaiho.calculator.core.parser.*;
import com.lassilaiho.calculator.persistence.HistoryDao;
import java.util.Collections;
import java.util.List;

/**
 * Calculator evaluates mathematical expressions.
 */
public class Calculator {
    private HistoryDao historyDao;

    /**
     * Constructs a new {@link Calculator} which persists the calculation history using the passed DAO.
     * 
     * @param historyDao DAO used to persist calculation history
     */
    public Calculator(HistoryDao historyDao) {
        this.historyDao = historyDao;
    }

    /**
     * Returns a list of past calculations. The entries are ordered from oldest to newest. Modifying the
     * returned list is undefined behavior.
     *
     * @return A list of history entries.
     */
    public List<HistoryEntry> getHistory() {
        return Collections.unmodifiableList(historyDao.getAllEntries());
    }

    /**
     * Removes all history entries.
     */
    public void clearHistory() {
        historyDao.removeAllEntries();
    }

    /**
     * Returns the newest history entry or null if history is empty.
     * 
     * @return the newest history entry or null
     */
    public HistoryEntry newestHistoryEntry() {
        return historyDao.getNewestEntry();
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
            historyDao.addEntry(new HistoryEntry(input, evaluator.getValue()));
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
