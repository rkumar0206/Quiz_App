package com.rohitthebest.quizzed_aquizapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.rohitthebest.quizzed_aquizapp.R
import com.rohitthebest.quizzed_aquizapp.databinding.FragmentCategoryChooseBinding

class ChooseCategoryFragment : Fragment(R.layout.fragment_category_choose) {

    private lateinit var binding : FragmentCategoryChooseBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCategoryChooseBinding.bind(view)
    }
}