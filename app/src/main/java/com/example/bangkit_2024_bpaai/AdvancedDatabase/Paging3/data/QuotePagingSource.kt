package com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.network.ApiService
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.network.QuoteResponseItem

class QuotePagingSource(private val apiService: ApiService) : PagingSource<Int, QuoteResponseItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, QuoteResponseItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getQuote(position, params.loadSize)
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1, // digunakan jika kita melakukan scroll ke atas
                nextKey = if (responseData.isEmpty()) null else position + 1 // untuk scroll ke bawah
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    // untuk mengetahui kapan mengambil data terbaru dan menggantikan data yang sudah tampil
    override fun getRefreshKey(state: PagingState<Int, QuoteResponseItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}