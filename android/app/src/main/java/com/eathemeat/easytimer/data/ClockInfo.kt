package com.eathemeat.easytimer.data

import android.os.SystemClock

class ClockInfo {

    var time:Long = 0

    fun getResetTime(): Long {
        var now = SystemClock.elapsedRealtime()
        return now-time
    }
    fun getResetTimeStr(): String {
        var now = SystemClock.elapsedRealtime()
        var resetTime = (now-time)/1000
        var seds = resetTime%60
        var min = resetTime/60%60
        var hour = resetTime/60/60
        return "$hour:$min:$seds"
    }
}