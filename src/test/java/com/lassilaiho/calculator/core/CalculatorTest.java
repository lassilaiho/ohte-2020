package com.lassilaiho.calculator.core;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import com.lassilaiho.calculator.core.evaluator.Evaluatable;
import com.lassilaiho.calculator.persistence.SqlSessionDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javafx.util.Pair;

public class CalculatorTest {
    private double delta;
    private Connection connection;
    private SqlSessionDao sessionDao;
    private HashMap<String, Evaluatable> namedValues;
    private Calculator calculator;

    @Before
    public void setUp() throws Exception {
        delta = 0.000001;
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        sessionDao = new SqlSessionDao(connection);
        sessionDao.initializeDatabase();
        namedValues = new HashMap<>(Calculator.BUILTIN_NAMED_VALUES);
        calculator = new Calculator(sessionDao, namedValues);
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void returnsNullForEmptyInput() {
        assertEquals(null, calculator.calculate(""));
        assertEquals(null, calculator.calculate("     "));
    }

    @Test
    public void calculatesCorrectly() {
        var subtests = List.<Pair<String, Number>>of(
            new Pair<>("5 * (3+ 4) / 2", 17.5),
            new Pair<>("3--2", 5),
            new Pair<>("4+9/2*2+-23", -10),
            new Pair<>(" 2*pi", 2 * Math.PI),
            new Pair<>("sqrt(2)", Math.sqrt(2)),
            new Pair<>("log2(64)", 6),
            new Pair<>("nroot(8, 3)", Math.cbrt(8)));
        for (var subtest : subtests) {
            assertEquals(
                subtest.getValue().doubleValue(),
                calculator.calculate(subtest.getKey()).doubleValue(),
                delta);
        }
    }

    @Test
    public void variablesWork() {
        assertEquals(1, calculator.calculate("x := 1").doubleValue(), delta);
        assertEquals(1, calculator.calculate("x").doubleValue(), delta);
        assertEquals(2, calculator.calculate("x := 2").doubleValue(), delta);
        assertEquals(2, calculator.calculate("x").doubleValue(), delta);
        assertEquals(1, calculator.calculate("y := 1").doubleValue(), delta);
        assertEquals(2, calculator.calculate("x").doubleValue(), delta);
        assertEquals(1, calculator.calculate("y").doubleValue(), delta);
    }

    @Test(expected = CalculatorException.class)
    public void throwsCalculationErrorOnInvalidInput() {
        calculator.calculate("3+(2-4");
    }

    @Test(expected = CalculatorException.class)
    public void throwsCalculationErrorOnUndefinedName() {
        calculator.calculate("_undefined2");
    }

    @Test(expected = CalculatorException.class)
    public void throwsCalculationErrorOnWrongArgumentCountForVariable() {
        namedValues.put("invalidConstant", new Evaluatable() {
            @Override
            public int getArgumentCount() {
                return 1;
            }

            @Override
            public double evaluate(double... arguments) {
                return 0;
            }
        });
        calculator.calculate("invalidConstant");
    }

    @Test(expected = CalculatorException.class)
    public void throwsCalculationErrorOnWrongArgumentCountForFunction() {
        calculator.calculate("nroot(2)");
    }

    @Test
    public void handlesHistoryCorrectly() {
        assertEquals(0, calculator.getHistory().size());
        assertEquals(null, calculator.newestHistoryEntry());

        calculator.calculate("4+9/2*2+-23");
        assertEquals(1, calculator.getHistory().size());
        var entry = calculator.newestHistoryEntry();
        assertEquals("4+9/2*2+-23", entry.getExpression());
        assertEquals(-10.0, entry.getValue().doubleValue(), delta);

        calculator.calculate("0");
        assertEquals(2, calculator.getHistory().size());
        entry = calculator.newestHistoryEntry();
        assertEquals("0", entry.getExpression());
        assertEquals(0.0, entry.getValue().doubleValue(), delta);

        calculator.clearHistory();
        assertEquals(0, calculator.getHistory().size());
    }
}
