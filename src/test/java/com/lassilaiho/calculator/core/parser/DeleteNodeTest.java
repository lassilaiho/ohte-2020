package com.lassilaiho.calculator.core.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Test;

public class DeleteNodeTest {
    @Test
    public void equalWhenSame() {
        var left = new DeleteNode(List.of("x", "y"));
        var right = new DeleteNode(List.of("x", "y"));
        assertTrue(left.equals(right));
    }

    @Test
    public void notEqualWhenDifferentNames() {
        var left = new DeleteNode(List.of("x"));
        var right = new DeleteNode(List.of("x", "y"));
        assertFalse(left.equals(right));
    }

    @Test
    public void notEqualWithNull() {
        assertFalse(new DeleteNode(List.of("x")).equals(null));
    }

    @Test
    public void notEqualWithDifferentType() {
        assertFalse(new DeleteNode(List.of("x")).equals((Object) new VariableNode("x")));
    }
}
