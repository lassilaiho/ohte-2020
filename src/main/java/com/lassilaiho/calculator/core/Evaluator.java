package com.lassilaiho.calculator.core;

import java.util.Map;
import com.lassilaiho.calculator.core.parser.*;

/**
 * {@link Evaluator} is a visitor that evaluates {@link Expression Expressions}.
 */
public class Evaluator implements ExpressionVisitor {
    private double value = 0;

    public double getValue() {
        return value;
    }

    private Map<String, Evaluatable> namedValues;

    /**
     * Constructs a new {@link Evaluator}.
     * 
     * @param initialValue the initial value of the constructor
     * @param namedValues  the set of named values in scope
     */
    public Evaluator(double initialValue, Map<String, Evaluatable> namedValues) {
        value = initialValue;
        this.namedValues = namedValues;
    }

    @Override
    public void visit(NumberNode node) {
        value = node.value;
    }

    @Override
    public void visit(VariableNode node) {
        var namedValue = resolveName(node.name);
        if (namedValue.getArgumentCount() != 0) {
            throw new EvaluationException("variables cannot have arguments");
        }
        value = namedValue.evaluate();
    }

    @Override
    public void visit(FunctionCallNode node) {
        var namedValue = resolveName(node.function);
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
        switch (node.operator) {
            case ADD:
                value = left.value + right.value;
                break;
            case SUBTRACT:
                value = left.value - right.value;
                break;
            case MULTIPLY:
                value = left.value * right.value;
                break;
            case DIVIDE:
                if (right.value == 0) {
                    throw new EvaluationException("cannot divide by zero");
                }
                value = left.value / right.value;
                break;
            default:
                throw new EvaluationException(
                    "invalid binary operator: " + node.operator);
        }
    }

    private Evaluatable resolveName(String name) {
        var namedValue = namedValues.get(name);
        if (namedValue == null) {
            throw new EvaluationException("undefined name: " + name);
        }
        return namedValue;
    }
}
