package com.example.bangkit_2024_bpaai.Media.GalleryDanKamera.api

import okhttp3.*
import retrofit2.http.*

// untuk menangani endpoint yang dibutuhkan
interface ApiService {
    @Multipart
    @POST("stories/guest")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): FileUploadResponse
}