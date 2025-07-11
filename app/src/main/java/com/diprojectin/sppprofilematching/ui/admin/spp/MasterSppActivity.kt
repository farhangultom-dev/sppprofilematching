package com.diprojectin.sppprofilematching.ui.admin.spp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.diprojectin.models.Kelas
import com.diprojectin.models.Spp
import com.diprojectin.network.ApiClient
import com.diprojectin.network.ApiInterface
import com.diprojectin.network.responses.SppResponse
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityMasterSiswaBinding
import com.diprojectin.sppprofilematching.databinding.ActivityMasterSppBinding
import com.diprojectin.sppprofilematching.ui.adapters.KategoriSppAdapter
import com.diprojectin.sppprofilematching.ui.adapters.KelasAdapter
import com.diprojectin.sppprofilematching.ui.admin.kelas.AddKelasActivity
import com.diprojectin.sppprofilematching.ui.admin.kelas.DetailKelasActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MasterSppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMasterSppBinding
    private lateinit var adapter: KategoriSppAdapter
    private lateinit var listSpp: List<Spp>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasterSppBinding.inflate(layoutInflater)
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
        getDataSpp()

    }

    private fun initView() {
        with(binding){
            appBar.tvTitleHeader.text = "Master Spp"
            appBar.btnBack.setOnClickListener {
                onBackPressed()
            }

            btnTambah.setOnClickListener {
                val intent = Intent(this@MasterSppActivity, AddSppActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getDataSpp() {
        binding.loadingView.visibility = View.VISIBLE
        val apiClient = ApiClient.client(this)?.create(ApiInterface::class.java)
        val call = apiClient?.getSpp()
        call?.enqueue(object: Callback<SppResponse> {
            override fun onResponse(call: Call<SppResponse>, response: Response<SppResponse>) {
                binding.loadingView.visibility = View.GONE

                if(!response.isSuccessful){
                    Toast.makeText(this@MasterSppActivity, "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        listSpp = response.body()?.data!!

                        setRecyclerView()
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(this@MasterSppActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<SppResponse>, t: Throwable) {
                binding.loadingView.visibility = View.GONE
                Toast.makeText(this@MasterSppActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setRecyclerView() {
        with(binding){
            adapter = KategoriSppAdapter(this@MasterSppActivity, arrayListOf(), object : KategoriSppAdapter.OnItemClickListener {
                override fun onItemClick(item: Spp) {
                    val intent = Intent(this@MasterSppActivity, DetailSppActivity::class.java)
                    intent.putExtra("namaKategori", item.nama)
                    intent.putExtra("idKategori", item.id)
                    intent.putExtra("nilaiSpp", item.nilaiSpp)
                    startActivity(intent)
                }

            })

            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MasterSppActivity)
            recyclerView.setHasFixedSize(true)

            adapter.setList(listSpp)
        }
    }
}