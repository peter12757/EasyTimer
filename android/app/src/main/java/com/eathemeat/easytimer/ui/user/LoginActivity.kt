package com.eathemeat.easytimer.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eathemeat.easytimer.ui.user.fragment.LoginFragment

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance())
                .commitNow()
        }
    }
}