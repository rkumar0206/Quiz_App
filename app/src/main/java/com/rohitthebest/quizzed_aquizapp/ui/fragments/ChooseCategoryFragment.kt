package com.rohitthebest.quizzed_aquizapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.rohitthebest.quizzed_aquizapp.R
import com.rohitthebest.quizzed_aquizapp.adapters.ChooseCategoryAdapter
import com.rohitthebest.quizzed_aquizapp.databinding.FragmentCategoryChooseBinding
import com.rohitthebest.quizzed_aquizapp.helperClasses.CustomQuizParameters
import com.rohitthebest.quizzed_aquizapp.helperClasses.Type
import com.rohitthebest.quizzed_aquizapp.util.Functions.Companion.populateCategoryList
import com.rohitthebest.quizzed_aquizapp.util.GsonConverters

private const val TAG = "ChooseCategoryFragment"

data class Category(var imageId: Int, var categoryName: String, var categoryNumber: Int = 9)

class ChooseCategoryFragment : Fragment(R.layout.fragment_category_choose),
    ChooseCategoryAdapter.OnClickListener {

    private var _binding: FragmentCategoryChooseBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryList: ArrayList<Category>

    private lateinit var mAdapter: ChooseCategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCategoryChooseBinding.bind(view)

        categoryList = ArrayList()

        categoryList = populateCategoryList()

        mAdapter = ChooseCategoryAdapter()

        setUpRecyclerView()

    }

    private fun setUpRecyclerView() {

        try {

            mAdapter.submitList(categoryList)

            binding.chooseCategoryRV.apply {

                setHasFixedSize(true)
                adapter = mAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
            }

            mAdapter.setOnClickListener(this)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onItemClick(category: Category) {

        val action = ChooseCategoryFragmentDirections.actionChooseCategoryFragmentToQuizFragment(
            GsonConverters.convertCustomQuizParameterToString(
                CustomQuizParameters(
                    Type.CHOOSE_CATEGORY,
                    category.categoryNumber.toString(),
                    "10"
                )
            )
        )

        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}