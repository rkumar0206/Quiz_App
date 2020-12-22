package com.rohitthebest.quizzed_aquizapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.rohitthebest.adapters.ChooseCategoryAdapter
import com.rohitthebest.helperClasses.CustomQuizParameters
import com.rohitthebest.helperClasses.Type
import com.rohitthebest.quizzed_aquizapp.R
import com.rohitthebest.quizzed_aquizapp.databinding.FragmentCategoryChooseBinding
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

        populateCategoryList()

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

    private fun populateCategoryList() {

        categoryList.add(Category(R.drawable.general_knowledge, "General Knowledge", 9))
        categoryList.add(Category(R.drawable.animals, "Animals", 27))
        categoryList.add(Category(R.drawable.art, "Art", 25))
        categoryList.add(Category(R.drawable.books, "Books", 10))
        categoryList.add(Category(R.drawable.computer, "Computer", 18))
        categoryList.add(Category(R.drawable.film, "Film", 11))
        categoryList.add(Category(R.drawable.politics, "Politics", 24))
        categoryList.add(Category(R.drawable.gadget2, "Gadget", 30))
        categoryList.add(Category(R.drawable.geography, "Geography", 22))
        categoryList.add(Category(R.drawable.mathematics, "Mathematics", 19))
        categoryList.add(Category(R.drawable.music, "Music", 12))
        categoryList.add(Category(R.drawable.mythology, "Mythology", 20))
        categoryList.add(Category(R.drawable.nature, "Nature", 17))
        categoryList.add(Category(R.drawable.sports, "Sports", 21))
        categoryList.add(Category(R.drawable.vehicles, "Vehicles", 28))
        categoryList.add(Category(R.drawable.video_games, "Video-games", 15))
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}