package com.mazibahrami.example

import com.mazibahrami.example.di.DaggerTestAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class TestBaseApplication : BaseApplication() {

    override fun initAppComponent() {
        appComponent = DaggerTestAppComponent.builder()
            .application(this)
            .build()
    }
}