package com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.database

import androidx.paging.PagingSource
import androidx.room.*
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.network.QuoteResponseItem

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuote(quote: List<QuoteResponseItem>)

    @Query("SELECT * FROM quote")
    fun getAllQuote(): PagingSource<Int, QuoteResponseItem>

    @Query("DELETE FROM quote")
    suspend fun deleteAll()
}