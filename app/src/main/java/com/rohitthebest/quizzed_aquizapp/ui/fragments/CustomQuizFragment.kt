package com.rohitthebest.quizzed_aquizapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.rohitthebest.quizzed_aquizapp.R
import com.rohitthebest.quizzed_aquizapp.databinding.FragmentCustomQuizBinding

class CustomQuizFragment : Fragment(R.layout.fragment_custom_quiz) {

    private lateinit var binding : FragmentCustomQuizBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCustomQuizBinding.bind(view)
    }
}