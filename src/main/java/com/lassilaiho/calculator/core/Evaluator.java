package com.lassilaiho.calculator.core;

import com.lassilaiho.calculator.core.parser.*;

/**
 * Evaluator is a visitor that evaluates {@link Expression Expressions}.
 */
public class Evaluator implements ExpressionVisitor {
    private double value = 0;

    public double getValue() {
        return value;
    }

    /**
     * Constructs a new {@link Evaluator}.
     * 
     * @param initialValue the initial value of the evaluator
     */
    public Evaluator(double initialValue) {
        value = initialValue;
    }

    @Override
    public void visit(NumberNode node) {
        value = node.value;

    }

    @Override
    public void visit(UnaryExpression node) {
        node.operand.accept(this);
        switch (node.operator) {
            case NEGATE:
                value = -value;
                break;
            default:
                throw new EvaluationException("invalid unary operator: " + node.operator);
        }
    }

    @Override
    public void visit(BinaryExpression node) {
        var left = new Evaluator(0);
        node.left.accept(left);
        var right = new Evaluator(0);
        node.right.accept(right);
        switch (node.operator) {
            case ADD:
                value = left.value + right.value;
                break;
            case SUBTRACT:
                value = left.value - right.value;
                break;
            case MULTIPLY:
                value = left.value * right.value;
                break;
            case DIVIDE:
                value = left.value / right.value;
                break;
            default:
                throw new EvaluationException(
                    "invalid binary operator: " + node.operator);
        }
    }
}
