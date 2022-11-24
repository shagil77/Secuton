package com.shagil.secuton.presentation.home

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shagil.secuton.R
import com.shagil.secuton.presentation.Login
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Secuton)
        setContentView(R.layout.activity_home)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.home_fragment_conatainer) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavView = findViewById<BottomNavigationView>(R.id.home_bottom_nav_bar)

        setupWithNavController(bottomNavView, navController)

        createNotificationChannel()

        val floatingActionBtn = findViewById<FloatingActionButton>(R.id.logout_user)
        floatingActionBtn.setOnClickListener {
            signOut()
        }
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name:CharSequence = "secutonTODOreminderChannel"
            val description = "Channel for Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("secutonAlarmNotification", name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)

            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun signOut() {
        Firebase.auth.signOut()
        Toast.makeText(this, "Thanks for passing by! : )", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this@HomeActivity, Login::class.java))
        finish()
    }
}