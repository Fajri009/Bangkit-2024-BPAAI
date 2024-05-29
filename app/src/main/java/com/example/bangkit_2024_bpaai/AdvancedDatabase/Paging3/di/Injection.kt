package com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.di

import android.content.Context
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.data.QuoteRepository
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.database.QuoteDatabase
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.network.ApiConfig

object Injection {
    fun provideRepository(context: Context): QuoteRepository {
        val database = QuoteDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return QuoteRepository(database, apiService)
    }
}