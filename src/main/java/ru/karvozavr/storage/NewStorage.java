package ru.karvozavr.storage;

import org.apache.http.HttpStatus;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.ByteArrayBody;
import ru.karvozavr.Config;
import ru.karvozavr.data.FileData;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import ru.karvozavr.Utils;
import ru.karvozavr.storage.util.StorageException;

import java.io.IOException;

public class NewStorage extends Storage {

    private final String ENDPOINT = Config.getProperty("BASE_URL") +
            Config.getProperty("NEW_STORAGE_ENDPOINT");

    @Override
    public String getEndpoint() {
        return ENDPOINT;
    }

    /**
     * Upload a file
     *
     * @param fileData file to upload
     */
    public boolean uploadFile(FileData fileData) throws StorageException {
        try (var httpClient = Utils.createRetryHttpClient()) {
            var request = new HttpPost(getEndpoint());
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addPart("file", new ByteArrayBody(fileData.getBytes(), fileData.getName()));
            request.setEntity(builder.build());
            var response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return false;
            }
        } catch (IOException e) {
            throw new StorageException("Upload failed", e);
        }

        return true;
    }
}
