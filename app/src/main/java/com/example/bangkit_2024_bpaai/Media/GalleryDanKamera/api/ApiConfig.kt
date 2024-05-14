package com.example.bangkit_2024_bpaai.Media.GalleryDanKamera.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// untuk menghubungkan ApiService dengan Retrofit
object ApiConfig {
    fun getApiService(): ApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
    /*
        HttpLoggingInterceptor : Menampilkan log permintaan dan informasi tanggapan selama pemanggilan endpoint.
        OkHTTPClient : Membangun koneksi dengan client.
        GsonConverterFactory : Menambahkan converter factory untuk object serialization dan deserialization.
     */
}