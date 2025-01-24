package com.example.if570_lab_uts_dafataufikallatief_67476

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.text.format.DateFormat
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.if570_lab_uts_dafataufikallatief_67476.fragments.HistoryFragment
import com.example.if570_lab_uts_dafataufikallatief_67476.fragments.HomeFragment
import com.example.if570_lab_uts_dafataufikallatief_67476.fragments.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigation: CurvedBottomNavigation
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        bottomNavigation = findViewById(R.id.bottomNavigation)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigation = findViewById<CurvedBottomNavigation>(R.id.bottomNavigation)
        bottomNavigation.add(
            CurvedBottomNavigation.Model(1,"History",R.drawable.history)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(2,"Home",R.drawable.home)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(3,"Profile",R.drawable.profile)
        )

        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                1 -> navController.navigate(R.id.historyFragment)
                2 -> navController.navigate(R.id.homeFragment)
                3 -> navController.navigate(R.id.profileFragment)
            }
        }
        if (auth.currentUser != null) {
            // User is already logged in, navigate to HomeFragment
            navController.navigate(R.id.homeFragment)
        } else {
            // User is not logged in, navigate to LoginFragment
            if (savedInstanceState == null) {
                navController.navigate(R.id.loginFragment)
            }
        }
        //default Bottom Tab Selected
        bottomNavigation.show(2)
    }
    fun showBottomNavigation() {
        bottomNavigation.visibility = View.VISIBLE
    }
    fun hideBottomNavigation() {
        bottomNavigation.visibility = View.GONE
    }

}