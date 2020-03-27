package com.lassilaiho.calculator.core.parser;

import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

/**
 * {@link FunctionCallNode} is a function call node.
 */
public class FunctionCallNode implements Expression {
    public final String function;
    /**
     * The arguments of the function call. Modifying the argument list is undefined behavior.
     */
    public final List<Expression> arguments;

    /**
     * Constructs a function call node. Modifying the passed list is undefined behavior.
     * 
     * @param function  the function identifier
     * @param arguments the arguments of the function
     */
    public FunctionCallNode(String function, List<Expression> arguments) {
        this.function = function;
        this.arguments = Collections.unmodifiableList(arguments);
    }

    @Override
    public void accept(ExpressionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        var joiner = new StringJoiner(",", function + "(", ")");
        arguments.stream().forEach(x -> joiner.add(x.toString()));
        return joiner.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof FunctionCallNode)) {
            return false;
        }
        var o = (FunctionCallNode) other;
        return function.equals(o.function) && arguments.equals(o.arguments);
    }
}
