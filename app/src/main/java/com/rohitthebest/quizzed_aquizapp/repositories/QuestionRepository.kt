package com.rohitthebest.quizzed_aquizapp.repositories

import com.rohitthebest.quizzed_aquizapp.dataStorage.roomDatabase.dao.QuestionDao
import com.rohitthebest.quizzed_aquizapp.remote.model.Result
import javax.inject.Inject

class QuestionRepository @Inject constructor(val dao: QuestionDao) {

    suspend fun insetQuestion(question: Result) = dao.insert(question)

    suspend fun updateQuestion(question: Result) = dao.update(question)

    suspend fun delete(question: Result) = dao.delete(question)

    suspend fun deleteAll() = dao.deleteAll()

    fun getAllSavedQuestions() = dao.getAllQuestions()

}