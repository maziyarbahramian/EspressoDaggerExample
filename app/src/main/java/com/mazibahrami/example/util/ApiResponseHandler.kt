package com.mazibahrami.example.util

import com.mazibahrami.example.util.Constants.NETWORK_ERROR
import com.mazibahrami.example.util.Constants.UNKNOWN_ERROR

abstract class ApiResponseHandler <ViewState, Data>(
    response: ApiResult<Data?>,
    stateEvent: StateEvent
){
    val result: DataState<ViewState> = when(response){

        is ApiResult.GenericError -> {
            DataState.error(
                errorMessage = stateEvent.errorInfo()
                        + "\n\nReason: " + response.errorMessage,
                stateEvent = stateEvent
            )
        }

        is ApiResult.NetworkError -> {
            DataState.error(
                errorMessage = stateEvent.errorInfo()
                        + "\n\nReason: " + NETWORK_ERROR,
                stateEvent = stateEvent
            )
        }

        is ApiResult.Success -> {
            if(response.value == null){
                DataState.error(
                    errorMessage = stateEvent.errorInfo()
                            + "\n\nReason: " + UNKNOWN_ERROR,
                    stateEvent = stateEvent
                )
            }
            else{
                this.handleSuccess(resultObj = response.value)
            }
        }

    }

    abstract fun handleSuccess(resultObj: Data): DataState<ViewState>

}