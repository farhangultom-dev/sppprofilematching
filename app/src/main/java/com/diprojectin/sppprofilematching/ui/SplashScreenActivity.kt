package com.diprojectin.sppprofilematching.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.utils.SharedPrefManager

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var prefManager: SharedPrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        prefManager = SharedPrefManager(this@SplashScreenActivity)

        Handler(Looper.getMainLooper()).postDelayed({
            if(!prefManager.isOnboarding()){
                startActivity(Intent(this@SplashScreenActivity, OnboardingActivity::class.java))
            }else{
                if (prefManager.getUser() == null){
                    startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                    finish()
                }else{
                    if (prefManager.getUser()?.isAdmin == "1") {
                        startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
                        finish()
                    }else{
                        startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
                        finish()
                    }
                }
            }
        },2000)
    }
}