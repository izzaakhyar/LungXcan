package com.bangkit.lungxcan.ui.disease

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.lungxcan.data.request.DiseaseRequest
import com.bangkit.lungxcan.databinding.ItemInfoDiseaseBinding
import com.bangkit.lungxcan.ui.diseasedetail.DiseaseDetailActivity
import com.bumptech.glide.Glide

class DiseaseAdapter(private val listDisease: ArrayList<DiseaseRequest>) : ListAdapter<DiseaseRequest, DiseaseAdapter.ListViewHolder>(DIFF_CALLBACK) {
    class ListViewHolder(private val binding: ItemInfoDiseaseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(diseaseRequest: DiseaseRequest) {
            binding.apply {
                tvDisease.text = diseaseRequest.disease
                Glide.with(itemView.context)
                    .load(diseaseRequest.diseaseIcon)
//                    .apply(
//                        RequestOptions.placeholderOf(R.drawable.ic_loading)
//                            .error(R.drawable.ic_error_24)
//                    )
                    .into(ivDisease)
            }
            //binding.tvDisease.text = diseaseRequest.disease



            //binding.tvHistoryDate.text = dummyHistory.date


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemInfoDiseaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val diseases = getItem(position)
        holder.bind(diseases)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DiseaseDetailActivity::class.java)
            intent.putExtra(DiseaseDetailActivity.KEY_DISEASE, listDisease[position])
            holder.itemView.context.startActivity(intent)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<DiseaseRequest> =
            object : DiffUtil.ItemCallback<DiseaseRequest>() {
                override fun areItemsTheSame(
                    oldItem: DiseaseRequest,
                    newItem: DiseaseRequest
                ): Boolean {
                    return oldItem.disease == newItem.disease
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: DiseaseRequest,
                    newItem: DiseaseRequest
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}