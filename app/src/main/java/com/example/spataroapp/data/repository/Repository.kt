package com.example.spataroapp.data.repository

import com.example.spataroapp.data.remote.RemoteDataSource
import javax.inject.Inject

class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource) {
    suspend fun getAllDocuments() = remoteDataSource.getAllDocuments()

    fun prueba():String{
        return "hola"
    }
}