package com.diprojectin.sppprofilematching.ui.admin.spp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.diprojectin.network.ApiClient
import com.diprojectin.network.ApiInterface
import com.diprojectin.network.responses.GenericResponse
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityDetailJurusanBinding
import com.diprojectin.sppprofilematching.databinding.ActivityDetailSppBinding
import com.diprojectin.sppprofilematching.ui.admin.jurusan.EditJurusanActivity
import com.diprojectin.sppprofilematching.ui.admin.jurusan.MasterJurusanActivity
import com.diprojectin.sppprofilematching.utils.DialogLoading
import com.diprojectin.sppprofilematching.utils.DialogUtils
import com.diprojectin.sppprofilematching.utils.Helper.formatCurrency
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailSppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailSppBinding
    private lateinit var loadingDialog: Dialog
    private lateinit var dialogConfirm: Dialog
    private var namaSpp = ""
    private var idSpp = ""
    private var nilaiSpp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dialogConfirm = DialogUtils(this).build()
        initData()
        initView()
    }

    private fun initData() {
        namaSpp = intent.getStringExtra("namaKategori").toString()
        idSpp = intent.getStringExtra("idKategori").toString()
        nilaiSpp = intent.getStringExtra("nilaiSpp").toString()

    }

    private fun initView() = with(binding) {
        tvNamaKategoriSpp.text = namaSpp
        tvNominalBayar.text = nilaiSpp.toInt().formatCurrency()

        appBar.tvTitleHeader.text = "Detail Kategori SPP"
        appBar.btnBack.setOnClickListener {
            onBackPressed()
        }

        btnDelete.setOnClickListener {
            val title = dialogConfirm.findViewById<TextView>(R.id.tv_title)
            val subtitle = dialogConfirm.findViewById<TextView>(R.id.tv_subtitle)
            val icon = dialogConfirm.findViewById<ImageView>(R.id.icon_dialog)
            val btnSimpan = dialogConfirm.findViewById<TextView>(R.id.btnSimpan)
            val btnCancel = dialogConfirm.findViewById<TextView>(R.id.btnCancel)

            title.text = "Hapus Data"
            subtitle.text = "Apakah anda yakin akan hapus data spp ini?"
            btnSimpan.text = "Ya"
            btnCancel.text = "Tidak"
            icon.setImageResource(R.drawable.ic_delete_data)

            btnSimpan.setOnClickListener {
                dialogConfirm.dismiss()
                deleteSpp()
            }

            btnCancel.setOnClickListener {
                dialogConfirm.dismiss()
            }

            dialogConfirm.show()
        }

        btnEdit.setOnClickListener {
            val intent = Intent(this@DetailSppActivity, EditSppActivity::class.java)
            intent.putExtra("namaKategori", namaSpp)
            intent.putExtra("idKategori", idSpp)
            intent.putExtra("nilaiSpp", nilaiSpp)
            startActivity(intent)
            finish()
        }

    }

    private fun deleteSpp() {
        loadingDialog = DialogLoading(this@DetailSppActivity,
            "Mohon tunggu, sedang menghapus data spp",false).build()

        loadingDialog.show()
        val apiClient = ApiClient.client(this)?.create(ApiInterface::class.java)
        val call = apiClient?.deleteSpp(idSpp)
        call?.enqueue(object: Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                loadingDialog.dismiss()
                if(!response.isSuccessful){
                    Toast.makeText(this@DetailSppActivity, "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        Toast.makeText(this@DetailSppActivity, "Berhasil menghapus spp", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@DetailSppActivity, MasterSppActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        return
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(this@DetailSppActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@DetailSppActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}