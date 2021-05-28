package com.example.spataroapp.data.remote

import com.example.spataroapp.data.entities.*
import com.example.spataroapp.presentation.order_screen.Order
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiServiceInterface
): BaseDataSource() {
    suspend fun getAllDocuments() = getResult { apiService.getAllDocuments() }
    //User Endpoints
    suspend fun login(json: UserApi) = getResult { apiService.login(json) }
    suspend fun searchUser(json: UserApi) = getResult { apiService.searchUser(json) }
    suspend fun createUser(json: UserApi) = getResult { apiService.createUser(json) }
    suspend fun editUser(json: UserApi) = getResult { apiService.editUser(json) }

    //Client Endpoints
    suspend fun searchClient(json: Client) = getResult { apiService.searchClient(json) }
    suspend fun createClient(json: Client) = getResult { apiService.createClient(json) }
    suspend fun editClient(json: Client) = getResult { apiService.editClient(json) }

    //reference endpoints
    suspend fun saveReferences(json: ApiRequestReference) = getResult { apiService.saveReferences(json)}

    //orders endpoints
    suspend fun searchClientByName(json: Client) = getResult { apiService.searchClientByName(json)}
    suspend fun createOrder(json: com.example.spataroapp.data.entities.Order) = getResult { apiService.createOrder(json)}

    suspend fun searchReference(json: Referencia) = getResult { apiService.searchReferences(json)}
    suspend fun searchColorByReference(json: Referencia) = getResult { apiService.searchColorByReference(json)}
    suspend fun searchTallaByColorReference(json: Referencia) = getResult { apiService.searchTallaByColorReference(json)}
    suspend fun addItemOrder(json: ApiItemOrder) = getResult { apiService.AddItemOrder(json)}
    suspend fun getItemsOder(json: ApiItemOrder) = getResult { apiService.getItemsOrder(json)}
    suspend fun deleteItemOrder(json: ApiItemOrder) = getResult { apiService.deleteItemOrder(json)}
}