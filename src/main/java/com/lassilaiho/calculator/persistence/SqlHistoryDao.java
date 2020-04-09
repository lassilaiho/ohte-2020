package com.lassilaiho.calculator.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.lassilaiho.calculator.core.HistoryEntry;

/**
 * Implements {@link HistoryDao} using a SQL database.
 */
public final class SqlHistoryDao implements HistoryDao {
    private Connection connection;

    /**
     * Constructs a new {@link HistoryDao} backed by a SQL database.
     * 
     * @param connection the database connection used for the operations
     */
    public SqlHistoryDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Initializes the database. For example, creates tables. It is safe to call this method multiple
     * times.
     * 
     * @throws SQLException thrown if the initialization fails
     */
    public void initializeDatabase() throws SQLException {
        try (var statement = connection.createStatement()) {
            statement.execute(
                "CREATE TABLE IF NOT EXISTS history (id INTEGER PRIMARY KEY, expression TEXT NOT NULL, value NUMERIC NOT NULL)");
        }
    }

    @Override
    public void addEntry(HistoryEntry entry) {
        try (var statement = connection
            .prepareStatement("INSERT INTO history (expression, value) VALUES (?, ?)")) {
            statement.setString(1, entry.getExpression());
            statement.setObject(2, entry.getValue());
            statement.execute();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public List<HistoryEntry> getAllEntries() {
        try (var statement = connection
            .prepareStatement("SELECT expression, value FROM history ORDER BY id")) {
            var results = new ArrayList<HistoryEntry>();
            try (var rows = statement.executeQuery()) {
                while (rows.next()) {
                    results.add(new HistoryEntry(rows.getString(1), rows.getDouble(2)));
                }
            }
            return results;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void removeAllEntries() {
        try (var statement = connection.prepareStatement("DELETE FROM history")) {
            statement.execute();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public HistoryEntry getNewestEntry() {
        try (var statement = connection.prepareStatement(
            "SELECT expression, value FROM history ORDER BY id DESC LIMIT 1")) {
            try (var rows = statement.executeQuery()) {
                if (rows.next()) {
                    return new HistoryEntry(rows.getString(1), rows.getDouble(2));
                }
                return null;
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
