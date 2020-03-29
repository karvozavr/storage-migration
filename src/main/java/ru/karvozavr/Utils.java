package ru.karvozavr;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;

public class Utils {

    public static final int RETRY_COUNT = Integer.parseInt(Config.getProperty("NUM_RETRIES"));

    public static CloseableHttpClient createRetryHttpClient() {
        return HttpClients
                .custom()
                .setRetryHandler(new StandardHttpRequestRetryHandler(RETRY_COUNT, true))
                .build();
    }
}
