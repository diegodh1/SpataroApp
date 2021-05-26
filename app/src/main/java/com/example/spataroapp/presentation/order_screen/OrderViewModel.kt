package com.example.spataroapp.presentation.order_screen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spataroapp.data.entities.*
import com.example.spataroapp.data.entities.Order
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
    val orderID:MutableLiveData<Int> = MutableLiveData()
    val date:MutableLiveData<String> = MutableLiveData()
    val user:MutableLiveData<User> = MutableLiveData()
    //information variables
    val docType: MutableLiveData<String> = MutableLiveData()
    val docNum: MutableLiveData<String> = MutableLiveData()
    val name: MutableLiveData<String> = MutableLiveData()
    val email: MutableLiveData<String> = MutableLiveData()
    //references variables
    val references: MutableLiveData<MutableList<String>> = MutableLiveData()
    val referenceID:MutableLiveData<String> = MutableLiveData()

    val colors: MutableLiveData<MutableList<ApiReferenceColorTalla>> = MutableLiveData()
    val colorsAux:MutableLiveData<MutableList<String>> = MutableLiveData()
    val colorID:MutableLiveData<String> = MutableLiveData()
    val tallaID:MutableLiveData<String> = MutableLiveData()
    val tallas: MutableLiveData<MutableList<ApiReferenceSize>> = MutableLiveData()
    val tallasAux:MutableLiveData<MutableList<String>> = MutableLiveData()
    val quatity:MutableLiveData<String> = MutableLiveData()
    val items: MutableLiveData<MutableList<ApiItemOrder>> = MutableLiveData()
    init {
        clients.value = mutableListOf()
        clientList.value = mutableListOf()
        references.value = mutableListOf()
        colors.value = mutableListOf()
        colorsAux.value = mutableListOf()
        tallas.value = mutableListOf()
        tallasAux.value = mutableListOf()
        clientID.value = ""
        message.value = ""
        date.value = ""
        orderID.value = -1
        referenceID.value = ""
        colorID.value = ""
        tallaID.value = ""
        quatity.value = ""
        items.value = mutableListOf()
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

    fun getItemsOrder(order:Int){
        if(orderID.value!! > 0) {
            viewModelScope.launch {
                val result =
                    repository.getItemsOrder(ApiItemOrder(id_pedido = order)) as ApiItemsResponse
                items.value = result.payload.items!!
                Log.i("ITEMS", result.payload.toString())
            }
        }
    }

    fun checkItemOrderFields():Boolean{
        when{
            orderID.value!! < 0 ->{
                message.value = "Para agregar una referencia debe crear un pedido"
                return false
            }
            referenceID.value!! == "" ->{
                message.value = "Debe seleccionar una referencia"
                return false
            }
            colorID.value!! == "" ->{
                message.value = "Debe seleccionar un color"
                return false
            }
            tallaID.value!! == "" ->{
                message.value = "Debe seleccionar una talla"
                return false
            }
            quatity.value!! == "" ||  quatity.value!! == "0"->{
                message.value = "La cantidad debe ser mayor a 0"
                return false
            }
            else -> return true
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
    //make request
    fun makeRequest(){
        val ok = checkOrderFields()
        if(ok){
            viewModelScope.launch{
                val result = repository.createOrder(Order(docNum.value!!.toInt(),user.value!!.id_usuario,date.value!!)) as ApiOrderResponse
                message.value = result.message
                orderID.value = result.payload
            }
        }
    }

    fun addItemOrder() {
        val ok = checkItemOrderFields()
        if (ok) {
            val pos = tallaID.value!!.split(".")[0].toInt()
            val item = tallas.value!![pos]
            viewModelScope.launch {
                val result = repository.AddItemOrder(
                    ApiItemOrder(
                        orderID.value!!,
                        item.id_consecutivo,
                        quatity.value!!.toInt(),
                        item.precio
                    )
                ) as ApiLoginResponse
                message.value = result.message
            }
        }

    }

    //search referencia by name
    fun searchReferences(value: String) {
        if(value != "") {
            viewModelScope.launch {
                val result = repository.searchReference(Referencia(value)) as ApiReferenceResponse
                references.value = result.payload
            }
        }
    }

    //search color
    fun searchColorByReference(value: String) {
        if(value != "") {
            viewModelScope.launch {
                val result =
                    repository.searchColorByReference(Referencia(value)) as ApiReferenceColorTallaResponse
                val temp = mutableListOf<String>()
                for (i in 0 until result.payload.size) {
                    temp.add("$i. ${result.payload[i].label}")
                }
                colors.value = result.payload
                colorsAux.value = temp
            }
        }
    }

    //search color
    fun searchSizeByColorReference(value: String) {
        if(value != "") {
            viewModelScope.launch {
                val result =
                    repository.searchTallaByColorReference(Referencia(value)) as ApiReferenceTallaResponse
                val temp = mutableListOf<String>()
                for (i in 0 until result.payload.size) {
                    temp.add("$i. ${result.payload[i].id_talla}")
                }
                tallas.value = result.payload
                tallasAux.value = temp
            }
        }
    }

    //search client by name
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