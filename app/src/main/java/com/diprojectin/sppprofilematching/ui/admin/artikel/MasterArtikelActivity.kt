package com.diprojectin.sppprofilematching.ui.admin.artikel

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityMasterArtikelBinding

class MasterArtikelActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMasterArtikelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasterArtikelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
        initData()
    }

    private fun initData() {

    }

    private fun initView() {

    }
}