package com.lassilaiho.calculator.core.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class AssignmentNodeTest {
    @Test
    public void equalWhenSameValue() {
        var left = new AssignmentNode("pi", new NumberNode(3.14));
        var right = new AssignmentNode("pi", new NumberNode(3.14));
        assertTrue(left.equals(right));
    }

    @Test
    public void notEqualWhenDifferentName() {
        var left = new AssignmentNode("x", new NumberNode(0));
        var right = new AssignmentNode("y", new NumberNode(0));
        assertFalse(left.equals(right));
    }

    @Test
    public void notEqualWhenDifferentValue() {
        var left = new AssignmentNode("pi", new NumberNode(3.14));
        var right = new AssignmentNode("pi", new NumberNode(1));
        assertFalse(left.equals(right));
        assertFalse(left.equals(null));
    }

    @Test
    public void notEqualWhenDifferentType() {
        assertFalse(new AssignmentNode("x", new NumberNode(1)).equals((Object) "pi"));
    }
}
