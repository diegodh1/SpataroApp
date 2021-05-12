package com.example.spataroapp.data.repository

import com.example.spataroapp.data.entities.Client
import com.example.spataroapp.data.entities.User
import com.example.spataroapp.data.entities.UserApi
import com.example.spataroapp.data.entities.UserProfile
import com.example.spataroapp.data.local.AppDataBaseDao
import com.example.spataroapp.data.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource, private val localDataSource: AppDataBaseDao) {
    //remote data source
    suspend fun getAllDocuments():Any {
        return withContext(Dispatchers.IO) {
            remoteDataSource.getAllDocuments()
        }
    }
    suspend fun login(json: UserApi):Any{
        return withContext(Dispatchers.IO) {
            remoteDataSource.login(json)
        }
    }
    suspend fun searchUser(json: UserApi):Any{
        return withContext(Dispatchers.IO) {
            remoteDataSource.searchUser(json)
        }
    }
    suspend fun createUser(json: UserApi):Any{
        return withContext(Dispatchers.IO) {
            remoteDataSource.createUser(json)
        }
    }
    suspend fun editUser(json: UserApi):Any{
        return withContext(Dispatchers.IO) {
            remoteDataSource.editUser(json)
        }
    }

    suspend fun createClient(json: Client):Any{
        return withContext(Dispatchers.IO) {
            remoteDataSource.createClient(json)
        }
    }

    //local data source
    suspend fun getLoggedUser():List<UserProfile>{
        return withContext(Dispatchers.IO) {
            localDataSource.getUserProfiles()
        }
    }
    suspend fun insertUser(user: User){
        withContext(Dispatchers.IO) {
            localDataSource.insertAll(user)
        }
    }
    suspend fun deleteUser(){
        withContext(Dispatchers.IO) {
            localDataSource.deleteAllUsers()
        }
    }
}