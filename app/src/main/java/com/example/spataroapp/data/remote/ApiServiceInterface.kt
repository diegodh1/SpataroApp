package com.example.spataroapp.data.remote

import com.example.spataroapp.data.entities.Documents
import retrofit2.Response
import retrofit2.http.GET

interface ApiServiceInterface {
    @GET("documents")
    suspend fun getAllDocuments() : Response<MutableList<Documents>>
}