package com.robert.nganga.rickmorty.utils

import retrofit2.Response
import kotlin.Exception

data class DataResponse<T>(
    val status: Status,
    val data: Response<T>?,
    val exception: Exception?
){
    companion object{
        fun <T> success(data: Response<T>): DataResponse<T>{
            return DataResponse(
                status = Status.Success,
                data = data,
                exception = null
            )
        }

        fun <T> failure(exception: Exception): DataResponse<T>{
            return DataResponse(
                status = Status.Failure,
                data = null,
                exception = exception
            )
        }
    }
    sealed class Status{
        object Success : Status()
        object Failure: Status()
    }

    val failed: Boolean
        get() = this.status == Status.Failure

    val isSuccessful: Boolean
        get() = !failed && this.data?.isSuccessful == true

    val body: T
        get() = this.data!!.body()!!
}
