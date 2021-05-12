package com.example.spataroapp.presentation.admin_user_screen

import android.net.Uri
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spataroapp.data.entities.ApiLoginResponse
import com.example.spataroapp.data.entities.User
import com.example.spataroapp.data.entities.UserApi
import com.example.spataroapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val documents: MutableLiveData<MutableList<String>> = MutableLiveData()

    //local variables
    val typeID: MutableLiveData<String> = MutableLiveData()
    val photo: MutableLiveData<Uri> = MutableLiveData()
    val photoBase64: MutableLiveData<String> = MutableLiveData()
    val userID: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()
    val name: MutableLiveData<String> = MutableLiveData()
    val lastName: MutableLiveData<String> = MutableLiveData()
    val email: MutableLiveData<String> = MutableLiveData()
    val admin: MutableLiveData<Boolean> = MutableLiveData()
    val seller: MutableLiveData<Boolean> = MutableLiveData()
    val isActive: MutableLiveData<Boolean> = MutableLiveData()
    val message: MutableLiveData<String> = MutableLiveData()
    val edit: MutableLiveData<Boolean> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        documents.value = mutableListOf()
        typeID.value = ""
        password.value = ""
        name.value = ""
        lastName.value = ""
        email.value = ""
        admin.value = true
        seller.value = false
        userID.value = ""
        isActive.value = true
        edit.value = false
        loading.value = false
        getAllDocuments()
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

    fun searchUser(user: Int) {
        loading.value = true
        viewModelScope.launch {
            val result = repository.searchUser(UserApi(user)) as ApiLoginResponse
            if (result.message == "ok") {
                userID.value = result.payload.id_usuario.toString()
                typeID.value = result.payload.id_tipo_doc
                name.value = result.payload.nombre
                lastName.value = result.payload.apellido
                email.value = result.payload.correo
                isActive.value = result.payload.activo
                photoBase64.value = result.payload.foto
                if (result.payload.menus.contains("ADMINISTRADOR")) {
                    admin.value = true
                }
                if (result.payload.menus.contains("VENDEDOR")) {
                    seller.value = true
                }
                loading.value = false
                edit.value = true
            } else {
                message.value = result.message
                cleanFields()
                loading.value = false
            }
        }
    }

    fun cleanFields() {
        password.value = ""
        name.value = ""
        lastName.value = ""
        email.value = ""
        admin.value = true
        seller.value = false
        userID.value = ""
        isActive.value = true
        edit.value = false
    }

    fun checkFields(): Boolean {
        when {
            userID.value == null || userID.value != null && (userID.value == "" || !userID.value!!.isDigitsOnly()) -> {
                message.value = "El ID de usuario no puede estar vacio y debe ser numérico"
                return false
            }
            edit.value == false && (password.value == "" || password.value!!.length < 3) -> {
                message.value = "La contraseña no puede estar vacia y debe ser mayor a 3 caracteres"
                return false
            }
            name.value == "" -> {
                message.value = "El nombre no puede estar vacio"
                return false
            }
            admin.value == false && seller.value == false -> {
                message.value = "Debe seleccionar al menos un perfil"
                return false
            }
            typeID.value == "" -> {
                message.value = "Debe seleccionar un tipo de documento"
                return false
            }
            email.value == "" ->{
                message.value = "El correo no puede estar vacio"
                return false
            }
        }
        return true
    }

    suspend fun insertUser(photo64: String): String {
        val menus = mutableListOf<String>()
        if (admin != null && admin.value == true) {
            menus.add("ADMINISTRADOR")
        }
        if (seller != null && seller.value == true) {
            menus.add("VENDEDOR")
        }
        val result = repository.createUser(
            UserApi(
                userID.value!!.toInt(),
                password.value!!,
                typeID.value!!,
                name.value!!,
                lastName.value!!,
                email.value!!,
                photo64,
                isActive.value!!,
                menus
            )
        ) as ApiLoginResponse
        return result.message
    }

    suspend fun editUser(photo64: String): String {
        val menus = mutableListOf<String>()
        if (admin != null && admin.value == true) {
            menus.add("ADMINISTRADOR")
        }
        if (seller != null && seller.value == true) {
            menus.add("VENDEDOR")
        }
        val result = repository.editUser(
            UserApi(
                userID.value!!.toInt(),
                password.value!!,
                typeID.value!!,
                name.value!!,
                lastName.value!!,
                email.value!!,
                photo64,
                isActive.value!!,
                menus
            )
        ) as ApiLoginResponse
        return result.message
    }
}