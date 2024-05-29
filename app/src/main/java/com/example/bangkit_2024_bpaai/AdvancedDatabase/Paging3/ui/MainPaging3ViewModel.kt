package com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.ui

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.data.QuoteRepository
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.di.Injection
import com.example.bangkit_2024_bpaai.AdvancedDatabase.Paging3.network.QuoteResponseItem
import kotlinx.coroutines.launch

class MainPaging3ViewModel(private val quoteRepository: QuoteRepository) : ViewModel() {
//    private val _quote = MutableLiveData<List<QuoteResponseItem>>()
    //    var quote: LiveData<List<QuoteResponseItem>> = _quote

//    fun getQuote() {
//        viewModelScope.launch {
//            _quote.postValue(quoteRepository.getQuote())
//        }
//    }

    val quote: LiveData<PagingData<QuoteResponseItem>> =
        quoteRepository.getQuote().cachedIn(viewModelScope)
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainPaging3ViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainPaging3ViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}