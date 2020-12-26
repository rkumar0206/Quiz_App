package com.rohitthebest.quizzed_aquizapp.dataStorage.roomDatabase.dao

import androidx.room.*
import com.rohitthebest.quizzed_aquizapp.remote.model.Result
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: Result)

    @Update
    suspend fun update(question: Result)

    @Delete
    suspend fun delete(question: Result)

    @Query("DELETE FROM question_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM question_table ORDER BY id DESC")
    fun getAllQuestions(): Flow<List<Result>>
}