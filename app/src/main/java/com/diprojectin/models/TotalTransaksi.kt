package com.diprojectin.models

import com.google.gson.annotations.SerializedName

data class TotalTransaksi(

	@field:SerializedName("total_success_payment")
	val totalSuccessPayment: String? = null,

	@field:SerializedName("total_siswa_bayar")
	val totalSiswaBayar: String? = null
)