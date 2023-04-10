package com.eathemeat.easytimer.util

import java.text.SimpleDateFormat
import java.util.*

class TimeUtil {

    companion object {
        fun Time2String(time: Long): String {
            var format = SimpleDateFormat("yyyy/MM/dd-HH:mm:ss", Locale.getDefault())
            return format.format(time)
        }
    }
}