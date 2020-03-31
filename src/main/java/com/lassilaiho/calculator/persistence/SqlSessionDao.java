package com.lassilaiho.calculator.persistence;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Implements {@link SessionDao} using a SQL database.
 */
public class SqlSessionDao implements SessionDao {
    private SqlHistoryDao historyDao;

    /**
     * Constructs a new {@link SqlSessionDao} backed by a SQL database.
     * 
     * @param connection the database connection used for the operations
     */
    public SqlSessionDao(Connection connection) {
        historyDao = new SqlHistoryDao(connection);
    }

    /**
     * Initializes the database. For example, creates tables. It is safe to call this method multiple
     * times.
     * 
     * @throws SQLException thrown if the initialization fails
     */
    public void initializeDatabase() throws SQLException {
        historyDao.initializeDatabase();
    }

    @Override
    public HistoryDao history() {
        return historyDao;
    }
}
