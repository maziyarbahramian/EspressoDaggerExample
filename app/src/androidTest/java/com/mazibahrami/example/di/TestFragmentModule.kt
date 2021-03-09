package com.mazibahrami.example.di

import androidx.fragment.app.FragmentFactory
import com.mazibahrami.example.fragments.FakeMainFragmentFactory
import com.mazibahrami.example.util.FakeGlideRequestManager
import com.mazibahrami.example.viewmodels.FakeMainViewModelFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
object TestFragmentModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideMainFragmentFactory(
        viewModelFactory: FakeMainViewModelFactory,
        fakeGlideRequestManager: FakeGlideRequestManager
    ): FragmentFactory {
        return FakeMainFragmentFactory(viewModelFactory, fakeGlideRequestManager)
    }
}