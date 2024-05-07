package com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.view.main

import androidx.lifecycle.*
import com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.data.UserRepository
import com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}