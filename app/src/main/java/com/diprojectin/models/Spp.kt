package com.diprojectin.models

import com.google.gson.annotations.SerializedName

data class Spp(
	@field:SerializedName("nilai_spp")
	val nilaiSpp: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)