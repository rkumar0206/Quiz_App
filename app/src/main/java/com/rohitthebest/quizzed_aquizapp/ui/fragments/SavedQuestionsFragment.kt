package com.rohitthebest.quizzed_aquizapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rohitthebest.quizzed_aquizapp.R
import com.rohitthebest.quizzed_aquizapp.adapters.SavedQuestionAdapter
import com.rohitthebest.quizzed_aquizapp.databinding.FragmentSavedQuestionsBinding
import com.rohitthebest.quizzed_aquizapp.remote.model.Result
import com.rohitthebest.quizzed_aquizapp.ui.viewModels.QuestionViewModel
import com.rohitthebest.quizzed_aquizapp.util.ExtensionFunctions.Companion.hide
import com.rohitthebest.quizzed_aquizapp.util.ExtensionFunctions.Companion.show
import com.rohitthebest.quizzed_aquizapp.util.Functions.Companion.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SavedQuestionsFragment : Fragment(R.layout.fragment_saved_questions),
    SavedQuestionAdapter.OnClickListener {

    private val viewModel: QuestionViewModel by viewModels()

    private var _binding: FragmentSavedQuestionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: SavedQuestionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSavedQuestionsBinding.bind(view)

        mAdapter = SavedQuestionAdapter()

        showProgressBar()

        GlobalScope.launch {
            delay(200)

            withContext(Dispatchers.Main) {

                getAllQuestionsList()
            }
        }
    }

    private fun getAllQuestionsList() {

        try {

            viewModel.savedQuestions.observe(viewLifecycleOwner) {

                if (it.isNotEmpty()) {

                    hideNoSavedQuestionTV()
                } else {

                    showNoSavedQuestionTV()
                }

                setUpRecyclerView(it)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setUpRecyclerView(questionList: List<Result>?) {

        try {

            mAdapter.submitList(questionList)

            binding.savedQuestionsRV.apply {

                adapter = mAdapter
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
            }

            hideProgressBar()

            mAdapter.setOnClickListener(this)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onItemClick(question: Result) {

        showToast(requireContext(), question.question)
    }

    override fun onCopyClicked(question: Result) {

        showToast(requireContext(), "OnCopy Clicked")
    }

    override fun onDeleteClick(question: Result) {

        showToast(requireContext(), "OnDelete Clicked")
    }

    private fun showProgressBar() {

        try {

            binding.progressBar.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun hideProgressBar() {

        try {

            binding.progressBar.hide()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showNoSavedQuestionTV() {

        try {

            binding.noQuestionAddedTV.show()
            binding.savedQuestionsRV.hide()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun hideNoSavedQuestionTV() {

        try {

            binding.noQuestionAddedTV.hide()
            binding.savedQuestionsRV.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}