package com.rohitthebest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rohitthebest.quizzed_aquizapp.databinding.ChooseCategoryLayoutForRvBinding
import com.rohitthebest.quizzed_aquizapp.ui.fragments.Category

class ChooseCategoryAdapter :
    ListAdapter<Category, ChooseCategoryAdapter.ChooseCategoryViewHolder>(DiffUtilCallback()) {

    private var mListener: OnClickListener? = null

    inner class ChooseCategoryViewHolder(var binding: ChooseCategoryLayoutForRvBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(category: Category?) {

            category?.let {

                binding.categoryImage.setImageResource(it.imageId)
                binding.categoryName.text = it.categoryName
            }
        }

        init {

            binding.chooseCategoryItemCV.setOnClickListener {

                if (checkForNull(absoluteAdapterPosition)) {

                    mListener!!.onItemClick(category = getItem(absoluteAdapterPosition))
                }
            }
        }

        private fun checkForNull(adapterPosition: Int): Boolean {

            return adapterPosition != RecyclerView.NO_POSITION &&
                    mListener != null
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Category>() {

        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {

            return oldItem.imageId == newItem.imageId
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {

            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseCategoryViewHolder {

        val binding = ChooseCategoryLayoutForRvBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ChooseCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChooseCategoryViewHolder, position: Int) {

        holder.setData(getItem(position))
    }

    interface OnClickListener {

        fun onItemClick(category: Category)
    }

    fun setOnClickListener(listener: OnClickListener) {
        mListener = listener
    }
}

