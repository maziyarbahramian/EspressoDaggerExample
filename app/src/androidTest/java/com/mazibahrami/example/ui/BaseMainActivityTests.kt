package com.mazibahrami.example.ui

import com.mazibahrami.example.TestBaseApplication
import com.mazibahrami.example.api.FakeApiService
import com.mazibahrami.example.di.TestAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
abstract class BaseMainActivityTests {

    abstract fun injectTest(application: TestBaseApplication)

    fun configureFakeApiService(
        blogsDataSource: String? = null,
        categoriesDataSource: String? = null,
        networkDelay: Long? = null,
        application: TestBaseApplication
    ): FakeApiService {
        val apiService = (application.appComponent as TestAppComponent).apiService
        blogsDataSource?.let { apiService.blogPostsJsonFileName = it }
        categoriesDataSource?.let { apiService.categoriesFileName = it }
        networkDelay?.let { apiService.networkDelay = it }
        return apiService
    }

    fun configureFakeRepository(
        apiService: FakeApiService,
        application: TestBaseApplication
    ) {
        val mainRepository = (application.appComponent as TestAppComponent).mainRepository
        mainRepository.apiService = apiService
    }

}