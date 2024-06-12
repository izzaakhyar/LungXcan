package com.bangkit.lungxcan.ui.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.lungxcan.data.DummyHistory
import com.bangkit.lungxcan.databinding.ItemHistoryBinding

class HistoryAdapter : ListAdapter<DummyHistory, HistoryAdapter.ListViewHolder>(DIFF_CALLBACK) {
    class ListViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dummyHistory: DummyHistory) {
            binding.tvResultProb.text =
                "${dummyHistory.prob}% Probability of ${dummyHistory.diseases}"
//            Glide.with(itemView.context)
//                .load(articleItem.urlToImage)
//                .apply(
//                    RequestOptions.placeholderOf(R.drawable.ic_loading)
//                        .error(R.drawable.ic_error_24)
//                )
//                .into(binding.imageView)

            binding.tvHistoryDate.text = dummyHistory.date


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val histories = getItem(position)
        holder.bind(histories)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<DummyHistory> =
            object : DiffUtil.ItemCallback<DummyHistory>() {
                override fun areItemsTheSame(
                    oldItem: DummyHistory,
                    newItem: DummyHistory
                ): Boolean {
                    return oldItem.diseases == newItem.diseases
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: DummyHistory,
                    newItem: DummyHistory
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}