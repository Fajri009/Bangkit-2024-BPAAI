package com.example.bangkit_2024_bpaai.AdvancedTesting.utils

import com.example.bangkit_2024_bpaai.AdvancedTesting.TDD.data.remote.ArticlesItem
import com.example.bangkit_2024_bpaai.AdvancedTesting.TDD.data.remote.NewsResponse
import com.example.bangkit_2024_bpaai.AdvancedTesting.TDD.data.remote.Source
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.local.entity.NewsEntity

object DataDummy {
    fun generateDummyNewsEntity(): List<NewsEntity> {
        val newsList = ArrayList<NewsEntity>()
        for (i in 0..10) {
            val news = NewsEntity(
                "Title $i",
                "2022-02-22T22:22:22Z",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "https://www.dicoding.com/",
            )
            newsList.add(news)
        }
        return newsList
    }

    fun generateDummyNewsResponse(): NewsResponse {
        val newsList = ArrayList<ArticlesItem>()
        for (i in 0..10) {
            val news = ArticlesItem(
                "2022-02-22T22:22:22Z",
                "author $i",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "description $i",
                Source("name", "id"),
                "Title $i",
                "https://www.dicoding.com/",
                "content $i",
            )
            newsList.add(news)
        }
        return NewsResponse(newsList.size, newsList, "Success")
    }
}