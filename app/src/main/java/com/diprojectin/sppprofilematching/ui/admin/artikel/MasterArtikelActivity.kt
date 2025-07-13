package com.diprojectin.sppprofilematching.ui.admin.artikel

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.diprojectin.network.ApiClient
import com.diprojectin.network.ApiInterface
import com.diprojectin.network.responses.GenericResponse
import com.diprojectin.network.responses.UploadFileResponse
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityMasterArtikelBinding
import com.diprojectin.sppprofilematching.ui.admin.HomeActivity
import com.diprojectin.sppprofilematching.utils.DialogLoading
import com.diprojectin.sppprofilematching.utils.DialogUtils
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MasterArtikelActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMasterArtikelBinding
    private lateinit var dialogConfirm: Dialog
    private lateinit var loadingDialog: Dialog
    private var filePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasterArtikelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dialogConfirm = DialogUtils(this).build()
        initView()
        initData()
    }

    private fun initData() {

    }

    private fun initView() = with(binding) {
        appBar.tvTitleHeader.text = "Artikel"
        appBar.btnBack.setOnClickListener {
            onBackPressed()
        }

        btnUploadPhoto.setOnClickListener {
            ImagePicker.with(this@MasterArtikelActivity)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
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
            subtitle.text = "Apakah anda yakin akan simpan data siswa?"
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

    private fun sendData() {
        if (!isValidated()){
            return
        }

        val judul = binding.etJudul.text.toString()
        val deskripsi = binding.etDeskripsi.text.toString()

        loadingDialog = DialogLoading(this@MasterArtikelActivity,
            "Mohon tunggu, sedang menambahkan data artikel",false).build()

        loadingDialog.show()
        val apiClient = ApiClient.client(this)?.create(ApiInterface::class.java)
        val call = apiClient?.addArticle(judul,deskripsi,filePath!!)
        call?.enqueue(object: Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                loadingDialog.dismiss()
                if(!response.isSuccessful){
                    Toast.makeText(this@MasterArtikelActivity, "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        Toast.makeText(this@MasterArtikelActivity, "Berhasil menambahkan artikel", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@MasterArtikelActivity, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()

                        return
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(this@MasterArtikelActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@MasterArtikelActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun isValidated(): Boolean {
        with(binding){
            if (etJudul.text.toString().isEmpty()){
                etJudul.error = "Judul tidak boleh kosong"
                return false
            }

            if (etDeskripsi.text.toString().isEmpty()){
                etDeskripsi.error = "Deskripsi tidak boleh kosong"
                return false
            }

            if (filePath.isNullOrEmpty()){
                Toast.makeText(this@MasterArtikelActivity, "Foto tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return false
            }

            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            // Use Uri object instead of File to avoid storage permissions
            binding.btnUploadPhoto.setImageURI(uri)

            val file = uri.toFile()

            loadingDialog = DialogLoading(this@MasterArtikelActivity,
                "Mohon tunggu, sedang mengupload foto",false).build()
            loadingDialog.show()
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
            val apiClient = ApiClient.client(this@MasterArtikelActivity)?.create(ApiInterface::class.java)
            val call = apiClient?.uploadFile(body)
            call?.enqueue(object : Callback<UploadFileResponse> {
                override fun onResponse(call: Call<UploadFileResponse>, response: Response<UploadFileResponse>) {
                    loadingDialog.dismiss()
                    if (response.isSuccessful && response.body()?.success == true) {
                        filePath = response.body()?.url.toString()
                    } else {
                        Log.e("Upload", "Failed: ${response.body()?.message}")
                    }
                }

                override fun onFailure(call: Call<UploadFileResponse>, t: Throwable) {
                    loadingDialog.dismiss()
                    Log.e("Upload", "Error: ${t.message}")
                }
            })

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}