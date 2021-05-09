package com.example.spataroapp.presentation.home_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spataroapp.data.entities.User
import com.example.spataroapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    val user: MutableLiveData<User> = MutableLiveData()
    init {
        getLoggedUser()
    }

    fun getLoggedUser(){
        viewModelScope.launch {
            val users = repository.getLoggedUser()
            user.value = users.last().user
        }
    }
}