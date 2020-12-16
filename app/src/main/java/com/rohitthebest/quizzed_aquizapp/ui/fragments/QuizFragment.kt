package com.rohitthebest.quizzed_aquizapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rohitthebest.helperClasses.Type
import com.rohitthebest.quizzed_aquizapp.R
import com.rohitthebest.quizzed_aquizapp.databinding.FragmentQuizBinding
import com.rohitthebest.quizzed_aquizapp.databinding.OptionsLayoutBinding
import com.rohitthebest.quizzed_aquizapp.module.QuizApiViewModel
import com.rohitthebest.quizzed_aquizapp.remote.Responses
import com.rohitthebest.quizzed_aquizapp.remote.model.Result
import com.rohitthebest.quizzed_aquizapp.util.Functions.Companion.hide
import com.rohitthebest.quizzed_aquizapp.util.Functions.Companion.show
import com.rohitthebest.quizzed_aquizapp.util.Functions.Companion.showToast
import com.rohitthebest.quizzed_aquizapp.util.GsonConverters.Companion.convertJSONStringToCustomQuizParameter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class QuizFragment : Fragment(R.layout.fragment_quiz), View.OnClickListener {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val quizViewModel: QuizApiViewModel by viewModels()

    private lateinit var optionsBinding: OptionsLayoutBinding

    private var questionList: List<Result> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentQuizBinding.bind(view)

        optionsBinding = binding.includeOption

        questionList = ArrayList()

        initListeners()

        getMessage()

        observeApiResponse()
    }

    private fun getMessage() {

        try {
            if (!arguments?.isEmpty!!) {

                val args = arguments?.let {

                    QuizFragmentArgs.fromBundle(it)
                }

                val quiz = args?.quizTypeMessage?.let { convertJSONStringToCustomQuizParameter(it) }

                when (quiz?.type) {

                    Type.ANY -> {

                        GlobalScope.launch {
                            delay(200)

                            withContext(Dispatchers.Main) {

                                quizViewModel.fetchQuiz(10)
                            }
                        }
                    }

                    Type.CHOOSE_CATEGORY -> {

                        val queryMap = HashMap<String, String>()
                        queryMap["category"] = quiz.categoryNumber.toString()

                        startCustomQuiz(queryMap)
                    }

                    else -> {

                        val queryMap = HashMap<String, String>()

                        queryMap["difficulty"] = quiz?.difficulty.toString()
                        queryMap["type"] = "multiple"
                        queryMap["amount"] = quiz?.numberOfQuestion.toString()

                        startCustomQuiz(queryMap)
                    }
                }
            }
        } catch (e: Exception) {

            e.printStackTrace()
        }
    }

    private fun startCustomQuiz(queryMap: java.util.HashMap<String, String>) {

        GlobalScope.launch {
            delay(200)

            withContext(Dispatchers.Main) {

                quizViewModel.fetchCustomQuiz(queryMap)
            }
        }

    }

    private fun initListeners() {

        binding.saveBtn.setOnClickListener(this)
        binding.nextBtn.setOnClickListener(this)
        binding.backBtn.setOnClickListener(this)
        binding.starBtn.setOnClickListener(this)

        optionsBinding.optionA.setOnClickListener(this)
        optionsBinding.optionB.setOnClickListener(this)
        optionsBinding.optionC.setOnClickListener(this)
        optionsBinding.optionD.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {


        }
    }


    private fun observeApiResponse() {

        quizViewModel.quiz.observe(viewLifecycleOwner) {

            when (it) {

                is Responses.Loading -> {

                    binding.pleaseWaitCL.show()
                }

                is Responses.Success -> {

                    try {
                        binding.pleaseWaitCL.hide()
                        binding.questionCL.show()

                        it.data?.let { quiz ->

                            //todo : aasign the list to the list

                            questionList = quiz.results
                            showToast(requireContext(), "${questionList[0]}")
                        }
                    } catch (e: Exception) {

                        e.printStackTrace()
                    }
                }

                is Responses.Error -> {

                }
            }


        }

    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}