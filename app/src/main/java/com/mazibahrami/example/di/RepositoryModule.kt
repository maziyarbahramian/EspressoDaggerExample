package com.mazibahrami.example.di

import com.mazibahrami.example.api.ApiService
import com.mazibahrami.example.repository.MainRepository
import com.mazibahrami.example.repository.MainRepositoryImpl
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.*
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object RepositoryModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor();
        logging.level = Level.BODY
        return logging
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideOkHttp(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideApiService(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient.Builder
    ): ApiService {
        return retrofitBuilder
            .client(okHttpClient.build())
            .build()
            .create(ApiService::class.java)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideMainRepository(apiService: ApiService): MainRepository {
        return MainRepositoryImpl(apiService)
    }
}