package com.example.bangkit_2024_bpaai.AdvancedTesting.di

import android.content.Context
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.NewsRepository
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.local.room.NewsDatabase
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.getApiService()
        val database = NewsDatabase.getInstance(context)
        val dao = database.newsDao()
        return NewsRepository.getInstance(apiService, dao)
    }
}