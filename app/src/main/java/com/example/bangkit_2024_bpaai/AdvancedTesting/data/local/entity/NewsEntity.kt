package com.example.bangkit_2024_bpaai.AdvancedTesting.data.local.entity

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "news")
class NewsEntity(
    @field:ColumnInfo(name = "title")
    @field:PrimaryKey
    val title: String,

    @field:ColumnInfo(name = "publishedAt")
    val publishedAt: String,

    @field:ColumnInfo(name = "urlToImage")
    val urlToImage: String? = null,

    @field:ColumnInfo(name = "url")
    val url: String? = null
) : Parcelable