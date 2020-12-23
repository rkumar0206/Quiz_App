package com.rohitthebest.quizzed_aquizapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.rohitthebest.quizzed_aquizapp.R
import com.rohitthebest.quizzed_aquizapp.adapters.SelectCategoryInCustomQuizAdapter
import com.rohitthebest.quizzed_aquizapp.databinding.CustomQuizLayoutBinding
import com.rohitthebest.quizzed_aquizapp.databinding.FragmentCustomQuizBinding
import com.rohitthebest.quizzed_aquizapp.util.Functions.Companion.populateCategoryList

private const val TAG = "CustomQuizFragment"

class CustomQuizFragment : Fragment(R.layout.fragment_custom_quiz), View.OnClickListener,
    SelectCategoryInCustomQuizAdapter.OnClickListener {

    private var _binding: FragmentCustomQuizBinding? = null
    private val binding get() = _binding!!
    private lateinit var includeBinding: CustomQuizLayoutBinding
    private lateinit var difficultyArray: Array<String>

    private lateinit var categoryList: ArrayList<Category>
    private lateinit var mAdapter: SelectCategoryInCustomQuizAdapter

    private var categoryNumber = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCustomQuizBinding.bind(view)
        includeBinding = binding.include

        difficultyArray = arrayOf("easy", "medium", "hard")

        categoryList = ArrayList()
        categoryList = populateCategoryList()
        categoryList.add(0, Category(R.drawable.nature, "Any", -1))

        mAdapter = SelectCategoryInCustomQuizAdapter()

        initListeners()
        initNumberPickers()
        setCategoryListRecyclerView()
    }

    private fun setCategoryListRecyclerView() {

        try {
            mAdapter.submitList(categoryList)

            includeBinding.categoryRV.apply {

                setHasFixedSize(true)
                adapter = mAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
            }

            mAdapter.setOnClickListener(this)
        } catch (e: Exception) {

            e.printStackTrace()
        }
    }

    override fun onItemClick(model: Category) {

        categoryNumber = model.categoryNumber
    }

    private fun initNumberPickers() {

        includeBinding.numberOfQuestionsNumberPicker.maxValue = 20
        includeBinding.numberOfQuestionsNumberPicker.minValue = 5


        includeBinding.difficultyNumberPicker.displayedValues = difficultyArray
    }

    private fun initListeners() {

        includeBinding.startQuizBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {


    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

    }

}