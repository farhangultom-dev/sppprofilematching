package com.diprojectin.sppprofilematching.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityOnboardingBinding
import com.diprojectin.sppprofilematching.utils.SharedPrefManager

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var prefManager: SharedPrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        prefManager = SharedPrefManager(this)

        initView()
    }

    private fun initView() {
        with(binding){
            with(onboarding1){
                appCompatButton.setOnClickListener {
                    constraintOnboarding1.visibility = View.GONE
                    onboarding2.constraintOnboarding2.visibility = View.VISIBLE
                }

                tvLewati.setOnClickListener {
                    prefManager.setOnboarding(true)
                    startActivity(Intent(this@OnboardingActivity,LoginActivity::class.java))
                    finish()
                }
            }

            with(onboarding2){
                appCompatButton.setOnClickListener {
                    constraintOnboarding2.visibility = View.GONE
                    onboarding3.constraintOnboarding3.visibility = View.VISIBLE
                }

                tvLewati.setOnClickListener {
                    prefManager.setOnboarding(true)
                    startActivity(Intent(this@OnboardingActivity,LoginActivity::class.java))
                    finish()
                }
            }

            with(onboarding3){
                appCompatButton.setOnClickListener {
                    prefManager.setOnboarding(true)
                    startActivity(Intent(this@OnboardingActivity,LoginActivity::class.java))
                    finish()
                }

                tvLewati.setOnClickListener {
                    prefManager.setOnboarding(true)
                    startActivity(Intent(this@OnboardingActivity,LoginActivity::class.java))
                    finish()
                }

            }
        }
    }
}