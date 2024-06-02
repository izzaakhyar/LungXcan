package com.bangkit.lungxcan.ui.article

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.data.DummyArticle
import com.bangkit.lungxcan.databinding.ItemArticleBinding

class ArticleAdapter : ListAdapter<DummyArticle, ArticleAdapter.ListViewHolder>(DIFF_CALLBACK) {
    class ListViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(articleItem: DummyArticle) {
            binding.tvArticleTitle.text = articleItem.title
            binding.tvArticleDesc.text =
                "Preview singkat berita dummy. Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet"
            binding.ivArticleThumbnail.setImageResource(R.drawable.ic_image_24)
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
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val articles = getItem(position)
        holder.bind(articles)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<DummyArticle> =
            object : DiffUtil.ItemCallback<DummyArticle>() {
                override fun areItemsTheSame(
                    oldItem: DummyArticle,
                    newItem: DummyArticle
                ): Boolean {
                    return oldItem.title == newItem.title
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: DummyArticle,
                    newItem: DummyArticle
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}