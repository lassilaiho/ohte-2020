package com.lassilaiho.calculator.core.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class NumberNodeTest {
    @Test
    public void equalWhenSameValue() {
        assertTrue(new NumberNode(6).equals(new NumberNode(6)));
    }

    @Test
    public void notEqualWhenDifferentValue() {
        assertFalse(new NumberNode(6).equals(new NumberNode(4)));
        assertFalse(new NumberNode(6).equals(null));
    }

    @Test
    public void notEqualWhenDifferentType() {
        assertFalse(new NumberNode(6).equals((Object) 6));
    }
}
