package com.example.storyapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.example.storyapp.ui.authentication.AuthenticationActivity
import com.example.storyapp.ui.dashboard.DashboardActivity
import com.example.storyapp.util.startNewActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userPreferences = UserPreferences(this)

        userPreferences.loginToken.asLiveData().observe(this){
            val activity = if (it == null) AuthenticationActivity::class.java else DashboardActivity::class.java
            startNewActivity(activity)
            userPreferences.setToken(it.toString())
        }

    }
}