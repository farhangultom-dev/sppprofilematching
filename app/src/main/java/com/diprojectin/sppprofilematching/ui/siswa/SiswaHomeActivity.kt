package com.diprojectin.sppprofilematching.ui.siswa

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityHomeBinding
import com.diprojectin.sppprofilematching.databinding.ActivitySiswaHomeBinding
import com.diprojectin.sppprofilematching.ui.admin.fragments.HomeFragment
import com.diprojectin.sppprofilematching.ui.admin.fragments.ProfileFragment
import com.diprojectin.sppprofilematching.ui.admin.fragments.SppFragment
import com.diprojectin.sppprofilematching.ui.siswa.fragments.SiswaHomeFragment
import com.diprojectin.sppprofilematching.ui.siswa.fragments.SiswaProfileFragment
import com.diprojectin.sppprofilematching.ui.siswa.fragments.SiswaRiwayatFragment
import com.diprojectin.sppprofilematching.ui.siswa.fragments.SiswaSppFragment

class SiswaHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySiswaHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySiswaHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load the first fragment at startup
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmen_container_siswa, SiswaHomeFragment())
            .commit()

        initView()
    }

    private fun initView() {
        with(binding){
            lnDashboard.setOnClickListener {
                refreshAndActivatingButton("home",ivDashboard,tvDashboard)

                val fragment = SiswaHomeFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmen_container_siswa, fragment)
                    .addToBackStack(null)
                    .commit()

            }

            lnSpp.setOnClickListener {
                refreshAndActivatingButton("spp",ivSpp,tvSpp)

                val fragment = SiswaSppFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmen_container_siswa, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            lnProfile.setOnClickListener {
                refreshAndActivatingButton("profile",ivProfile,tvProfile)

                val fragment = SiswaProfileFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmen_container_siswa, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            lnRiwayat.setOnClickListener {
                refreshAndActivatingButton("riwayat",ivRiwayat,tvRiwayat)

                val fragment = SiswaRiwayatFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmen_container_siswa, fragment)
                    .addToBackStack(null)
                    .commit()
            }

        }
    }

    private fun refreshAndActivatingButton(activating: String, icon: ImageView, text: TextView) {
        with(binding) {
            ivDashboard.setImageResource(R.drawable.ic_home_false)
            tvDashboard.setTextColor(Color.parseColor("#484C52"))

            ivSpp.setImageResource(R.drawable.ic_spp_siswa_false)
            tvSpp.setTextColor(Color.parseColor("#484C52"))

            ivProfile.setImageResource(R.drawable.ic_profile_false)
            tvProfile.setTextColor(Color.parseColor("#484C52"))

            ivRiwayat.setImageResource(R.drawable.li_clock)
            tvRiwayat.setTextColor(Color.parseColor("#484C52"))

            if (activating == "home") {
                icon.setImageResource(R.drawable.ic_home_true)
                text.setTextColor(Color.parseColor("#539DF3"))
            }

            if (activating == "spp") {
                icon.setImageResource(R.drawable.ic_spp_siswa_true)
                text.setTextColor(Color.parseColor("#539DF3"))
            }

            if (activating == "profile") {
                icon.setImageResource(R.drawable.ic_profile_true)
                text.setTextColor(Color.parseColor("#539DF3"))
            }

            if (activating == "riwayat") {
                icon.setImageResource(R.drawable.li_clock_true)
                text.setTextColor(Color.parseColor("#539DF3"))
            }
        }

    }
}