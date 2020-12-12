package com.rohitthebest.quizzed_aquizapp.remote.apiServices

import com.rohitthebest.quizzed_aquizapp.remote.model.ApiResponseDataClass
import retrofit2.Response

interface ApiHelper {

    suspend fun getQuiz(

        amount: Int = 10
    ): Response<ApiResponseDataClass>

    suspend fun getCustomQuiz(
        map: Map<String, String>
    ): Response<ApiResponseDataClass>
}