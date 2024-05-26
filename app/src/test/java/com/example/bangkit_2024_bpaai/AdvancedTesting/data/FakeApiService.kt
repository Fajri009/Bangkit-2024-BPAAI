package com.example.bangkit_2024_bpaai.AdvancedTesting.data

import com.example.bangkit_2024_bpaai.AdvancedTesting.TDD.data.remote.NewsResponse
import com.example.bangkit_2024_bpaai.AdvancedTesting.utils.DataDummy
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.remote.retrofit.ApiService

class FakeApiService : ApiService {
    private val dummyResponse = DataDummy.generateDummyNewsResponse()

    override suspend fun getNews(apiKey: String): NewsResponse {
        return dummyResponse
    }
}