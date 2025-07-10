package com.diprojectin.models

import com.google.gson.annotations.SerializedName

data class KelasByUser(

	@field:SerializedName("nama_kelas")
	val namaKelas: String? = null,

	@field:SerializedName("grade")
	val grade: String? = null,

	@field:SerializedName("jurusan")
	val jurusan: String? = null
)