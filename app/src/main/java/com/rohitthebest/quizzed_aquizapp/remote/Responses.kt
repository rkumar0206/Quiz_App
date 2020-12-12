package com.rohitthebest.quizzed_aquizapp.remote

sealed class Responses<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Success<T>(data: T?) : Responses<T>(data)

    class Error<T>(data: T? = null, message: String?) : Responses<T>(data, message)

    class Loading<T> : Responses<T>()
}