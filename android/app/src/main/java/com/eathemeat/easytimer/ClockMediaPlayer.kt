package com.eathemeat.easytimer

import android.app.Application
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.HandlerThread

object ClockMediaPlayer {

    var mediaPlayer:MediaPlayer = MediaPlayer()

    val fileName = "CastleintheSky.mp3"

    val playThread = HandlerThread("ClockPlayThreaad")

    var playHandler:Handler? = null

    lateinit var appCtx:Application
    lateinit var assertMgr:AssetManager

    fun init(ctx: Application): Unit {
        appCtx = ctx
        assertMgr = appCtx.resources.assets
    }


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
                        with(mediaPlayer){
                            setDataSource(file)
                            prepare()
                            start()
                            isLooping =true
                        }
                    }
                }
            }
        }
    }

    fun stop(): Unit {
        playHandler?.run {
            post(){
                mediaPlayer.stop()
                removeCallbacksAndMessages(null)
                playThread.quitSafely()
                playHandler = null
            }
        }

    }



} 