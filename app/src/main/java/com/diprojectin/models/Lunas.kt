package com.diprojectin.models

import com.google.gson.annotations.SerializedName

data class Lunas(

	@field:SerializedName("payment_url")
	val paymentUrl: String? = null,

	@field:SerializedName("status_payment")
	val statusPayment: String? = null,

	@field:SerializedName("is_deleted")
	val isDeleted: String? = null,

	@field:SerializedName("angsuran_id")
	val angsuranId: String? = null,

	@field:SerializedName("periode_bayar")
	val periodeBayar: String? = null,

	@field:SerializedName("id_kategori_spp")
	val idKategoriSpp: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("created_date")
	val createdDate: String? = null,

	@field:SerializedName("order_id")
	val orderId: String? = null,

	@field:SerializedName("total_payment")
	val totalPayment: String? = null,

	@field:SerializedName("id_siswa")
	val idSiswa: String? = null,

	@field:SerializedName("nama_kategori")
	val namaKategori: String? = null,
)