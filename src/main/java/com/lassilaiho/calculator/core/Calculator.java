package com.lassilaiho.calculator.core;

import java.io.IOException;
import java.io.StringReader;
import com.lassilaiho.calculator.core.evaluator.*;
import com.lassilaiho.calculator.core.lexer.*;
import com.lassilaiho.calculator.core.parser.*;
import com.lassilaiho.calculator.persistence.SessionDao;
import java.util.List;
import java.util.Map;

/**
 * {@link Calculator} evaluates mathematical expressions.
 */
public final class Calculator {
    private SessionDao sessionDao;
    private Scope scope;

    /**
     * An immutable map of built-in named values supported by all calculators.
     */
    public static final Map<String, Evaluatable> BUILTIN_NAMED_VALUES = Builtins.VALUES;

    /**
     * Constructs a new {@link Calculator} which persists the current session using {@code sessionDao}.
     *
     * @param sessionDao DAO used to persist the current session
     */
    public Calculator(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
        scope = Scope.ofMap(BUILTIN_NAMED_VALUES);
        loadNamedValuesFromDao();
    }

    /**
     * Constructs a new {@link Calculator} which persists the current session using {@code sessionDao}.
     * The constructed calculator uses globalScope as the default set of named values instead of
     * {@link #BUILTIN_NAMED_VALUES}. The passed map may be modified by both the calculator and the
     * caller.
     *
     * @param sessionDao  DAO used to persist the current session
     * @param globalScope default set of named values to use instead of {@link #BUILTIN_NAMED_VALUES}
     */
    public Calculator(SessionDao sessionDao, Scope globalScope) {
        this.sessionDao = sessionDao;
        scope = globalScope;
        loadNamedValuesFromDao();
    }

    /**
     * Returns a list of past calculations. The entries are ordered from oldest to newest. Modifying the
     * returned list is undefined behavior.
     *
     * @return A list of history entries.
     */
    public List<HistoryEntry> getHistory() {
        return sessionDao.history().getAllEntries();
    }

    /**
     * Removes all history entries.
     */
    public void clearHistory() {
        sessionDao.history().removeAllEntries();
    }

    /**
     * Returns the newest history entry or null if history is empty.
     * 
     * @return the newest history entry or null
     */
    public HistoryEntry newestHistoryEntry() {
        return sessionDao.history().getNewestEntry();
    }

    /**
     * Returns the {@link Scope} used by the calculator.
     * 
     * @return the scope
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * Calculates the value of expression. If the calculation succeeds, it is added to the history.
     * 
     * @param  expression          the expression to calculate
     * @return                     the value of the expression or null if input is empty
     * @throws CalculatorException thrown if an error occurs during calculation
     */
    public Number calculate(String expression) throws CalculatorException {
        try {
            var parsedNode = parseInput(expression);
            if (parsedNode == null) {
                return null;
            }
            var evaluator = new Evaluator(0, scope);
            parsedNode.accept(evaluator);
            sessionDao.history()
                .addEntry(new HistoryEntry(expression, evaluator.getValue()));
            persistOperation(parsedNode, evaluator.getValue());
            return evaluator.getValue();
        } catch (LexerException | ParserException | EvaluationException exception) {
            throw new CalculatorException(exception.getMessage(), exception);
        }
    }

    private void loadNamedValuesFromDao() {
        for (var entry : sessionDao.namedValues().getAllValues()) {
            if (entry.parameters.isEmpty()) {
                var constant = new Constant(Double.valueOf(entry.value));
                scope.declare(entry.name, constant, true);
            } else {
                var definition = new FunctionDefinitionNode(entry.name, entry.parameters,
                    (Expression) parseInput(entry.value));
                scope.declare(entry.name, Function.ofDefinition(definition, scope), true);
            }
        }
    }

    private Node parseInput(String input) {
        try {
            var lexemes = new Lexer(new StringReader(input)).lex();
            return new Parser(lexemes).parseNode();
        } catch (IOException e) {
            // The only source of IOExceptions in the method is StringReader,
            // which throws if it is read from after closing it. The reader is
            // never closed, so this should never happen.
            throw new AssertionError(
                "An IOException occurred. This is a bug in this library.", e);
        }
    }

    private void persistOperation(Node node, double value) {
        if (node instanceof AssignmentNode) {
            sessionDao.namedValues().setValue(
                ((AssignmentNode) node).name,
                List.of(),
                Double.toString(value));
        } else if (node instanceof FunctionDefinitionNode) {
            var definition = (FunctionDefinitionNode) node;
            sessionDao.namedValues().setValue(
                definition.name,
                definition.parameters,
                definition.body.toString());
        } else if (node instanceof DeleteNode) {
            ((DeleteNode) node).names.forEach(sessionDao.namedValues()::clearValue);
        }
    }
}
