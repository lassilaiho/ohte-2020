package com.lassilaiho.calculator.core.evaluator;

import com.lassilaiho.calculator.core.parser.*;

/**
 * {@link Evaluator} is a visitor that evaluates {@link Expression Expressions}.
 */
public final class Evaluator implements NodeVisitor {
    private double value = 0;

    /**
     * Returns the current value of the evaluator.
     * 
     * @return                     the value
     * @throws EvaluationException thrown if the current value is NaN
     */
    public double getValue() throws EvaluationException {
        if (Double.isNaN(value)) {
            throw new EvaluationException("calculation error");
        } else if (Double.isInfinite(value)) {
            throw new EvaluationException("overflow error");
        }
        return value;
    }

    private Scope namedValues;

    /**
     * Constructs a new {@link Evaluator}.
     * 
     * @param initialValue the initial value of the constructor
     * @param namedValues  the current scope
     */
    public Evaluator(double initialValue, Scope namedValues) {
        value = initialValue;
        this.namedValues = namedValues;
    }

    @Override
    public void visit(NumberNode node) {
        value = node.value;
    }

    @Override
    public void visit(VariableNode node) {
        var namedValue = namedValues.get(node.name);
        if (namedValue.getArgumentCount() != 0) {
            throw new EvaluationException(node.name + " is a function, not a variable");
        }
        value = namedValue.evaluate();
    }

    @Override
    public void visit(FunctionCallNode node) {
        var namedValue = namedValues.get(node.function);
        if (namedValue.getArgumentCount() != node.arguments.size()) {
            throw new EvaluationException("wrong number of arguments: expected "
                + namedValue.getArgumentCount() + ", got " + node.arguments.size());
        }
        var evaluatedArguments = new double[node.arguments.size()];
        var evaluator = new Evaluator(0, namedValues);
        for (var i = 0; i < evaluatedArguments.length; i++) {
            evaluator.value = 0;
            node.arguments.get(i).accept(evaluator);
            evaluatedArguments[i] = evaluator.value;
        }
        value = namedValue.evaluate(evaluatedArguments);
    }

    @Override
    public void visit(UnaryExpression node) {
        node.operand.accept(this);
        switch (node.operator) {
            case PLUS:
                break;
            case NEGATE:
                value = -value;
                break;
            default:
                throw new EvaluationException("invalid unary operator: " + node.operator);
        }
    }

    @Override
    public void visit(BinaryExpression node) {
        var left = new Evaluator(0, namedValues);
        node.left.accept(left);
        var right = new Evaluator(0, namedValues);
        node.right.accept(right);
        value = applyBinaryOperator(node.operator, left.value, right.value);
    }

    @Override
    public void visit(AssignmentNode node) {
        node.value.accept(this);
        namedValues.set(node.name, Function.constant(value));
    }

    @Override
    public void visit(FunctionDefinitionNode node) {
        namedValues.set(node.name, Function.ofDefinition(node, namedValues));
    }

    @Override
    public void visit(DeleteNode node) {
        for (var name : node.names) {
            namedValues.deleteAllMutable(name);
        }
    }

    private double applyBinaryOperator(BinaryOperator operator, double left,
        double right) {
        switch (operator) {
            case ADD:
                return left + right;
            case SUBTRACT:
                return left - right;
            case MULTIPLY:
                return left * right;
            case DIVIDE:
                if (right == 0) {
                    throw new EvaluationException("cannot divide by zero");
                }
                return left / right;
            default:
                throw new EvaluationException("invalid binary operator: " + operator);
        }
    }
}
