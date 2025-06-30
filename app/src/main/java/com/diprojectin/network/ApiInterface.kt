package com.diprojectin.network

import com.diprojectin.models.Kelas
import com.diprojectin.network.responses.GenericResponse
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

    @FormUrlEncoded
    @POST("add-kelas")
    fun addKelas(
        @Field("grade") grade: String,
        @Field("nama_kelas") namaKelas: String
    ): Call<GenericResponse>

    @POST("edit-kelas")
    fun editKelas(@Body kelasList: List<Kelas>): Call<GenericResponse>

    @FormUrlEncoded
    @POST("delete-kelas")
    fun deleteKelas(
        @Field("id") id: String
    ): Call<GenericResponse>

    @GET("get-dashboard-master-kelas")
    fun getKelas(): Call<KelasResponse>
}