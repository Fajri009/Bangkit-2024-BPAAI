package com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.view

import android.content.Context
import androidx.lifecycle.*
import com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.data.UserRepository
import com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.di.Injection
import com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.view.login.LoginViewModel
import com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.view.main.MainViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}