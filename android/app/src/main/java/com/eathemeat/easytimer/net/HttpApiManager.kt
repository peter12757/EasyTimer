package com.eathemeat.easytimer.net

import android.util.Log
import com.eathemeat.easytimer.net.time.TimeApi
import java.net.HttpURLConnection
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

const val TAG = "HttpApiManager"

object HttpApiManager {



    private lateinit var timeApi: TimeApi
    private lateinit var config:HttpApiConfig


    fun init(): Unit {
        config = HttpApiConfig()
        handleSSHHandleshake()
    }

    private fun handleSSHHandleshake() = try {
        var trustManager = object:X509TrustManager{
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                Log.d(TAG, "checkClientTrusted: $authType")
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                Log.d(TAG, "checkServerTrusted: $authType")
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                Log.d(TAG, "getAcceptedIssuers: ")
                return emptyArray()
            }

        }
        var trustAllCerts = arrayOf(trustManager)
        var sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null,trustAllCerts, SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier(object :HostnameVerifier(){
            override fun verify(hostname: String?, session: SSLSession?): Boolean {
                Log.d(TAG, "verify: ")
                return true
            }

        })
    }catch (e:Exception) {
        Log.e(TAG, "handleSSHHandleshake: ", e)
    }






}