package com.example.spataroapp.presentation.order_screen

import androidx.lifecycle.ViewModel
import com.example.spataroapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(repository: Repository): ViewModel(){

    init {

    }
}