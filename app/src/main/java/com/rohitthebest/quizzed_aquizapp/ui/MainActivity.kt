package com.rohitthebest.quizzed_aquizapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.rohitthebest.quizzed_aquizapp.databinding.ActivityMainBinding
import com.rohitthebest.quizzed_aquizapp.module.QuizApiViewModel
import com.rohitthebest.quizzed_aquizapp.remote.Responses
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: QuizApiViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getApiResponse()
    }

    /*private fun getApiResponse() {

        val queryMap = HashMap<String, String>()
        queryMap["amount"] = 10.toString()
        queryMap["category"] = 9.toString()
        queryMap["difficulty"] = "easy"
        queryMap["type"] = "multiple"

        viewModel.fetchCustomQuiz(queryMap)

        //viewModel.fetchQuiz(10)

        viewModel.quiz.observe(this) {


            when (it) {

                is Responses.Loading -> {

                    Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
                }

                is Responses.Success -> {

                    it.data?.let { quiz ->

                        Toast.makeText(this, "${quiz.results[0]}", Toast.LENGTH_LONG)
                            .show()

                    }
                }

                is Responses.Error -> {

                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }*/
}