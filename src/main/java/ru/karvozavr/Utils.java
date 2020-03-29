package ru.karvozavr;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public class Utils {

    public static final int STATUS_SERVER_ERROR = 500;
    public static final int RETRY_COUNT = Integer.parseInt(Config.getProperty("NUM_RETRIES"));

    public static CloseableHttpClient createRetryHttpClient() {
        return HttpClients
                .custom()
                .setRetryHandler(new StandardHttpRequestRetryHandler(RETRY_COUNT, true))
                .build();
    }
}
