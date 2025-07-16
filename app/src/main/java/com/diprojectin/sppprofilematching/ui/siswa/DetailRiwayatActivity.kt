package com.diprojectin.sppprofilematching.ui.siswa

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.diprojectin.models.Angsuran
import com.diprojectin.models.Lunas
import com.diprojectin.models.Siswa
import com.diprojectin.network.ApiClient
import com.diprojectin.sppprofilematching.R
import com.diprojectin.sppprofilematching.databinding.ActivityDetailRiwayatBinding
import com.diprojectin.sppprofilematching.utils.DialogUtils
import com.diprojectin.sppprofilematching.utils.Helper.convertToIndonesianDate
import com.diprojectin.sppprofilematching.utils.Helper.formatCurrency
import com.google.gson.Gson

class DetailRiwayatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRiwayatBinding
    private var modelData = ""
    private lateinit var angsuran: Angsuran
    private lateinit var lunas: Lunas
    private lateinit var dialogConfirm: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRiwayatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dialogConfirm = DialogUtils(this).build()
        initView()
    }

    private fun initView() = with(binding) {
        appBar.tvTitleHeader.text = "Detail Pembayaran"
        appBar.btnBack.setOnClickListener{ onBackPressed() }

        modelData = intent.getStringExtra("model_data").toString()

        if (modelData == "angsuran"){
            angsuran = Gson().fromJson(intent.getStringExtra("data_riwayat").toString(), Angsuran::class.java)

            tvNama.text = angsuran.namaSiswa
            tvNisnDanKelas.text = "${angsuran.nisn} - ${angsuran.grade} ${angsuran.jurusan} ${angsuran.namaKelas}"

            tvCategory.text = angsuran.namaKategori
            tvNominalBayar.text = angsuran.totalPayment?.toInt().formatCurrency()
            tvJenisPembayaran.text = "Angsuran"
            tvJumlahAngsuran.text = angsuran.jumlahAngsuran
            tvPembayaranKe.text = angsuran.pembayaranKe
            tvTanggalTransaksi.text = angsuran.createdDate
            tvPeriode.text = angsuran.periodeBayar

            when(angsuran.statusPembayaran){
                "PENDING" -> {
                    tvStatusPembayaran.text = "Menunggu Pembayaran"
                }
                "CANCELLED" -> {
                    tvStatusPembayaran.text = "Gagal Bayar"
                }
                "SUCCESS" -> {
                    tvStatusPembayaran.text = "Sudah Dibayar"
                }
            }
        }else{
            lunas = Gson().fromJson(intent.getStringExtra("data_riwayat").toString(), Lunas::class.java)

            tvNama.text = lunas.namaSiswa
            tvNisnDanKelas.text = "${lunas.nisn} - ${lunas.grade} ${lunas.jurusan} ${lunas.namaKelas}"

            tvCategory.text = lunas.namaKategori
            tvNominalBayar.text = lunas.totalPayment?.toInt().formatCurrency()
            tvJenisPembayaran.text = "Lunas"
            tvJumlahAngsuran.text = "0"
            tvPembayaranKe.text = "1"
            tvTanggalTransaksi.text = lunas.createdDate
            tvPeriode.text = lunas.periodeBayar

            when(lunas.statusPayment){
                "PENDING" -> {
                    tvStatusPembayaran.text = "Menunggu Pembayaran"
                }
                "CANCELLED" -> {
                    tvStatusPembayaran.text = "Gagal Bayar"
                }
                "SUCCESS" -> {
                    tvStatusPembayaran.text = "Sudah Dibayar"
                }
            }
        }

        btnCetakInvoice.setOnClickListener {
            val title = dialogConfirm.findViewById<TextView>(R.id.tv_title)
            val subtitle = dialogConfirm.findViewById<TextView>(R.id.tv_subtitle)
            val icon = dialogConfirm.findViewById<ImageView>(R.id.icon_dialog)
            val btnSimpan = dialogConfirm.findViewById<TextView>(R.id.btnSimpan)
            val btnCancel = dialogConfirm.findViewById<TextView>(R.id.btnCancel)

            title.text = "Cetak Invoice"
            subtitle.text = "Apakah anda yakin ingin cetak invoice?"
            btnSimpan.text = "Ya"
            btnCancel.text = "Tidak"
            icon.visibility = View.GONE

            btnSimpan.setOnClickListener {
                dialogConfirm.dismiss()
                cetakInvoice()
            }

            btnCancel.setOnClickListener {
                dialogConfirm.dismiss()
            }

            dialogConfirm.show()
        }
    }

    private fun cetakInvoice() {
        var url = ""
        url = if (modelData == "angsuran"){
            "${ApiClient.BASE_URL}download-invoice?order_id=${angsuran.orderId}&name=${angsuran.namaSiswa}&payment_date=${angsuran.createdDate.convertToIndonesianDate()}&category_spp=${angsuran.namaKategori}&jumlah_bayar=${angsuran.totalPayment?.toInt().formatCurrency()}&jumlah_angsuran=${angsuran.jumlahAngsuran}&pembayaran_ke=${angsuran.pembayaranKe}&last_update=${angsuran.createdDate}&jenis_pembayaran=Angsuran"
        }else "${ApiClient.BASE_URL}download-invoice?order_id=${lunas.orderId}&name=${lunas.namaSiswa}&payment_date=${lunas.createdDate.convertToIndonesianDate()}&category_spp=${lunas.namaKategori}&jumlah_bayar=${lunas.totalPayment?.toInt().formatCurrency()}&jumlah_angsuran=0&pembayaran_ke=1&last_update=${lunas.createdDate}&jenis_pembayaran=Lunas"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}