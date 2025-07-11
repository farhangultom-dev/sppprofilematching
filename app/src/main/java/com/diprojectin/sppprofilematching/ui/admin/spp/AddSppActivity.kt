package com.diprojectin.sppprofilematching.ui.admin.spp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.diprojectin.network.ApiClient
import com.diprojectin.network.ApiInterface
import com.diprojectin.network.responses.GenericResponse
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityAddSppBinding
import com.diprojectin.sppprofilematching.ui.admin.kelas.MasterKelasActivity
import com.diprojectin.sppprofilematching.utils.DialogLoading
import com.diprojectin.sppprofilematching.utils.DialogUtils
import com.diprojectin.sppprofilematching.utils.Helper
import com.diprojectin.sppprofilematching.utils.Helper.formatCurrency
import com.diprojectin.sppprofilematching.utils.Helper.formatCurrencyWithoutRp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddSppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSppBinding
    private lateinit var dialogConfirm: Dialog
    private lateinit var loadingDialog: Dialog
    private var nilaiBayar = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSppBinding.inflate(layoutInflater)
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
            appBar.tvTitleHeader.text = "Tambah Kategori SPP"
            appBar.btnBack.setOnClickListener {
                onBackPressed()
            }

//            etNominalBayar.setText(nilaiKonsumsi.formatCurrency())
//            etNominalBayar.setSelection(etNilaiKonsumsi.text.length)
            etNominalBayar.addTextChangedListener(object : TextWatcher {
                private var current = ""

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s.toString() != current) {
                        binding.etNominalBayar.removeTextChangedListener(this)

                        val cleanString = s.toString()
                            .replace("Rp", "")
                            .replace(".", "")
                            .replace(",", "")
                            .replace("\\s".toRegex(), "")

                        val parsed = cleanString.toDoubleOrNull() ?: 0.0

                        val formatted = parsed.formatCurrencyWithoutRp()
                        current = formatted

                        binding.etNominalBayar.setText(formatted)
                        binding.etNominalBayar.setSelection(formatted.length)
                        nilaiBayar = Helper.getCleanCurrencyValue(binding.etNominalBayar)

                        binding.etNominalBayar.addTextChangedListener(this)
                    }
                }

            })

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
                subtitle.text = "Apakah anda yakin akan simpan data spp?"
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

        val namaKategori = binding.etKategoriSpp.text.toString()

        loadingDialog = DialogLoading(this@AddSppActivity,
            "Mohon tunggu, sedang menambahkan data spp",false).build()

        loadingDialog.show()
        val apiClient = ApiClient.client(this)?.create(ApiInterface::class.java)
        val call = apiClient?.addSpp(namaKategori,nilaiBayar.toString())
        call?.enqueue(object: Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                loadingDialog.dismiss()
                if(!response.isSuccessful){
                    Toast.makeText(this@AddSppActivity, "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        Toast.makeText(this@AddSppActivity, "Berhasil menambahkan spp", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@AddSppActivity, MasterSppActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()

                        return
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(this@AddSppActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@AddSppActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun isValidated(): Boolean {
        with(binding){
            if (etKategoriSpp.text.toString().isEmpty()){
                etKategoriSpp.error = "Nama spp tidak boleh kosong"
                return false
            }

            if (etNominalBayar.text.toString().isEmpty()){
                etNominalBayar.error = "Nominal tidak boleh kosong"
                return false
            }

            return true
        }
    }
}