package com.example.spataroapp.presentation.login_screen

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spataroapp.data.entities.ApiLoginResponse
import com.example.spataroapp.data.entities.User
import com.example.spataroapp.data.entities.UserApi
import com.example.spataroapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val user: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()
    val errorUser: MutableLiveData<Boolean> = MutableLiveData()
    val errorPassword: MutableLiveData<Boolean> = MutableLiveData()
    val message: MutableLiveData<String> = MutableLiveData()

    init {
        user.value = ""
        password.value = ""
        message.value = ""
        errorUser.value = false
        errorPassword.value = false
        deleteRecords()
    }
    //delete old records
    fun deleteRecords(){
        viewModelScope.launch {
            repository.deleteUser()
        }
    }
    //make http request
    fun makeRequest() {
        viewModelScope.launch {
            val json = UserApi(user.value!!.toInt(), password.value!!)
            val result = repository.login(json) as? ApiLoginResponse
            when (result?.message) {
                "ok" -> {
                    val tempUser = User(
                        result.payload.id_usuario,
                        password.value!!,
                        result.payload.id_tipo_doc,
                        result.payload.nombre,
                        result.payload.apellido,
                        result.payload.correo,
                        result.payload.foto
                    )
                    repository.insertUser(tempUser)
                    message.value = "success"
                }
                else -> {
                    message.value = result?.message
                }
            }
        }
    }

    //check fields and call makeRequest
    fun requestLogin() {
        errorUser.value = false
        errorPassword.value = false
        when {
            user.value == null || user.value == ""  -> {
                errorUser.value = true
            }
            !user.value!!.isDigitsOnly() ->{
                errorUser.value = true
            }
            password.value == null || password.value == "" -> {
                errorPassword.value = true
            }
            else -> {
                errorUser.value = false
                errorPassword.value = false
                //make request
                makeRequest()
            }
        }
    }
}