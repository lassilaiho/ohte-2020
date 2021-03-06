package com.lassilaiho.calculator.core;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import com.lassilaiho.calculator.core.evaluator.Evaluatable;
import com.lassilaiho.calculator.core.evaluator.Scope;
import com.lassilaiho.calculator.persistence.SqlSessionDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javafx.util.Pair;

public class CalculatorTest {
    private double delta;
    private Connection connection;
    private SqlSessionDao sessionDao;
    private Scope scope;
    private Calculator calculator;

    @Before
    public void setUp() throws Exception {
        delta = 0.000001;
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        sessionDao = new SqlSessionDao(connection);
        sessionDao.initializeDatabase();
        scope = Scope.ofMap(Calculator.BUILTIN_NAMED_VALUES);
        calculator = new Calculator(sessionDao, scope);
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
            new Pair<>("-+-sqrt(2)", Math.sqrt(2)),
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

        calculator = new Calculator(sessionDao);
        assertEquals(2, calculator.calculate("x").doubleValue(), delta);
        assertEquals(1, calculator.calculate("y").doubleValue(), delta);
    }

    @Test
    public void userDefinedFunctionsWork() {
        assertEquals(1, calculator.calculate("x := 1").doubleValue(), delta);
        assertEquals(0, calculator.calculate("square(x) := x * x").doubleValue(), delta);
        assertEquals(4, calculator.calculate("square(2)").doubleValue(), delta);
        assertEquals(1, calculator.calculate("x").doubleValue(), delta);

        assertEquals(0, calculator.calculate("add(x, y) := x + y").doubleValue(), delta);
        assertEquals(5, calculator.calculate("add(1, 4)").doubleValue(), delta);

        assertEquals(0, calculator.calculate("zero() := 0").doubleValue(), delta);
        assertEquals(0, calculator.calculate("zero()").doubleValue(), delta);

        calculator = new Calculator(sessionDao);
        assertEquals(4, calculator.calculate("square(2)").doubleValue(), delta);
        assertEquals(0, calculator.calculate("zero()").doubleValue(), delta);
    }

    @Test(expected = CalculatorException.class)
    public void throwsCalculationErrorOnAssignImmutable() {
        calculator.calculate("pi:=3");
    }

    @Test(expected = CalculatorException.class)
    public void deletingVariableWorks() {
        assertEquals(1, calculator.calculate("x:=1").doubleValue(), delta);
        assertEquals(1, calculator.calculate("x").doubleValue(), delta);
        assertEquals(0, calculator.calculate("delete x").doubleValue(), delta);
        calculator.calculate("x");
    }

    @Test()
    public void deleteDoesNotDeleteImmutable() {
        assertEquals(0, calculator.calculate("delete pi").doubleValue(), delta);
        assertEquals(Math.PI, calculator.calculate("pi").doubleValue(), delta);
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
        scope.declare("invalidConstant", new Evaluatable() {
            @Override
            public int getArgumentCount() {
                return 1;
            }

            @Override
            public double evaluate(double... arguments) {
                return 0;
            }
        }, false);
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
