package com.diprojectin.models

import com.google.gson.annotations.SerializedName

data class Siswa(

	@field:SerializedName("nama_kelas")
	val namaKelas: String? = "",

	@field:SerializedName("nisn")
	val nisn: String? = null,

	@field:SerializedName("status_siswa")
	val statusSiswa: String? = null,

	@field:SerializedName("tahun_masuk")
	val tahunMasuk: String? = null,

	@field:SerializedName("users_detail_id")
	val usersDetailId: String? = null,

	@field:SerializedName("jurusan")
	val jurusan: String? = "",

	@field:SerializedName("tahun_ajaran")
	val tahunAjaran: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null,

	@field:SerializedName("is_admin")
	val isAdmin: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("tahun_keluar")
	val tahunKeluar: String? = "",

	@field:SerializedName("is_deleted")
	val isDeleted: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("tempat_lahir")
	val tempatLahir: String? = null,

	@field:SerializedName("kelas")
	val kelas: String? = "",

	@field:SerializedName("photo_profile_path")
	val photoProfile: String? = "",

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("created_date")
	val createdDate: String? = null,

	@field:SerializedName("jenis_kelamin")
	val jenisKelamin: String? = null,

	@field:SerializedName("tanggal_lahir")
	val tanggalLahir: String? = null,

	@field:SerializedName("nomor_telpon")
	val nomorTelpon: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("pendapatan_orang_tua")
	val pendapatanOrangTua: String? = null,

	@field:SerializedName("prestasi_akademik")
	val prestasiAkademik: String? = null,

	@field:SerializedName("jumlah_tanggungan_orang_tua")
	val jumlahTanggunganOrangTua: String? = null,

	@field:SerializedName("keterlibatan_dalam_ekskul")
	val keterlibatanDalamEkskul: String? = null
)