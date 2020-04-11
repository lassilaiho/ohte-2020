package com.lassilaiho.calculator.core.parser;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link DeleteNode} deletes variable or function bindings.
 */
public class DeleteNode implements Statement {
    public final List<String> names;

    /**
     * Constructs a delete node.
     * 
     * @param names the names to delete
     */
    public DeleteNode(List<String> names) {
        this.names = names;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "delete " + names.stream().collect(Collectors.joining(","));
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof DeleteNode)) {
            return false;
        }
        return names.equals(((DeleteNode) other).names);
    }
}
