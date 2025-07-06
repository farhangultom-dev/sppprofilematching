package com.diprojectin.models

import com.google.gson.annotations.SerializedName

data class Jurusan(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)