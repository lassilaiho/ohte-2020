package com.lassilaiho.calculator.core.parser;

/**
 * An assignment node.
 */
public class AssignmentNode implements Statement {
    public final String name;
    public final Expression value;

    /**
     * Constructs an assignment node.
     * 
     * @param name  the name of the variable
     * @param value the new value of the variable
     */
    public AssignmentNode(String name, Expression value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return name + ":=" + value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof AssignmentNode)) {
            return false;
        }
        var o = (AssignmentNode) other;
        return name.equals(o.name) && value.equals(o.value);
    }
}
