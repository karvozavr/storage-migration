package storage;

import data.FileInfo;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import storage.util.HttpUtils;
import storage.util.StorageException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Base RESTful file storage functionality
 */
public abstract class Storage {

    /**
     * REST endpoint for file handling
     * @return endpoint url
     */
    public abstract String getEndpoint();

    /**
     * List all the file names in storage
     * @return list of filenames
     */
    public List<String> listFiles() throws StorageException {
        List<String> files = new ArrayList<>();

        try (var httpClient = HttpUtils.createRetryHttpClient()) {
            HttpResponse response = httpClient.execute(new HttpGet(getEndpoint()));
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
     * @param fileName file name
     * @return FileInfo with information about downloaded file
     */
    public FileInfo getFile(String fileName) throws StorageException {
        Path file;

        try (var httpClient = HttpUtils.createRetryHttpClient()) {
            HttpResponse response = httpClient.execute(new HttpGet(getEndpoint() + "/" + fileName));
            var stream = response.getEntity().getContent();
            file = Files.createTempFile(fileName, ".tmp");
            Files.copy(stream, file);
        } catch (IOException e) {
            throw new StorageException("List request failed", e);
        }

        return new FileInfo(fileName, file);
    }

    /**
     * Delete file from storage by name
     * @param fileName file name
     */
    public void deleteFile(String fileName) throws StorageException {
        try (var httpClient = HttpUtils.createRetryHttpClient()) {
            HttpResponse response = httpClient.execute(new HttpDelete(getEndpoint() + "/" + fileName));
        } catch (IOException e) {
            throw new StorageException("List request failed", e);
        }
    }
}
