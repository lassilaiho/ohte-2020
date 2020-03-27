package com.lassilaiho.calculator.core.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Test;

public class FunctionCallNodeTest {
    @Test
    public void equalWhenSameNameAndArguments() {
        var left = new FunctionCallNode("sqrt", List.of(new NumberNode(2)));
        var right = new FunctionCallNode("sqrt", List.of(new NumberNode(2)));
        assertTrue(left.equals(right));
    }

    @Test
    public void notEqualWhenNameIsDifferent() {
        var left = new FunctionCallNode("sqrt", List.of(new NumberNode(2)));
        var right = new FunctionCallNode("log", List.of(new NumberNode(2)));
        assertFalse(left.equals(right));
    }

    @Test
    public void notEqualWhenDifferentArgumentCount() {
        var left = new FunctionCallNode("test", List.of(new NumberNode(2)));
        var right = new FunctionCallNode("test", List.of());
        assertFalse(left.equals(right));
    }

    @Test
    public void notEqualWhenDifferentArguments() {
        var left = new FunctionCallNode("sqrt", List.of(new NumberNode(3)));
        var right = new FunctionCallNode("sqrt", List.of(new NumberNode(2)));
        assertFalse(left.equals(right));
    }

    @Test
    public void notEqualWithNull() {
        var left = new FunctionCallNode("sqrt", List.of(new NumberNode(2)));
        assertFalse(left.equals(null));
    }

    @Test
    public void notEqualWithDifferentType() {
        var left = new FunctionCallNode("sqrt", List.of(new NumberNode(2)));
        var right = (Object) new NumberNode(2);
        assertFalse(left.equals(right));
    }
}
