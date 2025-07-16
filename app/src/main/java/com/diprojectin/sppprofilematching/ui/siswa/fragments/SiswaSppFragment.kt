package com.diprojectin.sppprofilematching.ui.siswa.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.RadioButton
import android.widget.Toast
import com.diprojectin.models.Spp
import com.diprojectin.models.User
import com.diprojectin.network.ApiClient
import com.diprojectin.network.ApiInterface
import com.diprojectin.network.responses.GenericResponse
import com.diprojectin.network.responses.TransactionLunasResponse
import com.diprojectin.network.responses.SppResponse
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.FragmentSiswaProfileBinding
import com.diprojectin.sppprofilematching.databinding.FragmentSiswaSppBinding
import com.diprojectin.sppprofilematching.ui.admin.HomeActivity
import com.diprojectin.sppprofilematching.ui.siswa.SiswaHomeActivity
import com.diprojectin.sppprofilematching.ui.siswa.WebViewActivity
import com.diprojectin.sppprofilematching.utils.CalculatingProfileMatching
import com.diprojectin.sppprofilematching.utils.DialogLoading
import com.diprojectin.sppprofilematching.utils.Helper.formatCurrency
import com.diprojectin.sppprofilematching.utils.JenisKriteria
import com.diprojectin.sppprofilematching.utils.Kriteria
import com.diprojectin.sppprofilematching.utils.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import kotlin.math.floor

class SiswaSppFragment : Fragment() {
    private lateinit var binding: FragmentSiswaSppBinding
    private lateinit var listSpp: List<Spp>
    private lateinit var loadingDialog: Dialog
    private lateinit var userData: User
    private lateinit var calculateProfileMatching: CalculatingProfileMatching
    private var kategoriSppAnswer = ""
    private var periodeBayarAnswer = ""
    private var tagihanAnswer = ""
    private var nominalBayar = ""
    private var jenisPembayaran = ""
    private var jumlahAngsuran = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSiswaSppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataSpp()
        initData()
        initView()
    }

    private fun initData() {
        userData = SharedPrefManager(requireActivity()).getUser()!!

        val prestasiAkademik = resources.getStringArray(R.array.prestasi)
        val jumlahTanggunganOrtu = resources.getStringArray(R.array.tanggungan_orang_tua)
        val pendapatanOrtu = resources.getStringArray(R.array.pendapatan_orang_tua)
        val keterlibatanEkskul = resources.getStringArray(R.array.keterlibatan_dalam_ekskul)

        val listKriteria: ArrayList<Kriteria> = ArrayList()

        if (listKriteria.isEmpty()){
            for ((index, item) in prestasiAkademik.withIndex()) {
                if (item == userData.prestasiAkademik){
                    listKriteria.add(Kriteria(nama = "Prestasi Akademik", jenis = JenisKriteria.CORE,index + 1,2))
                    break
                }
            }

            for ((index, item) in jumlahTanggunganOrtu.withIndex()) {
                if (item == userData.jumlahTanggunganOrtu){
                    listKriteria.add(Kriteria(nama = "Jumlah Tanggungan Orang Tua", jenis = JenisKriteria.CORE,index + 1,2))
                    break
                }
            }

            for ((index, item) in pendapatanOrtu.withIndex()) {
                if (item == userData.pendapatanOrtu){
                    listKriteria.add(Kriteria(nama = "Pendapatan Orang Tua", jenis = JenisKriteria.SECONDARY,index + 1,3))
                    break
                }
            }

            for ((index, item) in keterlibatanEkskul.withIndex()) {
                if (item == userData.keterlibatanEkskul){
                    listKriteria.add(Kriteria(nama = "Keterlibatan Ekstrakurikuler", jenis = JenisKriteria.SECONDARY,index + 1,3))
                    break
                }
            }
        }

        calculateProfileMatching = CalculatingProfileMatching(listKriteria)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() = with(binding) {
        tvNama.text = userData.nama
        tvNisnDanKelas.text = "${userData.nisn} - ${userData.kelas} ${userData.jurusan} ${userData.namaKelas}"

        etPeriodeBayar.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = etPeriodeBayar.compoundDrawablesRelative[2] // drawableEnd is index 2
                if (drawableEnd != null) {
                    val drawableWidth = drawableEnd.bounds.width()
                    val touchAreaStart = etPeriodeBayar.width - etPeriodeBayar.paddingEnd - drawableWidth
                    if (event.x >= touchAreaStart) {
                        // DrawableEnd was tapped
                        showDatePicker()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        jenisPembayaranGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = requireActivity().findViewById<RadioButton>(checkedId)

            if (tagihanAnswer == ""){
                Toast.makeText(requireActivity(), "Pilih kategori spp terlebih dahulu", Toast.LENGTH_SHORT).show()
                selectedRadioButton.isChecked = false
                return@setOnCheckedChangeListener
            }else{
                selectedRadioButton.isChecked = true
            }

            val selectedText = selectedRadioButton.text.toString()
            if (selectedText == "Bayar Lunas"){
                jenisPembayaran = "1"
                lblJumlahPembayaran.visibility = View.GONE
                spinnerJumlahAngsuran.visibility = View.GONE
                jumlahAngsuran = ""
                spinnerJumlahAngsuran.text = ""

                val bayarValue = floor(hitungTotalSetelahDiskon(tagihanAnswer.toDouble())).toInt()
                tvNominalBayar.text = bayarValue.formatCurrency()
                nominalBayar = bayarValue.toString()

            }else{
                jenisPembayaran = "2"
                lblJumlahPembayaran.visibility = View.VISIBLE
                spinnerJumlahAngsuran.visibility = View.VISIBLE
            }
        }

        spinnerJumlahAngsuran.setOnSpinnerItemSelectedListener<String> { _, _, newIndex, _ ->
            jumlahAngsuran = spinnerJumlahAngsuran.text.toString()
            val bayarValue = floor(hitungTotalSetelahDiskon(tagihanAnswer.toDouble())).toInt()
            val calculateAngsuran = bayarValue / jumlahAngsuran.toInt()
            tvNominalBayar.text = calculateAngsuran.formatCurrency()
            nominalBayar = calculateAngsuran.toString()
        }

        btnBayar.setOnClickListener {
            if (!isValidated()){
                return@setOnClickListener
            }

            if (jenisPembayaran == "1"){
                createPaymentLunas()
            }

            if (jenisPembayaran == "2"){
                createPaymentAngsuran()
            }
        }
    }

    private fun createPaymentLunas() {
        loadingDialog = DialogLoading(requireActivity(),
            "Mohon tunggu, sedang membuat pembayaran",false).build()

        loadingDialog.show()
        val apiClient = ApiClient.client(requireActivity())?.create(ApiInterface::class.java)
        val call = apiClient?.createPaymentLunas(userData.nama.toString(),userData.id.toString(),
            kategoriSppAnswer,nominalBayar,periodeBayarAnswer,"")
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

    private fun createPaymentAngsuran() {
        loadingDialog = DialogLoading(requireActivity(),
            "Mohon tunggu, sedang membuat pembayaran",false).build()

        loadingDialog.show()
        val apiClient = ApiClient.client(requireActivity())?.create(ApiInterface::class.java)
        val call = apiClient?.createPaymentAngsuran(userData.nama.toString(),userData.id.toString(),
            kategoriSppAnswer,nominalBayar,periodeBayarAnswer,jumlahAngsuran)
        call?.enqueue(object : Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                loadingDialog.dismiss()
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(
                        requireActivity(),
                        "Berhasil membuat angsuran",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(requireActivity(), SiswaHomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireActivity(), response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                Toast.makeText(requireActivity(), t.message, Toast.LENGTH_SHORT).show()
                loadingDialog.dismiss()
            }
        })
    }

    private fun isValidated(): Boolean {
        if (kategoriSppAnswer == "" || periodeBayarAnswer == "" ||
            tagihanAnswer == "" || nominalBayar == "" || jenisPembayaran == "")
        {
            Toast.makeText(
                requireActivity(),
                "Lengkapi data pembayaran terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        if (jenisPembayaran == "2"){
            if (jumlahAngsuran == "") {
                Toast.makeText(
                    requireActivity(),
                    "Lengkapi data pembayaran terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }

        return true
    }

    private fun showDatePicker() {
        with(binding){
            val calendar = Calendar.getInstance()
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentYear = calendar.get(Calendar.YEAR)

            val dialogView = layoutInflater.inflate(R.layout.dialog_month_year_picker, null)
            val monthPicker = dialogView.findViewById<NumberPicker>(R.id.month_picker)
            val yearPicker = dialogView.findViewById<NumberPicker>(R.id.year_picker)

            val months = arrayOf(
                "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                "Juli", "Agustus", "September", "Oktober", "November", "Desember"
            )

            monthPicker.minValue = 0
            monthPicker.maxValue = 11
            monthPicker.displayedValues = months
            monthPicker.value = currentMonth

            yearPicker.minValue = 2000
            yearPicker.maxValue = currentYear
            yearPicker.value = currentYear

            AlertDialog.Builder(requireActivity())
                .setTitle("Pilih Bulan dan Tahun")
                .setView(dialogView)
                .setPositiveButton("OK") { _, _ ->
                    val selectedMonth = monthPicker.value
                    val selectedYear = yearPicker.value
                    val selectedDate = "${months[selectedMonth]} $selectedYear"
                    binding.etPeriodeBayar.setText(selectedDate)
                    periodeBayarAnswer = selectedDate
                }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    private fun getDataSpp() {
        loadingDialog = DialogLoading(requireActivity(), "Sedang mendapatkan data ...", false).build()
        loadingDialog.show()
        val apiClient = ApiClient.client(requireActivity())?.create(ApiInterface::class.java)
        val call = apiClient?.getSpp()
        call?.enqueue(object: Callback<SppResponse> {
            override fun onResponse(call: Call<SppResponse>, response: Response<SppResponse>) {
                loadingDialog.dismiss()

                if(!response.isSuccessful){
                    Toast.makeText(requireActivity(), "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        listSpp = response.body()?.data!!

                        with(binding){
                            spinnerKategoriSpp.setItems(listSpp.map { it.nama })

                            spinnerKategoriSpp.setOnSpinnerItemSelectedListener<String>  { _, _, newIndex, _ ->
                                kategoriSppAnswer = listSpp[newIndex].id!!
                                tagihanAnswer = listSpp[newIndex].nilaiSpp!!
                                tvJumlahTagihan.text = tagihanAnswer.toInt().formatCurrency()

                                val bayarValue = floor(hitungTotalSetelahDiskon(tagihanAnswer.toDouble())).toInt()
                                tvNominalBayar.text = bayarValue.formatCurrency()
                                nominalBayar = bayarValue.toString()
                            }
                        }
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(requireActivity(), response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<SppResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(requireActivity(), t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun hitungDiskonPersentase(nilaiAkhir: Double): Double {
        return when {
            nilaiAkhir < 3.0 -> 0.0
            nilaiAkhir < 4.0 -> 0.05 // 5%
            else -> 0.10 // 10%
        }
    }

    private fun hitungTotalSetelahDiskon(totalPembayaran: Double): Double {
        val nilaiAkhir = calculateProfileMatching.hitungNilaiAkhir()
        val persentaseDiskon = hitungDiskonPersentase(nilaiAkhir)
        val nilaiDiskon = totalPembayaran * persentaseDiskon

        return (totalPembayaran - nilaiDiskon).coerceAtLeast(0.0)
    }

}