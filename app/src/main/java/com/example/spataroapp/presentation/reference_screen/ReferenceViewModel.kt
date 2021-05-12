package com.example.spataroapp.presentation.reference_screen

import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spataroapp.data.entities.ApiClientResponse
import com.example.spataroapp.data.entities.ApiRequestReference
import com.example.spataroapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ReferenceViewModel @Inject constructor(private val repository: Repository): ViewModel(){
    val fileType:MutableLiveData<String> = MutableLiveData()
    val excelFile: MutableLiveData<Uri> = MutableLiveData()
    val fileBase64:MutableLiveData<String> = MutableLiveData()
    val message:MutableLiveData<String> = MutableLiveData()
    val loading:MutableLiveData<Boolean> = MutableLiveData()
    init {
        fileType.value = "UND"
        fileBase64.value = ""
        loading.value = false
        message.value = ""
    }

    fun uploadFile(){
        viewModelScope.launch{
            loading.value = true
            val result = repository.saveReferences(ApiRequestReference(fileBase64.value!!, fileType.value!!)) as ApiClientResponse
            message.value = result.message
            loading.value = false
        }
    }
}