package com.eathemeat.easytimer

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import androidx.compose.ui.unit.dp

class EasyApplication : Application() {

    val TAG = "EasyApplication"

    override fun onCreate() {
        super.onCreate()
        EasyAlarmManager.init(this)
        ClockMediaPlayer.init(this)
        Log.d(TAG, "onCreate() called${this.resources.configuration.densityDpi.dp}  , ${this.resources.displayMetrics.widthPixels} " +
                ",${this.resources.displayMetrics.heightPixels}  ,${this.resources.displayMetrics.xdpi}  ,${this.resources.displayMetrics.ydpi}")
        EasyAlarmManager.registerTimeUpListener(object : EasyAlarmManager.OnTimeUpListener {
            override fun onTimeUp() {
                var intent2Alarm = Intent(this@EasyApplication,AlarmActivity::class.java)
                intent2Alarm.addFlags(FLAG_ACTIVITY_NEW_TASK)
                this@EasyApplication.startActivity(intent2Alarm)
            }

        })
    }

}