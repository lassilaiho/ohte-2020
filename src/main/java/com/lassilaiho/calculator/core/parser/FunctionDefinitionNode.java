package com.lassilaiho.calculator.core.parser;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A function definition node.
 */
public final class FunctionDefinitionNode implements Statement {
    public final String name;
    public final List<String> parameters;
    public final Expression body;

    /**
     * Constructs a function definition node.
     * 
     * @param name       the name of the function
     * @param parameters the parameter list of the function
     * @param body       the body of the function
     */
    public FunctionDefinitionNode(String name, List<String> parameters, Expression body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return name + "(" + parameters.stream().collect(Collectors.joining(",")) + ")"
            + ":=" + body;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof FunctionDefinitionNode)) {
            return false;
        }
        var o = (FunctionDefinitionNode) other;
        return name.equals(o.name) && parameters.equals(o.parameters)
            && body.equals(o.body);
    }
}
