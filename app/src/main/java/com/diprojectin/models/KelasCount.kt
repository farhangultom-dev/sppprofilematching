package com.diprojectin.models

import com.google.gson.annotations.SerializedName

data class KelasCount(

	@field:SerializedName("jumlah_pria")
	val jumlahPria: Int? = null,

	@field:SerializedName("jumlah_wanita")
	val jumlahWanita: Int? = null,

	@field:SerializedName("grade")
	val grade: String? = null,

	@field:SerializedName("total_siswa")
	val totalSiswa: Int? = null
)