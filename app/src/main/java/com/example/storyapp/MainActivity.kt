package com.example.storyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.storyapp.ui.dashboard.DashboardActivity
import com.example.storyapp.ui.dashboard.startNewActivity
import com.example.storyapp.ui.authentication.AuthenticationActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userPreferences = UserPreferences(this)

        userPreferences.loginToken.asLiveData().observe(this, Observer {
            val activity = if (it == null) AuthenticationActivity::class.java else DashboardActivity::class.java
            startNewActivity(activity)
        })

    }
}