package com.eathemeat.easytimer

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.eathemeat.easytimer.event.EventType
import com.eathemeat.easytimer.event.OnEvent
import com.eathemeat.easytimer.screen.alarm.AlarmScreen
import com.eathemeat.transkit.main.ui.theme.EasyTimerTheme

class AlarmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContent {
            EasyTimerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AlarmScreen(object :OnEvent{
                        override fun onEvent(type: EventType, data: Bundle): Boolean {
                            this@AlarmActivity.finish()
                            return true
                        }
                    })
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        ClockMediaPlayer.play()
    }

    override fun onStop() {
        super.onStop()
        ClockMediaPlayer.stop()
    }
}
