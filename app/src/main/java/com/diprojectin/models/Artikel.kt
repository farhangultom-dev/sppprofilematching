package com.diprojectin.models

import com.google.gson.annotations.SerializedName

data class Artikel(

	@field:SerializedName("image_path")
	val imagePath: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("created_date")
	val createdDate: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)