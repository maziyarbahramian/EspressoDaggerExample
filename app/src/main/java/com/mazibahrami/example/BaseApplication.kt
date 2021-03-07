package com.mazibahrami.example

import android.app.Application
import com.mazibahrami.example.di.AppComponent
import com.mazibahrami.example.di.DaggerAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
open class BaseApplication : Application() {
    private val TAG = "AppDebug"

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
    }

    open fun initAppComponent() {
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}