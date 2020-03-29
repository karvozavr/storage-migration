package storage.util;

import org.apache.http.HttpResponseInterceptor;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class HttpUtils {

    public static final int STATUS_SERVER_ERROR = 500;
    public static final int RETRY_COUNT = Integer.parseInt(System.getProperty("NUM_RETRIES"));

    public static CloseableHttpClient createRetryHttpClient() {
        return HttpClients
                .custom()
                .addInterceptorLast((HttpResponseInterceptor) (r, context) -> {
                    if (r.getStatusLine().getStatusCode() == STATUS_SERVER_ERROR) {
                        throw new IOException("Retry");
                    }
                })
                .setRetryHandler(new DefaultHttpRequestRetryHandler(RETRY_COUNT, false))
                .build();
    }
}
