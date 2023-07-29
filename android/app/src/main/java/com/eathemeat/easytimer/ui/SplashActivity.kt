package com.eathemeat.easytimer.ui

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.Window
import com.eathemeat.easytimer.databinding.ActivitySplashBinding
import com.eathemeat.easytimer.ui.home.HomeActivity
import com.eathemeat.easytimer.ui.user.LoginActivity
import com.eathemeat.easytimer.util.OtherThread
import java.lang.ref.WeakReference

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

    }

    override fun onResume() {
        super.onResume()
        binding.loadingView.startMoving()
        var ref = WeakReference(binding.loadingView)
        OtherThread.sInstance.postDelay(object : Runnable {
            override fun run() {
                ref.get()?.stopMoving()
                trans2LoginActivity()
            }

        },3000)

    }

    private fun trans2HomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    fun trans2LoginActivity(): Unit {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }


}