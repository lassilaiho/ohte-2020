package com.lassilaiho.calculator.core;

import java.io.IOException;
import java.io.StringReader;
import static com.lassilaiho.calculator.core.Function.*;
import com.lassilaiho.calculator.core.lexer.*;
import com.lassilaiho.calculator.core.parser.*;
import com.lassilaiho.calculator.persistence.SessionDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;

/**
 * {@link Calculator} evaluates mathematical expressions.
 */
public class Calculator {
    private SessionDao sessionDao;
    private Map<String, Evaluatable> namedValues;

    /**
     * An immutable map of built-in named values supported by all calculators.
     */
    public static final Map<String, Evaluatable> BUILTIN_NAMED_VALUES = Map.ofEntries(
        entry("pi", new Constant(Math.PI)),
        entry("e", new Constant(Math.exp(1))),
        entry("sin", unary(Math::sin)),
        entry("cos", unary(Math::cos)),
        entry("tan", unary(Math::tan)),
        entry("asin", unary(Math::asin)),
        entry("acos", unary(Math::acos)),
        entry("atan", unary(Math::atan)),
        entry("sqrt", unary(Math::sqrt)),
        entry("nroot", binary((x, y) -> Math.pow(x, 1 / y))),
        entry("pow", binary(Math::pow)),
        entry("log", unary(Math::log)),
        entry("log10", unary(Math::log10)),
        entry("log2", unary(x -> Math.log(x) / Math.log(2))),
        entry("ceil", unary(Math::ceil)),
        entry("floor", unary(Math::floor)),
        entry("round", unary(Math::round)),
        entry("max", binary(Math::max)),
        entry("min", binary(Math::min)));

    /**
     * Constructs a new {@link Calculator} which persists the current session using {@code sessionDao}.
     *
     * @param sessionDao DAO used to persist the current session
     */
    public Calculator(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
        namedValues = new HashMap<>(BUILTIN_NAMED_VALUES);
    }

    /**
     * Constructs a new {@link Calculator} which persists the current session using {@code sessionDao}.
     * The constructed calculator uses builtinNamedValues as the default set of named values instead of
     * {@link #BUILTIN_NAMED_VALUES}. The passed map may be modified by both the calculator and the
     * caller.
     *
     * @param sessionDao         DAO used to persist the current session
     * @param builtinNamedValues default set of named values to use instead of
     *                           {@link #BUILTIN_NAMED_VALUES}
     */
    public Calculator(SessionDao sessionDao,
        Map<String, Evaluatable> builtinNamedValues) {
        this.sessionDao = sessionDao;
        namedValues = builtinNamedValues;
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
     * Calculates the value of expression. If the calculation succeeds, it is added to the history.
     * 
     * @param  expression          the expression to calculate
     * @return                     the value of the expression or null if input is empty
     * @throws CalculatorException thrown if an error occurs during calculation
     */
    public Number calculate(String expression) throws CalculatorException {
        try {
            var lexer = new Lexer(new StringReader(expression));
            var parser = new Parser(lexer.lex());
            var parsedExpression = parser.parseExpression();
            if (parsedExpression == null) {
                return null;
            }
            var evaluator = new Evaluator(0, namedValues);
            parsedExpression.accept(evaluator);
            sessionDao.history()
                .addEntry(new HistoryEntry(expression, evaluator.getValue()));
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
