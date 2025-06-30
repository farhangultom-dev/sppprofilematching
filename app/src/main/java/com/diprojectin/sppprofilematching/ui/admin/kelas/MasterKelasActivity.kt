package com.diprojectin.sppprofilematching.ui.admin.kelas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.diprojectin.network.responses.KelasResponse
import com.diprojectin.models.Kelas
import com.diprojectin.models.KelasCount
import com.diprojectin.network.ApiClient
import com.diprojectin.network.ApiInterface
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityMasterKelasBinding
import com.diprojectin.sppprofilematching.ui.adapters.KelasAdapter
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MasterKelasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMasterKelasBinding
    private lateinit var adapter: KelasAdapter
    private lateinit var listKelas: List<Kelas>
    private lateinit var kelasCount: List<KelasCount>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasterKelasBinding.inflate(layoutInflater)
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
        getDataKelas()
    }

    private fun initView() {
        with(binding){
            appBar.btnBack.setOnClickListener {
                onBackPressed()
            }

            adapter = KelasAdapter(this@MasterKelasActivity, mutableListOf(), mutableListOf(), object : KelasAdapter.OnItemClickListener {
                override fun onItemClick(item: Kelas) {
                    val intent = Intent(this@MasterKelasActivity, DetailKelasActivity::class.java)
                    intent.putExtra("listKelas", Gson().toJson(listKelas.filter { it.grade == item.grade }))
                    intent.putExtra("kelas", item.grade)
                    startActivity(intent)
                }

            })

            btnTambah.setOnClickListener {
                val intent = Intent(this@MasterKelasActivity, AddKelasActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun setRecyclerView() {
        with(binding){

            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MasterKelasActivity)
            recyclerView.setHasFixedSize(true)

            adapter.setList(listKelas)
        }
    }

    private fun getDataKelas() {
        binding.loadingView.visibility = View.VISIBLE
        val apiClient = ApiClient.client(this)?.create(ApiInterface::class.java)
        val call = apiClient?.getKelas()
        call?.enqueue(object: Callback<KelasResponse> {
            override fun onResponse(call: Call<KelasResponse>, response: Response<KelasResponse>) {
                binding.loadingView.visibility = View.GONE

                if(!response.isSuccessful){
                    Toast.makeText(this@MasterKelasActivity, "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        listKelas = response.body()?.data!!
                        kelasCount = response.body()?.dataCount!!

                        for (i in kelasCount){
                            if (i.grade == "X"){
                                binding.tvJumlahX.text = i.totalSiswa.toString()
                                binding.tvJumlahGenderX.text = "${i.jumlahWanita} (P) / ${i.jumlahPria} (L)"
                            }

                            if (i.grade == "XI") {
                                binding.tvJumlahXi.text = i.totalSiswa.toString()
                                binding.tvJumlahGenderXi.text = "${i.jumlahWanita} (P) / ${i.jumlahPria} (L)"
                            }

                            if (i.grade == "XII") {
                                binding.tvJumlahXii.text = i.totalSiswa.toString()
                                binding.tvJumlahGenderXii.text = "${i.jumlahWanita} (P) / ${i.jumlahPria} (L)"
                            }
                        }

                        setRecyclerView()
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(this@MasterKelasActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<KelasResponse>, t: Throwable) {
                binding.loadingView.visibility = View.GONE
                Toast.makeText(this@MasterKelasActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}