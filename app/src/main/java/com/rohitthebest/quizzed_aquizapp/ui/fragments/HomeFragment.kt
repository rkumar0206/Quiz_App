package com.rohitthebest.quizzed_aquizapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.rohitthebest.quizzed_aquizapp.R
import com.rohitthebest.quizzed_aquizapp.dataStorage.preferenceDatastore.StoreScoreAndStar
import com.rohitthebest.quizzed_aquizapp.databinding.FragmentHomeBinding
import com.rohitthebest.quizzed_aquizapp.databinding.HomeLayoutBinding
import com.rohitthebest.quizzed_aquizapp.helperClasses.CustomQuizParameters
import com.rohitthebest.quizzed_aquizapp.helperClasses.Type
import com.rohitthebest.quizzed_aquizapp.util.CheckNetworkConnection.isInternetAvailable
import com.rohitthebest.quizzed_aquizapp.util.Functions.Companion.showNoInternetMessage
import com.rohitthebest.quizzed_aquizapp.util.GsonConverters.Companion.convertCustomQuizParameterToString

class HomeFragment : Fragment(R.layout.fragment_home), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var includeBinding: HomeLayoutBinding

    private lateinit var highScoreAndStarDataStore: StoreScoreAndStar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)
        includeBinding = binding.include

        highScoreAndStarDataStore = StoreScoreAndStar(requireContext())
        observeHighScoreAndStar()

        initListeners()
    }

    private fun observeHighScoreAndStar() {

        try {
            highScoreAndStarDataStore.flow.asLiveData().observe(viewLifecycleOwner) {

                binding.include.highScoreTV.text = it.high_score.toString()
                binding.include.starTV.text = it.star.toString()
            }
        } catch (e: Exception) {

            e.printStackTrace()
        }
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

                if (isInternetAvailable(requireContext())) {

                    val action = HomeFragmentDirections.actionHomeFragmentToQuizFragment(
                        convertCustomQuizParameterToString(
                            CustomQuizParameters(
                                Type.ANY
                            )
                        )
                    )

                    findNavController().navigate(action)
                } else {

                    showNoInternetMessage(requireContext())
                }
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