package com.eathemeat.easytimer

import android.app.Application
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore.Audio.Media
import androidx.annotation.Nullable

object ClockMediaPlayer {

    var mediaPlayer:MediaPlayer? = null

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
                if (!playThread.isAlive) playThread.start()
                playHandler = Handler(playThread.looper).apply {
                    post(){
                        mediaPlayer?.run {
                            stop()
                            release()
                        }
                        mediaPlayer = MediaPlayer().apply {
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
                mediaPlayer?.run {
                    stop()
                    release()
                }
                mediaPlayer = null
                removeCallbacksAndMessages(null)
                playHandler = null
            }
        }

    }



} 