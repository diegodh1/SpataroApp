package com.example.spataroapp.data.repository

import com.example.spataroapp.data.entities.UserLogin
import com.example.spataroapp.data.local.AppDataBaseDao
import com.example.spataroapp.data.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource, private val localDataSource: AppDataBaseDao) {
    suspend fun getAllDocuments():Any {
        return withContext(Dispatchers.IO) {
            remoteDataSource.getAllDocuments()
        }
    }
    suspend fun login(json: UserLogin):Any{
        return withContext(Dispatchers.IO) {
            remoteDataSource.login(json)
        }
    }
}