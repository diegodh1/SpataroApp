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
data class Client(var id_cliente:Int = 0,
                  var id_tipo_doc: String = "",
                  var nombre:String = "",
                  var apellido:String = "",
                  var correo:String = "",
                  var direcciones:MutableList<Address> = mutableListOf(),
                  var telefonos:MutableList<Telefono> = mutableListOf())

data class Address(var id_cliente:Int = 0, var id_pais:String = "", var id_ciudad:String = "",var direccion:String ="", var activo:Boolean = true)
data class Telefono(var id_cliente:Int = 0, var id_pais:String = "", var id_ciudad:String = "",var telefono:String ="", var activo:Boolean = true)






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