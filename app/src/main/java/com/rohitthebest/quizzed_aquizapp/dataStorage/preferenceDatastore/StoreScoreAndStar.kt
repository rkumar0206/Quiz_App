package com.rohitthebest.quizzed_aquizapp.dataStorage.preferenceDatastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.rohitthebest.others.Constants.HIGH_SCORE_KEY
import com.rohitthebest.others.Constants.STAR_KEY
import kotlinx.coroutines.flow.map

data class HighScoreAndStar(val high_score: Int, val star: Int)

class StoreScoreAndStar(context: Context) {
    //creating dataStore
    private val dataStore = context.createDataStore("preference_datastore")

    //creating dataStore
    companion object {

        val USER_HIGH_SCORE = preferencesKey<Int>(HIGH_SCORE_KEY)
        val USER_STAR = preferencesKey<Int>(STAR_KEY)
    }

    //storing data
    suspend fun storeData(high_score: Int, star: Int) {

        dataStore.edit {

            it[USER_HIGH_SCORE] = high_score
            it[USER_STAR] = star
        }
    }

    //creating flow
    val flow = dataStore.data.map {

        val highScore = it[USER_HIGH_SCORE] ?: 0
        val star = it[USER_STAR] ?: 0

        HighScoreAndStar(
            highScore,
            star
        )
    }

}

