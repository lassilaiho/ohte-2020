package com.lassilaiho.calculator.core.evaluator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link Scope} maps names to values.
 */
public class Scope {
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
        values.add(new NamedValue(value, mutable));
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
            values.add(new NamedValue(value, true));
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

    private class NamedValue {
        public Evaluatable value;
        public boolean mutable = false;

        public NamedValue(Evaluatable value, boolean mutable) {
            this.value = value;
            this.mutable = mutable;
        }

        public void assign(Evaluatable newValue) throws EvaluationException {
            if (mutable) {
                value = newValue;
            } else {
                throw new EvaluationException("this name cannot be reassigned");
            }
        }
    }
}
