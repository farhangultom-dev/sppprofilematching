package com.diprojectin.sppprofilematching.ui.admin.siswa

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.diprojectin.models.Kelas
import com.diprojectin.models.KelasByUser
import com.diprojectin.models.Siswa
import com.diprojectin.models.User
import com.diprojectin.network.ApiClient
import com.diprojectin.network.ApiInterface
import com.diprojectin.network.responses.GenericResponse
import com.diprojectin.network.responses.KelasByUserResponse
import com.diprojectin.network.responses.UploadFileResponse
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityDetailSiswaBinding
import com.diprojectin.sppprofilematching.ui.adapters.KelasByUserAdapter
import com.diprojectin.sppprofilematching.utils.DialogLoading
import com.diprojectin.sppprofilematching.utils.DialogUtils
import com.diprojectin.sppprofilematching.utils.SharedPrefManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DetailSiswaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailSiswaBinding
    private lateinit var user: Siswa
    private lateinit var adapterKelas: KelasByUserAdapter
    private var kelasByUsers: List<KelasByUser>? = null
    private lateinit var loadingDialog: Dialog
    private lateinit var dialogConfirm: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSiswaBinding.inflate(layoutInflater)
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

    private fun initData() = with(binding) {
        user = Gson().fromJson(intent.getStringExtra("data_siswa").toString(),Siswa::class.java)
    }

    private fun initView() = with(binding) {
        tvNamaSiswa.text = user.nama
        tvKelas.text = "${user.kelas}-${user.namaKelas} ${user.jurusan}"

        Glide.with(this@DetailSiswaActivity)
            .load(user.photoProfile)
            .into(ivPhotoSiswa)

        with(siswaBody){
            tvNisn.text = user.nisn
            tvNamaSiswa.text = user.nama
            tvJenisKelamin.text = user.jenisKelamin
            tvTempatLahir.text = user.tempatLahir
            tvTanggalLahir.text = user.tanggalLahir
            tvPhone.text = user.nomorTelpon
            tvStatusSiswa.text = user.statusSiswa
            tahunMasuk.text = user.tahunMasuk
            tvAlamat.text = user.alamat
        }

        btnDataDiri.setOnClickListener {
            disableButton()
            hideLayoutTab()

            btnDataDiri.background = getDrawable(R.drawable.btn_tab_active)
            btnDataDiri.setTextColor(getColor(R.color.white))
            siswaBody.mainSiswaBody.visibility = View.VISIBLE
        }

        btnKelas.setOnClickListener {
            disableButton()
            hideLayoutTab()

            btnKelas.background = getDrawable(R.drawable.btn_tab_active)
            btnKelas.setTextColor(getColor(R.color.white))
            kelasBody.mainKelasBody.visibility = View.VISIBLE

            if (kelasByUsers.isNullOrEmpty()){
                getKelasByUser()
            }
        }

        btnSpp.setOnClickListener {
            disableButton()
            hideLayoutTab()

            btnSpp.background = getDrawable(R.drawable.btn_tab_active)
            btnSpp.setTextColor(getColor(R.color.white))
            sppBody.mainSppBody.visibility = View.VISIBLE
        }

        btnDelete.setOnClickListener {
            val title = dialogConfirm.findViewById<TextView>(R.id.tv_title)
            val subtitle = dialogConfirm.findViewById<TextView>(R.id.tv_subtitle)
            val icon = dialogConfirm.findViewById<ImageView>(R.id.icon_dialog)
            val btnSimpan = dialogConfirm.findViewById<TextView>(R.id.btnSimpan)
            val btnCancel = dialogConfirm.findViewById<TextView>(R.id.btnCancel)

            title.text = "Hapus Data"
            subtitle.text = "Apakah anda yakin akan hapus data siswa ini?"
            btnSimpan.text = "Ya"
            btnCancel.text = "Tidak"
            icon.setImageResource(R.drawable.ic_delete_data)

            btnSimpan.setOnClickListener {
                dialogConfirm.dismiss()
                deleteSiswa(user)
            }

            btnCancel.setOnClickListener {
                dialogConfirm.dismiss()
            }

            dialogConfirm.show()
        }

        btnEdit.setOnClickListener {
            val intent = Intent(this@DetailSiswaActivity, EditSiswaActivity::class.java)
            intent.putExtra("data_siswa", Gson().toJson(user))
            startActivity(intent)
        }

        btnEditPhoto.setOnClickListener {
            ImagePicker.with(this@DetailSiswaActivity)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }
    }

    private fun deleteSiswa(item: Siswa) {
        loadingDialog = DialogLoading(this@DetailSiswaActivity,
            "Mohon tunggu, sedang menghapus data siswa",false).build()

        loadingDialog.show()
        val apiClient = ApiClient.client(this)?.create(ApiInterface::class.java)
        val call = apiClient?.deleteSiswa(item.id!!)
        call?.enqueue(object: Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                loadingDialog.dismiss()
                if(!response.isSuccessful){
                    Toast.makeText(this@DetailSiswaActivity, "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        Toast.makeText(this@DetailSiswaActivity, "Berhasil menghapus siswa", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@DetailSiswaActivity, MasterSiswaActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                        return
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(this@DetailSiswaActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@DetailSiswaActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun hideLayoutTab() = with(binding) {
        siswaBody.mainSiswaBody.visibility = View.GONE
        kelasBody.mainKelasBody.visibility = View.GONE
        sppBody.mainSppBody.visibility = View.GONE
    }

    private fun disableButton() = with(binding) {
        btnDataDiri.background = getDrawable(R.drawable.btn_tab_inactive)
        btnDataDiri.setTextColor(Color.parseColor("#54333333"))

        btnSpp.background = getDrawable(R.drawable.btn_tab_inactive)
        btnSpp.setTextColor(Color.parseColor("#54333333"))

        btnKelas.background = getDrawable(R.drawable.btn_tab_inactive)
        btnKelas.setTextColor(Color.parseColor("#54333333"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            // Use Uri object instead of File to avoid storage permissions
            binding.ivPhotoSiswa.setImageURI(uri)

            val file = uri.toFile()

            loadingDialog = DialogLoading(this@DetailSiswaActivity,
                "Mohon tunggu, sedang mengupload foto",false).build()
            loadingDialog.show()
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
            val apiClient = ApiClient.client(this@DetailSiswaActivity)?.create(ApiInterface::class.java)
            val call = apiClient?.uploadFilw(body)
            call?.enqueue(object : Callback<UploadFileResponse> {
                override fun onResponse(call: Call<UploadFileResponse>, response: Response<UploadFileResponse>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        val imageUrl = response.body()?.url.toString()
                        
                        sendPhoto(imageUrl)
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

    private fun sendPhoto(uploadedImage: String) {
        val apiClient = ApiClient.client(this@DetailSiswaActivity)?.create(ApiInterface::class.java)
        val call = apiClient?.updatePhotoProfile(user.usersDetailId!!, uploadedImage)
        call?.enqueue(object : Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                loadingDialog.dismiss()
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(
                        this@DetailSiswaActivity,
                        "berhasil upload foto",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.e("Upload", "Failed: ${response.body()?.message}")
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Log.e("Upload", "Error: ${t.message}")
            }
        })
    }

    private fun getKelasByUser() {
        loadingDialog = DialogLoading(this@DetailSiswaActivity,
            "Mohon tunggu, sedang mendapatkan data kelas",false).build()

        loadingDialog.show()
        val apiClient = ApiClient.client(this@DetailSiswaActivity)?.create(ApiInterface::class.java)
        val call = apiClient?.getKelasByUSer(user.usersDetailId!!)
        call?.enqueue(object : Callback<KelasByUserResponse> {
            override fun onResponse(call: Call<KelasByUserResponse>, response: Response<KelasByUserResponse>) {
                loadingDialog.dismiss()
                if (response.isSuccessful && response.body()?.success == true) {
                    kelasByUsers = response.body()?.data!!
                    initRecylerview()
                } else {
                    Log.e("Upload", "Failed: ${response.body()?.message}")
                }
            }

            override fun onFailure(call: Call<KelasByUserResponse>, t: Throwable) {
                loadingDialog.dismiss()
            }
        })
    }

    private fun initRecylerview() = with(binding) {
        adapterKelas = KelasByUserAdapter(this@DetailSiswaActivity, arrayListOf(),object : KelasByUserAdapter.OnItemClickListener{
            override fun onItemClick(item: KelasByUser) {}

        })

        with(kelasBody){
            recyclerView.adapter = adapterKelas
            recyclerView.layoutManager = LinearLayoutManager(this@DetailSiswaActivity)
            recyclerView.setHasFixedSize(true)

            adapterKelas.setList(kelasByUsers!!)
        }
    }

    //TODO jangan lupa buat list spp
}