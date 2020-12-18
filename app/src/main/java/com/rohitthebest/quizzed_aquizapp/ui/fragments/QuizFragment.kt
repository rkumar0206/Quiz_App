package com.rohitthebest.quizzed_aquizapp.ui.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
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
import kotlin.random.Random

private const val TAG = "QuizFragment"

@AndroidEntryPoint
class QuizFragment : Fragment(R.layout.fragment_quiz), View.OnClickListener {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val quizViewModel: QuizApiViewModel by viewModels()

    private lateinit var optionsBinding: OptionsLayoutBinding

    private var questionList: List<Result> = emptyList()
    private var questionNumber = -1
    private var selectedOption = ""
    private lateinit var timer: CountDownTimer
    private var totalQuestions = 10

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentQuizBinding.bind(view)

        optionsBinding = binding.includeOption

        questionList = ArrayList()

        binding.progressBar.max = 30000

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

                                val queryMap = HashMap<String, String>()
                                queryMap["amount"] = 10.toString()
                                queryMap["type"] = "multiple"

                                startCustomQuiz(queryMap)
                                totalQuestions = 10
                            }
                        }
                    }

                    Type.CHOOSE_CATEGORY -> {

                        val queryMap = HashMap<String, String>()

                        queryMap["amount"] = 10.toString()
                        queryMap["category"] = quiz.categoryNumber.toString()
                        queryMap["type"] = "multiple"

                        totalQuestions = 10

                        startCustomQuiz(queryMap)
                    }

                    else -> {

                        val queryMap = HashMap<String, String>()

                        queryMap["amount"] = quiz?.numberOfQuestion.toString()
                        queryMap["category"] = quiz?.categoryNumber.toString()
                        queryMap["difficulty"] = quiz?.difficulty.toString()
                        queryMap["type"] = "multiple"

                        totalQuestions = quiz?.numberOfQuestion?.toInt()!!

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

            binding.starBtn.id -> {

                showToast(requireContext(), "star btn pressed")
                //todo : show dialog to use the stars
            }

            binding.nextBtn.id -> {

                showToast(requireContext(), "next btn pressed")
                //todo : show the next question
            }

            binding.saveBtn.id -> {

                showToast(requireContext(), "Save btn pressed")
                //todo : save question to database
            }

            binding.backBtn.id -> {

                //ask for confirmation in dialog
                //timer.cancel()
                requireActivity().onBackPressed()
            }
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

                            //assigning the list to the question list
                            questionList = quiz.results

                            displayQuestion(++questionNumber)
                        }
                    } catch (e: Exception) {

                        e.printStackTrace()
                    }
                }

                is Responses.Error -> {

                    showToast(requireContext(), it.message.toString(), Toast.LENGTH_LONG)

                    requireActivity().onBackPressed()
                }
            }


        }

    }

    private fun displayQuestion(questionNumber: Int) {

        //set timer
        // set progress bar
        //set question number
        // set score

        binding.progressBar.progress = 30000

        setTimer()

        updateQuestionUI(questionNumber)
    }

    private fun updateQuestionUI(questionNumber: Int) {

        val question = questionList[questionNumber]

        question.let {

            binding.questionTV.text = it.question
            binding.difficultyTV.text = it.difficulty
            binding.questionNumberTV.text = if (questionNumber < 10) {

                "0$questionNumber/$totalQuestions"
            } else {

                "$questionNumber/$totalQuestions"
            }

            //set randomly the correct answer
            val randomNumber = Random.nextInt(1, 4)

            setUpOptions(randomNumber, question)

        }
    }

    private fun setUpOptions(randomNumber: Int, question: Result) {

        when (randomNumber) {

            1 -> {

                optionsBinding.optionATV.text = question.correct_answer

                optionsBinding.optionBTV.text = question.incorrect_answers[0]
                optionsBinding.optionCTV.text = question.incorrect_answers[1]
                optionsBinding.optionDTV.text = question.incorrect_answers[2]
            }

            2 -> {

                optionsBinding.optionBTV.text = question.correct_answer

                optionsBinding.optionATV.text = question.incorrect_answers[0]
                optionsBinding.optionCTV.text = question.incorrect_answers[1]
                optionsBinding.optionDTV.text = question.incorrect_answers[2]
            }

            3 -> {

                optionsBinding.optionCTV.text = question.correct_answer

                optionsBinding.optionBTV.text = question.incorrect_answers[0]
                optionsBinding.optionATV.text = question.incorrect_answers[1]
                optionsBinding.optionDTV.text = question.incorrect_answers[2]
            }

            else -> {

                optionsBinding.optionDTV.text = question.correct_answer

                optionsBinding.optionBTV.text = question.incorrect_answers[0]
                optionsBinding.optionCTV.text = question.incorrect_answers[1]
                optionsBinding.optionATV.text = question.incorrect_answers[2]
            }
        }
    }

    private fun setTimer() {

        timer = object : CountDownTimer(30000, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                binding.progressBar.progress = millisUntilFinished.toInt()

                //change the color of progress bar according to the progress finished
            }

            override fun onFinish() {

                Log.i(TAG, "onFinish: ")
                // display next question
            }
        }.start()

    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}