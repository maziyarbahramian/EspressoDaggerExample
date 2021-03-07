package com.mazibahrami.example.repository

import com.mazibahrami.example.ui.viewmodel.state.MainViewState
import com.mazibahrami.example.util.DataState
import com.mazibahrami.example.util.StateEvent
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getBlogs(stateEvent: StateEvent, category: String): Flow<DataState<MainViewState>>

    fun getAllBlogs(stateEvent: StateEvent): Flow<DataState<MainViewState>>

    fun getCategories(stateEvent: StateEvent): Flow<DataState<MainViewState>>
}