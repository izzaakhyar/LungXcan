package com.bangkit.lungxcan.ui.result

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.data.DummyHospital
import com.bangkit.lungxcan.databinding.ItemHospitalBinding


class HospitalAdapter : ListAdapter<DummyHospital, HospitalAdapter.ListViewHolder>(DIFF_CALLBACK) {
    class ListViewHolder(private val binding: ItemHospitalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hospitalItem: DummyHospital) {
            binding.tvHospitalName.text = hospitalItem.name
            binding.imageView.setImageResource(R.drawable.ic_image_24)
            //binding.tvDetailDescription.text = storyItem.description
//            Glide.with(itemView.context)
//                .load(storyItem.photoUrl)
//                .apply(
//                    RequestOptions.placeholderOf(R.drawable.ic_loading)
//                        .error(R.drawable.ic_error_24)
//                )
//                .into(binding.ivItemPhoto)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemHospitalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val hospitals = getItem(position)
        holder.bind(hospitals)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<DummyHospital> =
            object : DiffUtil.ItemCallback<DummyHospital>() {
                override fun areItemsTheSame(
                    oldItem: DummyHospital,
                    newItem: DummyHospital
                ): Boolean {
                    return oldItem.name == newItem.name
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: DummyHospital,
                    newItem: DummyHospital
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}