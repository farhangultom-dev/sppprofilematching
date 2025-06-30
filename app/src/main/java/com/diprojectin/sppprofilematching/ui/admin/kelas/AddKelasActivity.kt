package com.diprojectin.sppprofilematching.ui.admin.kelas

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
import com.diprojectin.models.Kelas
import com.diprojectin.network.ApiClient
import com.diprojectin.network.ApiInterface
import com.diprojectin.network.responses.GenericResponse
import com.diprojectin.network.responses.LoginResponse
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityAddKelasBinding
import com.diprojectin.sppprofilematching.databinding.ActivityMasterKelasBinding
import com.diprojectin.sppprofilematching.ui.HomeActivity
import com.diprojectin.sppprofilematching.ui.adapters.KelasAdapter
import com.diprojectin.sppprofilematching.utils.DialogLoading
import com.diprojectin.sppprofilematching.utils.DialogUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddKelasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddKelasBinding
    private lateinit var dialogConfirm: Dialog
    private lateinit var loadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddKelasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dialogConfirm = DialogUtils(this).build()
        initView()
    }

    private fun initView() {
        with(binding){
            appBar.tvTitleHeader.text = getString(R.string.tambah_nama_kelas)
            appBar.btnBack.setOnClickListener {
                onBackPressed()
            }

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
                subtitle.text = "Apakah anda yakin akan simpan data nama kelas?"
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

        val namaKelas = binding.etNamaKelas.text.toString()
        val kelas = binding.etKelas.text.toString()
        loadingDialog = DialogLoading(this@AddKelasActivity,
            "Mohon tunggu, sedang menambahkan data kelas",false).build()

        loadingDialog.show()
        val apiClient = ApiClient.client(this)?.create(ApiInterface::class.java)
        val call = apiClient?.addKelas(kelas,namaKelas)
        call?.enqueue(object: Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                loadingDialog.dismiss()
                if(!response.isSuccessful){
                    Toast.makeText(this@AddKelasActivity, "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        Toast.makeText(this@AddKelasActivity, "Berhasil menambahkan kelas", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@AddKelasActivity, MasterKelasActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()

                        return
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(this@AddKelasActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@AddKelasActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun isValidated(): Boolean {
        with(binding){
            if (etNamaKelas.text.toString().isEmpty()){
                etNamaKelas.error = "Nama kelas tidak boleh kosong"
                return false
            }

            if (etKelas.text.toString().isEmpty()){
                etKelas.error = "kelas tidak boleh kosong"
                return false
            }

            return true
        }
    }

}