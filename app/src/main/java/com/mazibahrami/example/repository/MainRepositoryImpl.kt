package com.mazibahrami.example.repository

import com.mazibahrami.example.models.BlogPost
import com.mazibahrami.example.models.Category
import com.mazibahrami.example.api.ApiService
import com.mazibahrami.example.ui.viewmodel.state.MainViewState
import com.mazibahrami.example.ui.viewmodel.state.MainViewState.*
import com.mazibahrami.example.util.ApiResponseHandler
import com.mazibahrami.example.util.DataState
import com.mazibahrami.example.util.StateEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl
@Inject
constructor(
    private val apiService: ApiService
) : MainRepository {

    override fun getBlogs(
        stateEvent: StateEvent,
        category: String
    ): Flow<DataState<MainViewState>> {
        return flow {
            val response = safeApiCall(IO) { apiService.getBlogPosts(category) }

            emit(
                object : ApiResponseHandler<MainViewState, List<BlogPost>>(
                    response = response,
                    stateEvent = stateEvent
                ) {
                    override fun handleSuccess(resultObj: List<BlogPost>): DataState<MainViewState> {
                        return DataState.data(
                            data = MainViewState(
                                listFragmentView = ListFragmentView(
                                    blogs = resultObj
                                ),
                            ),
                            stateEvent = stateEvent
                        )
                    }
                }.result
            )
        }
    }

    override fun getAllBlogs(stateEvent: StateEvent): Flow<DataState<MainViewState>> {
        return flow {

            val response = safeApiCall(IO) { apiService.getAllBlogPosts() }

            emit(
                object : ApiResponseHandler<MainViewState, List<BlogPost>>(
                    response = response,
                    stateEvent = stateEvent
                ) {
                    override fun handleSuccess(resultObj: List<BlogPost>): DataState<MainViewState> {
                        return DataState.data(
                            data = MainViewState(
                                listFragmentView = ListFragmentView(
                                    blogs = resultObj
                                )
                            ),
                            stateEvent = stateEvent
                        )
                    }

                }.result
            )
        }
    }

    override fun getCategories(stateEvent: StateEvent): Flow<DataState<MainViewState>> {
        return flow {

            val response = safeApiCall(IO) { apiService.getCategories() }

            emit(
                object : ApiResponseHandler<MainViewState, List<Category>>(
                    response = response,
                    stateEvent = stateEvent
                ) {
                    override fun handleSuccess(resultObj: List<Category>): DataState<MainViewState> {
                        return DataState.data(
                            data = MainViewState(
                                listFragmentView = ListFragmentView(
                                    categories = resultObj
                                )
                            ),
                            stateEvent = stateEvent
                        )
                    }

                }.result
            )
        }
    }
}