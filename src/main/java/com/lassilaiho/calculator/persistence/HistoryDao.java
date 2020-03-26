package com.lassilaiho.calculator.persistence;

import java.util.List;
import com.lassilaiho.calculator.core.HistoryEntry;

/**
 * Provides persistence operations for {@link HistoryEntry} objects.
 */
public interface HistoryDao {
    /**
     * Adds a new history entry to the data store.
     * 
     * @param entry the entry to add
     */
    void addEntry(HistoryEntry entry);

    /**
     * Returns all history entries. The entries are ordered from oldest to newest. Modifying the
     * returned list is undefined behavior.
     * 
     * @return history entries
     */
    List<HistoryEntry> getAllEntries();

    /**
     * Returns the newest history entry or null if the history is empty.
     * 
     * @return the newest history entry or null
     */
    HistoryEntry getNewestEntry();

    /**
     * Removes all history entries from the data store.
     */
    void removeAllEntries();
}
