package com.lassilaiho.calculator.core.evaluator;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import com.lassilaiho.calculator.core.evaluator.Scope.NamedValue;
import org.junit.Before;
import org.junit.Test;

public final class ScopeIteratorTest {
    private Scope scope;
    private double delta = 0.000001;

    @Before
    public void setUp() {
        scope = new Scope();
        scope.declare("x", Function.constant(2), false);
        scope.declare("y", Function.constant(-1), false);
        scope.declare("z", Function.constant(0), false);
        scope.declare("x", Function.constant(5), false);
        scope.delete("y");
    }

    @Test
    public void iterationWorks() {
        var values = new ArrayList<NamedValue>();
        for (var value : scope) {
            values.add(value);
        }
        assertEquals(2, values.size());
        values.sort((x, y) -> x.getName().compareTo(y.getName()));
        assertEquals(5, values.get(0).getValue().evaluate(), delta);
        assertEquals(0, values.get(1).getValue().evaluate(), delta);
    }

    @Test(expected = NoSuchElementException.class)
    public void nextThrowsWhenOutOfElements() {
        var iterator = scope.iterator();
        iterator.next();
        iterator.next();
        iterator.next();
    }
}
