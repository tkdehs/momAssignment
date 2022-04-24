package com.pnx.momassignment.network

import android.util.Log
import com.pnx.momassignment.Define
import com.pnx.momassignment.gsonConverter.CustomGsonConverterFactory
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * Retrofit 서비스 설정
 */
class NetworkService {
    private val CONNECT_TIMEOUT: Long = 10
    private val READ_TIMEOUT: Long = 10
    private val WRITE_TIMEOUT: Long = 10

    fun getApiService(url: String = Define.API_URL): IAPIService {
        return getRetrofitBuilder(url).create(IAPIService::class.java)
    }


    private fun getRetrofitBuilder(url: String): Retrofit {
        val retrofit = Retrofit.Builder()
        retrofit.baseUrl(url)

        retrofit.client(getOkHttpClient("API"))
        retrofit.addConverterFactory(CustomGsonConverterFactory.create())
//        retrofit.addConverterFactory(GsonConverterFactory.create())

        return retrofit.build()
    }

    /**
     * OKHttpClient 설정
     *  @param tag LogCat에 표시 될 TAG
     */
    private fun getOkHttpClient(tag: String): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()

        // 디버그 로그 트래킹 용
        val httpLoggingInterceptor = HttpLoggingInterceptor(CustomHttpLogging(tag))
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        okHttpBuilder.addInterceptor(httpLoggingInterceptor)

        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)

        okHttpBuilder.cookieJar(JavaNetCookieJar(cookieManager))
        setUnsafeCert(okHttpBuilder)    // 안전하지 않은 사이트로 이동 처리
        okHttpBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        okHttpBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)

        return okHttpBuilder.build()
    }

    /**
     * Json Pretty Print
     * @param tag LogCat에 표시 될 TAG
     */
    class CustomHttpLogging(val tag: String): HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            if (!message.startsWith("{")) { // Json 리시브 로그 제거(별도로 받음 CustomGsonConverterFactory)
                Log.d("$tag SEND", message)
                return
            }
        }
    }

    /**
     * 안전하지 않은 사이트로 이동 처리
     * @param builder: OkHtppBuilder
     */
    private fun setUnsafeCert(builder: OkHttpClient.Builder) {
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        builder.hostnameVerifier(HostnameVerifier { hostname, session -> true })
    }
}
