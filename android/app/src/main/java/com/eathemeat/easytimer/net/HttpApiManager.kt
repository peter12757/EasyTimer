package com.eathemeat.easytimer.net

import android.util.Log
import com.eathemeat.easytimer.data.UserInfo
import com.eathemeat.easytimer.net.time.TimeApi
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

const val TAG = "HttpApiManager"

object HttpApiManager {



    private lateinit var timeApi: TimeApi
    private lateinit var config:HttpApiConfig


    fun init(): Unit {
        config = HttpApiConfig()
        handleSSHHandleshake()
        initOkhttp()
    }

    private fun initOkhttp() {
        /*HttpLoggingInterceptor.Level共包含四个级别：NONE、BASIC、HEADER、BODY
        NONE       不记录
        BASIC      请求/响应行--> POST /greeting HTTP/1.1 (3-byte body)<-- HTTP/1.1 200 OK (22ms, 6-byte body)
        HEADER  请求/响应行 + 头--> Host: example.comContent-Type: plain/textContent-Length: 3<-- HTTP/1.1 200 OK (22ms)Content-Type: plain/textContent-Length: 6
        BODY       请求/响应行 + 头 + 体*/
        //设置打印数据的级别，要打印所有log，可设置成BODY级别
        var interceptor = HttpLoggingInterceptor()
        interceptor.level  = HttpLoggingInterceptor.Level.NONE

        var client = OkHttpClient.Builder().readTimeout(30,TimeUnit.SECONDS).connectTimeout(30,TimeUnit.SECONDS).addInterceptor(interceptor).build()
        var gsonBuild = GsonBuilder()

        gsonBuild.registerTypeAdapter(Date::class.java,object : JsonDeserializer<Date> {
            override fun deserialize(
                json: JsonElement?,
                typeOfT: Type?,
                context: JsonDeserializationContext?
            ): Date {
                return Date(json!!.asJsonPrimitive!!.asLong)
            }

        })

        var gson = gsonBuild.create()
        var retrofit = Retrofit.Builder().baseUrl(config[HttpApiConfig.KEY_TIME_BASE_URL] as String)
            .client(client).addConverterFactory(GsonConverterFactory.create(gson))
            .callbackExecutor(Executors.newSingleThreadExecutor())  //todo
//            .addCallAdapterFactory(CoroutineCallAdap)
//            .addConverterFactory(RxJava2CallAdapterFactory)
            .build()
        timeApi = retrofit.create(TimeApi::class.java)
    }

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object :Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                   if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response is null!!!"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
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
        HttpsURLConnection.setDefaultHostnameVerifier { hostname, session ->
            Log.d(TAG, "verify: ")
            true
        }
    }catch (e:Exception) {
        Log.e(TAG, "handleSSHHandleshake: ", e)
    }






}