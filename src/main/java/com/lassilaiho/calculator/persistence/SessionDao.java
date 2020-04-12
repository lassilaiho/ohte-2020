package com.lassilaiho.calculator.persistence;

/**
 * Provides persistence operations for calculator sessions.
 */
public interface SessionDao {
    /**
     * Returns a {@link HistoryDao} that can be used to access the calculation history of the session.
     * 
     * @return an object implementing {@link HistoryDao}
     */
    HistoryDao history();

    /**
     * Returns a {@link NamedValueDao} that can be used to access stored named values.
     * 
     * @return an object implementing {@link NamedValueDao}
     */
    NamedValueDao namedValues();
}
