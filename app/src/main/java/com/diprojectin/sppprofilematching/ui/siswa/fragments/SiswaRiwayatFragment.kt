package com.diprojectin.sppprofilematching.ui.siswa.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import com.diprojectin.models.Angsuran
import com.diprojectin.models.Lunas
import com.diprojectin.models.User
import com.diprojectin.network.ApiClient
import com.diprojectin.network.ApiInterface
import com.diprojectin.network.responses.DashboardHomeResponse
import com.diprojectin.network.responses.RiwayatTransactionsResponse
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.FragmentSiswaRiwayatBinding
import com.diprojectin.sppprofilematching.ui.admin.siswa.DetailSiswaActivity
import com.diprojectin.sppprofilematching.ui.siswa.DetailRiwayatActivity
import com.diprojectin.sppprofilematching.ui.siswa.adpters.RiwayatAngsuranAdapter
import com.diprojectin.sppprofilematching.ui.siswa.adpters.RiwayatLunasAdapter
import com.diprojectin.sppprofilematching.utils.DialogLoading
import com.diprojectin.sppprofilematching.utils.Helper.formatCurrency
import com.diprojectin.sppprofilematching.utils.SharedPrefManager
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SiswaRiwayatFragment : Fragment() {
    private lateinit var binding: FragmentSiswaRiwayatBinding
    private lateinit var userData: User
    private lateinit var loadingDialog: Dialog
    private lateinit var adapterLunas: RiwayatLunasAdapter
    private lateinit var adapterAngsuran: RiwayatAngsuranAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSiswaRiwayatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {
        userData = SharedPrefManager(requireActivity()).getUser()!!
        getRiwayat()
    }

    private fun initView()= with(binding) {
        btnLunas.setOnClickListener {
            disableButton()
            hideLayoutTab()

            btnLunas.background = getDrawable(requireActivity(),R.drawable.btn_tab_active)
            btnLunas.setTextColor(requireActivity().getColor(R.color.white))
            recyclerViewLunas.visibility = View.VISIBLE

        }

        btnAngsuran.setOnClickListener {
            disableButton()
            hideLayoutTab()

            btnAngsuran.background = getDrawable(requireActivity(),R.drawable.btn_tab_active)
            btnAngsuran.setTextColor(requireActivity().getColor(R.color.white))
            recyclerViewAngsuran.visibility = View.VISIBLE

        }
    }

    private fun hideLayoutTab() = with(binding) {
        recyclerViewAngsuran.visibility = View.GONE
        recyclerViewLunas.visibility = View.GONE
    }

    private fun disableButton() = with(binding) {
        btnAngsuran.background = getDrawable(requireActivity(),R.drawable.btn_tab_inactive)
        btnAngsuran.setTextColor(Color.parseColor("#54333333"))

        btnLunas.background = getDrawable(requireActivity(),R.drawable.btn_tab_inactive)
        btnLunas.setTextColor(Color.parseColor("#54333333"))

    }

    private fun getRiwayat() = with(binding) {
        loadingDialog = DialogLoading(requireActivity(),
            "Mohon tunggu, sedang mendapatkan data",false).build()

        loadingDialog.show()
        val apiClient = ApiClient.client(requireActivity())?.create(ApiInterface::class.java)
        val call = apiClient?.getRiwayat(userData.id!!)
        call?.enqueue(object: Callback<RiwayatTransactionsResponse> {
            override fun onResponse(call: Call<RiwayatTransactionsResponse>, response: Response<RiwayatTransactionsResponse>) {
                loadingDialog.dismiss()
                if(!response.isSuccessful){
                    Toast.makeText(requireActivity(), "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        setRecylerViewLunas(response.body()?.riwayatLunas)
                        setRecyclerViewAngsuran(response.body()?.riwayatAngsuran)

                        return
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(requireActivity(), response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<RiwayatTransactionsResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(requireActivity(), t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setRecyclerViewAngsuran(riwayatAngsuran: List<Angsuran>?) = with(binding) {
        adapterAngsuran = RiwayatAngsuranAdapter(requireActivity(), arrayListOf(),true, object: RiwayatAngsuranAdapter.OnItemClickListener{
            override fun onItemClick(item: Angsuran) {
                val intent = Intent(requireActivity(), DetailRiwayatActivity::class.java)
                intent.putExtra("model_data", "angsuran")
                intent.putExtra("data_riwayat", Gson().toJson(item))
                startActivity(intent)
            }
        })
        recyclerViewAngsuran.adapter =  adapterAngsuran
        recyclerViewAngsuran.layoutManager = LinearLayoutManager(requireActivity())
        recyclerViewAngsuran.setHasFixedSize(true)

        adapterAngsuran.setList(riwayatAngsuran!!)

    }

    private fun setRecylerViewLunas(riwayatLunas: List<Lunas>?) = with(binding) {
        adapterLunas = RiwayatLunasAdapter(requireActivity(), arrayListOf(), object: RiwayatLunasAdapter.OnItemClickListener{
            override fun onItemClick(item: Lunas) {
                val intent = Intent(requireActivity(), DetailRiwayatActivity::class.java)
                intent.putExtra("model_data", "lunas")
                intent.putExtra("data_riwayat", Gson().toJson(item))
                startActivity(intent)
            }
        })
        recyclerViewLunas.adapter =  adapterLunas
        recyclerViewLunas.layoutManager = LinearLayoutManager(requireActivity())
        recyclerViewLunas.setHasFixedSize(true)

        adapterLunas.setList(riwayatLunas!!)

    }

}