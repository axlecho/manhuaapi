package com.axlecho.api.untils

import android.net.SSLCertificateSocketFactory
import com.axlecho.api.MHConstant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*


class MHHttpsUtils private constructor() {

    val trustManager: TrustAllManager
    val client:OkHttpClient

    init {
        trustManager = TrustAllManager()
        client = standardBuilder().build()
    }

    fun standardBuilder(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS

        builder.addNetworkInterceptor(logging)
        // builder.addInterceptor(logging)

        val headerInterceptor = Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
                    .addHeader("user-agent", MHConstant.USER_AGENT)
                    // .addHeader("Referer", MHConstant.BGM_HOST)
                    .build()
            chain.proceed(newRequest)
        }
        builder.addInterceptor(headerInterceptor)

        // for https
        builder.hostnameVerifier(createHostnameVerifier())
        builder.sslSocketFactory(createSSLSocketFactory(), trustManager)
        return builder
    }

    fun createSSLSocketFactory(): SSLSocketFactory {
        var ssfFactory: SSLSocketFactory = SSLCertificateSocketFactory(100);
        try {
            val sc = SSLContext.getInstance("SSL")
            sc.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())
            ssfFactory = sc.socketFactory
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }

        return ssfFactory
    }

    fun createHostnameVerifier(): HostnameVerifier {
        return TrustAllHostnameVerifier()
    }

    inner class TrustAllManager : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls(0)
        }
    }

    private inner class TrustAllHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }

    companion object {
        val INSTANCE: MHHttpsUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MHHttpsUtils()
        }
    }
}