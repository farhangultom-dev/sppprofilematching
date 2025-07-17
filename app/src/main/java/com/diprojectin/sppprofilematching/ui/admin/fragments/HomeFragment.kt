package com.diprojectin.sppprofilematching.ui.admin.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.diprojectin.network.ApiClient
import com.diprojectin.network.ApiInterface
import com.diprojectin.network.responses.DashboardHomeAdminResponse
import com.diprojectin.network.responses.JurusanResponse
import com.diprojectin.sppprofilematching.databinding.FragmentHomeBinding
import com.diprojectin.sppprofilematching.ui.admin.artikel.MasterArtikelActivity
import com.diprojectin.sppprofilematching.ui.admin.jurusan.MasterJurusanActivity
import com.diprojectin.sppprofilematching.ui.admin.kelas.MasterKelasActivity
import com.diprojectin.sppprofilematching.ui.admin.notification.NotificationActivity
import com.diprojectin.sppprofilematching.ui.admin.siswa.MasterSiswaActivity
import com.diprojectin.sppprofilematching.ui.admin.spp.MasterSppActivity
import com.diprojectin.sppprofilematching.utils.DialogLoading
import com.diprojectin.sppprofilematching.utils.Helper.formatCurrency
import com.diprojectin.sppprofilematching.utils.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var prefManager: SharedPrefManager
    private lateinit var loadingDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = SharedPrefManager(requireActivity())
        getDataHomeDashboard()
        initView()
    }

    private fun initView() {
        with(binding){
            Glide.with(requireActivity())
                .load(prefManager.getUser()?.photoProfile)
                .into(circleImageView)

            tvName.text = prefManager.getUser()?.nama
            btnMasterSiswa.setOnClickListener {
                val intent = Intent(requireActivity(), MasterSiswaActivity::class.java)
                startActivity(intent)
            }

            btnMasterKelas.setOnClickListener {
                val intent = Intent(requireActivity(), MasterKelasActivity::class.java)
                startActivity(intent)
            }

            btnMasterJurusan.setOnClickListener {
                val intent = Intent(requireActivity(), MasterJurusanActivity::class.java)
                startActivity(intent)
            }

            btnMasterSpp.setOnClickListener {
                val intent = Intent(requireActivity(), MasterSppActivity::class.java)
                startActivity(intent)
            }

            btnMasterArticle.setOnClickListener {
                val intent = Intent(requireActivity(), MasterArtikelActivity::class.java)
                startActivity(intent)
            }

            cvNotif.setOnClickListener {
                val intent = Intent(requireActivity(), NotificationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getDataHomeDashboard() = with(binding) {
        loadingDialog = DialogLoading(requireActivity(),"Sedang mendapatkan data...",false).build()

        loadingDialog.show()
        val apiClient = ApiClient.client(requireActivity())?.create(ApiInterface::class.java)
        val call = apiClient?.getDashboardHomeAdmin()
        call?.enqueue(object: Callback<DashboardHomeAdminResponse> {
            override fun onResponse(call: Call<DashboardHomeAdminResponse>, response: Response<DashboardHomeAdminResponse>) {
                loadingDialog.dismiss()
                if(!response.isSuccessful){
                    Toast.makeText(requireActivity(), "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        totalBayarByBulan.text = response.body()?.totalTransaksi?.totalSuccessPayment?.toInt().formatCurrency()
                        jumlahSiswaBayar.text = "dari ${response.body()?.totalTransaksi?.totalSiswaBayar} Siswa yang telah bayar"
                        tvTotalSiswa.text = response.body()?.totalSiswa?.total.toString()
                        tvJumlahKelas.text = response.body()?.totalKelas?.total.     toString()

                        for (i in response.body()?.percentagePayment!!){
                            if (i?.kelas == "Kelas X"){
                                tvPercentageTotalSiswax.text = "${i.persen}% (${i.sudahBayar}/${i.totalSiswa} Siswa)"
                                progressBarx.progress = i.persen!!
                            }

//                            if (i?.kelas == "Kelas XI"){
//                                tvPercentageTotalSiswaxi.text = "${i.persen}% (${i.sudahBayar}/${i.totalSiswa} Siswa)"
//                                progressBarxi.progress = i.persen!!
//                            }
//
//                            if (i?.kelas == "Kelas XII"){
//                                tvPercentageTotalSiswaxii.text = "${i.persen}% (${i.sudahBayar}/${i.totalSiswa} Siswa)"
//                                progressBarxii.progress = i.persen!!
//                            }
                        }
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(requireActivity(), response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<DashboardHomeAdminResponse>, t: Throwable) {
                loadingDialog.dismiss()

                Toast.makeText(requireActivity(), t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}