package com.diprojectin.network.responses

import com.diprojectin.models.Artikel
import com.google.gson.annotations.SerializedName

data class ArtikelResponse(

    @field:SerializedName("data")
	val data: List<Artikel>? = null,

    @field:SerializedName("success")
	val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)