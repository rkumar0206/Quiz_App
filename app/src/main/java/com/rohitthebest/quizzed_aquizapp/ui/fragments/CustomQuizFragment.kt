package com.rohitthebest.quizzed_aquizapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.rohitthebest.quizzed_aquizapp.R
import com.rohitthebest.quizzed_aquizapp.adapters.SelectCategoryInCustomQuizAdapter
import com.rohitthebest.quizzed_aquizapp.databinding.CustomQuizLayoutBinding
import com.rohitthebest.quizzed_aquizapp.databinding.FragmentCustomQuizBinding
import com.rohitthebest.quizzed_aquizapp.helperClasses.CustomQuizParameters
import com.rohitthebest.quizzed_aquizapp.helperClasses.Difficulty
import com.rohitthebest.quizzed_aquizapp.helperClasses.Type
import com.rohitthebest.quizzed_aquizapp.util.Functions.Companion.populateCategoryList
import com.rohitthebest.quizzed_aquizapp.util.GsonConverters.Companion.convertCustomQuizParameterToString

private const val TAG = "CustomQuizFragment"

class CustomQuizFragment : Fragment(R.layout.fragment_custom_quiz), View.OnClickListener,
    SelectCategoryInCustomQuizAdapter.OnClickListener, RadioGroup.OnCheckedChangeListener,
    NumberPicker.OnValueChangeListener {

    private var _binding: FragmentCustomQuizBinding? = null
    private val binding get() = _binding!!
    private lateinit var includeBinding: CustomQuizLayoutBinding

    private lateinit var categoryList: ArrayList<Category>
    private lateinit var mAdapter: SelectCategoryInCustomQuizAdapter

    private var categoryNumber = -1
    private var numberOfQuestion = 5
    private var difficulty = Difficulty.MEDIUM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCustomQuizBinding.bind(view)
        includeBinding = binding.include

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
    }

    private fun initListeners() {

        includeBinding.startQuizBtn.setOnClickListener(this)
        includeBinding.numberOfQuestionsNumberPicker.setOnValueChangedListener(this)
        includeBinding.difficultyRG.setOnCheckedChangeListener(this)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            includeBinding.startQuizBtn.id -> {

                val action = CustomQuizFragmentDirections.actionCustomQuizFragmentToQuizFragment(
                    convertCustomQuizParameterToString(
                        CustomQuizParameters(
                            Type.CUSTOM,
                            categoryNumber.toString(),
                            numberOfQuestion.toString(),
                            difficulty
                        )
                    )
                )

                findNavController().navigate(action)
            }
        }
    }

    override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {

        numberOfQuestion = newVal
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {

        when (checkedId) {

            includeBinding.easyRB.id -> {

                difficulty = Difficulty.EASY
            }

            includeBinding.mediumRB.id -> {

                difficulty = Difficulty.MEDIUM
            }

            else -> {

                difficulty = Difficulty.HARD
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

    }
}
