package com.eathemeat.easytimer.net
class HttpApiConfig : HashMap<String,Any>() {

    companion object {

        val KEY_BASE_URL:String = "KEY_BASE_URL"
    }
    init {
        this.put(KEY_BASE_URL,"http://192.168.0.19:8000")

    }

}