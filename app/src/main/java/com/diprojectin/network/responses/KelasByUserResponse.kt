package com.diprojectin.network.responses

import com.diprojectin.models.KelasByUser
import com.google.gson.annotations.SerializedName

data class KelasByUserResponse(

    @field:SerializedName("data")
	val data: List<KelasByUser>? = null,

    @field:SerializedName("success")
	val success: Boolean? = null,

    @field:SerializedName("message")
	val message: String? = null
)