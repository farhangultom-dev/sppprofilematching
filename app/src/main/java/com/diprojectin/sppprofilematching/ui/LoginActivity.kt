package com.diprojectin.sppprofilematching.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.diprojectin.network.ApiClient
import com.diprojectin.network.ApiInterface
import com.diprojectin.network.responses.LoginResponse
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityLoginBinding
import com.diprojectin.sppprofilematching.ui.admin.HomeActivity
import com.diprojectin.sppprofilematching.ui.siswa.SiswaHomeActivity
import com.diprojectin.sppprofilematching.utils.DialogLoading
import com.diprojectin.sppprofilematching.utils.SharedPrefManager
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: SharedPrefManager
    private lateinit var loadingDialog: Dialog
    private var isCheckedSaveInfoAccount = false
    private var username = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        prefManager = SharedPrefManager(this@LoginActivity)
        initView()
    }

    private fun initView() {
        with(binding){
            isCheckedSaveInfoAccount = prefManager.isSaveAccount()

            cbSaveInfoAkun.setOnCheckedChangeListener { _, isChecked ->
                isCheckedSaveInfoAccount = isChecked
            }

            if (isCheckedSaveInfoAccount){
                etUsername.setText(prefManager.getUsername())
                cbSaveInfoAkun.isChecked = true
            }
            
            binding.btnLogin.setOnClickListener { 
                if (validating()){
                    loadingDialog = DialogLoading(this@LoginActivity,
                        "Mohon tunggu, sedang melakukan login",false).build()
                    login()
                }
            }

        }
    }

    private fun login() {
        loadingDialog.show()
        val apiClient = ApiClient.client(this)?.create(ApiInterface::class.java)
        val call = apiClient?.login(username,password)
        call?.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                loadingDialog.dismiss()
                if(!response.isSuccessful){
                    Toast.makeText(this@LoginActivity, "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        prefManager.saveUser(response.body()?.data!!)

                        if (isCheckedSaveInfoAccount){
                            prefManager.setSaveAccount(true,username)
                        }else{
                            prefManager.clearSaveAccount()
                        }

                        if (response.body()?.data?.isAdmin == "1") {
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                            return
                        }

                        val intent = Intent(this@LoginActivity, SiswaHomeActivity::class.java)
                        startActivity(intent)
                        finish()
                        return
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(this@LoginActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun validating(): Boolean {
        with(binding) {
            username = etUsername.text.toString()
            password = etPassword.text.toString()
            
            if (username.isEmpty()) {
                Snackbar.make(main, "Username tidak boleh kosong", Snackbar.LENGTH_SHORT).show()
                return false
            }

            if (password.isEmpty()) {
                Snackbar.make(main, "Password tidak boleh kosong", Snackbar.LENGTH_SHORT).show()
                return false
            }

            return true
        }
    }
}