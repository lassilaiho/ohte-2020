package com.lassilaiho.calculator.core.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class BinaryExpressionTest {
    @Test
    public void equalWhenSameOperatorAndOperands() {
        var left = new BinaryExpression(new NumberNode(-2), BinaryOperator.ADD,
            new NumberNode(6));
        var right = new BinaryExpression(new NumberNode(-2), BinaryOperator.ADD,
            new NumberNode(6));
        assertTrue(left.equals(right));
    }

    @Test
    public void notEqualWhenLeftOperandDifferent() {
        var left = new BinaryExpression(new NumberNode(-1), BinaryOperator.ADD,
            new NumberNode(6));
        var right = new BinaryExpression(new NumberNode(-2), BinaryOperator.ADD,
            new NumberNode(6));
        assertFalse(left.equals(right));
    }

    @Test
    public void notEqualWhenRightOperandDifferent() {
        var left = new BinaryExpression(new NumberNode(-2), BinaryOperator.ADD,
            new NumberNode(-6));
        var right = new BinaryExpression(new NumberNode(-2), BinaryOperator.ADD,
            new NumberNode(6));
        assertFalse(left.equals(right));
    }

    @Test
    public void notEqualWhenDifferentOperator() {
        var left = new BinaryExpression(new NumberNode(-2), BinaryOperator.MULTIPLY,
            new NumberNode(6));
        var right = new BinaryExpression(new NumberNode(-2), BinaryOperator.ADD,
            new NumberNode(6));
        assertFalse(left.equals(right));
    }

    @Test
    public void notEqualWithNull() {
        var left = new BinaryExpression(new NumberNode(-2), BinaryOperator.MULTIPLY,
            new NumberNode(6));
        assertFalse(left.equals(null));
    }

    @Test
    public void notEqualWhenDifferentType() {
        var left = new BinaryExpression(new NumberNode(-2), BinaryOperator.MULTIPLY,
            new NumberNode(6));
        var right = (Object) new NumberNode(6);
        assertFalse(left.equals(right));
    }
}
