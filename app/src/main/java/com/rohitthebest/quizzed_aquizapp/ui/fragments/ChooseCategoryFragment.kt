package com.rohitthebest.quizzed_aquizapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.rohitthebest.quizzed_aquizapp.R
import com.rohitthebest.quizzed_aquizapp.databinding.FragmentCategoryChooseBinding

data class Category(var imageId: Int, var categoryName: String)

class ChooseCategoryFragment : Fragment(R.layout.fragment_category_choose) {

    private var _binding: FragmentCategoryChooseBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryList: ArrayList<Category>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCategoryChooseBinding.bind(view)

        categoryList = ArrayList()

        populateCategoryList()


    }

    private fun populateCategoryList() {

        categoryList.add(Category(R.drawable.animals, "Animals"))
        categoryList.add(Category(R.drawable.art, "Art"))
        categoryList.add(Category(R.drawable.books, "Books"))
        categoryList.add(Category(R.drawable.computer, "Computer"))
        categoryList.add(Category(R.drawable.film, "Film"))
        categoryList.add(Category(R.drawable.gadget2, "Gadget"))
        categoryList.add(Category(R.drawable.geography, "Geography"))
        categoryList.add(Category(R.drawable.mathematics, "Mathematics"))
        categoryList.add(Category(R.drawable.music, "Music"))
        categoryList.add(Category(R.drawable.mythology, "Mythology"))
        categoryList.add(Category(R.drawable.nature, "Nature"))
        categoryList.add(Category(R.drawable.sports, "Sports"))
        categoryList.add(Category(R.drawable.vehicles, "Vehicles"))
        categoryList.add(Category(R.drawable.video_games, "Video-games"))
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}