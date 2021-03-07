package com.mazibahrami.example.di

import android.app.Application
import com.bumptech.glide.Glide
import com.mazibahrami.example.util.GlideManager
import com.mazibahrami.example.util.GlideRequestManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

// Alternative for Test: 'TestAppModule'
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
object AppModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideGlideRequestManager(
        application: Application
    ): GlideManager {
        return GlideRequestManager(
            Glide.with(application)
        )
    }
}