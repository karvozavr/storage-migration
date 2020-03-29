package ru.karvozavr.storage;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import ru.karvozavr.Utils;
import ru.karvozavr.data.FileData;
import ru.karvozavr.storage.util.StorageException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Base RESTful file storage functionality
 */
public abstract class Storage {

    /**
     * REST endpoint for file handling
     *
     * @return endpoint url
     */
    public abstract String getEndpoint();

    /**
     * List all the file names in storage
     *
     * @return list of filenames
     */
    public List<String> listFiles() throws StorageException {
        List<String> files = new ArrayList<>();

        try (var httpClient = Utils.createRetryHttpClient()) {
            HttpResponse response = httpClient.execute(new HttpGet(getEndpoint()));
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }
            String json = getJson(response);
            JSONArray filesJson = new JSONArray(json);
            filesJson.forEach(file -> files.add(file.toString()));
        } catch (IOException e) {
            throw new StorageException("List request failed", e);
        }

        return files;
    }

    private String getJson(HttpResponse response) throws IOException {
        return new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Download a file from storage
     *
     * @param fileName file name
     * @return FileInfo with information about downloaded file
     */
    public FileData getFile(String fileName) throws StorageException {
        byte[] bytes;

        try (var httpClient = Utils.createRetryHttpClient()) {
            HttpResponse response = httpClient.execute(new HttpGet(getEndpoint() + "/" + fileName));
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }
            var stream = response.getEntity().getContent();
            bytes = stream.readAllBytes();
        } catch (IOException e) {
            throw new StorageException("Get file request failed", e);
        }

        return new FileData(fileName, bytes);
    }

    /**
     * Delete file from storage by name
     *
     * @param fileName file name
     */
    public boolean deleteFile(String fileName) throws StorageException {
        try (var httpClient = Utils.createRetryHttpClient()) {
            HttpResponse response = httpClient.execute(new HttpDelete(getEndpoint() + "/" + fileName));
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return false;
            }
        } catch (IOException e) {
            throw new StorageException("List request failed", e);
        }

        return true;
    }
}
