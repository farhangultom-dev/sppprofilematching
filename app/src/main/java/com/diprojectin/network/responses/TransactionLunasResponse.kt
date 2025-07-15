package com.diprojectin.network.responses

import com.diprojectin.models.TransactionLunas
import com.google.gson.annotations.SerializedName

data class TransactionLunasResponse(

	@field:SerializedName("data")
	val data: TransactionLunas? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)