package com.lassilaiho.calculator.core.parser;

/**
 * A visitor for classes implementing {@link Node}.
 */
public interface NodeVisitor {
    /**
     * Visit a number node.
     * 
     * @param node number node to visit
     */
    void visit(NumberNode node);

    /**
     * Visit a variable node.
     * 
     * @param node variable node to visit
     */
    void visit(VariableNode node);

    /**
     * Visit a function call node.
     * 
     * @param node function call node to visit
     */
    void visit(FunctionCallNode node);

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

    /**
     * Visit an assignment node.
     * 
     * @param node assignment node to visit
     */
    void visit(AssignmentNode node);

    /**
     * Visit a function definition node.
     * 
     * @param node function definition node to visit
     */
    void visit(FunctionDefinitionNode node);
}
