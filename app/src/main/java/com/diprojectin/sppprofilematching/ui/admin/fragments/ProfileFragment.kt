package com.diprojectin.sppprofilematching.ui.admin.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import com.bumptech.glide.Glide
import com.diprojectin.models.User
import com.diprojectin.network.ApiClient
import com.diprojectin.network.ApiInterface
import com.diprojectin.network.responses.GenericResponse
import com.diprojectin.network.responses.UploadFileResponse
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.FragmentProfileBinding
import com.diprojectin.sppprofilematching.ui.LoginActivity
import com.diprojectin.sppprofilematching.utils.DialogLoading
import com.diprojectin.sppprofilematching.utils.DialogUtils
import com.diprojectin.sppprofilematching.utils.SharedPrefManager
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var userData: User
    private lateinit var loadingDialog: Dialog
    private lateinit var dialogConfirm: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        dialogConfirm = DialogUtils(requireActivity()).build()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {
        userData = SharedPrefManager(requireActivity()).getUser()!!
    }

    private fun initView() = with(binding) {
        tvNama.text = userData.nama
        tvJabatan.text = "Guru"

        with(adminBody){
            tvJabatan.text = "Guru"
            tvNama.text = userData.nama
            tvJenisKelamin.text = userData.jenisKelamin
            tvTempatLahir.text = userData.tempatLahir
            tvTanggalLahir.text = userData.tanggalLahir
            tvPhone.text = userData.nomorTelpon
            tvStatusSiswa.text = userData.statusSiswa
            tahunMasuk.text = userData.tahunMasuk
            tvAlamat.text = userData.alamat

            Glide.with(requireActivity())
                .load(userData.photoProfile)
                .into(ivPhoto)

            btnLogout.setOnClickListener {
                val title = dialogConfirm.findViewById<TextView>(R.id.tv_title)
                val subtitle = dialogConfirm.findViewById<TextView>(R.id.tv_subtitle)
                val icon = dialogConfirm.findViewById<ImageView>(R.id.icon_dialog)
                val btnSimpan = dialogConfirm.findViewById<TextView>(R.id.btnSimpan)
                val btnCancel = dialogConfirm.findViewById<TextView>(R.id.btnCancel)

                title.text = "Logout"
                subtitle.text = "Apakah anda yakin ingin logout?"
                btnSimpan.text = "Ya"
                btnCancel.text = "Tidak"
                icon.visibility = View.GONE

                btnSimpan.setOnClickListener {
                    dialogConfirm.dismiss()
                    logout()
                }

                btnCancel.setOnClickListener {
                    dialogConfirm.dismiss()
                }

                dialogConfirm.show()
            }
        }

        btnEditPhoto.setOnClickListener {
            ImagePicker.with(requireActivity())
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent: Intent -> startForProfileImageResult.launch(intent) }
        }

    }

    private fun logout() {
        SharedPrefManager(requireActivity()).clearUser()
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                binding.ivPhoto.setImageURI(fileUri)
                val file = fileUri.toFile()

                loadingDialog = DialogLoading(requireActivity(),
                    "Mohon tunggu, sedang mengupload foto",false).build()
                loadingDialog.show()
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
                val apiClient = ApiClient.client(requireActivity())?.create(ApiInterface::class.java)
                val call = apiClient?.uploadFile(body)
                call?.enqueue(object : Callback<UploadFileResponse> {
                    override fun onResponse(call: Call<UploadFileResponse>, response: Response<UploadFileResponse>) {
                        loadingDialog.dismiss()
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
                Toast.makeText(requireActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun sendPhoto(uploadedImage: String) {
        val apiClient = ApiClient.client(requireActivity())?.create(ApiInterface::class.java)
        val call = apiClient?.updatePhotoProfile(userData.id!!, uploadedImage)
        call?.enqueue(object : Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                loadingDialog.dismiss()
                if (response.isSuccessful && response.body()?.success == true) {
                    userData.photoProfile = uploadedImage
                    SharedPrefManager(requireActivity()).saveUser(userData)

                    Toast.makeText(
                        requireActivity(),
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
}