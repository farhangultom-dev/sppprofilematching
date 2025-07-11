package com.diprojectin.network.responses

import com.diprojectin.models.Spp
import com.google.gson.annotations.SerializedName

data class SppResponse(

    @field:SerializedName("data")
	val data: List<Spp>? = null,

    @field:SerializedName("success")
	val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null

)