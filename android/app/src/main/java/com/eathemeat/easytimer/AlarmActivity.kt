package com.eathemeat.easytimer

import android.os.Bundle
import android.view.Window
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.eathemeat.easytimer.ui.home.comm.AlarmScreen
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
                    AlarmScreen()
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
