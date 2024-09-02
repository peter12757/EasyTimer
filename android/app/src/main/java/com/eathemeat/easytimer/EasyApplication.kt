package com.eathemeat.easytimer

import android.app.Application
import android.util.Log
import androidx.compose.ui.unit.dp

class EasyApplication : Application() {

    val TAG = "EasyApplication"

    override fun onCreate() {
        super.onCreate()
        EasyAlarmManager.init(this)
        Log.d(TAG, "onCreate() called${this.resources.configuration.densityDpi.dp}  , ${this.resources.displayMetrics.widthPixels} " +
                ",${this.resources.displayMetrics.heightPixels}  ,${this.resources.displayMetrics.xdpi}  ,${this.resources.displayMetrics.ydpi}")
    }

}