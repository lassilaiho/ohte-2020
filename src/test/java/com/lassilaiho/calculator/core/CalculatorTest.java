package com.lassilaiho.calculator.core;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import javafx.util.Pair;

public class CalculatorTest {
    private double delta;
    private MemoryHistoryDao historyDao;
    private HashMap<String, Evaluatable> namedValues;
    private Calculator calculator;

    @Before
    public void setUp() {
        delta = 0.000001;
        historyDao = new MemoryHistoryDao();
        namedValues = new HashMap<>(Calculator.BUILTIN_NAMED_VALUES);
        calculator = new Calculator(historyDao, namedValues);
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
            new Pair<>(" 2*pi", 2 * Math.PI));
        for (var subtest : subtests) {
            assertEquals(
                subtest.getValue().doubleValue(),
                calculator.calculate(subtest.getKey()).doubleValue(),
                delta);
        }
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
    public void throwsCalculationErrorOnWrongArgumentCountForNamedValue() {
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
