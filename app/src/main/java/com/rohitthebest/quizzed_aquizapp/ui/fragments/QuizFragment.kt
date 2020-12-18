package com.rohitthebest.quizzed_aquizapp.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
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
import com.rohitthebest.quizzed_aquizapp.util.ExtensionFunctions.Companion.changeColor
import com.rohitthebest.quizzed_aquizapp.util.ExtensionFunctions.Companion.disable
import com.rohitthebest.quizzed_aquizapp.util.ExtensionFunctions.Companion.enable
import com.rohitthebest.quizzed_aquizapp.util.ExtensionFunctions.Companion.hide
import com.rohitthebest.quizzed_aquizapp.util.ExtensionFunctions.Companion.show
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

    private var oldHighScore = 0
    private var oldStar = 0
    private var highScore = 0
    private var score = 0
    private var star = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentQuizBinding.bind(view)

        optionsBinding = binding.includeOption

        questionList = ArrayList()

        binding.progressBar.max = 31000

        disableNextButton()

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

                try {

                    nextButtonTimer.cancel()
                    checkAndDisplayNextQuestion()

                } catch (e: Exception) {

                    e.printStackTrace()
                }
            }

            binding.saveBtn.id -> {

                showToast(requireContext(), "Save btn pressed")
                //todo : save question to database
            }

            binding.backBtn.id -> {

                //ask for confirmation in dialog
                try {

                    timer.cancel()
                    requireActivity().onBackPressed()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            //options

            optionsBinding.optionA.id -> {

                //todo : handle option A clicked
            }


            optionsBinding.optionB.id -> {

                //todo : handle option B clicked
            }

            optionsBinding.optionC.id -> {

                //todo : handle option C clicked
            }

            optionsBinding.optionD.id -> {

                //todo : handle option D clicked
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

        Log.i(TAG, "displayQuestion: $questionNumber")

        //set timer
        // set progress bar
        //set question number
        // set score
        // disable next button

        binding.progressBar.progress = 30000
        disableNextButton()

        setTimer()

        updateQuestionUI(questionNumber)
    }

    private fun updateQuestionUI(questionNumber: Int) {

        val question = questionList[questionNumber]

        question.let {

            binding.questionTV.text = it.question
            binding.categoryTV.text = it.category
            binding.questionNumberTV.text = if ((questionNumber + 1) < 10) {

                "0${(questionNumber + 1)}/$totalQuestions"
            } else {

                "${(questionNumber + 1)}/$totalQuestions"
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

        timer = object : CountDownTimer(31000, 100) {

            override fun onTick(millisUntilFinished: Long) {

                when {

                    (millisUntilFinished in 18000..25000) -> {

                        binding.progressBar.changeColor(R.color.color_green)

                    }
                    millisUntilFinished in 13000..18000 -> {

                        binding.progressBar.changeColor(R.color.color_light_yellow)
                    }
                    millisUntilFinished in 7000..13000 -> {

                        binding.progressBar.changeColor(R.color.color_light_orange)
                    }
                    millisUntilFinished < 7000 -> {

                        binding.progressBar.changeColor(R.color.color_orange)
                    }

                    else -> {

                        binding.progressBar.changeColor(R.color.color_dark_green)
                    }
                }

                binding.progressBar.progress = millisUntilFinished.toInt()

            }

            override fun onFinish() {

                Log.i(TAG, "onFinish: ")

                setNextButtonTimer()
            }
        }.start()

    }

    private lateinit var nextButtonTimer: CountDownTimer

    private fun setNextButtonTimer() {

        //todo : disable every options and also star button and also show the correct answer

        enableNextButton()

        nextButtonTimer = object : CountDownTimer(6000, 1000) {

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {

                binding.nextBtnTV.text = "Next (${millisUntilFinished / 1000})"
            }

            override fun onFinish() {

                checkAndDisplayNextQuestion()
            }
        }.start()

    }

    private fun checkAndDisplayNextQuestion() {

        if ((++questionNumber + 1) <= totalQuestions) {

            displayQuestion(questionNumber)
        } else {

            showToast(requireContext(), "Your result")
            //todo : show the result
        }

    }

    private fun enableNextButton() {

        binding.nextBtn.enable()

        binding.nextBtn.setCardBackgroundColor(context?.getColor(R.color.button_color)!!)
    }

    private fun disableNextButton() {

        binding.nextBtn.disable()

        binding.nextBtn.setCardBackgroundColor(Color.GRAY)

        binding.nextBtnTV.text = "Next"
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}