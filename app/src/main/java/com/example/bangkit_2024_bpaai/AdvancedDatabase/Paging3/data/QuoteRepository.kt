package com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.database.QuoteDatabase
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.network.ApiService
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.network.QuoteResponseItem

class QuoteRepository(private val quoteDatabase: QuoteDatabase, private val apiService: ApiService) {
//    suspend fun getQuote(): List<QuoteResponseItem> {
//        return apiService.getQuote(1, 5)
//    }

    @OptIn(ExperimentalPagingApi::class)
    fun getQuote(): LiveData<PagingData<QuoteResponseItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5 // mengatur jumlah data yang diambil per halamannya
            ),
            remoteMediator = QuoteRemoteMediator(quoteDatabase, apiService),
            pagingSourceFactory = {
//                QuotePagingSource(apiService)
                quoteDatabase.quoteDao().getAllQuote()
            }
        ).liveData
    }
}