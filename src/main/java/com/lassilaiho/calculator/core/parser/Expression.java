package com.lassilaiho.calculator.core.parser;

/**
 * {@link Expression} is an expression node in the abstract syntax tree representing a math
 * expression.
 */
public interface Expression {
    /**
     * Calls the visit method of visitor corresponding to the class implementing this interface.
     *
     * @param visitor visitor
     */
    void accept(ExpressionVisitor visitor);
}
