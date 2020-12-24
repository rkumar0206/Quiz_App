package com.rohitthebest.quizzed_aquizapp.helperClasses

data class CustomQuizParameters(
    var type: Type = Type.ANY,
    var categoryNumber: String? = "",
    var numberOfQuestion: String? = "",
    var difficulty: Difficulty = Difficulty.MEDIUM
)

enum class Type {
    ANY,
    CHOOSE_CATEGORY,
    CUSTOM
}

//enum class for selecting difficulty
enum class Difficulty {

    EASY,
    MEDIUM,
    HARD
}