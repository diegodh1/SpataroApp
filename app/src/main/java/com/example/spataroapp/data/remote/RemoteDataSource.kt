package com.example.spataroapp.data.remote

import com.example.spataroapp.data.entities.Client
import com.example.spataroapp.data.entities.UserApi
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiServiceInterface
): BaseDataSource() {
    suspend fun getAllDocuments() = getResult { apiService.getAllDocuments() }
    suspend fun login(json: UserApi) = getResult { apiService.login(json) }
    suspend fun searchUser(json: UserApi) = getResult { apiService.searchUser(json) }
    suspend fun createUser(json: UserApi) = getResult { apiService.createUser(json) }
    suspend fun editUser(json: UserApi) = getResult { apiService.editUser(json) }
    suspend fun createClient(json: Client) = getResult { apiService.createClient(json) }
}