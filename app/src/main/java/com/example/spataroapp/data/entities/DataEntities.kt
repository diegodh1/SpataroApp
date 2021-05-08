package com.example.spataroapp.data.entities

import androidx.room.*

data class Document(val id_tipo_doc: String)
//API REST ENTITIES
//general api response
data class ApiResponse(val message:String, val payload:Any, val status:Int)
// entitie for endpoint iniciar_sesion
data class UserLogin(val id_usuario: Int,
                     val passwrd: String,
                     val id_tipo_doc:String = "",
                     val nombre: String = "",
                     val apellido:String = "",
                     val correo:String = "",
                     val foto:String = "",
                     val menus:MutableList<String> = mutableListOf())


//ROOM ENTITIES
@Entity
data class User(
    @PrimaryKey
    @ColumnInfo(name = "user_id") val id_usuario: Int,
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