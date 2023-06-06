package com.eathemeat.easytimer.net

import com.eathemeat.easytimer.net.HttpApiConfig.Companion.KEY_TIME_BASE_URL
import com.eathemeat.easytimer.net.time.TimeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HttpApiManager {

    private constructor() {

    }

    companion object {
        var sIntance = HttpApiManager()


    }

    private lateinit var timeApi: TimeApi
    private lateinit var config:HttpApiConfig


    fun init(): Unit {
        config = HttpApiConfig()
    }

    fun getTimeApi(): TimeApi {
        if (timeApi == null) {
            var retrofit:Retrofit = Retrofit.Builder().baseUrl(config[KEY_TIME_BASE_URL] as String)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(Corou).build()
            timeApi = retrofit.create(TimeApi::class.java)
        }
        return timeApi
    }
}