package com.example.spataroapp.data.remote

import com.example.spataroapp.data.entities.ApiResponse
import com.example.spataroapp.data.entities.Document
import com.example.spataroapp.data.entities.UserLogin
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServiceInterface {
    @GET("get_documentos")
    suspend fun getAllDocuments() : Response<MutableList<Document>>

    @POST("iniciar_sesion")
    suspend fun login(@Body body: UserLogin): Response<ApiResponse>
}