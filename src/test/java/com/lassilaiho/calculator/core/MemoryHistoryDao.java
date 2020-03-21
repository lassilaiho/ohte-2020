package com.lassilaiho.calculator.core;

import java.util.ArrayList;
import java.util.List;
import com.lassilaiho.calculator.persistence.HistoryDao;

/**
 * Stores calculation history only in memory.
 */
public class MemoryHistoryDao implements HistoryDao {
    private ArrayList<HistoryEntry> entries = new ArrayList<>();

    @Override
    public void addEntry(HistoryEntry entry) {
        entries.add(entry);

    }

    @Override
    public List<HistoryEntry> getAllEntries() {
        return entries;
    }

    @Override
    public HistoryEntry getNewestEntry() {
        if (entries.isEmpty()) {
            return null;
        }
        return entries.get(entries.size() - 1);
    }

    @Override
    public void removeAllEntries() {
        entries.clear();
        entries.trimToSize();
    }
}
