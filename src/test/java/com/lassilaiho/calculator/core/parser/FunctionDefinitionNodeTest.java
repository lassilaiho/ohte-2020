package com.lassilaiho.calculator.core.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Test;

public class FunctionDefinitionNodeTest {
    @Test
    public void equalWhenSameNameAndArguments() {
        var left =
            new FunctionDefinitionNode("cube", List.of("x"), new FunctionCallNode("pow",
                List.of(new VariableNode("x"), new NumberNode(3))));
        var right =
            new FunctionDefinitionNode("cube", List.of("x"), new FunctionCallNode("pow",
                List.of(new VariableNode("x"), new NumberNode(3))));
        assertTrue(left.equals(right));
    }

    @Test
    public void notEqualWhenNameIsDifferent() {
        var left =
            new FunctionDefinitionNode("cube", List.of("x"), new FunctionCallNode("pow",
                List.of(new VariableNode("x"), new NumberNode(3))));
        var right =
            new FunctionDefinitionNode("other_cube", List.of("x"), new FunctionCallNode(
                "pow", List.of(new VariableNode("x"), new NumberNode(3))));
        assertFalse(left.equals(right));
    }

    @Test
    public void notEqualWhenDifferentArgumentCount() {
        var left =
            new FunctionDefinitionNode("cube", List.of("x"), new FunctionCallNode("pow",
                List.of(new VariableNode("x"), new NumberNode(3))));
        var right =
            new FunctionDefinitionNode("cube", List.of("x", "y"), new FunctionCallNode(
                "pow", List.of(new VariableNode("x"), new NumberNode(3))));
        assertFalse(left.equals(right));
    }

    @Test
    public void notEqualWhenDifferentArguments() {
        var left =
            new FunctionDefinitionNode("cube", List.of("x"), new FunctionCallNode("pow",
                List.of(new VariableNode("x"), new NumberNode(3))));
        var right =
            new FunctionDefinitionNode("cube", List.of("y"), new FunctionCallNode("pow",
                List.of(new VariableNode("x"), new NumberNode(3))));
        assertFalse(left.equals(right));
    }

    @Test
    public void notEqualWhenDifferentBody() {
        var left =
            new FunctionDefinitionNode("cube", List.of("x"), new FunctionCallNode("pow",
                List.of(new VariableNode("x"), new NumberNode(3))));
        var right = new FunctionDefinitionNode("cube", List.of("x"), new NumberNode(3));
        assertFalse(left.equals(right));
    }

    @Test
    public void notEqualWithNull() {
        var left =
            new FunctionDefinitionNode("cube", List.of("x"), new FunctionCallNode("pow",
                List.of(new VariableNode("x"), new NumberNode(3))));
        assertFalse(left.equals(null));
    }

    @Test
    public void notEqualWithDifferentType() {
        var left =
            new FunctionDefinitionNode("cube", List.of("x"), new FunctionCallNode("pow",
                List.of(new VariableNode("x"), new NumberNode(3))));
        var right = (Object) new NumberNode(2);
        assertFalse(left.equals(right));
    }
}
