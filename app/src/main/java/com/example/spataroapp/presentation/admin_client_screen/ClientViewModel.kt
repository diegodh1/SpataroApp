package com.example.spataroapp.presentation.admin_client_screen

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spataroapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(private val repository: Repository):ViewModel(){

    val documents: MutableLiveData<MutableList<String>> = MutableLiveData()
    val typeID: MutableLiveData<String> = MutableLiveData()
    val numDoc:MutableLiveData<String> = MutableLiveData()
    val name:MutableLiveData<String> = MutableLiveData()
    val lastName:MutableLiveData<String> = MutableLiveData()
    val email:MutableLiveData<String> = MutableLiveData()
    val address:MutableLiveData<String> = MutableLiveData()
    val tel:MutableLiveData<String> = MutableLiveData()
    val country:MutableLiveData<String> = MutableLiveData()
    val city:MutableLiveData<String> = MutableLiveData()
    val message:MutableLiveData<String> = MutableLiveData()
    init {
        getAllDocuments()
        name.value = ""
        numDoc.value = ""
        lastName.value = ""
        email.value = ""
        country.value = ""
        city.value = ""
        address.value = ""
        tel.value = ""
        message.value = ""
    }

    fun getAllDocuments() {
        viewModelScope.launch {
            val result = repository.getAllDocuments() as MutableList<String>
            if (result.isNotEmpty()) {
                typeID.value = result.first()
                documents.value = result
            }
        }
    }

    fun checkFields():Boolean{
        when{
            numDoc.value == "" || !numDoc.value!!.isDigitsOnly() ->{
                message.value = "El número de documento debe ser numérico"
                return false
            }
            name.value == "" ->{
                message.value = "El nombre no puede estar vacio"
                return false
            }
            email.value == "" ->{
                message.value = "El correo no puede estar vacio"
                return false
            }
            country.value == "" ->{
                message.value = "El país no puede estar vacio"
                return false
            }
            city.value == "" ->{
                message.value = "La ciudad no puede estar vacia"
                return false
            }
            address.value == "" ->{
                message.value = "La dirección no puede estar vacia"
                return false
            }
            tel.value == "" ->{
                message.value = "El teléfono no puede estar vacio"
                return false
            }
        }
        return true
    }
}