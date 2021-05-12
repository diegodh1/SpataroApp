package com.example.spataroapp.data.remote

import com.example.spataroapp.data.entities.ApiLoginResponse
import com.example.spataroapp.data.entities.Client
import com.example.spataroapp.data.entities.Document
import com.example.spataroapp.data.entities.UserApi
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServiceInterface {
    @GET("get_documentos")
    suspend fun getAllDocuments() : Response<MutableList<String>>

    @POST("iniciar_sesion")
    suspend fun login(@Body body: UserApi): Response<ApiLoginResponse>

    @POST("search_usuario")
    suspend fun searchUser(@Body body: UserApi): Response<ApiLoginResponse>

    @POST("crear_usuario")
    suspend fun createUser(@Body body: UserApi): Response<ApiLoginResponse>

    @POST("editar_usuario")
    suspend fun editUser(@Body body: UserApi): Response<ApiLoginResponse>

    @POST("crear_cliente")
    suspend fun createClient(@Body body: Client): Response<ApiLoginResponse>
}