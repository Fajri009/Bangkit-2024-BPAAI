package com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.database.QuoteDatabase
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.database.RemoteKeys
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.network.*

@OptIn(ExperimentalPagingApi::class)
class QuoteRemoteMediator(
    private val database: QuoteDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, QuoteResponseItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH // supaya data selalu ter-refresh

        // untuk memperbarui data dengan interval tertentu (semisal 1 jam)
//        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
//        return if (System.currentTimeMillis() - database.lastUpdated() >= cacheTimeout) {
//            InitializeAction.SKIP_INITIAL_REFRESH
//        } else {
//            InitializeAction.LAUNCH_INITIAL_REFRESH
//        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, QuoteResponseItem>
    ): MediatorResult {
        // menentukan halaman yang di-request berdasarkan loadType
        val page = when (loadType) {
            // Apabila aplikasi pertama kali dijalankan dengan inisialisasi LAUNCH_INITIAL_REFRESH,
            // getRemoteKeyClosestToCurrentPosition akan bernilai null, sehingga page berisi INITIAL_PAGE_INDEX bernilai 1
            LoadType.REFRESH ->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        return try {
            val responseData = apiService.getQuote(page, state.config.pageSize)

            val endOfPaginationReached = responseData.isEmpty()

            database.withTransaction {
                // Langkah pertama yaitu menghapus semua data, jika LoadType bertipe REFRESH,
                // tentu hal ini perlu dilakukan jika Anda ingin mendapatkan data yang terbaru saja.
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.quoteDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.quoteDao().insertQuote(responseData)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

    // untuk mengambil data dari RemoteKeys
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, QuoteResponseItem>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, QuoteResponseItem>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, QuoteResponseItem>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }
}