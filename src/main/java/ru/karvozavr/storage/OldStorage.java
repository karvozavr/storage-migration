package ru.karvozavr.storage;

import ru.karvozavr.Config;

public class OldStorage extends Storage {

    private final String ENDPOINT = Config.getProperty("BASE_URL") +
            Config.getProperty("OLD_STORAGE_ENDPOINT");

    @Override
    public String getEndpoint() {
        return ENDPOINT;
    }
}
