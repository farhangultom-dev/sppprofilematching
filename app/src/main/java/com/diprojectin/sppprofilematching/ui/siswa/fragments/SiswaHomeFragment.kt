package com.diprojectin.sppprofilematching.ui.siswa.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.diprojectin.models.Angsuran
import com.diprojectin.models.Lunas
import com.diprojectin.models.User
import com.diprojectin.network.ApiClient
import com.diprojectin.network.ApiInterface
import com.diprojectin.network.responses.ArtikelResponse
import com.diprojectin.network.responses.DashboardHomeResponse
import com.diprojectin.network.responses.TransactionLunasResponse
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.FragmentSiswaHomeBinding
import com.diprojectin.sppprofilematching.ui.admin.notification.NotificationActivity
import com.diprojectin.sppprofilematching.ui.siswa.WebViewActivity
import com.diprojectin.sppprofilematching.ui.siswa.adpters.RiwayatAngsuranAdapter
import com.diprojectin.sppprofilematching.ui.siswa.adpters.RiwayatLunasAdapter
import com.diprojectin.sppprofilematching.ui.siswa.adpters.SliderAdapter
import com.diprojectin.sppprofilematching.utils.DialogLoading
import com.diprojectin.sppprofilematching.utils.Helper.formatCurrency
import com.diprojectin.sppprofilematching.utils.SharedPrefManager
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs

class SiswaHomeFragment : Fragment() {
    private lateinit var binding: FragmentSiswaHomeBinding
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var userData: User
    private lateinit var loadingDialog: Dialog
    private lateinit var adapterLunas: RiwayatLunasAdapter
    private lateinit var adapterAngsuran: RiwayatAngsuranAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSiswaHomeBinding.inflate(inflater, container, false)
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
        tvName.text = userData.nama

        Glide.with(requireActivity())
            .load(userData.photoProfile)
            .into(circleImageView)

        btnLunas.setOnClickListener {
            disableButton()
            hideLayoutTab()

            btnLunas.background = getDrawable(requireActivity(),R.drawable.btn_tab_active)
            btnLunas.setTextColor(requireActivity().getColor(R.color.white))
            bodyLunas.main.visibility = View.VISIBLE

        }

        btnAngsuran.setOnClickListener {
            disableButton()
            hideLayoutTab()

            btnAngsuran.background = getDrawable(requireActivity(),R.drawable.btn_tab_active)
            btnAngsuran.setTextColor(requireActivity().getColor(R.color.white))
            bodyAngsuran.main.visibility = View.VISIBLE

        }

        cvNotif.setOnClickListener {
            val intent = Intent(requireActivity(), NotificationActivity::class.java)
            startActivity(intent)
        }

        initArtikel()
        getDashboard()
    }

    private fun getDashboard() = with(binding) {
        loadingDialog = DialogLoading(requireActivity(),
            "Mohon tunggu, sedang mendapatkan data",false).build()

        loadingDialog.show()
        val apiClient = ApiClient.client(requireActivity())?.create(ApiInterface::class.java)
        val call = apiClient?.getDashboardHomeSiswa(userData.id!!)
        call?.enqueue(object: Callback<DashboardHomeResponse> {
            override fun onResponse(call: Call<DashboardHomeResponse>, response: Response<DashboardHomeResponse>) {
                loadingDialog.dismiss()
                if(!response.isSuccessful){
                    Toast.makeText(requireActivity(), "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        with(bodyAngsuran){
                            if (response.body()?.angsuran != null){
                                lblKategori.text = response.body()?.angsuran?.namaKategori
                                tvNominalBayar.text = response.body()?.angsuran?.totalPayment?.toInt().formatCurrency()
                                btnBayar.setOnClickListener {
                                    createPayment(response.body()?.angsuran!!)
                                }
                            }else{
                                cardView5.visibility = View.GONE
                            }
                        }

                        with(bodyLunas){
                            if (response.body()?.lunas != null){
                                lblKategori.text = response.body()?.lunas?.namaKategori
                                tvNominalBayar.text = response.body()?.lunas?.totalPayment?.toInt().formatCurrency()
                                btnBayar.setOnClickListener {
                                    bayarLunas(response.body()?.lunas!!)
                                }
                            }else{
                                cardView5.visibility = View.GONE
                            }
                        }

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

            override fun onFailure(call: Call<DashboardHomeResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(requireActivity(), t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setRecyclerViewAngsuran(riwayatAngsuran: List<Angsuran>?) = with(binding.bodyAngsuran) {
        adapterAngsuran = RiwayatAngsuranAdapter(requireActivity(), arrayListOf(),false, object: RiwayatAngsuranAdapter.OnItemClickListener{
            override fun onItemClick(item: Angsuran) {

            }
        })
        recyclerView.adapter =  adapterAngsuran
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.setHasFixedSize(true)

        adapterAngsuran.setList(riwayatAngsuran!!)

    }

    private fun setRecylerViewLunas(riwayatLunas: List<Lunas>?) = with(binding.bodyLunas) {
        adapterLunas = RiwayatLunasAdapter(requireActivity(), arrayListOf(), object: RiwayatLunasAdapter.OnItemClickListener{
            override fun onItemClick(item: Lunas) {

            }
        })
        recyclerView.adapter =  adapterLunas
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.setHasFixedSize(true)

        adapterLunas.setList(riwayatLunas!!)

    }

    private fun bayarLunas(lunas: Lunas?) {
        if (lunas?.paymentUrl?.isNotEmpty()!!){
            val intent = Intent(requireActivity(), WebViewActivity::class.java)
            intent.putExtra("urlPayment", lunas.paymentUrl)
            startActivity(intent)
        }
    }

    private fun createPayment(angsuran: Angsuran) {
        loadingDialog = DialogLoading(requireActivity(),
            "Mohon tunggu, sedang membuat pembayaran",false).build()

        loadingDialog.show()
        val apiClient = ApiClient.client(requireActivity())?.create(ApiInterface::class.java)
        val call = apiClient?.createPaymentLunas(userData.nama.toString(),userData.id.toString(),
            angsuran.idKategoriSpp!!,angsuran.totalPayment!!,angsuran.periodeBayar!!,angsuran.orderId!!)
        call?.enqueue(object : Callback<TransactionLunasResponse> {
            override fun onResponse(call: Call<TransactionLunasResponse>, response: Response<TransactionLunasResponse>) {
                loadingDialog.dismiss()
                if (response.isSuccessful && response.body()?.success == true) {
                    val data = response.body()?.data
                    val intent = Intent(requireActivity(), WebViewActivity::class.java)
                    intent.putExtra("urlPayment", data?.paymentUrl)
                    startActivity(intent)
                } else {
                    Log.e("Upload", "Failed: ${response.body()?.message}")
                }
            }

            override fun onFailure(call: Call<TransactionLunasResponse>, t: Throwable) {
                loadingDialog.dismiss()
            }
        })
    }

    private fun initArtikel() = with(binding) {
        binding.loadingView.visibility = View.VISIBLE
        val apiClient = ApiClient.client(requireActivity())?.create(ApiInterface::class.java)
        val call = apiClient?.getArticles()
        call?.enqueue(object: Callback<ArtikelResponse> {
            override fun onResponse(call: Call<ArtikelResponse>, response: Response<ArtikelResponse>) {
                binding.loadingView.visibility = View.GONE

                if(!response.isSuccessful){
                    Toast.makeText(requireActivity(), "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        val listArtikel = response.body()?.data!!
                        sliderAdapter = SliderAdapter(listArtikel)
                        viewPager.adapter = sliderAdapter

                        viewPager.clipToPadding = false
                        viewPager.clipChildren = false
                        viewPager.offscreenPageLimit = 3
                        viewPager.getChildAt(0).overScrollMode = ViewPager2.OVER_SCROLL_NEVER

                        val compositePageTransformer = CompositePageTransformer()
                        compositePageTransformer.addTransformer(MarginPageTransformer(40))
                        compositePageTransformer.addTransformer { page, position ->
                            val r = 1 - abs(position)
                            page.scaleY = 0.85f + r * 0.15f
                        }
                        viewPager.setPageTransformer(compositePageTransformer)

                        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                        }.attach()
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(requireActivity(), response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<ArtikelResponse>, t: Throwable) {
                binding.loadingView.visibility = View.GONE
                Toast.makeText(requireActivity(), t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun hideLayoutTab() = with(binding) {
        bodyAngsuran.main.visibility = View.GONE
        bodyLunas.main.visibility = View.GONE
    }

    private fun disableButton() = with(binding) {
        btnAngsuran.background = getDrawable(requireActivity(),R.drawable.btn_tab_inactive)
        btnAngsuran.setTextColor(Color.parseColor("#54333333"))

        btnLunas.background = getDrawable(requireActivity(),R.drawable.btn_tab_inactive)
        btnLunas.setTextColor(Color.parseColor("#54333333"))

    }
}