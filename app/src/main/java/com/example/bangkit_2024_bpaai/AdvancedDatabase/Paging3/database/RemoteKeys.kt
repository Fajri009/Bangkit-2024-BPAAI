package com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.database

import androidx.room.*

// untuk menyimpan informasi tentang halaman terbaru yang diminta dari server
@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)