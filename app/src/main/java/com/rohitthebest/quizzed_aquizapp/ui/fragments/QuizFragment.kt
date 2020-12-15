package com.rohitthebest.quizzed_aquizapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.rohitthebest.quizzed_aquizapp.R
import com.rohitthebest.quizzed_aquizapp.databinding.FragmentQuizBinding

class QuizFragment : Fragment(R.layout.fragment_quiz) {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentQuizBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}