package com.example.spataroapp.data.remote

import com.example.spataroapp.data.entities.*
import com.example.spataroapp.presentation.order_screen.Order
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

    //reference endpoints
    @POST("guardar_referencia")
    suspend fun saveReferences(@Body body: ApiRequestReference): Response<ApiClientResponse>

    //order endpoints
    @POST("buscar_cliente")
    suspend fun searchClientByName(@Body body: Client): Response<MutableList<Client>>

    //order endpoints
    @POST("crear_pedido")
    suspend fun createOrder(@Body body: com.example.spataroapp.data.entities.Order): Response<ApiOrderResponse>

    @POST("search_ref")
    suspend fun searchReferences(@Body body: Referencia): Response<ApiReferenceResponse>


    @POST("search_ref_color")
    suspend fun searchColorByReference(@Body body: Referencia): Response<ApiReferenceColorTallaResponse>

    @POST("search_ref_color_talla")
    suspend fun searchTallaByColorReference(@Body body: Referencia): Response<ApiReferenceTallaResponse>

    @POST("agregar_item_pedido")
    suspend fun AddItemOrder(@Body item: ApiItemOrder): Response<ApiLoginResponse>

    @POST("dar_items_guardados")
    suspend fun getItemsOrder(@Body item: ApiItemOrder): Response<ApiItemsResponse>

}