package com.eathemeat.easytimer

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import androidx.compose.ui.unit.dp


class EasyApplication : Application() {

    val TAG = "EasyApplication"

    companion object {
        var sAppContext:EasyApplication? = null
            get() = sAppContext!!
    }

    override fun onCreate() {
        super.onCreate()
        sAppContext = this
        getScreenInfo()
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

    private fun getScreenInfo() {
        // （1）通过Display获取屏幕分辨率
        var screenWidth: Int = resources.displayMetrics.widthPixels // 屏幕宽
        var screenHeight: Int = resources.displayMetrics.heightPixels // 屏幕高
        Log.d(TAG,"屏幕分辨率_1：screenWidth=$screenWidth; screenHeight=$screenHeight")

        // （2）通过DisplayMetrics获取像素密度和屏幕密度等
        var dm = resources.displayMetrics


        val density = dm.density // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        val densityDPI = dm.densityDpi // 像素密度（每寸像素：120/160/240/320）
        val xdpi = dm.xdpi //X轴方向的精确物理像素
        val ydpi = dm.ydpi //Y轴方向的精确物理像素

        Log.d(TAG,"XY轴方向上的每英寸的精确物理像素：xdpi=$xdpi，ydpi=$ydpi")
        Log.d(TAG," 像素密度和屏幕密度：density=$density，densityDPI=$densityDPI")

        screenWidth = dm.widthPixels // 屏幕宽
        screenHeight = dm.heightPixels // 屏幕高

        Log.d(TAG,"屏幕分辨率_2：screenWidth=$screenWidth，screenHeight=$screenHeight")
    }

}