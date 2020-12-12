package com.rohitthebest.quizzed_aquizapp.module

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohitthebest.quizzed_aquizapp.remote.QuizApiRepository
import com.rohitthebest.quizzed_aquizapp.remote.Responses
import com.rohitthebest.quizzed_aquizapp.remote.model.ApiResponseDataClass
import kotlinx.coroutines.launch
import retrofit2.Response

class QuizApiViewModel @ViewModelInject constructor(
    private val repository: QuizApiRepository
) : ViewModel() {

    private val _quiz = MutableLiveData<Responses<ApiResponseDataClass>>()

    val quiz: LiveData<Responses<ApiResponseDataClass>> get() = _quiz

    fun fetchQuiz(amount: Int) {

        try {

            viewModelScope.launch {

                _quiz.postValue(Responses.Loading())

                repository.getQuiz(amount).let {

                    if (it.isSuccessful) {

                        _quiz.postValue(handleResponse(it))
                    } else {

                        _quiz.postValue(Responses.Error(null, it.errorBody().toString()))
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun fetchCustomQuiz(queryMap: Map<String, String>) {

        try {

            _quiz.postValue(Responses.Loading())

            viewModelScope.launch {

                repository.getCustomQuiz(queryMap).let {

                    if (it.isSuccessful) {

                        _quiz.postValue(handleResponse(it))
                    } else {

                        _quiz.postValue(Responses.Error(null, it.errorBody().toString()))
                    }
                }

            }

        } catch (e: Exception) {

            e.printStackTrace()
        }
    }

    private fun handleResponse(response: Response<ApiResponseDataClass>): Responses<ApiResponseDataClass>? {

        if (response.isSuccessful) {

            response.body()?.let {

                return Responses.Success(it)
            }
        }

        return Responses.Error(null, response.message())
    }

}