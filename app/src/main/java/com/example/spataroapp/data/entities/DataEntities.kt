package com.example.spataroapp.data.entities

import androidx.room.*

data class Document(val id_tipo_doc: String)
//API REST ENTITIES
//general api response
data class ApiLoginResponse(val message:String, val payload:UserApi, val status:Int)
// entitie for endpoint iniciar_sesion
data class UserApi(var id_usuario: Int = 0,
                     var passwrd: String = "",
                     var id_tipo_doc:String = "",
                     var nombre: String = "",
                     var apellido:String = "",
                     var correo:String = "",
                     var foto:String = "",
                     var activo:Boolean = false,
                     var menus:MutableList<String> = mutableListOf())

// entities for endpoint crear_clientes
data class ApiClientResponse(var message:String, var payload:Client, var status:Int)
data class Client(var id_cliente:Int = 0,
                  var id_tipo_doc: String = "",
                  var nombre:String = "",
                  var apellido:String = "",
                  var correo:String = "",
                  var direcciones:MutableList<Address> = mutableListOf(),
                  var telefonos:MutableList<Telefono> = mutableListOf(),
                  var activo:Boolean = true)

data class Address(var id_cliente:Int = 0, var id_pais:String = "", var id_ciudad:String = "",var direccion:String ="", var activo:Boolean = true)
data class Telefono(var id_cliente:Int = 0, var id_pais:String = "", var id_ciudad:String = "",var telefono:String ="", var activo:Boolean = true)

//entities for endpoint guardar_referencia
data class ApiRequestReference(var file:String, var tipo:String)

//entities for orders

data class ApiOrderResponse(var message:String, var status:Int, var payload:Int)
data class Order(var id_cliente:Int, var id_usuario: Int, var fecha:String, var firma:String = "", var observacion:String = "", var direccion:String = "")

data class Referencia(var id_referencia:String)
data class ApiReferenceResponse(var message:String, var status:Int, var payload:MutableList<String>)
data class ApiReferenceColorTallaResponse(var message:String, var status:Int, var payload:MutableList<ApiReferenceColorTalla>)
data class ApiReferenceColorTalla(var label:String, var value:String)
data class ApiReferenceTallaResponse(var message:String, var status:Int, var payload:MutableList<ApiReferenceSize>)
data class ApiReferenceSize(var id_consecutivo:Int, var id_talla:String, var metros:Double, var precio:Double, var unidades:Int)
data class ApiItemOrder(var id_pedido:Int=0, var id_consecutivo:Int=0, var unidades:Int=0, var precio:Double=0.0, var referencia:String="", var color:String="", var id_talla:String="")
data class ApiItemsOrder(var items: MutableList<ApiItemOrder> = mutableListOf(), var precio_total:Double = 0.0, var unidades_total:Int = 0, var status:Int)
data class ApiItemsResponse(var payload:ApiItemsOrder, var status:Int, var message:String)
//ROOM ENTITIES
@Entity
data class User(
    @PrimaryKey
    @ColumnInfo(name = "user_id") val id_usuario: Int,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "tipo_doc_id") val id_tipo_doc: String?,
    @ColumnInfo(name = "nombre") val nombre: String?,
    @ColumnInfo(name = "apellido") val apellido: String?,
    @ColumnInfo(name = "correo") val correo: String?,
    @ColumnInfo(name = "foto") val foto: String?
)

@Entity(tableName = "profile")
data class Profile(
    @PrimaryKey
    @ColumnInfo(name = "profile_id")
    var ProfileID: String,
)

data class UserProfile(
    @Embedded val user: User,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "profile_id"
    )
    val profiles: List<Profile>
)