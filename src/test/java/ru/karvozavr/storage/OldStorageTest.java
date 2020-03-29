package ru.karvozavr.storage;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import ru.karvozavr.storage.util.StorageException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

public class OldStorageTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    public void testListFiles() throws IOException, StorageException {
        String testFileName = "testfile";
        stubFor(get(urlEqualTo("/oldStorage/files"))
                .willReturn(okJson("[ \"" + testFileName + "\" ]")));

        var storage = new OldStorage();

        var list = storage.listFiles();
        assertArrayEquals("Correct list of files returned", List.of(testFileName).toArray(), list.toArray());
    }

    @Test
    public void testGetFile() throws IOException, StorageException {
        String testFileName = "testfile";
        var file = Files.createTempFile(Paths.get(System.getProperty("user.home")), ".tst", ".tmp");
        String testContent = "test content";
        Files.writeString(file, testContent);

        stubFor(get(urlEqualTo("/oldStorage/files/" + testFileName))
                .willReturn(ok().withBody(Files.readAllBytes(file))));

        var storage = new OldStorage();

        var fileInfo = storage.getFile(testFileName);
        assertEquals("File has correct name", testFileName, fileInfo.getName());
        assertEquals("File has correct contents", testContent, IOUtils.toString(fileInfo.getBytes(), StandardCharsets.UTF_8.toString()));
    }

    @Test
    public void testDeleteFile() throws StorageException {
        String testFileName = "testfile";

        stubFor(delete(urlEqualTo("/oldStorage/files/" + testFileName))
                .willReturn(ok()));

        var storage = new OldStorage();

        assertTrue(storage.deleteFile(testFileName));
    }
}