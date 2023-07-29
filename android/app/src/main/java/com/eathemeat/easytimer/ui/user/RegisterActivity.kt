package com.eathemeat.easytimer.ui.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eathemeat.easytimer.ui.user.ui.user.fragment.RegisterFragment

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RegisterFragment.newInstance())
                .commitNow()
        }
    }
}