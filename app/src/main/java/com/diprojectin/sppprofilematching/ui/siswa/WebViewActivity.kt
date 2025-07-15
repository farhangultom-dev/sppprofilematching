package com.diprojectin.sppprofilematching.ui.siswa

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityWebViewBinding
import com.diprojectin.sppprofilematching.utils.DialogUtils

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding){
            appBar.tvTitleHeader.text = "Pembayaran"
            appBar.btnBack.setOnClickListener {
                onBackPressed()
            }
        }

        webView = findViewById(R.id.webView)
        webView.webViewClient = WebViewClient() // open in app, not browser
        webView.settings.javaScriptEnabled = true

        val url = intent.getStringExtra("urlPayment").toString()
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        val dialogConfirm = DialogUtils(this@WebViewActivity).build()
        val title = dialogConfirm.findViewById<TextView>(R.id.tv_title)
        val subtitle = dialogConfirm.findViewById<TextView>(R.id.tv_subtitle)
        val icon = dialogConfirm.findViewById<ImageView>(R.id.icon_dialog)
        val btnSimpan = dialogConfirm.findViewById<TextView>(R.id.btnSimpan)
        val btnCancel = dialogConfirm.findViewById<TextView>(R.id.btnCancel)

        title.text = "Batal"
        subtitle.text = "Apakah anda yakin untuk batalkan pembayaran?"
        btnSimpan.text = "Ya"
        btnCancel.text = "Tidak"
        icon.setImageResource(R.drawable.ic_simpan_data)
        icon.visibility = View.GONE

        btnSimpan.setOnClickListener {
            dialogConfirm.dismiss()

            val intent = Intent(this, SiswaHomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        btnCancel.setOnClickListener {
            dialogConfirm.dismiss()
        }

        dialogConfirm.show()
    }
}