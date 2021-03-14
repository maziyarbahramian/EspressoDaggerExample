package com.mazibahrami.example.di

import androidx.lifecycle.ViewModelProvider
import com.mazibahrami.example.repository.FakeMainRepositoryImpl
import com.mazibahrami.example.viewmodels.FakeMainViewModelFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
object TestViewModelModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideViewModelFactory(
        mainRepositoryImpl: FakeMainRepositoryImpl
    ): ViewModelProvider.Factory {
        return FakeMainViewModelFactory(mainRepositoryImpl)
    }
}