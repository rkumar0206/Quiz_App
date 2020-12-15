package com.rohitthebest.quizzed_aquizapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.rohitthebest.quizzed_aquizapp.R
import com.rohitthebest.quizzed_aquizapp.databinding.FragmentCustomQuizBinding

class CustomQuizFragment : Fragment(R.layout.fragment_custom_quiz) {

    private var _binding: FragmentCustomQuizBinding? = null
    private val binding get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCustomQuizBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

    }
}