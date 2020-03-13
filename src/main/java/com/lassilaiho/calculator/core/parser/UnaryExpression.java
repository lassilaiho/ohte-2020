package com.lassilaiho.calculator.core.parser;

/**
 * {@link UnaryExpression} is a binary expression node.
 */
public class UnaryExpression implements Expression {
    public final Operator operator;
    public final Expression operand;

    /**
     * Constructs a unary expression node.
     * 
     * @param operator operator
     * @param operand  operand
     */
    public UnaryExpression(Operator operator, Expression operand) {
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return operator.toString() + operand.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof UnaryExpression)) {
            return false;
        }
        var o = (UnaryExpression) other;
        return operator.equals(o.operator) && operand.equals(o.operand);
    }
}
