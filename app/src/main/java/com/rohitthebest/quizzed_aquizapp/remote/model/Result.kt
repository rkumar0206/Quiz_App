package com.rohitthebest.quizzed_aquizapp.remote.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_table")
data class Result(
    val category: String,
    val correct_answer: String,
    val difficulty: String,
    val incorrect_answers: List<String>,
    val question: String,
    val type: String,
    @PrimaryKey(autoGenerate = true) var id: Int? = null
)