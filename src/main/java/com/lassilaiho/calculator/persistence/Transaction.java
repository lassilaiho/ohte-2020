package com.lassilaiho.calculator.persistence;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Provides utility functions for SQL transactions.
 */
public class Transaction {
    /**
     * Runs action in a transaction. The transaction is committed automatically if action doesn't throw
     * an exception. If it does, the transaction is rolled back.
     * 
     * @param  connection   the connection to use
     * @param  action       the operations performed during the transaction
     * @throws SQLException thrown if action throws this or something else fails
     */
    public static void with(Connection connection, Action action) throws SQLException {
        var autoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);
            action.run();
            connection.commit();
        } catch (SQLException | RuntimeException outer) {
            try {
                connection.rollback();
            } catch (Exception inner) {
                outer.addSuppressed(inner);
            }
            throw outer;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    /**
     * An action performed in a transaction.
     */
    @FunctionalInterface
    public interface Action {
        /**
         * Runs the action of the transaction.
         * 
         * @throws SQLException thrown if the action fails
         */
        void run() throws SQLException;
    }
}
