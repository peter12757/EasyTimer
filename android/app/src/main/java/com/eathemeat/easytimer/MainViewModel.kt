package com.eathemeat.easytimer

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


}