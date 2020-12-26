package com.rohitthebest.quizzed_aquizapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.rohitthebest.quizzed_aquizapp.R
import com.rohitthebest.quizzed_aquizapp.databinding.FragmentSavedQuestionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedQuestionsFragment : Fragment(R.layout.fragment_saved_questions) {

    private var _binding: FragmentSavedQuestionsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSavedQuestionsBinding.bind(view)


    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}