package com.diprojectin.sppprofilematching.ui.admin.jurusan

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
import com.diprojectin.sppprofilematching.databinding.ActivityAddJurusanBinding
import com.diprojectin.sppprofilematching.databinding.ActivityEditJurusanBinding
import com.diprojectin.sppprofilematching.ui.admin.kelas.MasterKelasActivity
import com.diprojectin.sppprofilematching.utils.DialogLoading
import com.diprojectin.sppprofilematching.utils.DialogUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditJurusanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditJurusanBinding
    private lateinit var dialogConfirm: Dialog
    private lateinit var loadingDialog: Dialog
    private var namaJurusanExtra = ""
    private var idJurusan = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditJurusanBinding.inflate(layoutInflater)
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
        namaJurusanExtra = intent.getStringExtra("namaJurusan").toString()
        idJurusan = intent.getStringExtra("id").toString()
    }

    private fun initView() {
        with(binding){
            appBar.tvTitleHeader.text = "Tambah Jurusan"
            appBar.btnBack.setOnClickListener {
                onBackPressed()
            }

            etNamaJurusan.setText(namaJurusanExtra)

            btnCancel.setOnClickListener {
                val title = dialogConfirm.findViewById<TextView>(R.id.tv_title)
                val subtitle = dialogConfirm.findViewById<TextView>(R.id.tv_subtitle)
                val icon = dialogConfirm.findViewById<ImageView>(R.id.icon_dialog)
                val btnSimpan = dialogConfirm.findViewById<TextView>(R.id.btnSimpan)
                val btnCancel = dialogConfirm.findViewById<TextView>(R.id.btnCancel)

                title.text = "Batal"
                subtitle.text = "Apakah anda yakin untuk batal menambah data?"
                btnSimpan.text = "Ya"
                btnCancel.text = "Tidak"
                icon.setImageResource(R.drawable.ic_simpan_data)

                btnSimpan.setOnClickListener {
                    dialogConfirm.dismiss()
                    finish()
                }

                btnCancel.setOnClickListener {
                    dialogConfirm.dismiss()
                }

                dialogConfirm.show()

            }

            btnSimpan.setOnClickListener {
                val title = dialogConfirm.findViewById<TextView>(R.id.tv_title)
                val subtitle = dialogConfirm.findViewById<TextView>(R.id.tv_subtitle)
                val icon = dialogConfirm.findViewById<ImageView>(R.id.icon_dialog)
                val btnSimpan = dialogConfirm.findViewById<TextView>(R.id.btnSimpan)
                val btnCancel = dialogConfirm.findViewById<TextView>(R.id.btnCancel)

                title.text = "Simpan Data"
                subtitle.text = "Apakah anda yakin akan simpan data jurusan?"
                btnSimpan.text = "Simpan"
                btnCancel.text = "Batal"
                icon.setImageResource(R.drawable.ic_simpan_data)

                btnSimpan.setOnClickListener {
                    dialogConfirm.dismiss()
                    sendData()
                }

                btnCancel.setOnClickListener {
                    dialogConfirm.dismiss()
                }

                dialogConfirm.show()
            }
        }
    }

    private fun sendData() {
        if (!isValidated()){
            return
        }

        val namaJurusan = binding.etNamaJurusan.text.toString()

        loadingDialog = DialogLoading(this@EditJurusanActivity,
            "Mohon tunggu, sedang menambahkan data jurusan",false).build()

        loadingDialog.show()
        val apiClient = ApiClient.client(this)?.create(ApiInterface::class.java)
        val call = apiClient?.editJurusan(idJurusan,namaJurusan)
        call?.enqueue(object: Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                loadingDialog.dismiss()
                if(!response.isSuccessful){
                    Toast.makeText(this@EditJurusanActivity, "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        Toast.makeText(this@EditJurusanActivity, "Berhasil menambahkan jurusan", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@EditJurusanActivity, MasterJurusanActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()

                        return
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(this@EditJurusanActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@EditJurusanActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun isValidated(): Boolean {
        with(binding){
            if (etNamaJurusan.text.toString().isEmpty()){
                etNamaJurusan.error = "Nama jurusan tidak boleh kosong"
                return false
            }

            return true
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@EditJurusanActivity, DetailJurusanActivity::class.java)
        intent.putExtra("id", idJurusan)
        intent.putExtra("namaJurusan", namaJurusanExtra)
        startActivity(intent)
    }
}