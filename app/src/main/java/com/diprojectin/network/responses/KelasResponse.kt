package com.diprojectin.network.responses

import com.diprojectin.models.KelasCount
import com.diprojectin.models.Kelas
import com.google.gson.annotations.SerializedName

data class KelasResponse(

	@field:SerializedName("data")
	val data: List<Kelas>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data_count")
	val dataCount: List<KelasCount>? = null
)