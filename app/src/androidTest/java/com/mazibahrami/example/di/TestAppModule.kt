package com.mazibahrami.example.di

import android.app.Application
import com.mazibahrami.example.util.FakeGlideRequestManager
import com.mazibahrami.example.util.GlideManager
import com.mazibahrami.example.util.JsonUtil
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object TestAppModule {
    @JvmStatic
    @Singleton
    @Provides
    fun provideGlideRequestManager(): GlideManager {
        return FakeGlideRequestManager()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideJsonUtil(application: Application): JsonUtil {
        return JsonUtil(application)
    }
}