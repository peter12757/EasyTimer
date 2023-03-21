package com.eathemeat.easytimer.data

import android.app.Application
import android.util.Log


data class TimeSegment(val startTime:Long,val EndTime:Long,val note:String)
data class Task(val name:String, val desc:String, val timeList:MutableList<TimeSegment>)

val TAG = DataManager::class.java.simpleName
class DataManager {

    lateinit var mApp:Application
    var mTaskList = mutableListOf<Task>()


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

    fun del(task: Task) {
        Log.d(TAG, "del() called with: task = $task")
        mTaskList.remove(task)
    }

}
