package com.lassilaiho.calculator.core.evaluator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * {@link Scope} maps names to values.
 */
public final class Scope implements Iterable<Scope.NamedValue> {
    private HashMap<String, ArrayList<NamedValue>> valueMap = new HashMap<>();

    /**
     * Returns a new {@link Scope} with default immutable bindings from namedValues.
     * 
     * @param  namedValues default bindings
     * @return             the constructed {@link Scope}
     */
    public static Scope ofMap(Map<String, Evaluatable> namedValues) {
        var scope = new Scope();
        namedValues.forEach((name, value) -> scope.declare(name, value, false));
        return scope;
    }

    /**
     * Returns the value bound to name. Throws if the name is undefined.
     * 
     * @param  name                the name of the value
     * @return                     the value bound to name
     * @throws EvaluationException thrown if name is undefined
     */
    public Evaluatable get(String name) throws EvaluationException {
        var values = valueMap.get(name);
        if (values == null || values.isEmpty()) {
            throw new EvaluationException("undefined name: " + name);
        }
        return values.get(values.size() - 1).value;
    }

    /**
     * Declares a binding of name to value, shadowing any existing binding with the same name.
     * 
     * @param name    the name of the value
     * @param value   the value to declare
     * @param mutable whether the value can be reassigned
     */
    public void declare(String name, Evaluatable value, boolean mutable) {
        var values = valueMap.get(name);
        if (values == null) {
            values = new ArrayList<>();
            valueMap.put(name, values);
        }
        values.add(new NamedValue(name, value, mutable));
    }

    /**
     * Sets the name to value. If no binding exists, a new one is created. If a binding exists, it is
     * rebound to value. Throws if rebinding is attempted on an immutable binding.
     * 
     * @param  name                the name of the value
     * @param  value               the value to set
     * @throws EvaluationException thrown if name exists and is immutable
     */
    public void set(String name, Evaluatable value) throws EvaluationException {
        var values = valueMap.get(name);
        if (values == null) {
            values = new ArrayList<>();
            valueMap.put(name, values);
        }
        if (values.isEmpty()) {
            values.add(new NamedValue(name, value, true));
        } else {
            values.get(values.size() - 1).assign(value);
        }
    }

    /**
     * Deletes the most recent binding of name. Does nothing if name is undefined. Immutable bindings
     * can be deleted.
     * 
     * @param name the name to delete
     */
    public void delete(String name) {
        var values = valueMap.get(name);
        if (values == null || values.isEmpty()) {
            return;
        }
        values.remove(values.size() - 1);
    }

    /**
     * Deletes all mutable bindings of name up to the first immutable binding. Does nothing if name is
     * undefined.
     * 
     * @param name the name to delete
     */
    public void deleteAllMutable(String name) {
        var values = valueMap.get(name);
        if (values == null) {
            return;
        }
        while (!values.isEmpty() && values.get(values.size() - 1).mutable) {
            values.remove(values.size() - 1);
        }
    }

    @Override
    public Iterator<Scope.NamedValue> iterator() {
        return new ScopeIterator();
    }

    /**
     * A container for name-value pairs.
     */
    public static final class NamedValue {
        private final String name;
        private Evaluatable value;
        private final boolean mutable;

        public String getName() {
            return name;
        }

        public Evaluatable getValue() {
            return value;
        }

        public boolean getMutable() {
            return mutable;
        }

        private NamedValue(String name, Evaluatable value, boolean mutable) {
            this.name = name;
            this.value = value;
            this.mutable = mutable;
        }

        private void assign(Evaluatable newValue) throws EvaluationException {
            if (mutable) {
                value = newValue;
            } else {
                throw new EvaluationException("this name cannot be reassigned");
            }
        }
    }

    private final class ScopeIterator implements Iterator<NamedValue> {
        private Iterator<ArrayList<NamedValue>> outer;
        private NamedValue nextValue;

        public ScopeIterator() {
            outer = valueMap.values().iterator();
            advance();
        }

        @Override
        public boolean hasNext() {
            return nextValue != null;
        }

        @Override
        public NamedValue next() {
            if (nextValue == null) {
                throw new NoSuchElementException();
            }
            var currentValue = nextValue;
            advance();
            return currentValue;
        }

        private void advance() {
            nextValue = null;
            while (nextValue == null && outer.hasNext()) {
                var list = outer.next();
                if (!list.isEmpty()) {
                    nextValue = list.get(list.size() - 1);
                }
            }
        }
    }
}
