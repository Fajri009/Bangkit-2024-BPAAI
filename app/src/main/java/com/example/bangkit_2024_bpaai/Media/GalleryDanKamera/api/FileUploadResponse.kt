package com.example.bangkit_2024_bpaai.Media.GalleryDanKamera.api

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String
)