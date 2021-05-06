package com.example.spataroapp.data.remote

import retrofit2.Response
import java.lang.Exception

abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Any {
        try{
            val response = call()
            if(response.isSuccessful){
                val body = response.body()
                return body!!
            }
            return  error(" ${response.code()} ${response.message()}")
        }
        catch (e: Exception){
            return error(e.message ?: e.toString())
        }
    }
}