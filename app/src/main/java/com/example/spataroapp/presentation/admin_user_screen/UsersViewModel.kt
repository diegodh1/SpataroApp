package com.example.spataroapp.presentation.admin_user_screen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spataroapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    val documents: MutableLiveData<MutableList<String>> = MutableLiveData()
    val typeID: MutableLiveData<String> = MutableLiveData()
    val photo:MutableLiveData<Uri> = MutableLiveData()

    init{
        documents.value = mutableListOf()
        typeID.value = ""
        getAllDocuments()
    }

    fun getAllDocuments(){
        viewModelScope.launch {
            val result = repository.getAllDocuments() as MutableList<String>
            if(result.isNotEmpty()){
                typeID.value = result.first()
                documents.value = result
            }
        }
    }
}