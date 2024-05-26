package com.example.bangkit_2024_bpaai.AdvancedTesting.data.remote.retrofit

import com.example.bangkit_2024_bpaai.AdvancedTesting.TDD.data.remote.NewsResponse
import retrofit2.http.*

interface ApiService {
    @GET("top-headlines?country=id&category=science")
    suspend fun getNews(@Query("apiKey") apiKey: String): NewsResponse
}