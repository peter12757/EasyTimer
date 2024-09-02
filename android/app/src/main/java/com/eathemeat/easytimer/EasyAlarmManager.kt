package com.eathemeat.easytimer

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import java.util.Calendar

object EasyAlarmManager {

    fun init(app: Application): EasyAlarmManager {
        mAppCtx = app
        manager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return this
    }

    val TAG = "EasyAlarmManager"

    lateinit var mAppCtx:Application

    lateinit var manager: AlarmManager

    val alarmIntentMap = mutableMapOf<Int,PendingIntent>()  //<time(UTC),intent>

    var config = mutableMapOf<String,Any>().apply {

    }


    fun addAlarm(time: Long): Unit {
        var alarmIntent = Intent(mAppCtx,EasyAlarmReceive::class.java).let {
            PendingIntent.getBroadcast(mAppCtx,0,it, PendingIntent.FLAG_IMMUTABLE)
        }
//        alarmIntentMap.put()
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime()+time,alarmIntent)
    }

    fun addAlarm(calendar: Calendar,repeatTime:Long = AlarmManager.INTERVAL_FIFTEEN_MINUTES): Unit {
        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        var alarmIntent = Intent(mAppCtx, EasyAlarmReceive::class.java).let {
            PendingIntent.getBroadcast(mAppCtx,0,it, PendingIntent.FLAG_IMMUTABLE)
        }
        manager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            repeatTime,
            alarmIntent)
    }

    fun canelAlarm(time:Long): Unit {
        var alarmIntent = Intent(mAppCtx,EasyAlarmReceive::class.java).let {
            PendingIntent.getBroadcast(mAppCtx,0,it, PendingIntent.FLAG_IMMUTABLE)
        }
        manager.cancel(alarmIntent)
    }



    class EasyAlarmReceive : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "onReceive() called with: context = $context, intent = $intent")
        }

    }


}