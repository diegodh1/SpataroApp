package com.example.spataroapp.presentation.splash_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spataroapp.data.entities.UserLogin
import com.example.spataroapp.data.repository.Repository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    init {
        getDocuments()
    }
    fun getDocuments() {
        viewModelScope.launch {
            val json = UserLogin(1144182874, "cristiano1994")
            val result = repository.login(json)
            Log.i("RESULTADO", result.toString())
        }
    }

}