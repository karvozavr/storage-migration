package storage;

import data.FileInfo;
import storage.util.StorageException;

import java.util.List;

/**
 * Common storage interface.
 */
public interface Storage {

    List<String> listFiles() throws StorageException;

    FileInfo getFile(String fileName);

    boolean deleteFile(String fileName);
}
