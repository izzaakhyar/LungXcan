package com.bangkit.lungxcan.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.lungxcan.R
import com.bangkit.lungxcan.data.response.ArticlesItem
import com.bangkit.lungxcan.databinding.ItemArticleHomeBinding
import com.bangkit.lungxcan.ui.article.ArticleDetailActivity
import com.bangkit.lungxcan.utils.DateFormatter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ArticleHomeAdapter : ListAdapter<ArticlesItem, ArticleHomeAdapter.ListViewHolder>(DIFF_CALLBACK) {
    class ListViewHolder(private val binding: ItemArticleHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(articleItem: ArticlesItem) {
            binding.tvArticleTitle.text = articleItem.title
            Glide.with(itemView.context)
                .load(articleItem.urlToImage)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error_24)
                )
                .into(binding.ivArticleThumbnail)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemArticleHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val articles = getItem(position)
        holder.bind(articles)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ArticleDetailActivity::class.java)
            intent.putExtra(ArticleDetailActivity.ID_ARTICLE, articles)
            holder.itemView.context.startActivity(intent)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ArticlesItem> =
            object : DiffUtil.ItemCallback<ArticlesItem>() {
                override fun areItemsTheSame(
                    oldItem: ArticlesItem,
                    newItem: ArticlesItem
                ): Boolean {
                    return oldItem.title == newItem.title
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: ArticlesItem,
                    newItem: ArticlesItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}