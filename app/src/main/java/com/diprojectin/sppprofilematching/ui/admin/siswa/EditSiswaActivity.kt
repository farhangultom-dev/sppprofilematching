package com.diprojectin.sppprofilematching.ui.admin.siswa

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.diprojectin.models.Jurusan
import com.diprojectin.models.Kelas
import com.diprojectin.models.Siswa
import com.diprojectin.network.ApiClient
import com.diprojectin.network.ApiInterface
import com.diprojectin.network.responses.GenericResponse
import com.diprojectin.network.responses.JurusanResponse
import com.diprojectin.network.responses.KelasResponse
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityAddSiswaBinding
import com.diprojectin.sppprofilematching.databinding.ActivityEditSiswaBinding
import com.diprojectin.sppprofilematching.utils.DialogLoading
import com.diprojectin.sppprofilematching.utils.DialogUtils
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import java.util.Calendar

class EditSiswaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditSiswaBinding
    private lateinit var listKelas: List<Kelas>
    private lateinit var listJurusan: List<Jurusan>
    private lateinit var dialogConfirm: Dialog
    private lateinit var loadingDialog: Dialog
    private lateinit var user: Siswa
    private var statePengisian = 1
    private var nisn = ""
    private var namaSiswa = ""
    private var phoneNumber = ""
    private var jenisKelamin = ""
    private var tempatLahir = ""
    private var tanggalLahir = ""
    private var alamat = ""
    private var tahunAjaran = ""
    private var tahunMasuk = ""
    private var statusSiswa = ""
    private var kelasAnswer = ""
    private var jurusanAnswer = ""
    private var prestasiAkademik = ""
    private var jumlahTanggungan = ""
    private var keterlibatanEkskul = ""
    private var pendapatanOrtu = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSiswaBinding.inflate(layoutInflater)
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
        user = Gson().fromJson(intent.getStringExtra("data_siswa").toString(), Siswa::class.java)

        nisn = user.nisn!!
        namaSiswa = user.nama!!
        phoneNumber = user.nomorTelpon!!
        jenisKelamin = user.jenisKelamin!!
        tempatLahir = user.tempatLahir!!
        tanggalLahir = user.tanggalLahir!!
        alamat = user.alamat!!
        tahunAjaran = user.tahunAjaran!!
        tahunMasuk = user.tahunMasuk!!
        statusSiswa = user.statusSiswa!!
        kelasAnswer = user.kelas!!
        jurusanAnswer = user.jurusan!!
        prestasiAkademik = user.prestasiAkademik!!
        jumlahTanggungan = user.jumlahTanggunganOrangTua!!
        pendapatanOrtu = user.pendapatanOrangTua!!
        keterlibatanEkskul = user.keterlibatanDalamEkskul!!
        jumlahTanggungan = user.jumlahTanggunganOrangTua!!
        prestasiAkademik = user.prestasiAkademik!!

        getListKelas()
        getListJurusan()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        with(binding) {
            appBar.tvTitleHeader.text = getString(R.string.tambah_siswa)
            appBar.btnBack.setOnClickListener {
                onBackPressed()
            }

            with(pengisianDataSiswa){
                textView9.visibility = View.GONE
                etPassword.visibility = View.GONE
                textView13.visibility = View.GONE

                etNisn.setText(nisn)
                etNisn.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        nisn = s.toString()
                    }

                })

                etNamaSiswa.setText(namaSiswa)
                etNamaSiswa.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        namaSiswa = s.toString()
                    }

                })

                etTempatLahir.setText(tempatLahir)
                etTempatLahir.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        tempatLahir = s.toString()
                    }

                })

                etAlamat.setText(alamat)
                etAlamat.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        alamat = s.toString()
                    }

                })

                etNoTelpon.setText(phoneNumber)
                etNoTelpon.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun afterTextChanged(s: Editable?) {
                        phoneNumber = s.toString()
                    }

                })

                if (jenisKelamin == "pria"){
                    radioMale.isChecked = true
                }else{
                    radioFemale.isChecked = true
                }

                genderGroup.setOnCheckedChangeListener { group, checkedId ->
                    val selectedRadioButton = findViewById<RadioButton>(checkedId)
                    val selectedText = selectedRadioButton.text.toString()

                    if (selectedText == "Laki-laki"){
                        jenisKelamin = "pria"
                    }else{
                        jenisKelamin = "wanita"
                    }
                }

                if (statusSiswa == "Aktif"){
                    radioAktif.isChecked = true
                }else{
                    radioTidakAktif.isChecked = true
                }

                statusGroup.setOnCheckedChangeListener { group, checkedId ->
                    val selectedRadioButton = findViewById<RadioButton>(checkedId)
                    val selectedText = selectedRadioButton.text.toString()
                    statusSiswa = selectedText
                }

                etTanggalLahir.setText(tanggalLahir)
                etTanggalLahir.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_UP) {
                        val drawableEnd = etTanggalLahir.compoundDrawablesRelative[2] // drawableEnd is index 2
                        if (drawableEnd != null) {
                            val drawableWidth = drawableEnd.bounds.width()
                            val touchAreaStart = etTanggalLahir.width - etTanggalLahir.paddingEnd - drawableWidth
                            if (event.x >= touchAreaStart) {
                                // DrawableEnd was tapped
                                showDatePicker()
                                return@setOnTouchListener true
                            }
                        }
                    }
                    false
                }

                spinnerTahunAjaran.text = tahunAjaran
                spinnerTahunAjaran.setOnSpinnerItemSelectedListener<String>  { _, _, _, newItem ->
                    tahunAjaran = newItem
                }

                spinnerTahunMasuk.text = tahunMasuk
                spinnerTahunMasuk.setOnSpinnerItemSelectedListener<String>  { _, _, _, newItem ->
                    tahunMasuk = newItem
                }

                spinnerPendapatanOrangtua.text = pendapatanOrtu
                spinnerPendapatanOrangtua.setOnSpinnerItemSelectedListener<String>  { _, _, _, newItem ->
                    pendapatanOrtu = newItem
                }

                spinnerPrestasiAkadaemik.text = prestasiAkademik
                spinnerPrestasiAkadaemik.setOnSpinnerItemSelectedListener<String>  { _, _, _, newItem ->
                    prestasiAkademik = newItem
                }

                spinnerKeterlibatanDalamEkskul.text = keterlibatanEkskul
                spinnerKeterlibatanDalamEkskul.setOnSpinnerItemSelectedListener<String>  { _, _, _, newItem ->
                    keterlibatanEkskul = newItem
                }

                spinnerJumlahTanggunganOrangTua.text = jumlahTanggungan
                spinnerJumlahTanggunganOrangTua.setOnSpinnerItemSelectedListener<String>  { _, _, _, newItem ->
                    jumlahTanggungan = newItem
                }
            }

            btnCancel.setOnClickListener {
                if (statePengisian == 1){
                    val title = dialogConfirm.findViewById<TextView>(R.id.tv_title)
                    val subtitle = dialogConfirm.findViewById<TextView>(R.id.tv_subtitle)
                    val icon = dialogConfirm.findViewById<ImageView>(R.id.icon_dialog)
                    val btnSimpan = dialogConfirm.findViewById<TextView>(R.id.btnSimpan)
                    val btnCancel = dialogConfirm.findViewById<TextView>(R.id.btnCancel)

                    title.text = "Batal"
                    subtitle.text = "Apakah anda yakin untuk batal menambah data?"
                    btnSimpan.text = "Ya"
                    btnCancel.text = "Tidak"
                    icon.setImageResource(R.drawable.ic_simpan_data)

                    btnSimpan.setOnClickListener {
                        dialogConfirm.dismiss()
                        finish()
                    }

                    btnCancel.setOnClickListener {
                        dialogConfirm.dismiss()
                    }

                    dialogConfirm.show()
                }else{
                    statePengisian = 1
                    icPositionPengisian.setImageResource(R.drawable.ic_pengisian_data_siswa)

                    btnSimpan.text = "Lanjut"
                    btnCancel.text = "Batal"

                    pengisianDataSiswa.mainView.visibility = View.VISIBLE
                    pengisianDataKelas.mainView.visibility = View.GONE
                }
            }

            btnSimpan.setOnClickListener {
                if (statePengisian == 1){
                    statePengisian = 2
                    icPositionPengisian.setImageResource(R.drawable.ic_pengisian_data_kelas)

                    btnSimpan.text = "Simpan"
                    btnCancel.text = "Kembali"

                    pengisianDataSiswa.mainView.visibility = View.GONE
                    pengisianDataKelas.mainView.visibility = View.VISIBLE
                }else{
                    val title = dialogConfirm.findViewById<TextView>(R.id.tv_title)
                    val subtitle = dialogConfirm.findViewById<TextView>(R.id.tv_subtitle)
                    val icon = dialogConfirm.findViewById<ImageView>(R.id.icon_dialog)
                    val btnSimpan = dialogConfirm.findViewById<TextView>(R.id.btnSimpan)
                    val btnCancel = dialogConfirm.findViewById<TextView>(R.id.btnCancel)

                    title.text = "Simpan Data"
                    subtitle.text = "Apakah anda yakin akan simpan data siswa?"
                    btnSimpan.text = "Simpan"
                    btnCancel.text = "Batal"
                    icon.setImageResource(R.drawable.ic_simpan_data)

                    btnSimpan.setOnClickListener {
                        dialogConfirm.dismiss()

                        if (!validateForm()) return@setOnClickListener

                        sendData()
                    }

                    btnCancel.setOnClickListener {
                        dialogConfirm.dismiss()
                    }

                    dialogConfirm.show()
                }
            }
        }
    }

    private fun validateForm(): Boolean {

        if (nisn == "" || namaSiswa == "" || phoneNumber == "" ||
            jenisKelamin == "" || tempatLahir == "" ||
            tanggalLahir == "" || alamat == "" || tahunAjaran == "" ||
            tahunMasuk == "" || statusSiswa == "" || kelasAnswer == "" ||
            jurusanAnswer == "" || prestasiAkademik == "" || jumlahTanggungan == "" ||
            keterlibatanEkskul == "" || pendapatanOrtu == ""){

            Toast.makeText(
                this@EditSiswaActivity,
                "Mohon isi semua data yang di perlukan",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true
    }

    private fun showDatePicker() {
        with(binding.pengisianDataSiswa){
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                etTanggalLahir.context,
                { _, year, month, dayOfMonth ->
                    val selectedDate = "%02d-%02d-%04d".format(dayOfMonth, month + 1, year)
                    etTanggalLahir.setText(selectedDate)
                    tanggalLahir = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
    }

    private fun sendData() {
        loadingDialog = DialogLoading(this@EditSiswaActivity,
            "Mohon tunggu, sedang melakukan login",false).build()

        loadingDialog.show()
        val apiClient = ApiClient.client(this)?.create(ApiInterface::class.java)
        val call = apiClient?.editSiswa(
            user.usersDetailId!!,
            nisn,
            nisn,
            namaSiswa,
            jenisKelamin,
            tempatLahir,
            tanggalLahir,
            alamat,
            phoneNumber,
            tahunAjaran,
            statusSiswa,
            tahunMasuk,
            "-",
            pendapatanOrtu,
            prestasiAkademik,
            jumlahTanggungan,
            keterlibatanEkskul,
            kelasAnswer,
            jurusanAnswer
        )
        call?.enqueue(object: Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                loadingDialog.dismiss()
                if(!response.isSuccessful){
                    Toast.makeText(this@EditSiswaActivity, "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        val intent = Intent(this@EditSiswaActivity, MasterSiswaActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        finish()
                        return
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(this@EditSiswaActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Toast.makeText(this@EditSiswaActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getListKelas() {
        val apiClient = ApiClient.client(this)?.create(ApiInterface::class.java)
        val call = apiClient?.getJustKelas()
        call?.enqueue(object: Callback<KelasResponse> {
            override fun onResponse(call: Call<KelasResponse>, response: Response<KelasResponse>) {
                if(!response.isSuccessful){
                    Toast.makeText(this@EditSiswaActivity, "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        listKelas = response.body()?.data!!

                        with(binding.pengisianDataKelas){
                            spinnerKelas.setItems(listKelas.map { "${it.grade} ${it.nama} " })

                            spinnerKelas.text = kelasAnswer
                            spinnerKelas.setOnSpinnerItemSelectedListener<String>  { _, _, newIndex, _ ->
                                kelasAnswer = listKelas[newIndex].id!!
                            }
                        }
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(this@EditSiswaActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<KelasResponse>, t: Throwable) {
                Toast.makeText(this@EditSiswaActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getListJurusan() {
        val apiClient = ApiClient.client(this)?.create(ApiInterface::class.java)
        val call = apiClient?.getJurusan()
        call?.enqueue(object: Callback<JurusanResponse> {
            override fun onResponse(call: Call<JurusanResponse>, response: Response<JurusanResponse>) {
                if(!response.isSuccessful){
                    Toast.makeText(this@EditSiswaActivity, "Terjadi kesalahan, lakukan beberapa saat", Toast.LENGTH_SHORT).show()
                    return
                }

                if(response.isSuccessful){
                    if (response.body()?.success == true){
                        listJurusan = response.body()?.data!!

                        with(binding.pengisianDataKelas){
                            spinnerJurusan.text = jurusanAnswer
                            spinnerJurusan.setItems(listJurusan.map { "${it.nama}" })

                            spinnerJurusan.setOnSpinnerItemSelectedListener<String>  { _, _, newIndex, _ ->
                                jurusanAnswer = listJurusan[newIndex].id!!
                            }
                        }
                    }

                    if (response.body()?.success == false){
                        Toast.makeText(this@EditSiswaActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            }

            override fun onFailure(call: Call<JurusanResponse>, t: Throwable) {
                Toast.makeText(this@EditSiswaActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onBackPressed() {
        val title = dialogConfirm.findViewById<TextView>(R.id.tv_title)
        val subtitle = dialogConfirm.findViewById<TextView>(R.id.tv_subtitle)
        val icon = dialogConfirm.findViewById<ImageView>(R.id.icon_dialog)
        val btnSimpan = dialogConfirm.findViewById<TextView>(R.id.btnSimpan)
        val btnCancel = dialogConfirm.findViewById<TextView>(R.id.btnCancel)

        title.text = "Batal"
        subtitle.text = "Apakah anda yakin untuk batal menambah data?"
        btnSimpan.text = "Ya"
        btnCancel.text = "Tidak"
        icon.setImageResource(R.drawable.ic_simpan_data)

        btnSimpan.setOnClickListener {
            dialogConfirm.dismiss()
            super.onBackPressed()
            finish()
        }

        btnCancel.setOnClickListener {
            dialogConfirm.dismiss()
        }

        dialogConfirm.show()
    }
}