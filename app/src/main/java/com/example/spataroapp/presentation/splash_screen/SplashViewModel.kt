package com.example.spataroapp.presentation.splash_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.spataroapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    init {
        prueba()
    }
    fun prueba(){
        val temp = repository.prueba()
        Log.i("HILT_PRUEBA", temp)
    }
}