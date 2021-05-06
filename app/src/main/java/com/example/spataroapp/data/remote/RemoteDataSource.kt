package com.example.spataroapp.data.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiServiceInterface
): BaseDataSource() {
    suspend fun getAllDocuments() = getResult { apiService.getAllDocuments() }
}