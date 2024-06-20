package com.bangkit.lungxcan.ui.result.hospital

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.lungxcan.BuildConfig
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.data.request.HospitalRequest
import com.bangkit.lungxcan.databinding.ItemHospitalBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class HospitalAdapter :
    ListAdapter<HospitalRequest, HospitalAdapter.ListViewHolder>(DIFF_CALLBACK) {

    class ListViewHolder(private val binding: ItemHospitalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hospitalRequest: HospitalRequest) {
            binding.tvHospitalName.text = hospitalRequest.name
            binding.tvAddress.text = hospitalRequest.address
            Glide.with(binding.root.context)
                .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=${hospitalRequest.imageLink}&key=${BuildConfig.MAP_API_KEY}")
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error_24)
                )
                .into(binding.imageView)
            binding.btnOpenMap.setOnClickListener {
                val gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(hospitalRequest.name))
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                binding.root.context.startActivity(mapIntent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemHospitalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val hospitalItem = getItem(position)
        holder.bind(hospitalItem)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<HospitalRequest> =
            object : DiffUtil.ItemCallback<HospitalRequest>() {
                override fun areItemsTheSame(
                    oldItem: HospitalRequest,
                    newItem: HospitalRequest
                ): Boolean {
                    return oldItem.name == newItem.name
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: HospitalRequest,
                    newItem: HospitalRequest
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}