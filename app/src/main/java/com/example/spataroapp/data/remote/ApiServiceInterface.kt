package com.example.spataroapp.data.remote

import com.example.spataroapp.data.entities.*
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServiceInterface {
    @GET("get_documentos")
    suspend fun getAllDocuments() : Response<MutableList<String>>

    //user endpoints
    @POST("iniciar_sesion")
    suspend fun login(@Body body: UserApi): Response<ApiLoginResponse>

    @POST("search_usuario")
    suspend fun searchUser(@Body body: UserApi): Response<ApiLoginResponse>

    @POST("crear_usuario")
    suspend fun createUser(@Body body: UserApi): Response<ApiLoginResponse>

    @POST("editar_usuario")
    suspend fun editUser(@Body body: UserApi): Response<ApiLoginResponse>

    //client endpoints

    @POST("crear_cliente")
    suspend fun createClient(@Body body: Client): Response<ApiClientResponse>

    @POST("editar_cliente")
    suspend fun editClient(@Body body: Client): Response<ApiClientResponse>

    @POST("search_cliente")
    suspend fun searchClient(@Body body: Client): Response<ApiClientResponse>

    //reference
    @POST("guardar_referencia")
    suspend fun saveReferences(@Body body: ApiRequestReference): Response<ApiClientResponse>
}