package com.lassilaiho.calculator.core.parser;

/**
 * A math input abstract syntax tree node.
 */
public interface Node {
    /**
     * Calls the visit method of visitor corresponding to the class implementing this interface.
     *
     * @param visitor visitor
     */
    void accept(NodeVisitor visitor);
}
