package com.example.spataroapp.presentation.admin_client_screen

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spataroapp.data.entities.Address
import com.example.spataroapp.data.entities.ApiClientResponse
import com.example.spataroapp.data.entities.Client
import com.example.spataroapp.data.entities.Telefono
import com.example.spataroapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    //variables
    val documents: MutableLiveData<MutableList<String>> = MutableLiveData()
    val typeID: MutableLiveData<String> = MutableLiveData()
    val numDoc: MutableLiveData<String> = MutableLiveData()
    val name: MutableLiveData<String> = MutableLiveData()
    val lastName: MutableLiveData<String> = MutableLiveData()
    val email: MutableLiveData<String> = MutableLiveData()
    val address: MutableLiveData<String> = MutableLiveData()
    val tel: MutableLiveData<String> = MutableLiveData()
    val country: MutableLiveData<String> = MutableLiveData()
    val city: MutableLiveData<String> = MutableLiveData()
    val message: MutableLiveData<String> = MutableLiveData()
    val isEdit: MutableLiveData<Boolean> = MutableLiveData()

    //initialize
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

    //get all documents type
    fun getAllDocuments() {
        viewModelScope.launch {
            val result = repository.getAllDocuments() as MutableList<String>
            if (result.isNotEmpty()) {
                typeID.value = result.first()
                documents.value = result
            }
        }
    }

    //search the client by ID
    fun searchClient(clientID: Int) {
        viewModelScope.launch {
            val result = repository.searchClient(Client(clientID)) as ApiClientResponse
            if (result.message == "ok") {
                numDoc.value = result.payload.id_cliente.toString()
                typeID.value = result.payload.id_tipo_doc
                name.value = result.payload.nombre
                lastName.value = result.payload.apellido
                email.value = result.payload.correo
                if (result.payload.direcciones.isNotEmpty()) {
                    country.value = result.payload.direcciones.first().id_pais
                    city.value = result.payload.direcciones.first().id_ciudad
                    address.value = result.payload.direcciones.first().direccion
                }
                if (result.payload.telefonos.isNotEmpty()) {
                    tel.value = result.payload.telefonos.first().telefono
                }
                isEdit.value = true
            } else {
                message.value = result.message
            }
        }
    }

    //make request create_client
    fun createClient() {
        val isAllOk = checkFields()
        if (isAllOk) {
            viewModelScope.launch {
                val telefonos: MutableList<Telefono> = mutableListOf(
                    Telefono(
                        numDoc.value!!.toInt(),
                        country.value!!,
                        city.value!!,
                        tel.value!!
                    )
                )
                val direcciones: MutableList<Address> = mutableListOf(
                    Address(
                        numDoc.value!!.toInt(),
                        country.value!!,
                        city.value!!,
                        address.value!!
                    )
                )
                when {
                    isEdit.value!! -> {
                        val result = repository.editClient(
                            Client(
                                numDoc.value!!.toInt(),
                                typeID.value!!,
                                name.value!!,
                                lastName.value!!,
                                email.value!!,
                                direcciones,
                                telefonos
                            )
                        ) as ApiClientResponse
                        message.value = result.message
                        clearData()
                    }
                    else -> {
                        val result = repository.createClient(
                            Client(
                                numDoc.value!!.toInt(),
                                typeID.value!!,
                                name.value!!,
                                lastName.value!!,
                                email.value!!,
                                direcciones,
                                telefonos
                            )
                        ) as ApiClientResponse
                        message.value = result.message
                        clearData()
                    }
                }
            }
        }
    }

    //clear data
    fun clearData() {
        name.value = ""
        numDoc.value = ""
        lastName.value = ""
        email.value = ""
        country.value = ""
        city.value = ""
        address.value = ""
        tel.value = ""
        message.value = ""
        isEdit.value = false
    }

    //check all the fields before make the request
    fun checkFields(): Boolean {
        when {
            numDoc.value == "" || !numDoc.value!!.isDigitsOnly() -> {
                message.value = "El número de documento debe ser numérico"
                return false
            }
            name.value == "" -> {
                message.value = "El nombre no puede estar vacio"
                return false
            }
            email.value == "" -> {
                message.value = "El correo no puede estar vacio"
                return false
            }
            country.value == "" -> {
                message.value = "El país no puede estar vacio"
                return false
            }
            city.value == "" -> {
                message.value = "La ciudad no puede estar vacia"
                return false
            }
            address.value == "" -> {
                message.value = "La dirección no puede estar vacia"
                return false
            }
            tel.value == "" -> {
                message.value = "El teléfono no puede estar vacio"
                return false
            }
        }
        return true
    }
}