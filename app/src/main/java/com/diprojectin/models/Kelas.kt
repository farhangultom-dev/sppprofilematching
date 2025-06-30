package com.diprojectin.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Kelas(

	@field:SerializedName("nama")
	var nama: String? = null,

	@field:SerializedName("grade")
	val grade: String? = null,

	@field:SerializedName("id")
	val id: String? = null
) : Parcelable