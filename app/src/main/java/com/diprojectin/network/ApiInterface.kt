package com.diprojectin.network

import com.diprojectin.models.Kelas
import com.diprojectin.network.responses.ArtikelResponse
import com.diprojectin.network.responses.DashboardHomeAdminResponse
import com.diprojectin.network.responses.DashboardHomeResponse
import com.diprojectin.network.responses.GenericResponse
import com.diprojectin.network.responses.JurusanResponse
import com.diprojectin.network.responses.KelasByUserResponse
import com.diprojectin.network.responses.KelasResponse
import com.diprojectin.network.responses.LoginResponse
import com.diprojectin.network.responses.NotificationResponse
import com.diprojectin.network.responses.RiwayatTransactionsResponse
import com.diprojectin.network.responses.SiswaResponse
import com.diprojectin.network.responses.SppResponse
import com.diprojectin.network.responses.TransactionLunasResponse
import com.diprojectin.network.responses.UploadFileResponse
import okhttp3.MultipartBody
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

    @FormUrlEncoded
    @POST("add-spp")
    fun addSpp(
        @Field("nama") nama: String,
        @Field("nilai_spp") nilaiSpp: String
    ): Call<GenericResponse>

    @FormUrlEncoded
    @POST("edit-spp")
    fun updateSpp(
        @Field("id") id: String,
        @Field("nama") nama: String,
        @Field("nilai_spp") nilaiSpp: String
    ): Call<GenericResponse>

    @FormUrlEncoded
    @POST("add-jurusan")
    fun addJurusan(
        @Field("nama_jurusan") namaJurusan: String
    ): Call<GenericResponse>

    @FormUrlEncoded
    @POST("edit-jurusan")
    fun editJurusan(
        @Field("id") id: String,
        @Field("nama_jurusan") namaJurusan: String
    ): Call<GenericResponse>

    @FormUrlEncoded
    @POST("add-article")
    fun addArticle(
        @Field("title") title: String,
        @Field("deskripsi") deskripsi: String,
        @Field("image_path") imagePath: String
    ): Call<GenericResponse>

    @POST("edit-kelas")
    fun editKelas(@Body kelasList: List<Kelas>): Call<GenericResponse>

    @FormUrlEncoded
    @POST("delete-kelas")
    fun deleteKelas(
        @Field("id") id: String
    ): Call<GenericResponse>

    @FormUrlEncoded
    @POST("delete-jurusan")
    fun deleteJurusan(
        @Field("id") id: String
    ): Call<GenericResponse>

    @FormUrlEncoded
    @POST("delete-spp")
    fun deleteSpp(
        @Field("id") id: String
    ): Call<GenericResponse>

    @FormUrlEncoded
    @POST("delete-siswa")
    fun deleteSiswa(
        @Field("id") id: String
    ): Call<GenericResponse>

    @FormUrlEncoded
    @POST("get-dashboard-home-siswa")
    fun getDashboardHomeSiswa(
        @Field("id_siswa") id: String
    ): Call<DashboardHomeResponse>

    @FormUrlEncoded
    @POST("get-riwayat")
    fun getRiwayat(
        @Field("id_siswa") id: String
    ): Call<RiwayatTransactionsResponse>

    @GET("get-riwayat-admin")
    fun getRiwayatAdmin(): Call<RiwayatTransactionsResponse>

    @GET("get-dashboard-master-kelas")
    fun getKelas(): Call<KelasResponse>

    @GET("get-dashboard-master-siswa")
    fun getSiswa(): Call<SiswaResponse>

    @GET("get-spp")
    fun getSpp(): Call<SppResponse>

    @GET("get-article")
    fun getNotification(): Call<NotificationResponse>

    @GET("get-article")
    fun getArticles(): Call<ArtikelResponse>

    @GET("get-kelas")
    fun getJustKelas(): Call<KelasResponse>

    @GET("get-dashboard-master-jurusan")
    fun getJurusan(): Call<JurusanResponse>

    @GET("dashboard-home-admin")
    fun getDashboardHomeAdmin(): Call<DashboardHomeAdminResponse>

    @FormUrlEncoded
    @POST("register-siswa")
    fun addSiswa(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("nisn") nisn: String,
        @Field("nama") nama: String,
        @Field("jenis_kelamin") jenisKelamin: String,
        @Field("tempat_lahir") tempatLahir: String,
        @Field("tanggal_lahir") tanggalLahir: String,
        @Field("alamat") alamat: String,
        @Field("nomor_telpon") nomorTelpon: String,
        @Field("tahun_ajaran") tahunAjaran: String,
        @Field("status_siswa") statusSiswa: String,
        @Field("tahun_masuk") tahunMasuk: String,
        @Field("tahun_keluar") tahunKeluar: String,
        @Field("pendapatan_orang_tua") pendapatanOrtu: String,
        @Field("prestasi_akademik") prestasiAkademik: String,
        @Field("jumlah_tanggungan_orang_tua") tanggunganOrtu: String,
        @Field("keterlibatan_dalam_ekskul") ekskul: String,
        @Field("kelas_id") kelasId: String,
        @Field("jurusan_id") jurusanId: String
    ): Call<GenericResponse>

    @FormUrlEncoded
    @POST("edit-siswa")
    fun editSiswa(
        @Field("id") id: String,
        @Field("username") username: String,
        @Field("nisn") nisn: String,
        @Field("nama") nama: String,
        @Field("jenis_kelamin") jenisKelamin: String,
        @Field("tempat_lahir") tempatLahir: String,
        @Field("tanggal_lahir") tanggalLahir: String,
        @Field("alamat") alamat: String,
        @Field("nomor_telpon") nomorTelpon: String,
        @Field("tahun_ajaran") tahunAjaran: String,
        @Field("status_siswa") statusSiswa: String,
        @Field("tahun_masuk") tahunMasuk: String,
        @Field("tahun_keluar") tahunKeluar: String,
        @Field("pendapatan_orang_tua") pendapatanOrtu: String,
        @Field("prestasi_akademik") prestasiAkademik: String,
        @Field("jumlah_tanggungan_orang_tua") tanggunganOrtu: String,
        @Field("keterlibatan_dalam_ekskul") ekskul: String,
        @Field("kelas_id") kelasId: String,
        @Field("jurusan_id") jurusanId: String
    ): Call<GenericResponse>

    @Multipart
    @POST("upload")
    fun uploadFile(
        @Part image: MultipartBody.Part
    ): Call<UploadFileResponse>

    @FormUrlEncoded
    @POST("update_photo")
    fun updatePhotoProfile(
        @Field("id") id: String,
        @Field("photo_url") photoUrl: String
    ): Call<GenericResponse>

    @FormUrlEncoded
    @POST("get-kelas-by-user")
    fun getKelasByUSer(
        @Field("user_detail_id") userDetailId: String,
        ): Call<KelasByUserResponse>

    @FormUrlEncoded
    @POST("create-payment")
    fun createPaymentLunas(
        @Field("nama_siswa") namaSiswa: String,
        @Field("id_siswa") idSiswa: String,
        @Field("id_kategori_spp") idKategoriSpp: String,
        @Field("total_payment") totalPayment: String,
        @Field("periode_bayar") periodeBayar: String,
        @Field("order_id") orderId:String
        ): Call<TransactionLunasResponse>

    @FormUrlEncoded
    @POST("create-angsuran")
    fun createPaymentAngsuran(
        @Field("nama_siswa") namaSiswa: String,
        @Field("id_siswa") idSiswa: String,
        @Field("id_kategori_spp") idKategoriSpp: String,
        @Field("total_payment") totalPayment: String,
        @Field("periode_bayar") periodeBayar: String,
        @Field("jumlah_angsuran") jumlahAngsuran: String,
        ): Call<GenericResponse>
}