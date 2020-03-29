package ru.karvozavr.storage;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import ru.karvozavr.data.FileData;
import ru.karvozavr.storage.util.StorageException;

import java.io.IOException;
import java.nio.file.Files;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;


public class NewStorageTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    public void testFileUpload() throws IOException, StorageException {
        var storage = new NewStorage();
        var file = Files.createTempFile("testfile", "");
        String testContent = "test content";
        Files.writeString(file, testContent);
        byte[] bytes = Files.readAllBytes(file);

        stubFor(post(urlEqualTo("/newStorage/files"))
                .withMultipartRequestBody(aMultipart("file")
                        .withBody(binaryEqualTo(bytes)))
                .willReturn(aResponse().withStatus(200)));

        assertTrue(storage.uploadFile(new FileData("file.test", bytes)));
        verify(postRequestedFor(urlEqualTo("/newStorage/files")));
    }
}