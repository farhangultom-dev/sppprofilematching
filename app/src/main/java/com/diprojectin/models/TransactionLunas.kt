package com.diprojectin.models

import com.google.gson.annotations.SerializedName

data class TransactionLunas(

	@field:SerializedName("payment_url")
	val paymentUrl: String? = null,

	@field:SerializedName("periode_bayar")
	val periodeBayar: String? = null,

	@field:SerializedName("id_kategori_spp")
	val idKategoriSpp: String? = null,

	@field:SerializedName("order_id")
	val orderId: String? = null,

	@field:SerializedName("total_payment")
	val totalPayment: String? = null,

	@field:SerializedName("id_siswa")
	val idSiswa: String? = null
)