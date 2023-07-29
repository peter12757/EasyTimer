package com.eathemeat.easytimer.util

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message

class OtherThread : HandlerThread {

    private lateinit var handler: OtherHandler

    private constructor() : super(OtherThread::class.java.simpleName) {
        start()
    }


    override fun start() {
        super.start()
        handler = OtherHandler(this.looper)
    }

    fun post(runnable: Runnable): Unit {
        if (Thread.currentThread() == this) {
            runnable.run()
        }else {
            handler.post(runnable)
        }
    }

    fun postDelay(runnable: Runnable,delayMillis:Long): Unit {
        handler.postDelayed(runnable,delayMillis)
    }


    fun registerHandler(runnable: OtherMsgHandler): Boolean {
        return null == handler.msgMap.put(runnable.getCode(),runnable)
    }



    interface OtherMsgHandler {
        fun getCode(): Int

        fun handleMsg(msg: Message): Boolean
    }

    class OtherHandler(looper: Looper): Handler(looper) {

         val msgMap = mutableMapOf<Int,OtherMsgHandler>()


        override fun handleMessage(msg: Message) {
            if (msgMap.containsKey(msg.what)) {
                msgMap[msg.what]?.handleMsg(msg)
            }
            super.handleMessage(msg)
        }


    }

    companion object {
        val sInstance:OtherThread = OtherThread()

    }

}