package com.rohitthebest.quizzed_aquizapp.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rohitthebest.helperClasses.CustomQuizParameters

class GsonConverters {

    companion object {

        private val gson = Gson()

        fun convertCustomQuizParameterToString(customQuizParameters: CustomQuizParameters): String {

            return gson.toJson(customQuizParameters)
        }

        fun convertJSONStringToCustomQuizParameter(JSONString: String): CustomQuizParameters {

            val type = object : TypeToken<CustomQuizParameters>() {}.type

            return gson.fromJson(JSONString, type)
        }

    }
}