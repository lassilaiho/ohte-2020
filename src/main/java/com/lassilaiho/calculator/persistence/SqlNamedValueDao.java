package com.lassilaiho.calculator.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Implements {@link NamedValueDao} using a SQL database.
 */
public class SqlNamedValueDao implements NamedValueDao {
    private Connection connection;

    /**
     * Constructs a new {@link SqlNamedValueDao} backed by a SQL database.
     * 
     * @param connection the database connection to use
     */
    public SqlNamedValueDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Initializes the database. It is safe to cal this method multiple times.
     * 
     * @throws SQLException thrown if the initialization fails
     */
    public void initializeDatabase() throws SQLException {
        Transaction.with(connection, () -> {
            try (var statement = connection.createStatement()) {
                statement.execute(
                    "CREATE TABLE IF NOT EXISTS named_value (id INTEGER PRIMARY KEY, name TEXT UNIQUE NOT NULL, body TEXT NOT NULL)");
                statement.execute(
                    "CREATE TABLE IF NOT EXISTS named_value_param (named_value_id INTEGER NOT NULL REFERENCES named_value(id) ON DELETE CASCADE, name TEXT NOT NULL)");
                statement.execute(
                    "CREATE INDEX IF NOT EXISTS ix_named_value_param_name ON named_value_param(name)");
            }
        });
    }

    /**
     * Switches the database used to persist named values. Current named values replaces the named
     * values in the new database.
     * 
     * @param  newConnection the new database connection used for persistence
     * @throws SQLException  thrown if the operation fails
     */
    public void switchDatabase(Connection newConnection) throws SQLException {
        Transaction.with(connection, () -> {
            Transaction.with(newConnection, () -> {
                var tempDao = new SqlNamedValueDao(newConnection);
                tempDao.initializeDatabase();
                try (var statement = newConnection.createStatement()) {
                    statement.execute("DELETE FROM named_value_param");
                    statement.execute("DELETE FROM named_value");
                }
                for (var entry : getAllValues()) {
                    var id = tempDao.insertNamedValue(entry.name, entry.value);
                    for (var param : entry.parameters) {
                        tempDao.insertParam(id, param);
                    }
                }
                connection = newConnection;
            });
        });
    }

    @Override
    public void setValue(String name, List<String> params, String value) {
        try {
            Transaction.with(connection, () -> {
                clearValue(name);
                var id = insertNamedValue(name, value);
                for (var param : params) {
                    insertParam(id, param);
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Entry> getAllValues() {
        var results = new ArrayList<Entry>();
        var resultMap = new HashMap<Long, Entry>();
        try {
            Transaction.with(connection, () -> {
                queryNamedValues(results, resultMap);
                queryParams(resultMap);
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }

    @Override
    public void clearValue(String name) {
        try (var statement =
            connection.prepareStatement("DELETE FROM named_value WHERE name = ?")) {
            statement.setString(1, name);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private long insertNamedValue(String name, String body) throws SQLException {
        try (var statement = connection.prepareStatement(
            "INSERT INTO named_value (name, body) VALUES (?,?)",
            Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setString(2, body);
            statement.execute();
            try (var keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
                throw new SQLException("no ID was returned");
            }
        }
    }

    private void insertParam(long id, String name) throws SQLException {
        try (var statement = connection.prepareStatement(
            "INSERT INTO named_value_param (named_value_id, name) VALUES (?,?)",
            Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, id);
            statement.setString(2, name);
            statement.execute();
        }
    }

    private void queryNamedValues(ArrayList<Entry> entryList,
        HashMap<Long, Entry> entryMap) throws SQLException {
        try (var statement = connection.prepareStatement("SELECT * FROM named_value");
            var rows = statement.executeQuery()) {
            while (rows.next()) {
                var id = rows.getLong(1);
                var entry =
                    new Entry(rows.getString(2), new ArrayList<>(), rows.getString(3));
                entryList.add(entry);
                entryMap.put(id, entry);
            }
        }
    }

    private void queryParams(HashMap<Long, Entry> entries) throws SQLException {
        try (
            var statement =
                connection.prepareStatement("SELECT * FROM named_value_param");
            var rows = statement.executeQuery()) {
            while (rows.next()) {
                entries.get(rows.getLong(1)).parameters.add(rows.getString(2));
            }
        }
    }
}
