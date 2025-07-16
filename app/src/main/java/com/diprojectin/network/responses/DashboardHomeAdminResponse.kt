package com.diprojectin.network.responses

import com.diprojectin.models.PercentagePaymentItem
import com.diprojectin.models.TotalKelas
import com.diprojectin.models.TotalSiswa
import com.diprojectin.models.TotalTransaksi
import com.google.gson.annotations.SerializedName

data class DashboardHomeAdminResponse(

    @field:SerializedName("total_transaksi")
	val totalTransaksi: TotalTransaksi? = null,

    @field:SerializedName("percentage_payment")
	val percentagePayment: List<PercentagePaymentItem?>? = null,

    @field:SerializedName("success")
	val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("total_kelas")
	val totalKelas: TotalKelas? = null,

    @field:SerializedName("total_siswa")
	val totalSiswa: TotalSiswa? = null
)