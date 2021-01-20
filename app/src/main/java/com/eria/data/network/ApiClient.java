package com.eria.data.network;

import android.content.Context;
import android.text.TextUtils;

import com.eria.BuildConfig;
import com.eria.app.AppData;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * <p>
 * ApiClient used for instantiate Retrofit API service
 */

public class ApiClient {

    public static Retrofit mRetrofit;

    public static Retrofit retrofit(final Context context, String apiServerUrl) {
//        if (mRetrofit == null && context != null) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        //Timeout
        httpClient.readTimeout(ApiConfig.READ_TIMEOUT, TimeUnit.SECONDS);
        httpClient.connectTimeout(ApiConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClient.writeTimeout(ApiConfig.WRITE_TIMEOUT, TimeUnit.SECONDS);

        //Logging
        if (BuildConfig.LOG_DEBUG_MODE) {
            // Log
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
            //Debug Log
            httpClient.addNetworkInterceptor(loggingInterceptor);
        }

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            httpClient.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            httpClient.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Cache
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(context.getCacheDir(), cacheSize);
        httpClient.cache(cache);

        //Header Interceptor
        if (!TextUtils.isEmpty(AppData.ACCESS_TOKEN)) {
            httpClient.addNetworkInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()

                            .header(ApiConfig.AUTHORIZATION, AppData.ACCESS_TOKEN.trim())
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });
        }


        OkHttpClient okHttpClient = httpClient.build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(apiServerUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
//        }
        return mRetrofit;
    }
}
