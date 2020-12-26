package com.rohitthebest.quizzed_aquizapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rohitthebest.quizzed_aquizapp.databinding.SavedQuestionsRvLayoutBinding
import com.rohitthebest.quizzed_aquizapp.remote.model.Result

class SavedQuestionAdapter :
    ListAdapter<Result, SavedQuestionAdapter.SavedQuestionViewHolder>(DiffUtilCallback()) {

    private var mListener: OnClickListener? = null

    inner class SavedQuestionViewHolder(val binding: SavedQuestionsRvLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {

            binding.savedQuestionParentView.setOnClickListener(this)
            binding.copyBtn.setOnClickListener(this)
            binding.deleteBtn.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            when (v?.id) {

                binding.savedQuestionParentView.id -> {

                    if (checkForNull(absoluteAdapterPosition)) {

                        mListener!!.onItemClick(getItem(absoluteAdapterPosition))
                    }
                }

                binding.copyBtn.id -> {

                    mListener!!.onCopyClicked(getItem(absoluteAdapterPosition))
                }

                binding.deleteBtn.id -> {

                    mListener!!.onDeleteClick(getItem(absoluteAdapterPosition))
                }
            }
        }

        private fun checkForNull(adapterPosition: Int): Boolean {

            return adapterPosition != RecyclerView.NO_POSITION &&
                    mListener != null
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Result>() {

        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedQuestionViewHolder {

        return SavedQuestionViewHolder(
            SavedQuestionsRvLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SavedQuestionViewHolder, position: Int) {

    }

    interface OnClickListener {

        fun onItemClick(question: Result)
        fun onCopyClicked(question: Result)
        fun onDeleteClick(question: Result)
    }

    fun setOnClickListener(listener: OnClickListener) {
        mListener = listener
    }
}
