package com.eathemeat.easytimer.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.eathemeat.easytimer.data.ClockInfo
import com.eathemeat.easytimer.ui.home.date.DateScreenState
import kotlinx.coroutines.flow.MutableStateFlow

class HomeViewModel(var isComposeTest:Boolean =false): ViewModel(){


    val TAG = HomeViewModel::class.java.name
    var timeNow by mutableStateOf(Pair<String,String>("1989-11-28","00:00:00"))
    var dateScreenState = DateScreenState()




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



    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: ")
    }
}