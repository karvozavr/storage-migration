package ru.karvozavr.app;

import ru.karvozavr.Utils;
import ru.karvozavr.data.FileData;
import ru.karvozavr.storage.NewStorage;
import ru.karvozavr.storage.OldStorage;
import ru.karvozavr.storage.util.StorageException;

import java.util.List;
import java.util.function.Supplier;

public class StorageMigrationManager {

    private OldStorage oldStorage;
    private NewStorage newStorage;

    // this is just a progress for a better user experience, may be a bit incorrect because of concurrency
    private int processed = 0;

    public StorageMigrationManager(OldStorage oldStorage, NewStorage newStorage) {
        this.oldStorage = oldStorage;
        this.newStorage = newStorage;
    }

    public void migrateOldToNew() {
        var fileNames = getFileNames();
        fileNames.parallelStream().forEach(fileName -> {
            var fileInfo = getFileInfo(fileName);
            uploadFileToNewStorage(fileInfo);
            deleteFileFromOldStorage(fileName);
            System.out.println(processed + " of " + fileNames.size() + " migrated");
            ++processed; // may lead to incorrect values, but it's OK
        });
    }

    private void deleteFileFromOldStorage(String fileName) {
        tryRepeatGet(() -> {
            try {
                return oldStorage.deleteFile(fileName) ? true : null;
            } catch (StorageException e) {
                return null;
            }
        });
    }

    private void uploadFileToNewStorage(FileData fileData) {
        tryRepeatGet(() -> {
            try {
                return newStorage.uploadFile(fileData) ? true : null;
            } catch (StorageException e) {
                return null;
            }
        });
    }

    private List<String> getFileNames() {
        return tryRepeatGet(() -> {
            try {
                return oldStorage.listFiles();
            } catch (StorageException e) {
                return null;
            }
        });
    }

    private FileData getFileInfo(String fileName) {
        return tryRepeatGet(() -> {
            try {
                return oldStorage.getFile(fileName);
            } catch (StorageException e) {
                return null;
            }
        });
    }

    private <T> T tryRepeatGet(Supplier<T> function) throws RuntimeException {
        int timeout = 100;
        for (int tries = Utils.RETRY_COUNT; tries != 0; tries--) {
            T data = function.get();
            if (data != null) {
                return data;
            }
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeout *= 2;
        }

        throw new RuntimeException("Server unavailable");
    }
}
