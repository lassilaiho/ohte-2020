package com.lassilaiho.calculator.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages a {@link SessionDao} implementation which uses an SQLite database as the backing store.
 */
public final class SqliteSessionManager {
    private String driver;
    private Connection connection;
    private SqlSessionDao sessionDao;
    private String currentPath;

    /**
     * Returns the current active connection or null if no connection exists.
     * 
     * @return the connection or null
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Returns the current session or null if there is no active session.
     * 
     * @return the session or null
     */
    public SessionDao getSession() {
        return sessionDao;
    }

    /**
     * Returns the path of the current session file or null if there is no active session.
     * 
     * @return the path or null
     */
    public String getCurrentPath() {
        return currentPath;
    }

    private void setCurrentPath(String value) {
        currentPath = ":memory:".equals(value) ? null : value;
    }

    /**
     * Constructs a new {@link SqliteSessionManager} which uses driver for connecting to the database.
     * 
     * @param driver driver to use
     */
    public SqliteSessionManager(String driver) {
        this.driver = driver;
    }

    /**
     * Replaces the current session (if one exists) with a new session. The current session is closed
     * automatically. Using the {@link Connection} or {@link SessionDao} of the current session after
     * opening a new one is undefined behavior.
     * 
     * @param  file         database file to open
     * @throws IOException  thrown if opening the session fails
     * @throws SQLException thrown if opening the session fails
     */
    public void openSession(String file) throws IOException, SQLException {
        var newConnection = openDatabase(file);
        try {
            close();
        } finally {
            connection = newConnection;
            setCurrentPath(file);
            sessionDao = new SqlSessionDao(connection);
            sessionDao.initializeDatabase();
        }
    }

    private Connection openDatabase(String file) throws IOException, SQLException {
        var path = Paths.get(file);
        var parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        return DriverManager.getConnection(driver + ":" + path);
    }

    /**
     * Switches the currently used database to file without changing the state of the current session
     * (if one exists).
     * 
     * @param  file         the new database
     * @throws IOException  thrown if switching the database fails
     * @throws SQLException thrown if switching the database fails
     */
    public void switchDatabase(String file) throws IOException, SQLException {
        if (sessionDao == null) {
            openSession(file);
        } else {
            var newConnection = openDatabase(file);
            try {
                sessionDao.switchDatabase(newConnection);
            } catch (Exception e) {
                newConnection.close();
                throw e;
            }
            try {
                closeConnection();
            } finally {
                connection = newConnection;
                setCurrentPath(file);
            }
        }
    }

    private void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    /**
     * Closes the current session. If a new session is opened after calling {@link close}, {@link close}
     * must be called again to close the new session.
     * 
     * @throws SQLException thrown if closing the session fails
     */
    public void close() throws SQLException {
        closeConnection();
        sessionDao = null;
    }
}
