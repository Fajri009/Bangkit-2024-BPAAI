package com.example.bangkit_2024_bpaai.AdvancedTesting.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.local.entity.NewsEntity
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.local.room.NewsDao

class FakeNewsDao : NewsDao {
    private var newsData = mutableListOf<NewsEntity>()

    override fun getBookmarkedNews(): LiveData<List<NewsEntity>> {
        val observableNews = MutableLiveData<List<NewsEntity>>()
        observableNews.value = newsData
        return observableNews
    }

    override suspend fun saveNews(news: NewsEntity) {
        newsData.add(news)
    }

    override suspend fun deleteNews(newsTitle: String) {
        newsData.removeIf { it.title == newsTitle }
    }

    override fun isNewsBookmarked(title: String): LiveData<Boolean> {
        val observableExistence = MutableLiveData<Boolean>()
        observableExistence.value = newsData.any { it.title == title }
        return observableExistence
    }
}