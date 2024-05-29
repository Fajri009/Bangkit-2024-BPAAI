package com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.network

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "quote")
data class QuoteResponseItem(
    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("author")
    val author: String? = null,

    @field:SerializedName("en")
    val en: String? = null
)