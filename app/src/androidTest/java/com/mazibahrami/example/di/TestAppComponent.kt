package com.mazibahrami.example.di

import android.app.Application
import com.mazibahrami.example.api.FakeApiService
import com.mazibahrami.example.fragments.MainNavHostFragment
import com.mazibahrami.example.repository.FakeMainRepositoryImpl
import com.mazibahrami.example.ui.DetailFragmentTest
import com.mazibahrami.example.ui.ListFragmentErrorTests
import com.mazibahrami.example.ui.ListFragmentIntegrationTests
import com.mazibahrami.example.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Singleton
@Component(
    modules = [
        TestFragmentModule::class,
        TestAppModule::class,
        TestViewModelModule::class,
    ]
)
interface TestAppComponent : AppComponent {

    val apiService: FakeApiService

    val mainRepository: FakeMainRepositoryImpl

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): TestAppComponent
    }

    fun inject(detailFragmentTest: DetailFragmentTest)

    fun inject(listFragmentIntegrationTests: ListFragmentIntegrationTests)

    fun inject(listFragmentErrorTests: ListFragmentErrorTests)

}