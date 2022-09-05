package com.perimeterx.android_sdk_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.perimeterx.mobile_sdk.PerimeterX
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity: AppCompatActivity() {

    // AppCompatActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<TextView>(R.id.login_button)
        loginButton.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                val username = findViewById<TextView>(R.id.username)
                val password = findViewById<TextView>(R.id.password)
                APIDataManager.sendLoginRequest(username.text.toString(), password.text.toString())
            }
        }

        loginButton.isEnabled = false
        PerimeterX.addInitializationFinishedCallback(null) {
            loginButton.isEnabled = true
        }
    }
}
