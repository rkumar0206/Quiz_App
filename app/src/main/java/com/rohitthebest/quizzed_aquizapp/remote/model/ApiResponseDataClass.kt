package com.rohitthebest.quizzed_aquizapp.remote.model

data class ApiResponseDataClass(
    val response_code: Int,
    val results: List<Result>
)