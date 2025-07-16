package com.diprojectin.network.responses

import com.diprojectin.models.Angsuran
import com.diprojectin.models.Lunas
import com.google.gson.annotations.SerializedName

data class DashboardHomeResponse(

    @field:SerializedName("success")
	val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("riwayat_lunas")
	val riwayatLunas: List<Lunas>? = null,

    @field:SerializedName("angsuran")
	val angsuran: Angsuran? = null,

    @field:SerializedName("riwayat_angsuran")
	val riwayatAngsuran: List<Angsuran>? = null,

    @field:SerializedName("lunas")
	val lunas: Lunas? = null
)