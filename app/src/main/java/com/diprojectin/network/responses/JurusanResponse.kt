package com.diprojectin.network.responses

import com.diprojectin.models.Jurusan
import com.google.gson.annotations.SerializedName

data class JurusanResponse(

    @field:SerializedName("data")
	val data: List<Jurusan>? = null,

    @field:SerializedName("success")
	val success: Boolean? = null,

    @field:SerializedName("message")
val message: String? = null
)