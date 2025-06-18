package com.diprojectin.network

import com.diprojectin.network.responses.KelasResponse
import com.diprojectin.network.responses.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("get-dashboard-master-kelas")
    fun getKelas(): Call<KelasResponse>
}