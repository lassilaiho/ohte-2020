package com.lassilaiho.calculator.persistence;

import java.util.List;

/**
 * Provides persistence operations for named values.
 */
public interface NamedValueDao {
    /**
     * Sets the value of name to value, overwriting the current value if present. Modifying params after
     * passing it to this function is undefined behavior.
     * 
     * @param name   the name to set
     * @param params the parameters for the value
     * @param value  the value to set
     */
    void setValue(String name, List<String> params, String value);

    /**
     * Returns a list of all stored values. Modifying the returned list is undefined behavior.
     * 
     * @return the list of values
     */
    List<Entry> getAllValues();

    /**
     * Clears the value associated with name. Does nothing if name doesn't exist.
     * 
     * @param name the name to clear
     */
    void clearValue(String name);

    /**
     * A container for named values. Modifying {@link #parameters} is undefined behavior.
     */
    class Entry {
        public final String name;
        public final List<String> parameters;
        public final String value;

        Entry(String name, List<String> params, String value) {
            this.name = name;
            parameters = params;
            this.value = value;
        }
    }
}
