package com.eathemeat.easytimer.net
class HttpApiConfig : HashMap<String,Any>() {

    companion object {

        val KEY_TIME_BASE_URL:String = "KEY_TIME_BASE_URL"
    }
    init {
        this.put(KEY_TIME_BASE_URL,"http://192.168.1.1:8000")

    }

}