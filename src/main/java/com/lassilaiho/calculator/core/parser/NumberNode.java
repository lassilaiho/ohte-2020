package com.lassilaiho.calculator.core.parser;

/**
 * {@link NumberNode} is a number node.
 */
public final class NumberNode implements Expression {
    public final double value;

    /**
     * Constructs a number node.
     * 
     * @param value the value of the node
     */
    public NumberNode(double value) {
        this.value = value;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof NumberNode)) {
            return false;
        }
        return value == ((NumberNode) other).value;
    }
}
