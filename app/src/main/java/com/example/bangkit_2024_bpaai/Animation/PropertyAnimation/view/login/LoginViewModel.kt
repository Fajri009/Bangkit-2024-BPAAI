package com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.view.login

import androidx.lifecycle.*
import com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.data.UserRepository
import com.example.bangkit_2024_bpaai.Animation.PropertyAnimation.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}