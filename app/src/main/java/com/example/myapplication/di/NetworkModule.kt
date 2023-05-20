package com.example.myapplication.di

import com.example.myapplication.api.AuthInterceptor
import com.example.myapplication.api.NotesAPI
import com.example.myapplication.api.UserAPI
import com.example.myapplication.utils.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Singleton
    @Provides
    fun provideUserAPI(retrofitBuilder: Retrofit.Builder): UserAPI {
        return retrofitBuilder.build().create(UserAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideNoteAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): NotesAPI {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(NotesAPI::class.java)
    }

}