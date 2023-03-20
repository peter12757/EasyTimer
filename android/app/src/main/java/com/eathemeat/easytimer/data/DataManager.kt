package com.eathemeat.easytimer.data

import android.app.Application


data class TimeSegment(val startTime:Long,val EndTime:Long,val note:String)
data class Time(val name:String,val desc:String,val timeList:MutableList<TimeSegment>)
class DataManager {

    lateinit var mApp:Application



    companion object {
        val sIntance:DataManager = DataManager()

        fun init(app:Application) {
            sIntance.init(app)
        }
    }

    private constructor() {

    }

    private fun init(app:Application) {
        mApp = app
        // TODO: 从内存中加载时间的数据 
    }

}
