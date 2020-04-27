package com.lassilaiho.calculator.core.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class UnaryExpressionTest {
    @Test
    public void equalWhenSameOperatorAndOperand() {
        var left = new UnaryExpression(UnaryOperator.NEGATE, new NumberNode(6));
        var right = new UnaryExpression(UnaryOperator.NEGATE, new NumberNode(6));
        assertTrue(left.equals(right));
    }

    @Test
    public void notEqualWhenDifferentOperand() {
        var left = new UnaryExpression(UnaryOperator.NEGATE, new NumberNode(1));
        var right = new UnaryExpression(UnaryOperator.NEGATE, new NumberNode(6));
        assertFalse(left.equals(right));
    }

    @Test
    public void notEqualWhenDifferentOperator() {
        var left = new UnaryExpression(UnaryOperator.PLUS, new NumberNode(6));
        var right = new UnaryExpression(UnaryOperator.NEGATE, new NumberNode(6));
        assertFalse(left.equals(right));
    }

    @Test
    public void notEqualWithNull() {
        var left = new UnaryExpression(UnaryOperator.NEGATE, new NumberNode(6));
        assertFalse(left.equals(null));
    }

    @Test
    public void notEqualWhenDifferentType() {
        var left = new UnaryExpression(UnaryOperator.NEGATE, new NumberNode(6));
        var right = (Object) new NumberNode(6);
        assertFalse(left.equals(right));
    }
}
