package com.lassilaiho.calculator.core.parser;

/**
 * {@link VariableNode} is a variable node.
 */
public class VariableNode implements Expression {
    public final String name;

    /**
     * Constructs a variable node.
     * 
     * @param name the name of the variable
     */
    public VariableNode(String name) {
        this.name = name;
    }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof VariableNode)) {
            return false;
        }
        return name == ((VariableNode) other).name;
    }
}
