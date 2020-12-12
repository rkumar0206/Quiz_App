package com.rohitthebest.quizzed_aquizapp.remote

import com.rohitthebest.quizzed_aquizapp.remote.apiServices.ApiHelper
import javax.inject.Inject

class QuizApiRepository @Inject constructor(
    var apiHelper: ApiHelper
) {

    suspend fun getQuiz(amount: Int = 10) = apiHelper.getQuiz(amount)

    suspend fun getCustomQuiz(queryMap: Map<String, String>) = apiHelper.getCustomQuiz(queryMap)
}