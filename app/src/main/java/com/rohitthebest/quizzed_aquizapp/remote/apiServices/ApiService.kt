package com.rohitthebest.quizzed_aquizapp.remote.apiServices

import com.rohitthebest.quizzed_aquizapp.remote.model.ApiResponseDataClass
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {

    @GET("/api.php")
    suspend fun getQuizResponse(
        @Query("amount") amount: Int = 10
    ): Response<ApiResponseDataClass>


    @GET("/api.php")
    suspend fun getCustomQuizResponse(
        @QueryMap queryMap: Map<String, String>
    ): Response<ApiResponseDataClass>
}