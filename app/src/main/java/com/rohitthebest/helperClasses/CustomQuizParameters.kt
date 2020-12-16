package com.rohitthebest.helperClasses

data class CustomQuizParameters(
    var type: Type = Type.ANY,
    var categoryNumber: String? = "",
    var numberOfQuestion: String? = "",
    var difficulty: String? = ""
)

enum class Type {
    ANY,
    CHOOSE_CATEGORY,
    CUSTOM
}