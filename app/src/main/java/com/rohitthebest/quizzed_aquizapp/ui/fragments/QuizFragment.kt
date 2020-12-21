package com.rohitthebest.quizzed_aquizapp.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rohitthebest.helperClasses.Type
import com.rohitthebest.quizzed_aquizapp.R
import com.rohitthebest.quizzed_aquizapp.dataStorage.preferenceDatastore.StoreScoreAndStar
import com.rohitthebest.quizzed_aquizapp.databinding.FragmentQuizBinding
import com.rohitthebest.quizzed_aquizapp.databinding.OptionsLayoutBinding
import com.rohitthebest.quizzed_aquizapp.module.QuizApiViewModel
import com.rohitthebest.quizzed_aquizapp.remote.Responses
import com.rohitthebest.quizzed_aquizapp.remote.model.Result
import com.rohitthebest.quizzed_aquizapp.util.ExtensionFunctions.Companion.changeColor
import com.rohitthebest.quizzed_aquizapp.util.ExtensionFunctions.Companion.disable
import com.rohitthebest.quizzed_aquizapp.util.ExtensionFunctions.Companion.enable
import com.rohitthebest.quizzed_aquizapp.util.ExtensionFunctions.Companion.hide
import com.rohitthebest.quizzed_aquizapp.util.ExtensionFunctions.Companion.invisible
import com.rohitthebest.quizzed_aquizapp.util.ExtensionFunctions.Companion.setColor
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

    private lateinit var highScoreAndStarDataStore: StoreScoreAndStar

    private var questionList: List<Result> = emptyList()
    private var questionNumber = -1
    private lateinit var timer: CountDownTimer
    private var totalQuestions = 10
    private var correctAnswer = ""

    private var oldHighScore = 0
    private var score = 0
    private var star = 0
    private var correctAnswers = 0
    private var wrongAnswers = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentQuizBinding.bind(view)

        optionsBinding = binding.includeOption

        questionList = ArrayList()

        binding.progressBar.max = 31000

        highScoreAndStarDataStore = StoreScoreAndStar(requireContext())

        disableNextButton()

        initListeners()

        getMessage()

        observeHighScoreAndStar()

        observeApiResponse()
    }

    private fun observeHighScoreAndStar() {

        try {
            highScoreAndStarDataStore.flow.asLiveData().observe(viewLifecycleOwner) {

                oldHighScore = it.high_score
                star = it.star
                binding.numberOfStarTV.text = star.toString()
            }
        } catch (e: Exception) {

            e.printStackTrace()
        }

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

                val timeLeft = binding.progressBar.progress

                timer.cancel()

                showDialogForUsingStars(timeLeft)
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

                if (checkAnswerWithClickedOption(optionsBinding.optionATV.text.toString().trim())) {

                    increaseScore()
                } else {

                    optionsBinding.optionA.setColor(R.color.color_orange)
                    wrongAnswers++
                }

                setNextButtonTimer()
            }

            optionsBinding.optionB.id -> {

                if (checkAnswerWithClickedOption(optionsBinding.optionBTV.text.toString().trim())) {

                    increaseScore()
                } else {

                    optionsBinding.optionB.setColor(R.color.color_orange)
                    wrongAnswers++
                }

                setNextButtonTimer()
            }

            optionsBinding.optionC.id -> {

                if (checkAnswerWithClickedOption(optionsBinding.optionCTV.text.toString().trim())) {

                    increaseScore()
                } else {

                    optionsBinding.optionC.setColor(R.color.color_orange)
                    wrongAnswers++
                }

                setNextButtonTimer()

            }

            optionsBinding.optionD.id -> {

                if (checkAnswerWithClickedOption(optionsBinding.optionDTV.text.toString().trim())) {

                    increaseScore()
                } else {

                    optionsBinding.optionD.setColor(R.color.color_orange)
                    wrongAnswers++
                }

                setNextButtonTimer()
            }
        }
    }

    private fun showDialogForUsingStars(timeLeft: Int) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Want to use your stars?")
            .setMessage("Use your 3 stars to get the answer.")
            .setPositiveButton("Use 3 stars") { dialog, _ ->

                star -= 3
                saveData(oldHighScore, star)

                increaseScore()

                setNextButtonTimer()
                dialog.dismiss()

            }.setNegativeButton("Cancel") { dialog, _ ->

                setTimer(timeLeft.toLong())
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun checkAnswerWithClickedOption(clickedOption: String): Boolean {

        try {

            timer.cancel()
        } catch (e: Exception) {

            e.printStackTrace()
        }

        when {

            optionsBinding.optionATV.text.toString()
                .trim() == questionList[questionNumber].correct_answer.trim() -> optionsBinding.optionA.setColor(
                R.color.color_green
            )

            optionsBinding.optionBTV.text.toString()
                .trim() == questionList[questionNumber].correct_answer.trim() -> optionsBinding.optionB.setColor(
                R.color.color_green
            )

            optionsBinding.optionCTV.text.toString()
                .trim() == questionList[questionNumber].correct_answer.trim() -> optionsBinding.optionC.setColor(
                R.color.color_green
            )

            optionsBinding.optionDTV.text.toString()
                .trim() == questionList[questionNumber].correct_answer.trim() -> optionsBinding.optionD.setColor(
                R.color.color_green
            )
        }

        return clickedOption == questionList[questionNumber].correct_answer.trim()
    }

    @SuppressLint("SetTextI18n")
    private fun increaseScore() {

        correctAnswers++

        score += 2
        binding.scoreTV.text = "Score : $score"

        if (score == 8) {

            showStarAnimation()
            star++

            saveData(oldHighScore, star)
        }

        if (score == 16) {

            showStarAnimation()

            star += 2

            saveData(oldHighScore, star)
        }
    }

    private fun showStarAnimation() {

        binding.starAnimation.show()
        binding.starBtn.invisible()
        binding.numberOfStarTV.invisible()

        GlobalScope.launch {

            delay(1200)

            withContext(Dispatchers.Main) {

                binding.starAnimation.hide()
                binding.starBtn.show()
                binding.numberOfStarTV.show()

                binding.starAnimation.progress = 0.0f
            }
        }
    }

    private fun observeApiResponse() {

        try {
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
        } catch (e: Exception) {

            e.printStackTrace()
        }
    }

    private fun displayQuestion(questionNumber: Int) {

        Log.i(TAG, "displayQuestion: $questionNumber")

        binding.progressBar.progress = 31000
        disableNextButton()
        enableAllTheRequiredButtons()
        resetTheColorBackgroundOfOptionsCardView()
        setTimer(31000)
        updateQuestionUI(questionNumber)
    }

    private fun resetTheColorBackgroundOfOptionsCardView() {

        optionsBinding.optionA.setColor(R.color.white)
        optionsBinding.optionB.setColor(R.color.white)
        optionsBinding.optionC.setColor(R.color.white)
        optionsBinding.optionD.setColor(R.color.white)
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
            val randomNumber = Random.nextInt(1, 5) //5 is exclusive

            Log.i(TAG, "updateQuestionUI: random number : $randomNumber")

            setUpOptions(randomNumber, question)
        }
    }

    private fun setUpOptions(randomNumber: Int, question: Result) {

        correctAnswer = question.correct_answer

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

    private fun setTimer(millisInFuture: Long) {

        timer = object : CountDownTimer(millisInFuture, 100) {

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

        disableAllTheRequiredButtons()

        checkAnswerWithClickedOption("")

        enableNextButton()

        try {
            nextButtonTimer = object : CountDownTimer(6000, 1000) {

                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {

                    binding.nextBtnTV.text = "Next (${millisUntilFinished / 1000})"
                }

                override fun onFinish() {

                    checkAndDisplayNextQuestion()
                }
            }.start()
        } catch (e: Exception) {

            e.printStackTrace()
        }

    }

    private fun disableAllTheRequiredButtons() {

        try {

            optionsBinding.optionA.disable()
            optionsBinding.optionB.disable()
            optionsBinding.optionC.disable()
            optionsBinding.optionD.disable()
            binding.starBtn.disable()
        } catch (e: Exception) {

            e.printStackTrace()
        }
    }

    private fun enableAllTheRequiredButtons() {

        try {

            optionsBinding.optionA.enable()
            optionsBinding.optionB.enable()
            optionsBinding.optionC.enable()
            optionsBinding.optionD.enable()
            binding.starBtn.enable()
        } catch (e: Exception) {

            e.printStackTrace()
        }
    }

    private fun checkAndDisplayNextQuestion() {

        if ((++questionNumber + 1) <= totalQuestions) {

            displayQuestion(questionNumber)
        } else {

            showToast(requireContext(), "Your result")

            showResult()


            if (score > oldHighScore) {

                saveData(score, star)
            }
        }
    }

    private fun showResult() {

        MaterialDialog(requireContext(), BottomSheet()).show {

            title(text = "Your result")

            customView(
                R.layout.result_layout,
                scrollable = true
            )
            getCustomView().findViewById<TextView>(R.id.correctAnswersTV).text =
                correctAnswers.toString()
            getCustomView().findViewById<TextView>(R.id.incoreectAnwersTV).text =
                wrongAnswers.toString()
            getCustomView().findViewById<TextView>(R.id.resultScoreTV).text = score.toString()

        }.positiveButton(text = "Start again") {

            //getMessage()

        }.negativeButton(text = "Go back") {

            requireActivity().onBackPressed()
        }


    }

    private fun saveData(highScore: Int, star: Int) {

        GlobalScope.launch {

            highScoreAndStarDataStore.storeData(highScore, star)
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

        try {

            timer.cancel()
            nextButtonTimer.cancel()

        } catch (e: java.lang.Exception) {

            e.printStackTrace()
        }

        _binding = null
    }

    override fun onPause() {
        super.onPause()

        try {
            requireActivity().onBackPressed()
        } catch (e: Exception) {

            e.printStackTrace()
        }
    }
}