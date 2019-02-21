package com.axlecho.api.untils

import android.net.SSLCertificateSocketFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*


class MHHttpsUtils private constructor() {

    val trustManager: TrustAllManager

    init {
        trustManager = TrustAllManager()
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
        val instance: MHHttpsUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MHHttpsUtils()
        }
    }
}