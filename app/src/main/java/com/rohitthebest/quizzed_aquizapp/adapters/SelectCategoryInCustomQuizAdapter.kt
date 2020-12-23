package com.rohitthebest.quizzed_aquizapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rohitthebest.quizzed_aquizapp.databinding.AdapterSelectCategoryCustomQuizBinding
import com.rohitthebest.quizzed_aquizapp.ui.fragments.Category

class SelectCategoryInCustomQuizAdapter :
    ListAdapter<Category, SelectCategoryInCustomQuizAdapter.SelectCategoryViewHolder>(
        DiffUtilCallback()
    ) {

    private var mListener: OnClickListener? = null
    private var selectedItemPosition = 0

    inner class SelectCategoryViewHolder(var binding: AdapterSelectCategoryCustomQuizBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.selectCategoryParentView.setOnClickListener {

                if (checkForNullability(absoluteAdapterPosition)) {

                    mListener!!.onItemClick(getItem(absoluteAdapterPosition))

                    if (selectedItemPosition != absoluteAdapterPosition) {

                        notifyItemChanged(absoluteAdapterPosition)

                        if (selectedItemPosition != -1) {

                            notifyItemChanged(selectedItemPosition)
                        }

                        selectedItemPosition = absoluteAdapterPosition
                    }

                }
            }
        }

        private fun checkForNullability(position: Int): Boolean {

            return position != RecyclerView.NO_POSITION &&
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectCategoryViewHolder {

        return SelectCategoryViewHolder(
            AdapterSelectCategoryCustomQuizBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SelectCategoryViewHolder, position: Int) {

        holder.binding.apply {

            categoryNameTextView.text = getItem(position).categoryName

            if (selectedItemPosition == position) {

                selectCategoryParentView.setCardBackgroundColor(Color.parseColor("#FF6200EE"))
                selectCategoryParentView.strokeWidth = 2
                selectCategoryParentView.strokeColor = Color.parseColor("#FFC107")
                categoryNameTextView.setTextColor(Color.parseColor("#FFFFFF"))

            } else {

                selectCategoryParentView.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                selectCategoryParentView.strokeWidth = 1
                selectCategoryParentView.strokeColor = Color.DKGRAY
                categoryNameTextView.setTextColor(Color.parseColor("#212121"))
            }
        }
    }

    interface OnClickListener {

        fun onItemClick(model: Category)
    }

    fun setOnClickListener(listener: OnClickListener) {
        mListener = listener
    }
}
