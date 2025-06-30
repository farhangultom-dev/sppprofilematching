package com.diprojectin.sppprofilematching.ui.admin.kelas

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.diprojectin.models.Kelas
import com.diprojectin.network.ApiClient
import com.diprojectin.network.ApiInterface
import com.diprojectin.network.responses.GenericResponse
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityDetailKelasBinding
import com.diprojectin.sppprofilematching.ui.adapters.DetailKelasAdapter
import com.diprojectin.sppprofilematching.utils.DialogLoading
import com.diprojectin.sppprofilematching.utils.DialogUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailKelasActivity : AppCompatActivity() {
    private lateinit var loadingDialog: Dialog
    private lateinit var binding: ActivityDetailKelasBinding
    private lateinit var listKelas: List<Kelas>
    private lateinit var copyListKelas: List<Kelas>
    private lateinit var adapter: DetailKelasAdapter
    private lateinit var dialogConfirm: Dialog
    private var kelas = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailKelasBinding.inflate(layoutInflater)
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

    private fun initData() {
        val stringListKelas = intent.getStringExtra("listKelas")
        val type = object : TypeToken<List<Kelas>>() {}.type
        listKelas = Gson().fromJson(stringListKelas, type)
        copyListKelas = Gson().fromJson(stringListKelas, type)

        kelas = intent.getStringExtra("kelas")!!
    }

    private fun initView() {
        with(binding){
            appBar.tvTitleHeader.text = "Detail Kelas"
            appBar.btnBack.setOnClickListener {
                onBackPressed()
            }
            tvDaftarKelas.text = "Daftar Kelas $kelas"

            binding.btnEdit.setOnClickListener {
                binding.btnEdit.visibility = View.INVISIBLE
                btnSimpan.visibility = View.VISIBLE
                btnCancel.visibility = View.VISIBLE

                setAdapter(1)
                setRecyclerView()
            }

            btnCancel.setOnClickListener {
                binding.btnEdit.visibility = View.VISIBLE
                btnSimpan.visibility = View.GONE
                btnCancel.visibility = View.GONE

                setAdapter(0)

                with(binding){
                    rvListKelas.adapter = adapter
                    rvListKelas.layoutManager = LinearLayoutManager(this@DetailKelasActivity)
                    rvListKelas.setHasFixedSize(true)

                    adapter.setList(copyListKelas)
                }
            }

            btnSimpan.setOnClickListener {
                updateKelas()
            }

            setAdapter(0)
            setRecyclerView()
        }

    }

    private fun updateKelas() {
        with(binding){
            loadingDialog = DialogLoading(this@DetailKelasActivity,
                "Mohon tunggu, sedang mengupdate data kelas",false).build()

            loadingDialog.show()
            val apiClient = ApiClient.client(this@DetailKelasActivity)?.create(ApiInterface::class.java)
            val call = apiClient?.editKelas(listKelas)
            call?.enqueue(object: Callback<GenericResponse> {
                override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                    loadingDialog.dismiss()
                    if(!response.isSuccessful){
                        Toast.makeText(this@DetailKelasActivity, "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                        return
                    }

                    if(response.isSuccessful){
                        if (response.body()?.success == true){
                            Toast.makeText(this@DetailKelasActivity, "Berhasil update kelas", Toast.LENGTH_SHORT).show()

                            binding.btnEdit.visibility = View.VISIBLE
                            btnSimpan.visibility = View.GONE
                            btnCancel.visibility = View.GONE
                            setAdapter(0)
                            setRecyclerView()
                            return
                        }

                        if (response.body()?.success == false){
                            Toast.makeText(this@DetailKelasActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                            return
                        }
                    }
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    loadingDialog.dismiss()
                    Toast.makeText(this@DetailKelasActivity, t.message, Toast.LENGTH_SHORT).show()
                    return
                }

            })
        }
    }

    private fun setAdapter(isEdit: Int) {
        adapter = DetailKelasAdapter(this@DetailKelasActivity, mutableListOf(),object : DetailKelasAdapter.OnItemClickListener {
            override fun onItemClick(item: Kelas, adapterPosition: Int) {
                val title = dialogConfirm.findViewById<TextView>(R.id.tv_title)
                val subtitle = dialogConfirm.findViewById<TextView>(R.id.tv_subtitle)
                val icon = dialogConfirm.findViewById<ImageView>(R.id.icon_dialog)
                val btnSimpan = dialogConfirm.findViewById<TextView>(R.id.btnSimpan)
                val btnCancel = dialogConfirm.findViewById<TextView>(R.id.btnCancel)

                title.text = "Hapus Kelas"
                subtitle.text = "Apakah anda yakin akan hapus data kelas?"
                btnSimpan.text = "Ya"
                btnCancel.text = "Tidak"
                icon.setImageResource(R.drawable.ic_delete_data)

                btnSimpan.setOnClickListener {
                    dialogConfirm.dismiss()
                    deleteKelas(item,adapterPosition)
                }

                btnCancel.setOnClickListener {
                    dialogConfirm.dismiss()
                }

                dialogConfirm.show()
            }

        },object : DetailKelasAdapter.OnTextChangedListener {
            override fun onTextChanged(text: String, item: Kelas) {
                val listToUpdate = listKelas.indexOfFirst { it.id == item.id}
                listKelas[listToUpdate].nama = text
                adapter.setList(listKelas)
            }

        },isEdit)
    }

    private fun deleteKelas(item: Kelas, adapterPosition: Int) {
        loadingDialog = DialogLoading(this@DetailKelasActivity,
            "Mohon tunggu, sedang menghapus data kelas",false).build()

        loadingDialog.show()
        val apiClient = ApiClient.client(this)?.create(ApiInterface::class.java)
        val call = apiClient?.deleteKelas(item.id!!)
        call?.enqueue(object: Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                loadingDialog.dismiss()
                if(!response.isSuccessful){
                    Toast.makeText(this@DetailKelasActivity, "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        Toast.makeText(this@DetailKelasActivity, "Berhasil menghapus kelas", Toast.LENGTH_SHORT).show()
                        listKelas = listKelas.filter { it.id != item.id }
                        copyListKelas = listKelas
                        adapter.setList(listKelas)
                        return
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(this@DetailKelasActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@DetailKelasActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setRecyclerView() {
        with(binding){

            rvListKelas.adapter = adapter
            rvListKelas.layoutManager = LinearLayoutManager(this@DetailKelasActivity)
            rvListKelas.setHasFixedSize(true)

            adapter.setList(listKelas)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@DetailKelasActivity, MasterKelasActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }
}