package com.eathemeat.easytimer.util

import android.app.ActivityManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService


class ActivityUtil {
    public fun getTopActivityByActivityManager(ctx:Context): String? {
        val activityManager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        val listTask = activityManager!!.getRunningTasks(0)
        var activityName = ""
        if (listTask != null && !listTask.isEmpty()) {
            val runningTaskInfo = listTask[1]
            activityName = runningTaskInfo.topActivity!!.className
        }
        return activityName
    }

}