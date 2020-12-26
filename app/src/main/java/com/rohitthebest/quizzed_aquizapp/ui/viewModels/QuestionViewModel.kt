package com.rohitthebest.quizzed_aquizapp.ui.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rohitthebest.quizzed_aquizapp.remote.model.Result
import com.rohitthebest.quizzed_aquizapp.repositories.QuestionRepository
import kotlinx.coroutines.launch

class QuestionViewModel @ViewModelInject constructor(
    val repository: QuestionRepository
) : ViewModel() {

    fun insertQuestion(question: Result) = viewModelScope.launch {

        repository.insetQuestion(question)
    }

    fun updateQuestion(question: Result) = viewModelScope.launch {

        repository.updateQuestion(question)
    }

    fun deleteQuestion(question: Result) = viewModelScope.launch {

        repository.delete(question)
    }

    fun deleteAllQuestions() = viewModelScope.launch {

        repository.deleteAll()
    }

    val savedQuestions = repository.getAllSavedQuestions().asLiveData()
}