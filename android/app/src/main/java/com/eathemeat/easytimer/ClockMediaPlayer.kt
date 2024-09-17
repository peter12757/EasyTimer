package com.eathemeat.easytimer

import android.app.Application
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Handler
import android.os.HandlerThread
import android.os.MessageQueue.IdleHandler
import java.io.File

class ClockMediaPlayer(val appCtx:Application) {

    var player:MediaPlayer = MediaPlayer()

    val fileName = "CastleintheSky.mp3"

    val playThread = HandlerThread("ClockPlayThreaad")

    var playHandler:Handler? = null



    var assertMgr = appCtx.resources.assets

    fun getAssertFile(fileName: String): AssetFileDescriptor? {
        var files = assertMgr.list("")
        files?.map {
            if (it.equals(fileName)) {
                return assertMgr.openFd(it)
            }
        }
        return null
    }

    fun play() {
        var file = getAssertFile(fileName)
        file?.run {
            playHandler?:let {
                playThread.start()
                playHandler = Handler(playThread.looper).apply {
                    post(){
                        //play
                    }
                }
            }
        }
    }

    fun stop(): Unit {
        playHandler?.run {
            removeCallbacksAndMessages(null)
            playThread.quitSafely()
        }
        playHandler = null
    }



} 