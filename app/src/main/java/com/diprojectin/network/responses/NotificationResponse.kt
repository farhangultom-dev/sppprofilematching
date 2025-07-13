package com.diprojectin.network.responses

import com.diprojectin.models.Notification
import com.google.gson.annotations.SerializedName

data class NotificationResponse(

    @field:SerializedName("data")
	val data: List<Notification>? = null,

    @field:SerializedName("success")
	val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)