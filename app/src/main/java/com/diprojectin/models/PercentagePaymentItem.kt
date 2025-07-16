package com.diprojectin.models

import com.google.gson.annotations.SerializedName

data class PercentagePaymentItem(

	@field:SerializedName("sudah_bayar")
	val sudahBayar: Int? = null,

	@field:SerializedName("kelas")
	val kelas: String? = null,

	@field:SerializedName("persen")
	val persen: Int? = null,

	@field:SerializedName("total_siswa")
	val totalSiswa: Int? = null
)