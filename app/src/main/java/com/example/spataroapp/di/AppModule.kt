package com.example.spataroapp.di

import android.app.Application
import android.content.Context
import com.example.spataroapp.data.local.AppDataBase
import com.example.spataroapp.data.local.AppDataBaseDao
import com.example.spataroapp.data.remote.ApiServiceInterface
import com.example.spataroapp.data.remote.RemoteDataSource
import com.example.spataroapp.data.repository.Repository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    //EXTERNAL DATA
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.6:4000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideApiServiceInterface(retrofit: Retrofit): ApiServiceInterface = retrofit.create(ApiServiceInterface::class.java)

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService:ApiServiceInterface) = RemoteDataSource(apiService)

    //LOCAL DATA
    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext appContext: Context):AppDataBase = AppDataBase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideDataBaseDao(db: AppDataBase):AppDataBaseDao = db.appDataBaseDao()

    //REPOSITORY
    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: RemoteDataSource, localDataSource: AppDataBaseDao) = Repository(remoteDataSource, localDataSource)

}