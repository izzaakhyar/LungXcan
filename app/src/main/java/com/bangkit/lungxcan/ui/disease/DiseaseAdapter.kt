package com.bangkit.lungxcan.ui.disease

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.lungxcan.data.DummyDisease
import com.bangkit.lungxcan.databinding.ItemInfoDiseaseBinding

class DiseaseAdapter : ListAdapter<DummyDisease, DiseaseAdapter.ListViewHolder>(DIFF_CALLBACK) {
    class ListViewHolder(private val binding: ItemInfoDiseaseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dummyDisease: DummyDisease) {
            binding.tvDisease.text = dummyDisease.diseases
//            Glide.with(itemView.context)
//                .load(articleItem.urlToImage)
//                .apply(
//                    RequestOptions.placeholderOf(R.drawable.ic_loading)
//                        .error(R.drawable.ic_error_24)
//                )
//                .into(binding.imageView)

            //binding.tvHistoryDate.text = dummyHistory.date


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemInfoDiseaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val histories = getItem(position)
        holder.bind(histories)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<DummyDisease> =
            object : DiffUtil.ItemCallback<DummyDisease>() {
                override fun areItemsTheSame(
                    oldItem: DummyDisease,
                    newItem: DummyDisease
                ): Boolean {
                    return oldItem.diseases == newItem.diseases
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: DummyDisease,
                    newItem: DummyDisease
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}