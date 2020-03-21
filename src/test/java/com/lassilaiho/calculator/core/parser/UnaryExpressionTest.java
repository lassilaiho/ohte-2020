package com.lassilaiho.calculator.core.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class UnaryExpressionTest {
    @Test
    public void equalWhenSameOperatorAndOperand() {
        var left = new UnaryExpression(Operator.NEGATE, new NumberNode(6));
        var right = new UnaryExpression(Operator.NEGATE, new NumberNode(6));
        assertTrue(left.equals(right));
    }

    @Test
    public void notEqualWhenDifferentOperand() {
        var left = new UnaryExpression(Operator.NEGATE, new NumberNode(1));
        var right = new UnaryExpression(Operator.NEGATE, new NumberNode(6));
        assertFalse(left.equals(right));
    }

    @Test
    public void notEqualWhenDifferentOperator() {
        // left uses an invalid unary operator because currently there is only one.
        var left = new UnaryExpression(Operator.SUBTRACT, new NumberNode(6));
        var right = new UnaryExpression(Operator.NEGATE, new NumberNode(6));
        assertFalse(left.equals(right));
    }

    @Test
    public void notEqualWithNull() {
        var left = new UnaryExpression(Operator.NEGATE, new NumberNode(6));
        assertFalse(left.equals(null));
    }

    @Test
    public void notEqualWhenDifferentType() {
        var left = new UnaryExpression(Operator.NEGATE, new NumberNode(6));
        var right = (Object) new NumberNode(6);
        assertFalse(left.equals(right));
    }
}
