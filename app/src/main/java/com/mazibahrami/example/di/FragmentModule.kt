package com.mazibahrami.example.di

import androidx.fragment.app.FragmentFactory
import com.mazibahrami.example.fragments.MainFragmentFactory
import com.mazibahrami.example.util.GlideManager
import com.mazibahrami.example.viewmodels.MainViewModelFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
object FragmentModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideMainFragmentFactory(
        viewModelFactory: MainViewModelFactory,
        glideManager: GlideManager
    ): FragmentFactory {
        return MainFragmentFactory(viewModelFactory, glideManager)
    }
}