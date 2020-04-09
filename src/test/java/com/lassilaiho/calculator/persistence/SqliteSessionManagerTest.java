package com.lassilaiho.calculator.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class SqliteSessionManagerTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private SqliteSessionManager manager;

    @Before
    public void setUp() {
        manager = new SqliteSessionManager("jdbc:sqlite");
    }

    @Test
    public void openSessionInExistingFolderWorks() throws Exception {
        var root = folder.newFolder();
        Files.createDirectories(Paths.get(root.getAbsolutePath() + "/folder"));
        manager.openSession(root.getAbsolutePath() + "/folder/test.session");
        assertNotNull(manager.getConnection());
        assertNotNull(manager.getSession());
    }

    @Test
    public void openSessionInNonexistentFolderWorks() throws Exception {
        var root = folder.newFolder();
        manager.openSession(root.getAbsolutePath() + "/folder/test.session");
        assertNotNull(manager.getConnection());
        assertNotNull(manager.getSession());
    }

    @Test
    public void switchDatabaseWorks() throws Exception {
        var root = folder.newFolder();
        manager.switchDatabase(root + "/first.session");
        var previousConnection = manager.getConnection();
        var previousSession = manager.getSession();
        assertNotNull(previousConnection);
        assertNotNull(previousSession);

        manager.switchDatabase(root + "/second.session");
        assertNotNull(manager.getConnection());
        assertNotNull(manager.getSession());
        assertNotEquals(previousConnection, manager.getConnection());
        assertEquals(previousSession, manager.getSession());
        previousConnection = manager.getConnection();
        previousSession = manager.getSession();

        manager.switchDatabase(root + "/first.session");
        assertNotNull(manager.getConnection());
        assertNotNull(manager.getSession());
        assertNotEquals(previousConnection, manager.getConnection());
        assertEquals(previousSession, manager.getSession());
    }

    @After
    public void tearDown() throws Exception {
        manager.close();
    }
}
