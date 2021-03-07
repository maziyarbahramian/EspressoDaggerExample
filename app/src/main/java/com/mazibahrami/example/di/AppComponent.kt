package com.mazibahrami.example.di

import android.app.Application
import com.mazibahrami.example.fragments.MainNavHostFragment
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
        FragmentModule::class,
        AppModule::class,
        InternalBindingModule::class,
        RepositoryModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(mainActivity: MainActivity)

    fun inject(mainNavHostFragment: MainNavHostFragment)
}