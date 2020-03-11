package com.lassilaiho.calculator.core.parser;

import com.lassilaiho.calculator.core.Operator;

/**
 * {@link BinaryExpression} is a binary expression node.
 */
public class BinaryExpression implements Expression {
    public final Operator operator;
    public final Expression left, right;

    /**
     * Constructs a binary expression node.
     * 
     * @param left     left operand
     * @param operator operator
     * @param right    right operand
     */
    public BinaryExpression(Expression left, Operator operator, Expression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + operator.toString() + right.toString() + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof BinaryExpression)) {
            return false;
        }
        var o = (BinaryExpression) other;
        return operator.equals(o.operator) && left.equals(o.left)
            && right.equals(o.right);
    }
}
