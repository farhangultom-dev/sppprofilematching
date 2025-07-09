package com.diprojectin.network.responses

import com.google.gson.annotations.SerializedName

data class UploadFileResponse(

	@field:SerializedName("filename")
	val filename: String? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)
