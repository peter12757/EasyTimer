package com.eathemeat.easytimer

import android.app.Application
import android.util.Log
import com.eathemeat.easytimer.data.DataManager
import com.eathemeat.easytimer.ui.widget.toast.ToastManager
import com.eathemeat.easytimer.util.OtherThread
import java.util.logging.LogManager

val TAG = EasyTimeApplication::class.java.simpleName

class EasyTimeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DataManager.init(this)
        ToastManager.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d(TAG, "onTerminate() called")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Log.d(TAG, "onTrimMemory() called with: level = $level")
    }
}