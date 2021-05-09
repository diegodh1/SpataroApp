package com.example.spataroapp.presentation.splash_screen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spataroapp.data.entities.ApiLoginResponse
import com.example.spataroapp.data.entities.User
import com.example.spataroapp.data.entities.UserLogin
import com.example.spataroapp.data.repository.Repository
import com.google.gson.Gson
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    val user:MutableLiveData<User> = MutableLiveData()
    val message:MutableLiveData<String> = MutableLiveData()
    init {
        message.value = ""
        checkIsLoggedUser()
    }
    fun checkIsLoggedUser(){
        viewModelScope.launch {
            val users = repository.getLoggedUser()
            if(users.isNotEmpty()){
                val json = UserLogin(users.last().user.id_usuario, users.last().user.password)
                val result = repository.login(json) as? ApiLoginResponse
                when(result?.message){
                    "ok" -> {
                        delay(2000L)
                        message.value = "success"
                    }
                    else ->{
                        message.value = result?.message
                    }
                }
            }
            else{
                message.value = "not logged"
            }
        }
    }

}