package storage;

public class OldStorage extends Storage {

    private final String ENDPOINT = System.getProperty("BASE_URL") + System.getProperty("OLD_STORAGE_ENDPOINT");

    @Override
    public String getEndpoint() {
        return ENDPOINT;
    }
}
