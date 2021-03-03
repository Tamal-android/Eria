package com.magicmind.eria.data.network

import android.content.Context
import android.text.TextUtils
import com.magicmind.eria.BuildConfig
import com.magicmind.eria.app.AppData
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object ApiClient {
    var mRetrofit: Retrofit? = null

    fun retrofit(context: Context, apiServerUrl: String?): Retrofit? {
//        if (mRetrofit == null && context != null) {
        val httpClient = OkHttpClient.Builder()

        //Timeout
        httpClient.readTimeout(ApiConfig.READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        httpClient.connectTimeout(ApiConfig.CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        httpClient.writeTimeout(ApiConfig.WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)

        //Logging
        if (BuildConfig.LOG_DEBUG_MODE) {
            // Log
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            //Debug Log
            httpClient.addNetworkInterceptor(loggingInterceptor)
        }
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            httpClient.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            httpClient.hostnameVerifier(HostnameVerifier { hostname, session -> true })
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        //Cache
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val cache = Cache(context.cacheDir, cacheSize.toLong())
        httpClient.cache(cache)

        //Header Interceptor
        if (!TextUtils.isEmpty(AppData.ACCESS_TOKEN)) {
            val interceptor  = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClient.apply {
                addNetworkInterceptor(object : Interceptor {
                    @Throws(IOException::class)
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val original: Request = chain.request()
                        val request = original.newBuilder()
                            .header(
                                ApiConfig.AUTHORIZATION,
                                AppData.ACCESS_TOKEN.trim { it <= ' ' })
                            .method(original.method, original.body)
                            .build()
                        return chain.proceed(request)
                    }
                })
                addNetworkInterceptor(interceptor)
            }
        }


        val okHttpClient: OkHttpClient = httpClient.build()
        mRetrofit = Retrofit.Builder()
            .baseUrl(apiServerUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        //        }
        return mRetrofit
    }
}