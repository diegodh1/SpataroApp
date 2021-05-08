package com.example.spataroapp.data.remote

import com.example.spataroapp.data.entities.UserLogin
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiServiceInterface
): BaseDataSource() {
    suspend fun getAllDocuments() = getResult { apiService.getAllDocuments() }
    suspend fun login(json: UserLogin) = getResult { apiService.login(json) }
}