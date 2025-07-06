package com.diprojectin.network.responses

import com.diprojectin.models.Siswa
import com.google.gson.annotations.SerializedName

data class SiswaResponse(

	@field:SerializedName("jumlah_wanita")
	val jumlahWanita: Int? = null,

	@field:SerializedName("jumlah_pria")
	val jumlahPria: Int? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("data")
	val data: List<Siswa>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)