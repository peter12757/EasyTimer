package com.eathemeat.easytimer

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.Log

class TimeGetter(var looper: Looper?) {

    val time_interval = 1000L

    val MSG_FLAG_REFRESH = 0x01
    val TAG = TimeGetter::class.java.name
    var timeNow = System.currentTimeMillis()
    var timeHandler:Handler
    lateinit var  thread:HandlerThread
    var listener:OnTimeUpdateListener? = null

    init {
        if (looper == null) {
            thread = HandlerThread(TAG)
            thread.start()
            looper = thread.looper
        }
        timeHandler = object :Handler(looper!!) {
            override fun handleMessage(msg: Message) {

                when(msg.what) {
                    MSG_FLAG_REFRESH->{
                        callback()
                        refresh()
                    }
                    else-> {
                        Log.d(TAG,"handleMessage not handle with msg.what=${msg.what}")
                    }
                }
            }
        }
    }

    private fun callback() {
        listener?.let {
            it.onTimeUpdate(getNow())
        }
    }

    fun setTimeUpdateListener(listener:OnTimeUpdateListener) {
        this.listener = listener
    }




    private fun getNow():Long {
        timeNow = System.currentTimeMillis()
        return timeNow
    }


    private fun refresh(){
        timeHandler.sendMessageDelayed(timeHandler.obtainMessage(MSG_FLAG_REFRESH),time_interval);
    }

    fun start() {
        refresh()
    }

    fun stop() {
        timeHandler.removeCallbacksAndMessages(null)
    }

    interface OnTimeUpdateListener {
        fun onTimeUpdate(time:Long)

    }


}