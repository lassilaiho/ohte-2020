package com.lassilaiho.calculator.core.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class VariableNodeTest {
    @Test
    public void equalWhenSameValue() {
        assertTrue(new VariableNode("pi").equals(new VariableNode("pi")));
    }

    @Test
    public void notEqualWhenDifferentValue() {
        assertFalse(new VariableNode("pi").equals(new VariableNode("e")));
        assertFalse(new VariableNode("pi").equals(null));
    }

    @Test
    public void notEqualWhenDifferentType() {
        assertFalse(new VariableNode("pi").equals((Object) "pi"));
    }
}
