package com.example.spataroapp.di

import android.app.Application
import com.example.spataroapp.data.remote.ApiServiceInterface
import com.example.spataroapp.data.remote.RemoteDataSource
import com.example.spataroapp.data.repository.Repository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.9:4000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideApiServiceInterface(retrofit: Retrofit): ApiServiceInterface = retrofit.create(ApiServiceInterface::class.java)

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService:ApiServiceInterface) = RemoteDataSource(apiService)

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: RemoteDataSource) = Repository(remoteDataSource)

}