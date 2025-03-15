package com.eathemeat.easytimer.ui.home

import android.os.SystemClock
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.eathemeat.easytimer.TimeGetter
import com.eathemeat.easytimer.data.ClockInfo
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.Date

class MainViewModel(var isComposeTest:Boolean =false): ViewModel(),
    TimeGetter.OnTimeUpdateListener {


    val TAG = MainViewModel::class.java.name
    lateinit var  time: TimeGetter
    var timeNow by mutableStateOf(Pair<String,String>("1989-11-28","00:00:00"))

    init {
        time = TimeGetter(null)
        time.listener = this
        time.start()

    }

    var alarmList = MutableStateFlow(mutableListOf<ClockInfo>().apply {
        if (isComposeTest) {
            add(ClockInfo(1000*60))
            add(ClockInfo(1000*60*3))
            add(ClockInfo(1000*60*4))
            add( ClockInfo(1000*60*5))
            add(ClockInfo(1000*60*10))
            add(ClockInfo(1000*60*30))
            add(ClockInfo(1000*60*60))
        }
    })

    var screenType = mutableStateOf(AddScreenType.ADD)

    var _timeList = mutableListOf<ClockInfo>().apply {
        if (isComposeTest) {
            add(ClockInfo(SystemClock.elapsedRealtime()+1000*60))
            add(ClockInfo(SystemClock.elapsedRealtime()+1000*60*2))
            add(ClockInfo(SystemClock.elapsedRealtime()+1000*60*3))
            add(ClockInfo(SystemClock.elapsedRealtime()+1000*60*4))
            add(ClockInfo(SystemClock.elapsedRealtime()+1000*60*5))
        }
    }
    var timerList = mutableStateOf(_timeList)



    fun nowStr() :Pair<String,String>{
        var date_format = SimpleDateFormat("yyyy-MM-dd")
        var time_format = SimpleDateFormat("HH:mm:ss")
        return Pair<String,String>(date_format.format(Date()),time_format.format(Date()))
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: ")
    }

    override fun onTimeUpdate(time: Long) {
        timeNow = nowStr()
    }


}

enum class AddScreenType {
    ADD,TIMEADD,TIMEDETAIL,DATEADD,DATEDETAIL,
}