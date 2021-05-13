package com.example.spataroapp.presentation.order_screen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spataroapp.data.entities.Client
import com.example.spataroapp.data.entities.User
import com.example.spataroapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val clients: MutableLiveData<MutableList<Client>> = MutableLiveData()
    val clientID: MutableLiveData<String> = MutableLiveData()
    val clientList: MutableLiveData<MutableList<String>> = MutableLiveData()
    val message:MutableLiveData<String> = MutableLiveData()
    val date:MutableLiveData<String> = MutableLiveData()
    val user:MutableLiveData<User> = MutableLiveData()
    //information variables
    val docType: MutableLiveData<String> = MutableLiveData()
    val docNum: MutableLiveData<String> = MutableLiveData()
    val name: MutableLiveData<String> = MutableLiveData()
    val email: MutableLiveData<String> = MutableLiveData()
    init {
        clients.value = mutableListOf()
        clientList.value = mutableListOf()
        clientID.value = ""
        message.value = ""
        date.value = ""
        //initalizing information variables
        docType.value = ""
        docNum.value = ""
        name.value = ""
        email.value = ""
        //call functions
        getLoggedUser()
    }
    fun getLoggedUser(){
        viewModelScope.launch{
            val result = repository.getLoggedUser()
            if(result.isNotEmpty()){
                user.value = result.first().user
                Log.i("LOGGED", user.value.toString())
            }
        }
    }

    fun checkOrderFields():Boolean{
        when{
            clientID.value == "" ->{
                message.value = "Debe seleccionar un cliente"
                return false
            }
            date.value == "" ->{
                message.value = "Debe seleccionar una fecha"
                return false
            }
        }
        return true
    }

    fun searchClientByName(value: String){
        viewModelScope.launch {
            val result = repository.searchClientByName(
                Client(
                    nombre = value
                )
            ) as MutableList<Client>
            val temp = mutableListOf<String>()
            for (i in 0 until result.size) {
                temp.add(i.toString() + ". " + result[i].nombre + " " + result[i].apellido)
            }
            if(temp.isNotEmpty()){
                clientID.value = temp.first()
            }
            clients.value = result
            clientList.value = temp
        }
    }
}