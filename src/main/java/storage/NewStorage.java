package storage;

import data.FileInfo;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import storage.util.HttpUtils;
import storage.util.StorageException;

import java.io.IOException;

public class NewStorage extends Storage {

    private final String ENDPOINT = System.getProperty("BASE_URL") + System.getProperty("NEW_STORAGE_ENDPOINT");

    @Override
    public String getEndpoint() {
        return ENDPOINT;
    }

    /**
     * Upload a file
     *
     * @param fileInfo file to upload
     */
    public void uploadFile(FileInfo fileInfo) throws StorageException {
        try (var httpClient = HttpUtils.createRetryHttpClient()) {
            var request = new HttpPost(getEndpoint());
            request.setHeader("Content-Type", "multipart/form-data");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("upfile", fileInfo.getPath().toFile(), ContentType.DEFAULT_BINARY, fileInfo.getName());
            request.setEntity(builder.build());
            httpClient.execute(request);
        } catch (IOException e) {
            throw new StorageException("List request failed", e);
        }
    }
}
