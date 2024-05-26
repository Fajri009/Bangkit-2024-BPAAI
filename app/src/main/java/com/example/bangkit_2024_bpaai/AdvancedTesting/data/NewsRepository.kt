package com.example.bangkit_2024_bpaai.AdvancedTesting.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.local.entity.NewsEntity
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.local.room.NewsDao
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.remote.retrofit.ApiService
import com.example.bangkit_2024_bpaai.AdvancedTesting.util.wrapEspressoIdlingResource
import com.example.bangkit_2024_bpaai.BuildConfig

class NewsRepository(
    private val apiService: ApiService,
    private val newsDao: NewsDao
) {
    fun getHeadlineNews(): LiveData<Result<List<NewsEntity>>> = liveData {
        // wrapEspressoIdlingResource untuk menandai kapan idlingResource memulai melakukan increment untuk menunggu sampai terakhir melakukan decrement ketika proses sudah selesai
        wrapEspressoIdlingResource {
            try {
                val response = apiService.getNews(BuildConfig.TESTING_API_KEY)
                val articles = response.articles
                val newsList = articles.map { article ->
                    NewsEntity(
                        article.title,
                        article.publishedAt,
                        article.urlToImage,
                        article.url
                    )
                }
                emit(Result.Success(newsList))
            } catch (e: Exception) {
                Log.d("NewsRepository", "getHeadlineNews: ${e.message.toString()} ")
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    fun getBookmarkedNews(): LiveData<List<NewsEntity>> {
        return newsDao.getBookmarkedNews()
    }

    suspend fun saveNews(news: NewsEntity) {
        newsDao.saveNews(news)
    }

    suspend fun deleteNews(title: String) {
        newsDao.deleteNews(title)
    }

    fun isNewsBookmarked(title: String): LiveData<Boolean> {
        return newsDao.isNewsBookmarked(title)
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null
        fun getInstance(
            apiService: ApiService,
            newsDao: NewsDao
        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService, newsDao)
            }.also { instance = it }
    }
}