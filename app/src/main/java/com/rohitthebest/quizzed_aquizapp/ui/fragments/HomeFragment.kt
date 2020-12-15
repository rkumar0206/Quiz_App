package com.rohitthebest.quizzed_aquizapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rohitthebest.quizzed_aquizapp.R
import com.rohitthebest.quizzed_aquizapp.databinding.FragmentHomeBinding
import com.rohitthebest.quizzed_aquizapp.databinding.HomeLayoutBinding

class HomeFragment : Fragment(R.layout.fragment_home), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var includeBinding: HomeLayoutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)
        includeBinding = binding.include

        initListeners()
    }

    private fun initListeners() {

        includeBinding.startQuizOption.setOnClickListener(this)
        includeBinding.chooseCategoryOption.setOnClickListener(this)
        includeBinding.customizeQuizOption.setOnClickListener(this)
        includeBinding.savedQuestionsOption.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            includeBinding.startQuizOption.id -> {

                findNavController().navigate(R.id.action_homeFragment_to_quizFragment)
            }

            includeBinding.chooseCategoryOption.id -> {

                findNavController().navigate(R.id.action_homeFragment_to_chooseCategoryFragment)
            }

            includeBinding.customizeQuizOption.id -> {

                findNavController().navigate(R.id.action_homeFragment_to_customQuizFragment)
            }

            includeBinding.savedQuestionsOption.id -> {

                //todo : open saved questions fragment
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}