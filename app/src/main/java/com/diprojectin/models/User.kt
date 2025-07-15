package com.diprojectin.models

import com.google.gson.annotations.SerializedName

data class User(

	@field:SerializedName("nama_kelas")
	val namaKelas: String? = null,

	@field:SerializedName("nisn")
	val nisn: String? = null,

	@field:SerializedName("status_siswa")
	val statusSiswa: String? = null,

	@field:SerializedName("tahun_masuk")
	val tahunMasuk: String? = null,

	@field:SerializedName("jurusan")
	val jurusan: String? = null,

	@field:SerializedName("tahun_ajaran")
	val tahunAjaran: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("is_admin")
	val isAdmin: String? = null,

	@field:SerializedName("tahun_keluar")
	val tahunKeluar: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("tempat_lahir")
	val tempatLahir: String? = null,

	@field:SerializedName("kelas")
	val kelas: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String? = null,

	@field:SerializedName("tanggal_lahir")
	val tanggalLahir: String? = null,

	@field:SerializedName("nomor_telpon")
	val nomorTelpon: String? = null,

	@field:SerializedName("photo_profile_path")
	var photoProfile: String? = "",

	@field:SerializedName("pendapatan_orang_tua")
	val pendapatanOrtu: String? = null,

	@field:SerializedName("prestasi_akademik")
	val prestasiAkademik: String? = null,

	@field:SerializedName("jumlah_tanggungan_orang_tua")
	val jumlahTanggunganOrtu: String? = null,

	@field:SerializedName("keterlibatan_dalam_ekskul")
	var keterlibatanEkskul: String? = "",
)