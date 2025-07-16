package com.diprojectin.models

import com.google.gson.annotations.SerializedName

data class Angsuran(

	@field:SerializedName("jumlah_angsuran")
	val jumlahAngsuran: String? = null,

	@field:SerializedName("nama_siswa")
	val namaSiswa: String? = null,

	@field:SerializedName("status_pembayaran")
	val statusPembayaran: String? = null,

	@field:SerializedName("periode_bayar")
	val periodeBayar: String? = null,

	@field:SerializedName("id_kategori_spp")
	val idKategoriSpp: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("created_date")
	val createdDate: String? = null,

	@field:SerializedName("pembayaran_ke")
	val pembayaranKe: String? = null,

	@field:SerializedName("order_id")
	val orderId: String? = null,

	@field:SerializedName("total_payment")
	val totalPayment: String? = null,

	@field:SerializedName("id_siswa")
	val idSiswa: String? = null,

	@field:SerializedName("nama_kategori")
	val namaKategori: String? = null,

	@field:SerializedName("nisn")
	val nisn: String? = null,

	@field:SerializedName("grade")
	val grade: String? = null,

	@field:SerializedName("nama_kelas")
	val namaKelas: String? = null,

	@field:SerializedName("jurusan")
	val jurusan: String? = null,
)