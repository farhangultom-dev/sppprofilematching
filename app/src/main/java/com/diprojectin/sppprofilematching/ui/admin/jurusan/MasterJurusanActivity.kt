package com.diprojectin.sppprofilematching.ui.admin.jurusan

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.diprojectin.models.Jurusan
import com.diprojectin.network.ApiClient
import com.diprojectin.network.ApiInterface
import com.diprojectin.network.responses.JurusanResponse
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityMasterJurusanBinding
import com.diprojectin.sppprofilematching.ui.adapters.JurusanAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MasterJurusanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMasterJurusanBinding
    private lateinit var listJurusan: List<Jurusan>
    private lateinit var adapter: JurusanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasterJurusanBinding.inflate(layoutInflater)
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
        getListJurusan()

    }

    private fun initView() {
        with(binding){
            appBar.tvTitleHeader.text = "Master Jurusan"
            appBar.btnBack.setOnClickListener {
                onBackPressed()
            }

            btnTambah.setOnClickListener {
                val intent = Intent(this@MasterJurusanActivity, AddJurusanActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getListJurusan() = with(binding) {
        loadingView.visibility = View.VISIBLE

        val apiClient = ApiClient.client(this@MasterJurusanActivity)?.create(ApiInterface::class.java)
        val call = apiClient?.getJurusan()
        call?.enqueue(object: Callback<JurusanResponse> {
            override fun onResponse(call: Call<JurusanResponse>, response: Response<JurusanResponse>) {
                loadingView.visibility = View.GONE

                if(!response.isSuccessful){
                    Toast.makeText(this@MasterJurusanActivity, "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        listJurusan = response.body()?.data!!
                        setRecylerView()
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(this@MasterJurusanActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<JurusanResponse>, t: Throwable) {
                loadingView.visibility = View.GONE

                Toast.makeText(this@MasterJurusanActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setRecylerView() = with(binding) {
        adapter = JurusanAdapter(this@MasterJurusanActivity, arrayListOf(), object : JurusanAdapter.OnItemClickListener{
            override fun onItemClick(item: Jurusan) {
                val intent = Intent(this@MasterJurusanActivity, DetailJurusanActivity::class.java)
                intent.putExtra("id", item.id)
                intent.putExtra("namaJurusan", item.nama)
                startActivity(intent)
            }
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@MasterJurusanActivity)
        recyclerView.setHasFixedSize(true)

        adapter.setList(listJurusan)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}