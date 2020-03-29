package ru.karvozavr.app;

import ru.karvozavr.storage.NewStorage;
import ru.karvozavr.storage.OldStorage;

public class App {

    public static void main(String[] args) {
        var migrationManager = new StorageMigrationManager(new OldStorage(), new NewStorage());
        migrationManager.migrateOldToNew();
        System.out.println("Successfully migrated.");
    }
}
