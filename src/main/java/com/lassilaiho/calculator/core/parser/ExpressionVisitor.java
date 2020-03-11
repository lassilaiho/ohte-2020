package com.lassilaiho.calculator.core.parser;

/**
 * A visitor for classes implementing {@link Expression}.
 */
public interface ExpressionVisitor {
    /**
     * Visit a number node.
     * 
     * @param node number node to visit
     */
    void visit(Number node);

    /**
     * Visit a unary expression node.
     * 
     * @param node unary expression node to visit
     */
    void visit(UnaryExpression node);

    /**
     * Visit a binary expression node.
     * 
     * @param node binary expression node to visit
     */
    void visit(BinaryExpression node);
}
