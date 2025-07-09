package com.diprojectin.sppprofilematching.ui.admin.siswa

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.diprojectin.models.Kelas
import com.diprojectin.models.Siswa
import com.diprojectin.network.ApiClient
import com.diprojectin.network.ApiInterface
import com.diprojectin.network.responses.KelasResponse
import com.diprojectin.network.responses.SiswaResponse
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityMasterSiswaBinding
import com.diprojectin.sppprofilematching.ui.adapters.KelasAdapter
import com.diprojectin.sppprofilematching.ui.adapters.SiswaAdapter
import com.diprojectin.sppprofilematching.ui.admin.kelas.AddKelasActivity
import com.diprojectin.sppprofilematching.ui.admin.kelas.DetailKelasActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MasterSiswaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMasterSiswaBinding
    private lateinit var adapter: SiswaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasterSiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initData()
        initView()
    }

    private fun initData() {

    }

    private fun initView() {
        with(binding){
            appBar.tvTitleHeader.text = "Master Siswa"
            appBar.btnBack.setOnClickListener {
                onBackPressed()
            }

            adapter = SiswaAdapter(this@MasterSiswaActivity, mutableListOf(), object : SiswaAdapter.OnItemClickListener {
                override fun onItemClick(item: Siswa) {
                    val intent = Intent(this@MasterSiswaActivity, DetailSiswaActivity::class.java)
                    intent.putExtra("data_siswa", Gson().toJson(item))
                    startActivity(intent)
                }

            })

            btnTambah.setOnClickListener {
                val intent = Intent(this@MasterSiswaActivity, AddSiswaActivity::class.java)
                startActivity(intent)
            }

            setRecyclerView()
            getData()
        }
    }

    private fun setRecyclerView(){
        with(binding){
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MasterSiswaActivity)
            recyclerView.setHasFixedSize(true)
        }
    }

    private fun getData() {
        binding.loadingView.visibility = View.VISIBLE
        val apiClient = ApiClient.client(this)?.create(ApiInterface::class.java)
        val call = apiClient?.getSiswa()
        call?.enqueue(object: Callback<SiswaResponse> {
            override fun onResponse(call: Call<SiswaResponse>, response: Response<SiswaResponse>) {
                binding.loadingView.visibility = View.GONE

                if(!response.isSuccessful){
                    Toast.makeText(this@MasterSiswaActivity, "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        with(binding){
                            tvTotalSiswa.text = response.body()?.total.toString()
                            tvTotalPerempuan.text = response.body()?.jumlahWanita.toString()
                            tvTotalLakiLaki.text = response.body()?.jumlahPria.toString()

                            adapter.setList(response.body()?.data!!)
                        }
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(this@MasterSiswaActivity, response.message(), Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<SiswaResponse>, t: Throwable) {
                binding.loadingView.visibility = View.GONE
                Toast.makeText(this@MasterSiswaActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}