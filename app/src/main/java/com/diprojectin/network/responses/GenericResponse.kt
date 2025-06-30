package com.diprojectin.network.responses

import com.diprojectin.models.KelasCount
import com.diprojectin.models.Kelas
import com.google.gson.annotations.SerializedName

data class GenericResponse(
	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,
)