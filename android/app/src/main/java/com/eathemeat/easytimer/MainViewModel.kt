package com.eathemeat.easytimer

import android.os.SystemClock
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import com.eathemeat.easytimer.data.ClockInfo
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel(var isComposeTest:Boolean =false): ViewModel() {

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




}

enum class AddScreenType {
    ADD,TIMEADD,TIMEDETAIL,DATEADD,DATEDETAIL,
}