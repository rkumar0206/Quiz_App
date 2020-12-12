package com.rohitthebest.quizzed_aquizapp.remote.apiServices

import com.rohitthebest.quizzed_aquizapp.remote.model.ApiResponseDataClass
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImplementation @Inject constructor(
    val apiService: ApiService
) : ApiHelper {

    override suspend fun getQuiz(amount: Int): Response<ApiResponseDataClass> =

        apiService.getQuizResponse(amount)

    override suspend fun getCustomQuiz(map: Map<String, String>): Response<ApiResponseDataClass> =
        apiService.getCustomQuizResponse(map)

}