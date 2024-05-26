package com.example.bangkit_2024_bpaai.AdvancedTesting.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.local.room.NewsDao
import com.example.bangkit_2024_bpaai.AdvancedTesting.data.remote.retrofit.ApiService
import com.example.bangkit_2024_bpaai.AdvancedTesting.utils.DataDummy
import com.example.bangkit_2024_bpaai.AdvancedTesting.utils.MainDispatcherRule
import com.example.bangkit_2024_bpaai.AdvancedTesting.utils.getOrAwaitValue
import com.example.bangkit_2024_bpaai.AdvancedTesting.utils.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*

@ExperimentalCoroutinesApi
class NewsRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var apiService: ApiService
    private lateinit var newsDao : NewsDao
    private lateinit var newsRepository: NewsRepository

    @Before
    fun setUp() {
        apiService = FakeApiService()
        newsDao = FakeNewsDao()
        newsRepository = NewsRepository(apiService, newsDao)
    }

    @Test
    fun `when getHeadlineNews Should Not Null`() = runTest {
        val expectedNews = DataDummy.generateDummyNewsResponse()
        val actualNews = newsRepository.getHeadlineNews()

        // gunakan observeForTesting jika repo aslinya menggunakan emit
        // jika tidak, hanya akan mendapatkan nilai pertama, yakni Result.Loading
        actualNews.observeForTesting {
            Assert.assertNotNull(actualNews)
            Assert.assertEquals(
                expectedNews.articles.size,
                (actualNews.value as Result.Success).data.size
            )
        }
    }

    @Test
    fun `when saveNews Should Exist in getBookmarkedNews`() = runTest {
        val sampleNews = DataDummy.generateDummyNewsEntity()[0]
        newsDao.saveNews(sampleNews)
        val actualNews = newsRepository.getBookmarkedNews().getOrAwaitValue()

        Assert.assertTrue(actualNews.contains(sampleNews))
        Assert.assertTrue(newsRepository.isNewsBookmarked(sampleNews.title).getOrAwaitValue())
    }

    @Test
    fun `when deleteNews Should Not Exist in getBookmarkedNews`() = runTest {
        val sampleNews = DataDummy.generateDummyNewsEntity()[0]
        newsRepository.saveNews(sampleNews)
        newsRepository.deleteNews(sampleNews.title)

        val actualNews = newsRepository.getBookmarkedNews().getOrAwaitValue()
        Assert.assertFalse(actualNews.contains(sampleNews))
        Assert.assertFalse(newsRepository.isNewsBookmarked(sampleNews.title).getOrAwaitValue())
    }
}