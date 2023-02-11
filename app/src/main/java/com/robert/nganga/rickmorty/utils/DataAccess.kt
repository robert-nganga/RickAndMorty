package com.robert.nganga.rickmorty.utils

import okio.IOException
import retrofit2.HttpException
import retrofit2.Response

object DataAccess {
    inline fun <T> safeApiCall(apiCall: () -> Response<T>): Resource<T>{
        return try {
            val responseStatus = apiCall.invoke()
            if (responseStatus.isSuccessful){
                Resource.success(responseStatus.body()!!)
            }else{
                Resource.error(responseStatus.message())
            }
        } catch (e: Exception){
            when(e){
                is IOException ->{Resource.error(e.message!!)}
                is HttpException ->{Resource.error(e.message!!)}
                else -> {Resource.error(e.message!!)}
            }

        }
    }
}