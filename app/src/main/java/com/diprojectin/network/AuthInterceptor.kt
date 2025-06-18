package com.diprojectin.besti.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Interceptor untuk menambahkan header Authorization dengan token Bearer ke permintaan keluar.
 *
 * Interceptor ini mengambil token JWT dari Shared Preferences dan menambahkannya ke
 * header Authorization dari setiap permintaan.
 *
 * @param context Konteks aplikasi yang digunakan untuk mengakses Shared Preferences.
 */
class AuthInterceptor(context: Context) : Interceptor {
    val ctx = context

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val requestBuilder = chain.request().newBuilder()

            val token = "no token yet"
            requestBuilder.addHeader("Authorization", "Bearer $token")

            return chain.proceed(requestBuilder.build())
        } catch (e: Exception) {
            throw e
        }
    }
}