package com.diprojectin.sppprofilematching.ui.admin.notification

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityDetailNotifiactionBinding
import com.diprojectin.sppprofilematching.databinding.ActivityNotificationBinding

class DetailNotifiactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailNotifiactionBinding
    private var judul = ""
    private var deskripsi = ""
    private var thumbnailImage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNotifiactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initData()
        initView()
    }

    private fun initData() {
        judul = intent.getStringExtra("judul").toString()
        deskripsi = intent.getStringExtra("deskripsi").toString()
        thumbnailImage = intent.getStringExtra("image").toString()
    }

    private fun initView() = with(binding) {
        appBar.tvTitleHeader.text = "Detail Notifikasi"
        appBar.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}